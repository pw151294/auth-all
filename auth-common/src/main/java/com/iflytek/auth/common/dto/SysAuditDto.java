package com.iflytek.auth.common.dto;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Data
public class SysAuditDto {

    private List<SysAuditItem> items = Lists.newArrayList();

    private String remark;

    @Data
    public static class SysAuditItem {

        private Integer auditId;

        private Integer result;
    }
}
