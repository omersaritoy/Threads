package com.threads;

public class CreateThread {
    public static void main(String[] args) throws InterruptedException {
        Runnable task=()->{
            System.out.println("Current thread: "+Thread.currentThread().getName());
        };
        Thread t=new Thread(task,"My-Thread");
        System.out.println("Before Start:"+t.getState());//new
        t.start();
        t.join();//wait until finish
        System.out.println("After Finish:"+t.getState());


    }
}
