package com.iflytek.auth.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iflytek.auth.common.pojo.SysAudit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author panwei
* @description 针对表【sys_audit】的数据库操作Mapper
* @createDate 2024-10-07 18:18:15
* @Entity com.iflytek.auth.common.pojo.SysAudit
*/
@Mapper
public interface SysAuditMapper extends BaseMapper<SysAudit> {

    void batchInsert(@Param("audits") List<SysAudit> audits);

}
