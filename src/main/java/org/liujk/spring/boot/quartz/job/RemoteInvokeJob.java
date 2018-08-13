package org.liujk.spring.boot.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 远程调用方法job
 */
public class RemoteInvokeJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

    }
}
