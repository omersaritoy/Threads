package com.springbootasenkron.exampleThreadConfig;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@Async
public class AsyncConfig implements AsyncConfigurer {
//    @Bean(name = "taskExecutor")
//    public Executor taskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(4);       // her zaman hazır bekleyen minimum thread sayısı
//        executor.setMaxPoolSize(10);       // yoğunlukta çıkabilecek maksimum thread sayısı
//        executor.setQueueCapacity(100);    // core pool doluyken görevlerin bekleyeceği kuyruk boyutu
//        executor.setThreadNamePrefix("MyAsync-"); // thread isimlerini tanınabilir yap (log'larda çok işe yarar)
//        executor.initialize();
//        return executor;
 //   }
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, params) -> {
            System.out.println("Async hata yakalandı!");
            System.out.println("Metod: " + method.getName());
            System.out.println("Hata: " + throwable.getMessage());
        };
    }
}

//Eğer sen özel bir thread pool tanımlamazsan, Spring @Async için SimpleAsyncTaskExecutor kullanır. Bunun adı kulağa "basit" geliyor ama gerçek anlamı şudur:

//SimpleAsyncTaskExecutor, HER @Async çağrısı için YENİ bir thread oluşturur, hiçbir havuzlama (pooling) yapmaz, hiçbir sınır yoktur!

//Yani newCachedThreadPool()'un bile daha kötüsü — sınırsız thread patlaması riski gerçek bir production tehlikesi. Bu yüzden production'da mutlaka kendi thread pool'unu

