package com.iflytek.auth.manager.controller;

import com.iflytek.auth.common.dto.SysDeptDto;
import com.iflytek.auth.common.vo.SysDeptVo;
import com.iflytek.auth.manager.annotations.AclValidate;
import com.iflytek.auth.manager.service.IDeptService;
import com.iflytek.itsc.web.response.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Slf4j
@RestController
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    private IDeptService deptService;

    @PostMapping("/add")
    public RestResponse addDept(@RequestBody @Validated SysDeptDto sysDeptDto) {
        return deptService.addDept(sysDeptDto);
    }

    @PostMapping("/update")
    public RestResponse updateDept(@RequestBody @Validated SysDeptDto sysDeptDto) {
        return deptService.updateDept(sysDeptDto);
    }

    @PostMapping("/delete/{deptId}")
    public RestResponse deleteDept(@PathVariable Integer deptId) {
        return deptService.deletDept(deptId);
    }

    @PostMapping("/tree")
    @AclValidate
    public RestResponse<List<SysDeptVo>> deptTree() {
        return deptService.deptTree();
    }

    @PostMapping("/submit/add")
    public RestResponse subAddDept(@RequestBody @Validated SysDeptDto sysDeptDto) {
        return deptService.submitAddDept(sysDeptDto);
    }

    @PostMapping("/submit/update")
    public RestResponse submitUpdateDept(@RequestBody @Validated SysDeptDto sysDeptDto) {
        return deptService.submitUpdateDept(sysDeptDto);
    }

    @PostMapping("/submit/delete/{deptId}")
    public RestResponse submitDeleteDept(@PathVariable Integer deptId) {
        return deptService.submitDeleteDept(deptId);
    }
}
