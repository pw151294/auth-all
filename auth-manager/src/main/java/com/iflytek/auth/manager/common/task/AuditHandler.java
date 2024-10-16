package com.iflytek.auth.manager.common.task;

import com.google.common.collect.Maps;
import com.iflytek.auth.common.common.enums.TargetType;
import com.iflytek.auth.common.pojo.SysAudit;
import com.iflytek.auth.manager.common.handlers.PojoHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 * 审核任务类
 */
@Slf4j
public class AuditHandler {

    private LinkedBlockingQueue<SysAudit> auditQueue;

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private Map<Integer, PojoHandler> typeHandlerMap = Maps.newHashMap();

    private Runnable task = () -> {
        log.info("begin to fetch audit records from queue >>>>>>>>>>");
        //确定需要获取数据的条数
        int size = auditQueue.size();
        int pollCnt = Math.min(size, 16);
        try {
            for (int i = 0; i < pollCnt; i++) {
                SysAudit sysAudit = auditQueue.poll(1, TimeUnit.SECONDS);
                if (sysAudit == null || sysAudit.getStatus().equals(0) || sysAudit.getResult().equals(0)) {
                    //审核记录为空 或未审核 或审核结果为不通过 不进行任何操作
                    continue;
                }
                //操作落地
                PojoHandler handler = typeHandlerMap.get(sysAudit.getTargetType());
                handler.handle(sysAudit);
            }
        } catch (Exception e) {
            log.error("save data into database failed:{}", e.getLocalizedMessage());
        }
    };

    public void init(int queueSize) {
        log.info("begin to init audit handler >>>>>>>>>>");
        this.auditQueue = new LinkedBlockingQueue<>(queueSize);
        //启动周期任务方法
        executorService.scheduleAtFixedRate(task, 0, 10, TimeUnit.SECONDS);
        log.info("init audit handler success!");
    }

    public void setHandler(PojoHandler handler) {
        typeHandlerMap.put(handler.getTargetType(), handler);
        log.info("set handler, type:{}", TargetType.getTargetType(handler.getTargetType()));
    }

    /**
     * 批量插入审核记录表
     *
     * @param audits
     */
    public void offer(List<SysAudit> audits) {
        try {
            auditQueue.addAll(audits);
            log.info("save audit records into queue success!");
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            log.error("batch save audit record failed: {}", e.getLocalizedMessage());
        }
    }

    // 在应用关闭时，需调用此方法以优雅地关闭线程池
    public void shutdown() {
        executorService.shutdown(); // 关闭线程池
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow(); // 如果未终止，强制关闭
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow(); // 处理中断异常
        }
    }
}
