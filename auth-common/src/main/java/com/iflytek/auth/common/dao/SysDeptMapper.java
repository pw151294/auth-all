package com.iflytek.auth.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iflytek.auth.common.dto.SysDeptDto;
import com.iflytek.auth.common.pojo.SysDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author panwei
* @description 针对表【sys_dept】的数据库操作Mapper
* @createDate 2024-10-03 14:58:41
* @Entity com.iflytek.shiro.manager.pojo.SysDept
*/
@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept> {

    /**
     * 查询同名或者同顺的同级部门
     * @param sysDeptDto
     * @return
     */
    int findByNameAndSeq(@Param("sysDeptDto") SysDeptDto sysDeptDto);

    /**
     * 查询指定部门下的用户数量
     * @param deptId
     * @return
     */
    int countUsersByDeptId(@Param("deptId") Integer deptId);

    /**
     * 查询指定部门下的子部门数
     * @param deptId
     * @return
     */
    int countSubDepts(@Param("deptId") Integer deptId);


    /**
     * 查询所有部门
     * @return
     */
    List<SysDept> findAll();

    String findLevelById(@Param("deptId") Integer deptId);
}
