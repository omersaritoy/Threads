package com.springbootasenkron.exampleForAsync;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class HesaplamaService {

    @Async
    public CompletableFuture<Integer> hesapla(int sayi){
        int sonuc=sayi*sayi;
        return CompletableFuture.completedFuture(sonuc);
    }

}
@RestController
 class MyController {
    private final HesaplamaService hesaplamaService;


    MyController(HesaplamaService hesaplamaService) {
        this.hesaplamaService = hesaplamaService;
    }

    @GetMapping("/hesapla")
    public String hesapla() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future=hesaplamaService.hesapla(5);
        Integer sonuc=future.get();
        return "Sonuc:"+sonuc;
    }
}
