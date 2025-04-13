package com.act.worklog.service;

import com.act.act.dto.ActLogResponseDto;
import com.act.worklog.dto.WorkLogDto;

import java.io.IOException;
import java.util.List;

public interface WorkLogService {

    List<WorkLogDto> getWorkLog3();

    List<ActLogResponseDto> getWorkLog6();

    void fillInTheLog3();

    void fillInTheLog6();

    void export3InExcel() throws IOException;

    void export6InExcel() throws IOException;
}
