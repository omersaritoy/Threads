package com.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadSafety {
    private int count=0;

    private void incrementAndReport(){
        System.out.print((++count)+" ");
    }

    public static void main(String[] args) {
        ExecutorService service= Executors.newFixedThreadPool(20);
        try {
            ThreadSafety manager=new ThreadSafety();
            for (int i=0;i<10;i++){
                service.submit(()->manager.incrementAndReport());

            }
        }
        finally {
            service.shutdown();
        }

    }
}
