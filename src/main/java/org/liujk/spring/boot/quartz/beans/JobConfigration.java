package org.liujk.spring.boot.quartz.beans;

import org.liujk.spring.boot.quartz.job.LoadConfigJob;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfigration {

    @Bean
    public LoadConfigJob loadConfigJob(){
        return new LoadConfigJob();
    }

}
