package com.iflytek.auth.manager.common.utils;

import com.alibaba.fastjson.JSON;
import com.iflytek.auth.common.dao.SysLogMapper;
import com.iflytek.auth.common.pojo.SysLog;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 * 异步任务实现日志存储
 */
@Data
public class LogUtils {

    private static final Logger logger = LoggerFactory.getLogger(LogUtils.class);

    /**
     * 创建阻塞队列 用来存取日志记录
     */
    private LinkedBlockingQueue<SysLog> logQueue;

    /**
     * 线程池创建定时任务 进行日志存储
     */
    private ExecutorService executorService;

    @Resource
    private SysLogMapper logMapper;

    public void init(int queueSize, int poolSize) {
        logger.info("begin init log utils >>>>>>>>>>");
        logQueue = new LinkedBlockingQueue<>(queueSize);
        executorService = Executors.newFixedThreadPool(poolSize);
        startProcessor();
        logger.info("init log utils success >>>>>>>>>>");
    }

    /**
     * 向队列存储日志对象
     *
     * @param sysLog
     */
    public void offer(SysLog sysLog) {
        try {
            //阻塞方式放入队列
            logQueue.offer(sysLog);
            logger.info("save log into queue success!");
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            logger.info("save log failed：{}", e.getLocalizedMessage());
        }
    }

    /**
     * 启动日志处理任务
     */
    private void startProcessor() {
        executorService.submit(() -> {
            while (true) {
                try {
                    SysLog sysLog = logQueue.take();
                    logger.info("get operation log from queue:{}", JSON.toJSONString(sysLog));
                    logMapper.insert(sysLog);
                    logger.info("save operation log successfully!");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.error("save log failed：{}", e.getLocalizedMessage());
                    break; //任务执行异常 则让退出循环
                }
            }
        });
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
