package com.springbootasenkron.completableFutreExample;


//Neden void her zaman yeterli değil:
//void dönen @Async metodlar "fire and forget" (ateşle unut) için iyidir — sonucu asla öğrenemezsin. Ama bazen sonuca ihtiyacın olur. Bunun için CompletableFuture<T> dönüş tipi kullanılır.


import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Service
public class StokService {

    @Async
    public CompletableFuture<Integer> stokKontrolEt(String urunId) {
        System.out.println("Stok kontrolü başladı, thread:" + Thread.currentThread().getName());

        try {
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        int stokAdedi = 42;
        return CompletableFuture.completedFuture(stokAdedi);
    }


}

@Service
class FiyatService {

    @Async
    public CompletableFuture<Double> fiyatGetir(String urunId) {
        System.out.println("Fiyat sorgusu başladı, thread: " + Thread.currentThread().getName());
        try {
            Thread.sleep(3000); // başka bir uzun sorgu simülasyonu
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return CompletableFuture.completedFuture(199.99);
    }
}


@Service
 class UrunService {
    private final StokService stokService;
    private final FiyatService fiyatService;

    UrunService(StokService stokService, FiyatService fiyatService) {
        this.stokService = stokService;
        this.fiyatService = fiyatService;
    }

    public String urunBilgisiGetir(String urunId) throws Exception {
        long baslangic = System.currentTimeMillis();

        // İkisi de HEMEN başlar, ikisi de arka planda paralel çalışır
        CompletableFuture<Integer> stokFuture = stokService.stokKontrolEt(urunId);
        CompletableFuture<Double> fiyatFuture = fiyatService.fiyatGetir(urunId);

        // thenCombine ile ikisinin sonucunu birleştir
        CompletableFuture<String> sonuc = stokFuture.thenCombine(fiyatFuture, (stok, fiyat) -> {
            return "Stok: " + stok + ", Fiyat: " + fiyat;
        });

        String cevap = sonuc.get(); // burada gerçek sonucu bekle

        long sure = System.currentTimeMillis() - baslangic;
        System.out.println("Toplam süre: " + sure + " ms");

        return cevap;
    }
}
@RestController
class UrunController {

    private final UrunService urunService;

    public UrunController(UrunService urunService) {
        this.urunService = urunService;
    }

    @GetMapping("/urun/{id}")
    public String urunGetir(@PathVariable String id) throws Exception {
        return urunService.urunBilgisiGetir(id);
    }
}