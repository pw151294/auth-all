package com.iflytek.auth.manager.controller;

import com.github.pagehelper.PageInfo;
import com.iflytek.auth.common.dto.SysLogDto;
import com.iflytek.auth.common.vo.SysLogVo;
import com.iflytek.auth.manager.service.ILogService;
import com.iflytek.itsc.web.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private ILogService logService;

    @PostMapping("/page")
    public RestResponse<PageInfo<SysLogVo>> pageLogs(@RequestBody SysLogDto sysLogDto) {
        return logService.pageLogs(sysLogDto);
    }
}
