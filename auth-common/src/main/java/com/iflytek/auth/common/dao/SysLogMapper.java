package com.iflytek.auth.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iflytek.auth.common.dto.SysLogDto;
import com.iflytek.auth.common.pojo.SysLog;
import com.iflytek.auth.common.vo.SysLogVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author panwei
* @description 针对表【sys_log】的数据库操作Mapper
* @createDate 2024-10-03 15:04:18
* @Entity com.iflytek.shiro.manager.pojo.SysLog
*/
@Mapper
public interface SysLogMapper extends BaseMapper<SysLog> {

    List<SysLogVo> pageLogs(@Param("sysLogDto") SysLogDto sysLogDto);
}
