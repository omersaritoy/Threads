package com.threads;

public class InterruptExample {
    public static void main(String[] args) throws InterruptedException {
        Thread t=new Thread(()->{
            while (!Thread.currentThread().isInterrupted()){
                try {
                    System.out.println("Working...");
                    Thread.sleep(300);

                } catch (InterruptedException e) {
                    System.out.println("I'm interrupted");
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        t.start();
        Thread.sleep(1000);
        t.interrupt();
        t.join();

        Thread d = new Thread(() -> {
            while (true) {
                System.out.println("arka plan...");
                try { Thread.sleep(500); } catch (InterruptedException e) {}
            }
        });
        d.setDaemon(true);   // start()'tan ÖNCE çağrılmalı
        d.start();
        Thread.sleep(1200);
        System.out.println("main bitiyor");  // daemon olduğu için JVM kapanır
    }
}
