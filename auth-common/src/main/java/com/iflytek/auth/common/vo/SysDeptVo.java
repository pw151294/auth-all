package com.iflytek.auth.common.vo;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Data
public class SysDeptVo {

    private Integer id;

    private Integer parentId;

    private String name;

    private Integer seq;

    private String remark;

    private List<SysDeptVo> childs = Lists.newArrayList();
}
