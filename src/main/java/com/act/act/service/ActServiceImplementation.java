package com.act.act.service;

import com.act.act.dto.ActMapper;
import com.act.act.dto.ActRequestDto;
import com.act.act.dto.ActResponseDto;
import com.act.act.model.Act;
import com.act.act.model.EntranceControl;
import com.act.act.repository.ActRepository;
import com.act.act.repository.EntranceControlRepository;
import com.act.exception.exception.NotFoundException;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final static String MATERIALS_FOR_ACT = "Акт результатов входного контроля МТР и оборудования №";

    @Override
    public ActResponseDto get(Long id) {
        return ActMapper.INSTANCE.toDto(findActOrNot(id));
    }

    @Override
    public List<ActResponseDto> getAll() {
        List<Act> acts = actRepository.findAll();
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

        log.info(requestDto.toString());

        SubObject subObject = subObjectService.get(requestDto.getSubObjectId());
        act.setActNumber(subObject.getTitle() + "/" + (actRepository.countBySubObject(subObject) + 1));

        act.setProject(projectService.findProjectOrNot(requestDto.getProjectId()));
        act.setSubObject(subObjectService.findSubObjectOrNot(requestDto.getSubObjectId()));

        Working working = workingService.findWorkingOrNot(requestDto.getWorkId());
        act.setWorks(subObject.getName() + ": " + working.getName() + " - " + requestDto.getWorkDone() + " " + working.getUnits());

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

        act = actRepository.save(act);

        addEntranceControl(act, requestDto);


        return act;
    }

    @Transactional
    @Override
    public Act update(long id, ActRequestDto requestDto) {
        Act updatedAct = findActOrNot(id);
//
//        if (act.getName() != null) {
//            updatedAct.setName(act.getName());
//        }

        return actRepository.save(updatedAct);
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
        return actRepository.findById(id).orElseThrow(() -> new NotFoundException("Подобъект не найден"));
    }

    private LocalDate jsDateToLocalDate(String date) {
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

    private void addEntranceControl(Act act, ActRequestDto requestDto) {
        List<ActRequestDto.ActMaterial> actMaterials = requestDto.getActMaterials();
        int counter = 1;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate controlDate = jsDateToLocalDate(requestDto.getControlDate());
        String formattedDate = controlDate.format(formatter);

        for (ActRequestDto.ActMaterial actMaterial : actMaterials) {
            EntranceControl entranceControl = new EntranceControl();

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

            counter++;

            try {
                entranceControlRepository.save(entranceControl);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private String addMaterialParameter(ActRequestDto requestDto) {
        return null;
    }
}