package com.threads;

public class RaceConditionToplam {
    private static long toplam = 0; // PAYLAŞILAN değişken

    public static void main(String[] args) throws InterruptedException {
        long parcaBoyu = 10_000_000L;
        Thread[] threadler = new Thread[4];

        for (int i = 0; i < 4; i++) {
            final int index = i;
            threadler[i] = new Thread(() -> {
                long baslangic = index * parcaBoyu + 1;
                long bitis = (index + 1) * parcaBoyu;
                for (long j = baslangic; j <= bitis; j++) {
                    toplam += j; // <-- BURASI KRİTİK: paylaşılan değişkene doğrudan ekleme
                }
            });
            threadler[i].start();
        }

        // join() DOĞRU kullanılıyor - zamanlama sorunu YOK
        for (int i = 0; i < 4; i++) {
            threadler[i].join();
        }

        System.out.println("Sonuç: " + toplam);

        long n = 40_000_000L;
        long dogruSonuc = n * (n + 1) / 2;
        System.out.println("Doğrulama (formülle): " + dogruSonuc);
        System.out.println("Eşleşiyor mu: " + (toplam == dogruSonuc));
    }
}
