package com.iflytek.auth.manager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflytek.auth.common.common.utils.PoCommonUtils;
import com.iflytek.auth.common.dao.SysAuditMapper;
import com.iflytek.auth.common.dto.SysAuditDto;
import com.iflytek.auth.common.pojo.SysAudit;
import com.iflytek.auth.manager.common.task.AuditHandler;
import com.iflytek.auth.manager.service.IAuditService;
import com.iflytek.itsc.web.response.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.iflytek.auth.common.dto.SysAuditDto.SysAuditItem;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Slf4j
@Service
public class AuditServiceImpl extends ServiceImpl<SysAuditMapper, SysAudit> implements IAuditService {

    @Autowired
    private SysAuditMapper auditMapper;

    @Autowired
    private AuditHandler auditHandler;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse audit(SysAuditDto sysAuditDto) {
        //获取审核记录和审核结果之间的映射关系
        Map<Integer, Integer> idResultMap = sysAuditDto.getItems().stream()
                .collect(Collectors.toMap(SysAuditItem::getAuditId, SysAuditItem::getResult));
        List<SysAudit> sysAudits = auditMapper.selectBatchIds(idResultMap.keySet());
        sysAudits.forEach(sysAudit -> {
            sysAudit.setStatus(1); //设置审核状态为已审核
            sysAudit.setResult(idResultMap.get(sysAudit.getId()));
            PoCommonUtils.setAuditorInfo(sysAudit);
            PoCommonUtils.setOperationInfo(sysAudit);
            //TODO 在for循环内部执行sql 多次IO操作需要关注性能问题
//            auditMapper.updateById(sysAudit);
        });
        //更新审核表 并将审核信息提交队列
        this.saveOrUpdateBatch(sysAudits);
        auditHandler.offer(sysAudits);

        return RestResponse.buildSuccess("审核结果提交成功！");
    }
}
