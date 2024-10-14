package com.iflytek.auth.manager.common.task;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Slf4j
public class SysTask<T> {

    /**
     * 创建阻塞队列 用来存取日志记录
     */
    private LinkedBlockingQueue<T> sysQueue;

    /**
     * 线程池创建定时任务 进行日志存储
     */
    private ExecutorService executor;


    private  BaseMapper<T> sysMapper;

    public void init(int queueSize, int poolSize, BaseMapper<T> sysMapper) {
        log.info("begin init task >>>>>>>>>>");
        this.sysQueue = new LinkedBlockingQueue<>(queueSize);
        this.executor = Executors.newFixedThreadPool(poolSize);
        this.sysMapper = sysMapper;
        startTask();
        log.info("init task success!");
    }

    /**
     * 向队列存储实体
     */
    public void offer(T pojo) {
        try {
            //阻塞方式放入队列
            sysQueue.offer(pojo);
            log.info("save data into queue success!");
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            log.info("save data failed：{}", e.getMessage());
        }
    }

    /**
     * 启动日志处理任务
     */
    private void startTask() {
        executor.submit(() -> {
            while (true) {
                try {
                    T pojo = sysQueue.take();
                    log.info("get data from queue:{}", JSON.toJSONString(pojo));
                    sysMapper.insert(pojo);
                    log.info("save data into database success!");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("save data failed：{}", e.getLocalizedMessage());
                    break; //任务执行异常 则让退出循环
                }
            }
        });
    }

    public void shutdown() {
        executor.shutdown();
    }
}
