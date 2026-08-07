package com.threads;

import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final AtomicInteger counter=new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Runnable task=()->{
            for (int i=0;i<100000;i++)
                counter.incrementAndGet();
        };

        Thread thread1=new Thread(task);
        Thread thread2=new Thread(task);

        thread1.start(); thread2.start();
        thread1.join(); thread2.join();

        System.out.println(counter.get());
    }
}
