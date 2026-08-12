package com.threads;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicTemel {

    public static void main(String[] args) throws InterruptedException {
        testSenkronizasyonsuz();
        testAtomic();
    }

    static void testSenkronizasyonsuz() throws InterruptedException {
        int[] sayac = {0}; // dizi kullandık çünkü lambda içinde normal int değiştirilemez
        Thread[] threadler = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threadler[i] = new Thread(() -> {
                for (int j = 0; j < 100_000; j++) {
                    sayac[0]++;
                }
            });
            threadler[i].start();
        }
        for (Thread t : threadler) t.join();

        System.out.println("Senkronizasyonsuz sonuç: " + sayac[0] + " (beklenen: 1000000)");
    }

    static void testAtomic() throws InterruptedException {
        AtomicInteger sayac = new AtomicInteger(0);
        Thread[] threadler = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threadler[i] = new Thread(() -> {
                for (int j = 0; j < 100_000; j++) {
                    sayac.incrementAndGet();
                }
            });
            threadler[i].start();
        }
        for (Thread t : threadler) t.join();

        System.out.println("Atomic sonuç: " + sayac.get() + " (beklenen: 1000000)");
    }
}

