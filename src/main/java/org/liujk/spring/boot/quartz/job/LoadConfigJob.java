package org.liujk.spring.boot.quartz.job;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * 将数据库重配置的任务同步到调度引擎里面去
 */
public class LoadConfigJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println(new Date() + ":start to execute task");
    }
}
