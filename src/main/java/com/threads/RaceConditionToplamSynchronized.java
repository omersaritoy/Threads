package com.threads;

public class RaceConditionToplamSynchronized {
    private static long toplam=0;
    private   static final Object lock=new Object();

    public static void main(String[] args) throws InterruptedException {
        long parcaBoyu = 10_000_000L;
        Thread[] threadler = new Thread[4];

        long baslangicZaman = System.currentTimeMillis();

        for (int i=0;i<4;i++){
            final int index=i;
            threadler[i] = new Thread(() -> {
                long baslangic = index * parcaBoyu + 1;
                long bitis = (index + 1) * parcaBoyu;
                for (long j = baslangic; j <= bitis; j++) {
                    synchronized (lock) {
                        toplam += j; // her adımda kilit al/bırak
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
