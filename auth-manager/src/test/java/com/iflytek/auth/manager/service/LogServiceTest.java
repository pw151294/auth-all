package com.iflytek.auth.manager.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.iflytek.auth.common.dto.SysLogDto;
import com.iflytek.auth.common.vo.SysLogVo;
import com.iflytek.auth.manager.AuthManagerApplicationTest;
import com.iflytek.itsc.web.response.RestResponse;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@SpringBootTest(classes = AuthManagerApplicationTest.class)
public class LogServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(LogServiceTest.class);

    @Autowired
    private ILogService logService;

    @Test
    public void testPageLogs() {
        SysLogDto sysLogDto = new SysLogDto();
        sysLogDto.setPageNum(1);
        sysLogDto.setPageSize(1);
        RestResponse<PageInfo<SysLogVo>> response = logService.pageLogs(sysLogDto);
        assert response.getSuccess();
        logger.info("日志记录：{}", JSON.toJSONString(response.getData()));
    }
}
