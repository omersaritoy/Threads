package com.threads;

public class VisibilityProblemi {
    //private static boolean running = true; // volatile YOK, bilerek

    private static volatile boolean running=true;
    public static void main(String[] args) throws InterruptedException {
        Thread worker=new Thread(()->{
            long sayac=0;
            while (running)
                sayac++;
            System.out.println("Worker durdu! Sayaç: " + sayac);
        });
        worker.start();

        Thread.sleep(2000);
        System.out.println("Main: running = false yapıyorum, şu anki zaman: " + System.currentTimeMillis());
        running = false;

        worker.join(5000);

        if (worker.isAlive()) {
            System.out.println("!!! Worker HALA ÇALIŞIYOR, running=false'u görmedi !!!");
        } else {
            System.out.println("Worker durdu, running=false'u gördü.");
        }
    }



}
