package com.sidorii.scheduler;

import com.sidorii.scheduler.util.AutowiringSpringBeanJobFactory;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@Configuration
public class RootConfig {

    @Autowired
    private ApplicationContext context;

    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
    }


    @Bean
    public SchedulerFactory schedulerFactory() throws SchedulerException{
        StdSchedulerFactory factory = new StdSchedulerFactory();
        factory.initialize(new ClassPathResource("quartz.properties").getPath());

        return factory;
    }

    @Bean(destroyMethod = "shutdown")
    public Scheduler schedulerBean() throws SchedulerException {
        Scheduler scheduler = schedulerFactory().getScheduler();
        scheduler.setJobFactory(jobFactory());
        scheduler.start();
        return scheduler;
    }

    @Bean
    public SpringBeanJobFactory jobFactory() {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(context);

        return jobFactory;
    }
}
