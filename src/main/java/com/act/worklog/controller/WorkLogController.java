package com.act.worklog.controller;

import com.act.act.dto.ActLogResponseDto;
import com.act.working.dto.WorkingRequestDto;
import com.act.working.dto.WorkingUpdateDto;
import com.act.working.model.Working;
import com.act.working.service.WorkingService;
import com.act.worklog.dto.WorkLogDto;
import com.act.worklog.model.WorkLog;
import com.act.worklog.service.WorkLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/worklog")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
public class WorkLogController {

    private final WorkLogService workLogService;

    @GetMapping
    public List<WorkLogDto> getWorkLog3() {
        return workLogService.getWorkLog3();
    }

    @GetMapping("/6")
    public List<ActLogResponseDto> getWorkLog6() {
        return workLogService.getWorkLog6();
    }

    @GetMapping("/fill3")
    void fillInTheLog3() {
        workLogService.fillInTheLog3();
    }



    @GetMapping("/excel3")
    void exportWorkLog3ToExcel() throws IOException {
        workLogService.export3InExcel();
    }

    @GetMapping("/excel6")
    void exportWorkLog6ToExcel() throws IOException {
        workLogService.export6InExcel();
    }
}