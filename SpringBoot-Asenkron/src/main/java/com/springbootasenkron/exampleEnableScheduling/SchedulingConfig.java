package com.springbootasenkron.exampleEnableScheduling;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
public class SchedulingConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler scheduler=new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5); // 5 thread, artık paralel çalışabilirler
        scheduler.setThreadNamePrefix("scheduled-task-");
        scheduler.initialize();
        taskRegistrar.setTaskScheduler(scheduler);
    }

}
