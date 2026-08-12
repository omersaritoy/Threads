package com.springbootasenkron.siparissimulator.service;


import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class SiparisService {

    private final StokService stokService;
    private final OdemeService odemeService;

    public SiparisService(StokService stokService, OdemeService odemeService) {
        this.stokService = stokService;
        this.odemeService = odemeService;
    }

    public String siparisIsle(int siparisId, String urunAdi, int adet, int tutar) throws ExecutionException, InterruptedException {
        long baslangic = System.currentTimeMillis();
        // İkisi de HEMEN başlar, arka planda PARALEL çalışır
        CompletableFuture<Boolean> stokFuture = stokService.stoktanDus(siparisId, adet);
        CompletableFuture<Boolean> odemeFuture = odemeService.odemeYap(siparisId, tutar);

        // İkisinin de bitmesini bekleyip sonuçlarını birleştir
        CompletableFuture<String> sonuc = stokFuture.thenCombine(odemeFuture, (stokBasarili, odemeBasarili) -> {
            if (stokBasarili && odemeBasarili) {
                return "Sipariş " + siparisId + " (" + urunAdi + "): BAŞARILI";
            } else if (!stokBasarili) {
                return "Sipariş " + siparisId + " (" + urunAdi + "): BAŞARISIZ - yetersiz stok";
            } else {
                return "Sipariş " + siparisId + " (" + urunAdi + "): BAŞARISIZ - ödeme reddedildi";
            }
        });
        String cevap = sonuc.get(); // gerçek sonucu burada bekle

        long sure = System.currentTimeMillis() - baslangic;
        System.out.println("Toplam işlem süresi: " + sure + "ms");

        return cevap;
    }
}
