package com.threads;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicYetersizDuzeltilmis {
    private static final AtomicInteger bakiyeA = new AtomicInteger(1000);
    private static final AtomicInteger bakiyeB = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Thread transferYapan = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                bakiyeA.addAndGet(-10); // A'dan düş

                try { Thread.sleep(10); } catch (InterruptedException e) { Thread.currentThread().interrupt(); } // PENCEREYİ BÜYÜT

                bakiyeB.addAndGet(10);  // B'ye ekle
            }
        });

        Thread denetciler = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                int a = bakiyeA.get();
                int b = bakiyeB.get();
                int toplam = a + b;
                if (toplam != 1000) {
                    System.out.println("!!! TUTARSIZLIK YAKALANDI !!! A=" + a + ", B=" + b + ", Toplam=" + toplam);
                }
                try { Thread.sleep(1); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        });

        transferYapan.start();
        denetciler.start();
        transferYapan.join();
        denetciler.join();

        System.out.println("Test bitti.");
    }
}