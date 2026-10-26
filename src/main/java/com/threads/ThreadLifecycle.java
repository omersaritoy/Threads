package com.threads;

public class ThreadLifecycle {
    static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        //NEW ve TERMINATED
        Thread t1 = new Thread(() -> {
        });
        System.out.println("t1 (before start):" + t1.getState());       //new
        t1.start();
        t1.join();
        System.out.println("t1 (bitti): " + t1.getState());          // TERMINATED

        // TIMED_WAITING
        Thread t2=new Thread(()->{
            try {Thread.sleep(1000);} catch (InterruptedException e) {throw new RuntimeException(e);}
        });
        t2.start();
        Thread.sleep(200);
        System.out.println("t2 (sleep): " + t2.getState());           // TIMED_WAITING

        // BLOCKED:
        Thread keeper = new Thread(() -> {
            synchronized (lock) {
                try { Thread.sleep(1000); } catch (InterruptedException e) {}
            }
        });
        Thread waiter = new Thread(() -> {
            synchronized (lock) {  }
        });
        keeper.start();
        Thread.sleep(100);           // tutan'ın kilidi almasını bekle
        waiter.start();
        Thread.sleep(100);
        System.out.println("bekleyen: " + waiter.getState());      // BLOCKED

        // WAITING: başka bir thread'in bitmesini süresiz bekliyor
        Thread t3 = new Thread(() -> {
            try { keeper.join(); } catch (InterruptedException e) {}
        });
        t3.start();
        Thread.sleep(100);
        System.out.println("t3 (join): " + t3.getState());           // WAITING


    }
}
