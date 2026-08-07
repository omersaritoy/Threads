package com.threads;

import java.util.concurrent.locks.ReentrantLock;

public class RaceConditionToplamLock {
    private static long toplam = 0;
    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        long parcaBoyu = 10_000_000L;
        Thread[] threadler = new Thread[4];

        long baslangicZaman = System.currentTimeMillis();

        for (int i = 0; i < 4; i++) {
            final int index = i;
            threadler[i] = new Thread(() -> {
                long baslangic = index * parcaBoyu + 1;
                long bitis = (index + 1) * parcaBoyu;
                for (long j = baslangic; j <= bitis; j++) {
                    lock.lock();
                    try {
                        toplam += j;
                    } finally {
                        lock.unlock();
                    }
                }
            });
            threadler[i].start();
        }

        for (int i = 0; i < 4; i++) {
            threadler[i].join();
        }

        long sure = System.currentTimeMillis() - baslangicZaman;
        System.out.println("Sonuç: " + toplam + " | Süre: " + sure + "ms");
    }
}