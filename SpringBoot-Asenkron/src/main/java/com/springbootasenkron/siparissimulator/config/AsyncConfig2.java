package com.springbootasenkron.siparissimulator.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig2 implements AsyncConfigurer {
    @Bean(name = "siparisTaskExecutor")
    public Executor siparisTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("Siparis-Async-");
        executor.initialize();
        return executor;
    }
    @Override
    public Executor getAsyncExecutor(){
        return siparisTaskExecutor();
    }
}
