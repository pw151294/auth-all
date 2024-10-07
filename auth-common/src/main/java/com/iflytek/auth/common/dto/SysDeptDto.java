package com.iflytek.auth.common.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Data
@Valid
public class SysDeptDto {

    /**
     * 部门id
     */
    private Integer id;

    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称不能为空")
    private String name;

    /**
     * 上级部门id
     */
    @NotNull(message = "上级部门ID不能为空")
    private Integer parentId;

    /**
     * 部门层级
     */
    private String level;

    /**
     * 部门在当前层级下的顺序，由小到大
     */
    @NotNull(message = "部门在当前层级下的顺序不能为空")
    private Integer seq;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作者
     */
    private String operator;

    /**
     * 最后一次操作时间
     */
    private Date operateTime;

    /**
     * 最后一次更新操作者的ip地址
     */
    private String operateIp;
}
