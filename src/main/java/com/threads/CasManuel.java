package com.threads;

import java.util.concurrent.atomic.AtomicInteger;

public class CasManuel {
    public static void main(String[] args) throws InterruptedException {
        AtomicInteger sayac = new AtomicInteger(0);
        Thread[] threadler = new Thread[4];

        for (int i = 0; i < 4; i++) {
            threadler[i] = new Thread(() -> {
                for (int j = 0; j < 50_000; j++) {
                    ikiArtir(sayac);
                }
            });
            threadler[i].start();
        }

        for (Thread t : threadler) t.join();

        System.out.println("Sonuç: " + sayac.get() + " (beklenen: 400000)");
    }
    // KENDİ CAS mantığımızı, incrementAndGet() KULLANMADAN yazıyoruz
    static void ikiArtir(AtomicInteger sayac) {
        while (true) {
            int eskiDeger = sayac.get();          // 1. OKU
            int yeniDeger = eskiDeger + 2;         // 2. HESAPLA
            if (sayac.compareAndSet(eskiDeger, yeniDeger)) { // 3. DENE: hâlâ eskiDeger mi? yaz.
                break; // başarılı, döngüden çık
            }
            // başarısız olursa (başka thread araya girdi), while döngüsü BAŞTAN döner
        }
    }
}
