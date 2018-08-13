package org.liujk.spring.boot.quartz.beans;

import org.liujk.spring.boot.quartz.job.LoadConfigJob;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.text.ParseException;
import java.util.Properties;

@Configuration
public class QuartzConfigration {


    @Autowired
    private DataSource dataSource;

    @Autowired
    private LoadConfigJob loadConfigJob;

    @Autowired
    private CronTrigger cronTrigger;

    @Bean
    public JobDetail jobDetail() {

        JobDetailFactoryBean jobDetail = new JobDetailFactoryBean();
        try {
            jobDetail.setName("loadConfigJob1");
            jobDetail.setGroup("default");
            jobDetail.setJobClass(LoadConfigJob.class);
            jobDetail.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobDetail.getObject();
    }

    @Bean
    public CronTrigger cronTrigger() {
        CronTriggerFactoryBean cronTrigger = new CronTriggerFactoryBean();
        try {
            cronTrigger.setJobDetail(jobDetail());
            cronTrigger.setName("loadConfigTrigger1");
            cronTrigger.setGroup("default");
            cronTrigger.setCronExpression("*/10 * * * * ?");
            cronTrigger.afterPropertiesSet();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cronTrigger.getObject();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        try {
            schedulerFactoryBean.setOverwriteExistingJobs(true);
            schedulerFactoryBean.setQuartzProperties(quartzProperties());
            schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(true);
            schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContext");
            schedulerFactoryBean.setDataSource(dataSource);
            schedulerFactoryBean.setTriggers(cronTrigger);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return schedulerFactoryBean;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        Properties properties = new Properties();

//        不使用配置文件
//        propertiesFactoryBean.setLocation(new ClassPathResource("/config/quartz.properties"));

//        threadPool
        properties.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        properties.setProperty("org.quartz.threadPool.threadCount", "35");
        properties.setProperty("org.quartz.threadPool.threadPriority", "5");
//        jobinfo store
        properties.setProperty("org.quartz.scheduler.instanceId", "AUTO");
        properties.setProperty("org.quartz.scheduler.instanceName", "JobCluster");
        properties.setProperty("org.quartz.jobStore.misfireThreshold", "60000");
        properties.setProperty("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
        properties.setProperty("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
        properties.setProperty("org.quartz.jobStore.isClustered", "true");
        properties.setProperty("org.quartz.jobStore.useProperties", "false");
        properties.setProperty("org.quartz.jobStore.tablePrefix", "QRTZ_");
        properties.setProperty("org.quartz.jobStore.clusterCheckinInterval", "2000");

        propertiesFactoryBean.setProperties(properties);
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    @Bean(name = "scheduler")
    public Scheduler scheduler() {
        return schedulerFactoryBean().getScheduler();
    }
}
