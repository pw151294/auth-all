package com.iflytek.auth.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.iflytek.auth.common.common.enums.AuthErrorCodeEnum;
import com.iflytek.auth.common.common.utils.PoCommonUtils;
import com.iflytek.auth.common.common.utils.TreeUtils;
import com.iflytek.auth.common.dao.SysDeptMapper;
import com.iflytek.auth.common.dto.SysDeptDto;
import com.iflytek.auth.common.dto.SysLogDto;
import com.iflytek.auth.common.pojo.SysDept;
import com.iflytek.auth.common.vo.SysDeptVo;
import com.iflytek.auth.common.common.enums.TargetType;
import com.iflytek.auth.manager.service.IDeptService;
import com.iflytek.auth.manager.service.ILogService;
import com.iflytek.itsc.web.exception.BaseBizException;
import com.iflytek.itsc.web.response.RestResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Service
public class DeptServiceImpl implements IDeptService {

    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private ILogService logService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse addDept(SysDeptDto sysDeptDto) {
        //校验上级部门下 是否有同名或者同序部门
        if (deptMapper.findByNameAndSeq(sysDeptDto) > 0) {
            throw new BaseBizException(AuthErrorCodeEnum.DEPT_DUPLICATE_NAME_OR_SEQ);
        }
        SysDept sysDept = new SysDept();
        transferProperties(sysDept, sysDeptDto);
        deptMapper.insert(sysDept);

        //记录权限更新日志
        SysLogDto sysLogDto = new SysLogDto();
        sysLogDto.setType(TargetType.DEPT.getType());
        sysLogDto.setTargetId(sysDept.getId());
        sysLogDto.setOldValue("");
        sysLogDto.setNewValue(JSON.toJSONString(sysDept));
        logService.addLog(sysLogDto);

        return RestResponse.buildSuccess("新建部门成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse updateDept(SysDeptDto sysDeptDto) {
        //查询对应的部门是否存在
        Integer deptId = sysDeptDto.getId();
        Preconditions.checkNotNull(sysDeptDto.getId(), "被更新部门ID不能为空");
        SysDept sysDept = deptMapper.selectById(deptId);
        if (sysDept == null) {
            return RestResponse.buildError(String.format("待更新部门不存在，id:%s", deptId));
        }
        //校验上级部门下 是否有同名或者同序部门
        if (deptMapper.findByNameAndSeq(sysDeptDto) > 0) {
            throw new BaseBizException(AuthErrorCodeEnum.DEPT_DUPLICATE_NAME_OR_SEQ);
        }
        String oldValue = JSON.toJSONString(sysDept);
        transferProperties(sysDept, sysDeptDto);
        String newValue = JSON.toJSONString(sysDept);
        deptMapper.updateById(sysDept);

        //记录权限更新日志
        SysLogDto sysLogDto = new SysLogDto();
        sysLogDto.setType(TargetType.DEPT.getType());
        sysLogDto.setTargetId(sysDept.getId());
        sysLogDto.setOldValue(oldValue);
        sysLogDto.setNewValue(newValue);
        logService.addLog(sysLogDto);

        return RestResponse.buildSuccess("部门更新成功");
    }

    @Override
    public RestResponse deletDept(Integer deptId) {
        Preconditions.checkNotNull(deptId, "被删除部门的ID不能为空");
        //校验部门下是否有成员或者子部门
        if (deptMapper.countUsersByDeptId(deptId) > 0) {
            return RestResponse.buildError("该部门下还有成员存在，不能删除！");
        }
        if (deptMapper.countSubDepts(deptId) > 0) {
            return RestResponse.buildError("该部门下还有子部门，不能删除！");
        }
        deptMapper.deleteById(deptId);

        return RestResponse.buildSuccess("删除部门成功！");
    }

    @Override
    public RestResponse<List<SysDeptVo>> deptTree() {
        //查询出所有部门和子部门的映射关系
        Map<Integer, List<SysDept>> deptIdChildMap =
                deptMapper.findAll().stream().collect(Collectors.groupingBy(SysDept::getParentId));
        //获取一级部门
        List<SysDeptVo> deptTree = deptIdChildMap.get(0)
                .stream()
                .map(sysDept -> {
                    SysDeptVo deptVo = new SysDeptVo();
                    BeanUtils.copyProperties(sysDept, deptVo);
                    return deptVo;
                }).collect(Collectors.toList());
        //逐个构建各部门的子部门
        deptTree.forEach(sysDeptVo -> TreeUtils.buildTree(sysDeptVo, deptIdChildMap));

        return RestResponse.buildSuccess(deptTree);
    }

    private void transferProperties(SysDept sysDept, SysDeptDto sysDeptDto) {
        sysDept.setName(sysDeptDto.getName());
        sysDept.setParentId(sysDeptDto.getParentId());
        sysDept.setSeq(sysDeptDto.getSeq());
        String parentLevel = deptMapper.findLevelById(sysDeptDto.getParentId());
        sysDept.setLevel(String.format("%s.%s", parentLevel, sysDeptDto.getParentId()));
        if (StringUtils.isNotBlank(sysDeptDto.getRemark())) {
            sysDept.setRemark(sysDeptDto.getRemark());
        }
        PoCommonUtils.setOperationInfo(sysDept);
    }
}
