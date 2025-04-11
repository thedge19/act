package com.act.act.service;

import com.act.act.dto.*;
import com.act.act.model.Act;
import com.act.act.model.EntranceControl;
import com.act.act.repository.ActRepository;
import com.act.act.repository.EntranceControlRepository;
import com.act.exception.exception.NotFoundException;
import com.act.material.model.Material;
import com.act.material.repository.MaterialRepository;
import com.act.project.model.Project;
import com.act.project.service.ProjectService;
import com.act.subobject.model.SubObject;
import com.act.subobject.service.SubObjectService;
import com.act.working.model.Working;
import com.act.working.service.WorkingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActServiceImplementation implements ActService {

    private final ActRepository actRepository;
    private final ProjectService projectService;
    private final SubObjectService subObjectService;
    private final WorkingService workingService;
    private final EntranceControlRepository entranceControlRepository;
    private final MaterialRepository materialRepository;
    private final static String CONTROL_ACT = "Акт результатов входного контроля МТР и оборудования №";
    private final static String EXECUTIVE_SCHEMA = "Исполнительная схема №";
    private final static String SETS_OF_RULES = " СП 48.13330.2019 «Организация строительства»; СП 49.13330.2010\n" +
            "«Безопасность труда в строительстве» ";

    @Override
    public ActResponseDto get(Long id) {
        return ActMapper.INSTANCE.toDto(findActOrNot(id));
    }

    @Override
    public ActUpdateResponseDto getUpdatedAct(long id) {
        return ActMapper.INSTANCE.toUpdateDto(findActOrNot(id));
    }

    @Override
    public List<ActResponseDto> getAll() {
        List<Act> acts = actRepository.findAllByOrderByEndDateAscActNumberAsc();
        return acts.stream().map(ActMapper.INSTANCE::toDto).toList();
    }

    @Override
    public List<ActResponseDto> findAllByEndDateBetween(LocalDate startDate, LocalDate endDate) {
        List<Act> acts = actRepository.findAllByEndDateBetweenOrderByEndDateAscActNumberAsc(startDate, endDate);
        return acts.stream().map(ActMapper.INSTANCE::toDto).toList();
    }

    @Override
    public List<EntranceControl> getAllEntranceControl() {
        return entranceControlRepository.findAll();
    }

    @Transactional
    @Override
    public Act create(ActRequestDto requestDto) {
        Act act = new Act();

        SubObject subObject = subObjectService.get(requestDto.getSubObjectId());

        String actSequenceNumber = String.valueOf(actRepository.countBySubObject(subObject) + 1);
        actSequenceNumber = actSequenceNumber.length() == 1 ? "0" + actSequenceNumber : actSequenceNumber;

        String actNumber = subObject.getTitle() + "/" + actSequenceNumber;

        Project project = projectService.findProjectOrNot(requestDto.getProjectId());
        String projectName = project.getName();

        act.setActNumber(actNumber);
        act.setProject(project);

        act.setSubObject(subObjectService.findSubObjectOrNot(requestDto.getSubObjectId()));

        Working working = workingService.findWorkingOrNot(requestDto.getWorkId());

        String nextWorking = requestDto.getNextWorkId() == null ?
                "н/п" :
                subObject.getName() + ": " +
                        workingService.findWorkingOrNot(requestDto.getNextWorkId()).getName();

        working.setDone(requestDto.getWorkDone());

        String standard = working.getStandard().getName();


        String inAccordWith = projectName.split("\\.")[2] + "; " + SETS_OF_RULES + "; " + standard;
        act.setInAccordWith(inAccordWith);

        act.setWorks(subObject.getName() + ": " + working.getName() + " - " + requestDto.getWorkDone() + " " + working.getUnits());

        act.setNextWorks(nextWorking);
        act.setWorkDone(requestDto.getWorkDone());

        act.setStartDate(jsDateToLocalDate(requestDto.getStartDate()));
        act.setEndDate(jsDateToLocalDate(requestDto.getEndDate()));

        String materials = String.join("; ", requestDto.getActMaterials()
                .stream()
                .map(actMaterial -> (
                        actMaterial.getName() + " - " +
                                actMaterial.getQuantity() + " " +
                                actMaterial.getUnits() + ", " +
                                actMaterial.getDocuments()))
                .toList());

        act.setMaterials(materials);
        act.setSubmittedDocuments(addSubmittedDocuments(requestDto, actNumber));
        try {
            act = actRepository.save(act);
        } catch (Exception e) {
            e.printStackTrace();
        }
        addEntranceControl(act, requestDto);


        return act;
    }

    @Transactional
    @Override
    public Act update(long id, ActUpdateRequestDto requestDto) {
        Act act = findActOrNot(id);
        act.setWorks(requestDto.getWorks());
        return actRepository.save(act);
    }

    @Transactional
    @Override
    public void delete(long id) {
        findActOrNot(id);
        actRepository.deleteById(id);
    }

    @Override
    public void deleteControl(long id) {
        entranceControlRepository.deleteById(id);
    }

    @Override
    public Act findActOrNot(long id) {
        return actRepository.findById(id).orElseThrow(() -> new NotFoundException("Акт найден"));
    }

    @Override
    public EntranceControl findEntranceControl(long id) {
        return entranceControlRepository.findById(id).orElse(null);
    }

    @Override
    public LocalDate jsDateToLocalDate(String date) {
        String[] arr = date.split(" ");

        int year = Integer.parseInt(arr[3]);
        int day = Integer.parseInt(arr[2]);
        String stringMonth = arr[1];

        Integer[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        String[] keys = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        Map<String, Integer> monthMap = new HashMap<String, Integer>();

        for (int i = 0; i < keys.length; i++) {
            monthMap.put(keys[i], values[i]);
        }

        int month = monthMap.get(stringMonth);

        return LocalDate.of(year, month, day);
    }

    @Override
    public List<ActResponseDto> filterBySubObject() {
        return actRepository.findAllByOrderByActNumberAsc().stream().map(ActMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ActResponseDto> getAllWithNullInRegistries() {
        return actRepository.findAllByOrderByEndDateAsc()
                .stream()
                .map(ActMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    private void addEntranceControl(Act act, ActRequestDto requestDto) {
        List<ActRequestDto.ActMaterial> actMaterials = requestDto.getActMaterials();
        int counter = 1;

        LocalDate controlDate = jsDateToLocalDate(requestDto.getControlDate());

        for (ActRequestDto.ActMaterial actMaterial : actMaterials) {
            EntranceControl entranceControl = new EntranceControl();

            Material material = materialRepository.findById(actMaterial.getId()).orElse(null);
            String addedMaterial = actMaterial.getName() + " - " + actMaterial.getQuantity() + " " + actMaterial.getUnits();
            String addedDocuments = actMaterial.getDocuments();
            String controlActNumber = actMaterials.size() == 1 ? act.getActNumber() : act.getActNumber() + "-" + counter;


            entranceControl.setProject(act.getProject());
            entranceControl.setSubObjectName(act.getSubObject().getName());
            entranceControl.setControlNumber(controlActNumber);
            entranceControl.setDate(controlDate);
            entranceControl.setMaterials(addedMaterial);
            entranceControl.setDocuments(addedDocuments);
            entranceControl.setStandard(actMaterial.getStandard());
            entranceControl.setAct(act);
            assert material != null;
            entranceControl.setControlSheetNumbers(material.getNumberOfPages());
            entranceControl.setAuthor(material.getAuthor());

            counter++;

            try {
                entranceControlRepository.save(entranceControl);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private String addSubmittedDocuments(ActRequestDto requestDto, String actNumber) {
        List<String> submittedDocuments = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate controlDate = jsDateToLocalDate(requestDto.getControlDate());
        LocalDate endDate = jsDateToLocalDate(requestDto.getEndDate());
        String formattedControlDate = controlDate.format(formatter);
        String formattedEndDate = endDate.format(formatter);

        if (Objects.equals(requestDto.getExecutiveSchema(), "Есть")) {
            submittedDocuments.add(EXECUTIVE_SCHEMA + actNumber + " от " + formattedEndDate + " г.");
        }

        List<ActRequestDto.ActMaterial> materials = requestDto.getActMaterials();

        switch (materials.size()) {
            case 0:
                break;
            case 1:
                submittedDocuments.add(CONTROL_ACT + actNumber + " от " + formattedControlDate + " г.");
                break;
            default:
                for (int count = 0; count < materials.size(); count++) {
                    submittedDocuments.add(CONTROL_ACT + actNumber + "-" + (count + 1) + " от " + formattedControlDate + " г.");
                }
                break;
        }

        return String.join("; ", submittedDocuments);
    }

    @Override
    public List<EntranceControl> controls(Act act) {
        return entranceControlRepository.findAllByAct(act);
    }

    @Transactional
    @Override
    public EntranceControl updateEntranceControl(long id, EntranceControlRequestDto requestDto) {
        EntranceControl entranceControl = entranceControlRepository.findById(id).orElse(null);
        assert entranceControl != null;
        if (requestDto.getControlSheetNumbers() != null) {
            entranceControl.setControlSheetNumbers(requestDto.getControlSheetNumbers());
        }

        if (requestDto.getAuthor() != null) {
            entranceControl.setAuthor(requestDto.getAuthor());
        }

        return entranceControl;
    }
}