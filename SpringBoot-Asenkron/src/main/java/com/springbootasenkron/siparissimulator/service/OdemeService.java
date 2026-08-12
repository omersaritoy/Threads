package com.springbootasenkron.siparissimulator.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class OdemeService {

    @Async("siparisTaskExecutor")
    public CompletableFuture<Boolean> odemeYap(int siparisId, int tutar) {
        System.out.println("Ödeme kontrolü başladı, sipariş: " + siparisId + ", thread: " + Thread.currentThread().getName());

        try {
            Thread.sleep(2000); // ödeme sağlayıcısına bağlanma simülasyonu
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean basarili = tutar > 0; // basit bir kural, gerçek bir ödeme kontrolü simülasyonu
        System.out.println("Ödeme kontrolü bitti, sipariş: " + siparisId + ", sonuç: " + basarili);

        return CompletableFuture.completedFuture(basarili);
    }
}
