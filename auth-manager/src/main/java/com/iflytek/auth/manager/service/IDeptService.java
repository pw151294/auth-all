package com.iflytek.auth.manager.service;

import com.iflytek.auth.common.dto.SysDeptDto;
import com.iflytek.auth.common.pojo.SysDept;
import com.iflytek.auth.common.vo.SysDeptVo;
import com.iflytek.itsc.web.response.RestResponse;

import java.util.List;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public interface IDeptService {

    RestResponse addDept(SysDeptDto sysDeptDto);

    RestResponse updateDept(SysDeptDto sysDeptDto);

    RestResponse deletDept(Integer deptId);

    RestResponse<List<SysDeptVo>> deptTree();

    RestResponse submitAddDept(SysDeptDto sysDeptDto);

    RestResponse submitUpdateDept(SysDeptDto sysDeptDto);

    RestResponse submitDeleteDept(Integer deptId);

    void updateAll(List<SysDept> sysDepts);
}
