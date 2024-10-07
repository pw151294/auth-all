package com.iflytek.auth.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.iflytek.auth.common.common.Validator;
import com.iflytek.auth.common.common.utils.PoCommonUtils;
import com.iflytek.auth.common.dao.SysLogMapper;
import com.iflytek.auth.common.dto.SysLogDto;
import com.iflytek.auth.common.pojo.SysLog;
import com.iflytek.auth.common.vo.SysLogVo;
import com.iflytek.auth.manager.service.ILogService;
import com.iflytek.itsc.web.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Service
public class LogServiceImpl implements ILogService {

    @Autowired
    private SysLogMapper logMapper;

    @Override
    public void addLog(SysLogDto sysLogDto) {
        Validator.validateLogAdd(sysLogDto);
        SysLog sysLog = new SysLog();
        PoCommonUtils.copyLogProperties(sysLogDto, sysLog);
        PoCommonUtils.setOperationInfo(sysLog);
        logMapper.insert(sysLog);
    }

    @Override
    public RestResponse<PageInfo<SysLogVo>> pageLogs(SysLogDto sysLogDto) {
        Preconditions.checkNotNull(sysLogDto, "请求参数为空");
        PageHelper.startPage(sysLogDto.getPageNum(), sysLogDto.getPageSize());
        List<SysLogVo> sysLogVos = logMapper.pageLogs(sysLogDto);

        return RestResponse.buildSuccess(new PageInfo<>(sysLogVos));
    }
}
