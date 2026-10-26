package com.threads;

import org.w3c.dom.ls.LSOutput;

public class CreatingThreadExtendClass extends Thread{
    @Override public void run(){
        System.out.println("Hello:"+getName());
    }

    public static void main(String[] args) {
        new CreatingThreadExtendClass().start();
        Runnable is = () -> System.out.println("Hello: " + Thread.currentThread().getName());
        Thread t = new Thread(is, "worker-1");
        t.start();
    }
}
