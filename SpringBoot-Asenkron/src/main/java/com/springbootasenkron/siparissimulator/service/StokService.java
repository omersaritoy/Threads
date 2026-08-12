package com.springbootasenkron.siparissimulator.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StokService {

    private final AtomicInteger stok = new AtomicInteger(100);

    @Async("siparisTaskExecutor")
    public CompletableFuture<Boolean> stoktanDus(int siparisId, int adet) {
        System.out.println("Stok kontrolü başladı, sipariş: " + siparisId + ", thread: " + Thread.currentThread().getName());

        try {
            Thread.sleep(1500); // veritabanı sorgusu simülasyonu
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean basarili = false;
        while (true) {
            int mevcutStok = stok.get();
            if (mevcutStok < adet) {
                break; // yetersiz stok, basarili = false kalır
            }
            int yeniStok = mevcutStok - adet;
            if (stok.compareAndSet(mevcutStok, yeniStok)) {
                basarili = true;
                break;
            }
        }

        System.out.println("Stok kontrolü bitti, sipariş: " + siparisId + ", sonuç: " + basarili + ", kalan stok: " + stok.get());
        return CompletableFuture.completedFuture(basarili);
    }

    public int getStok() {
        return stok.get();
    }
}