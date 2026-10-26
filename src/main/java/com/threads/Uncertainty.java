package com.threads;

public class Uncertainty {
    public static void main(String[] args) throws InterruptedException {
        Runnable is=()->{
            for(int i=1;i<=5;i++)
                System.out.println(Thread.currentThread().getName()+"->"+i);
        };
        Thread a=new Thread(is,"A");
        Thread b=new Thread(is,"B");
        a.start();
        b.start();
        a.join();
        b.join();

        System.out.println("All Finished");
    }
}
