package com.iflytek.auth.manager.common.handlers;

import com.alibaba.fastjson.JSON;
import com.iflytek.auth.common.common.enums.TargetType;
import com.iflytek.auth.common.common.utils.PoCommonUtils;
import com.iflytek.auth.common.common.utils.TreeUtils;
import com.iflytek.auth.common.dao.SysDeptMapper;
import com.iflytek.auth.common.pojo.SysAudit;
import com.iflytek.auth.common.pojo.SysDept;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Slf4j
@Component
public class DeptHandler implements PojoHandler {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Override
    public Integer getTargetType() {
        return TargetType.DEPT.getType();
    }

    @Override
    public void handle(SysAudit sysAudit) {
        log.info("begin to handler dept operation >>>>>>>>>>");
        doHandle(sysAudit);
    }

    private void doHandle(SysAudit sysAudit) {
        switch (sysAudit.getOperationType()) {
            case 1:
                doAdd(sysAudit);
                break;
            case 2:
                doUpdate(sysAudit);
                break;
            case 3:
                doDel(sysAudit);
                break;
            default:
                break;
        }
    }

    private void doAdd(SysAudit sysAudit) {
        SysDept sysDept = JSON.parseObject(sysAudit.getNewValue(), SysDept.class);
        sysDeptMapper.insert(sysDept);
        log.info("add dept success!");
    }

    private void doUpdate(SysAudit sysAudit) {
        SysDept oldValue = JSON.parseObject(sysAudit.getOldValue(), SysDept.class);
        SysDept newValue = JSON.parseObject(sysAudit.getNewValue(), SysDept.class);
        //查询该部门的所有子部门
        List<SysDept> sysDepts = sysDeptMapper.findAll();
        List<SysDept> childs = TreeUtils.findChilds(sysAudit.getTargetId(), sysDepts);
        //更新所有子部门的level
        TreeUtils.changeChildLevel(childs, oldValue.getLevel(), newValue.getLevel());

        //更新部门信息和子部门信息
        sysDeptMapper.updateById(newValue);
        log.info("update dept success!");

        childs.forEach(child -> PoCommonUtils.copyOperateInfo(sysAudit, child));
        sysDeptMapper.updateAll(sysDepts);
        log.info("update level of child dept success!");
    }

    private void doDel(SysAudit sysAudit) {
        Integer deptId = sysAudit.getTargetId();
        sysDeptMapper.deleteById(deptId);
        log.info("delete dept success!");
    }
}
