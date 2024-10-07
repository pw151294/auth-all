package com.iflytek.auth.common.common.utils;

import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.Lists;
import com.iflytek.auth.common.vo.SysRoleAclModuleVo;
import com.iflytek.auth.common.vo.SysRoleAclVo;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public class TreeUtils {


    /**
     * 构建Vo的树结构
     *
     * @param vo
     * @param idChildMap Po对象和子Po对象之间的映射关系 key表示父对象ID value表示父对象下的所有子对象集合
     * @param <V>        SysDeptVo SysAclModuleVo SysRoleAclModuleVo
     * @param <P>        SysDept SysAclModule SysAclModule
     */
    public static <V, P> void buildTree(V vo, Map<Integer, List<P>> idChildMap) {
        //查询Vo的子对象
        Integer parentId = (Integer) ReflectUtil.getFieldValue(vo, "id");
        List<P> childs = idChildMap.get(parentId);
        if (CollectionUtils.isEmpty(childs)) {
            return;
        }
        childs.forEach(po -> {
            V childVo = (V) ReflectUtil.newInstance(vo.getClass());
            BeanUtils.copyProperties(po, childVo);
            buildTree(childVo, idChildMap);
            List<V> childVos = (List<V>) ReflectUtil.getFieldValue(vo, "childs");
            childVos.add(childVo);
        });
    }

    /**
     * 角色权限树 填充每个权限模块的所有权限点
     *
     * @param roleAclModuleVo 角色权限树里的每个权限模块
     * @param moduleIdAclMap  权限模块ID及其权限点列表的映射关系 key权限模块ID value权限点列表
     */
    public static void fillAcls(SysRoleAclModuleVo roleAclModuleVo, Map<Integer, List<SysRoleAclVo>> moduleIdAclMap) {
        roleAclModuleVo.setAclVos(moduleIdAclMap.getOrDefault(roleAclModuleVo.getId(), Lists.newArrayList()));
        if (!CollectionUtils.isEmpty(roleAclModuleVo.getChilds())) {
            roleAclModuleVo.getChilds().forEach(roleAclModuleVo1 -> fillAcls(roleAclModuleVo1, moduleIdAclMap));
        }
    }
}
