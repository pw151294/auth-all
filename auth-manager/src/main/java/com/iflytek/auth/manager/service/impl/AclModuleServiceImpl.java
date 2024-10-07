package com.iflytek.auth.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.iflytek.auth.common.common.utils.PoCommonUtils;
import com.iflytek.auth.common.common.utils.TreeUtils;
import com.iflytek.auth.common.common.Validator;
import com.iflytek.auth.common.dao.SysAclModuleMapper;
import com.iflytek.auth.common.dto.SysAclModuleDto;
import com.iflytek.auth.common.dto.SysLogDto;
import com.iflytek.auth.common.pojo.SysAclModule;
import com.iflytek.auth.common.vo.SysAclModuleVo;
import com.iflytek.auth.common.common.enums.TargetType;
import com.iflytek.auth.manager.service.IAclModuleService;
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
public class AclModuleServiceImpl implements IAclModuleService {

    @Autowired
    private SysAclModuleMapper aclModuleMapper;

    @Autowired
    private ILogService logService;

    @Override
    public RestResponse<List<SysAclModuleVo>> aclModuleTree() {
        //获取权限模块ID及其子模块之间的映射关系
        Map<Integer, List<SysAclModule>> idChildMap = aclModuleMapper.findAll()
                .stream().collect(Collectors.groupingBy(SysAclModule::getParentId));
        List<SysAclModuleVo> aclModuleTree = idChildMap.get(0)
                .stream().map(sysAclModule -> {
                    SysAclModuleVo aclModuleVo = new SysAclModuleVo();
                    BeanUtils.copyProperties(sysAclModule, aclModuleVo);
                    return aclModuleVo;
                }).collect(Collectors.toList());
        //构建各个权限模块的子模块
        aclModuleTree.forEach(sysAclModuleVo -> TreeUtils.buildTree(sysAclModuleVo, idChildMap));

        return RestResponse.buildSuccess(aclModuleTree);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse addAclModule(SysAclModuleDto sysAclModuleDto) {
        Validator.validateAclModuleAdd(sysAclModuleDto);
        //校验同级或者同顺序的权限模块是否存在
        if (aclModuleMapper.countByNameAndSeq(sysAclModuleDto) > 0) {
            return RestResponse.buildError("存在同名或者是同序的权限模块");
        }
        SysAclModule sysAclModule = new SysAclModule();
        transferProperties(sysAclModuleDto, sysAclModule);
        aclModuleMapper.insert(sysAclModule);

        //记录权限更新日志
        SysLogDto sysLogDto = new SysLogDto();
        sysLogDto.setType(TargetType.ACL_MODULE.getType());
        sysLogDto.setTargetId(sysAclModule.getId());
        sysLogDto.setOldValue("");
        sysLogDto.setNewValue(JSON.toJSONString(sysAclModule));
        logService.addLog(sysLogDto);

        return RestResponse.buildSuccess("新增权限模块成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse updateAclModule(SysAclModuleDto sysAclModuleDto) {
        Validator.validateAclModuleUpdate(sysAclModuleDto);
        SysAclModule aclModule = aclModuleMapper.selectById(sysAclModuleDto.getId());
        Preconditions.checkNotNull(aclModule, "被更新的权限模块不存在");
        String oldValue = JSON.toJSONString(aclModule);
        transferProperties(sysAclModuleDto, aclModule);
        String newValue = JSON.toJSONString(aclModule);
        aclModuleMapper.updateById(aclModule);

        //记录权限更新日志
        SysLogDto sysLogDto = new SysLogDto();
        sysLogDto.setType(TargetType.ACL_MODULE.getType());
        sysLogDto.setTargetId(aclModule.getId());
        sysLogDto.setOldValue(oldValue);
        sysLogDto.setNewValue(newValue);
        logService.addLog(sysLogDto);

        return RestResponse.buildSuccess("更新权限模块成功");
    }

    @Override
    public RestResponse deleteAclModule(Integer aclModuleId) {
        Preconditions.checkNotNull(aclModuleId, "权限模块ID不能为空");
        //校验该权限模块下是否有子模块或者绑定的权限
        if (aclModuleMapper.countChilds(aclModuleId) > 0) {
            throw new BaseBizException("该权限模块下有子模块，不能删除");
        }
        if (aclModuleMapper.countAcls(aclModuleId) > 0) {
            throw new BaseBizException("该权限模块有绑定的权限，不能删除");
        }
        aclModuleMapper.deleteById(aclModuleId);

        return RestResponse.buildSuccess("权限模块删除成功");
    }

    private void transferProperties(SysAclModuleDto sysAclModuleDto, SysAclModule sysAclModule) {
        sysAclModule.setParentId(sysAclModuleDto.getParentId());
        sysAclModule.setName(sysAclModuleDto.getName());
        sysAclModule.setSeq(sysAclModuleDto.getSeq());
        sysAclModule.setLevel(String.format("%s.%s",
                aclModuleMapper.findLevelById(sysAclModuleDto.getParentId()), sysAclModuleDto.getParentId()));
        sysAclModule.setStatus(sysAclModuleDto.getStatus() == null ? 1 : sysAclModuleDto.getStatus());
        if (StringUtils.isNotBlank(sysAclModuleDto.getRemark())) {
            sysAclModule.setRemark(sysAclModuleDto.getRemark());
        }
        PoCommonUtils.setOperationInfo(sysAclModule);
    }
}
