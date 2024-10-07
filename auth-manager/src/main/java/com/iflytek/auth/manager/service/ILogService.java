package com.iflytek.auth.manager.service;

import com.github.pagehelper.PageInfo;
import com.iflytek.auth.common.dto.SysLogDto;
import com.iflytek.auth.common.vo.SysLogVo;
import com.iflytek.itsc.web.response.RestResponse;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public interface ILogService {

    void addLog(SysLogDto sysLogDto);

    RestResponse<PageInfo<SysLogVo>> pageLogs(SysLogDto sysLogDto);
}
