package com.threads;

import java.util.Arrays;

public class ParalelToplam {
    public static void main(String[] args) throws InterruptedException {
        long[] sonuclar = new long[4];
        long parcaBoyu = 10_000_000L;

        Thread[] threadler = new Thread[4];

        for (int i = 0; i < 4; i++) {
            final int index = i;

            threadler[i] = new Thread(() -> {
                long baslangic = index * parcaBoyu + 1;
                long bitis = (index + 1) * parcaBoyu;
                long toplam = 0;
                for (long j = baslangic; j <= bitis; j++) {
                    toplam += j;
                }
                sonuclar[index] = toplam;
            });
            threadler[i].start();
        }
        for (int i = 0; i < 4; i++) {
            threadler[i].join();
        }

        // BİLEREK join() ÇAĞIRMIYORUZ
        long genelToplam = 0;
        for (long s : sonuclar) {
            genelToplam += s;
        }

        System.out.println("Sonuç: " + genelToplam);

        // Doğrulama için matematiksel formülle karşılaştır: n*(n+1)/2
        long n = 40_000_000L;
        long dogruSonuc = n * (n + 1) / 2;
        System.out.println("Doğrulama (formülle): " + dogruSonuc);
        System.out.println("Eşleşiyor mu: " + (genelToplam == dogruSonuc));
    }
}