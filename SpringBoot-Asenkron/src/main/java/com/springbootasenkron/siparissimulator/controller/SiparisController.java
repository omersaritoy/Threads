package com.springbootasenkron.siparissimulator.controller;


import com.springbootasenkron.siparissimulator.service.SiparisService;
import com.springbootasenkron.siparissimulator.service.StokService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class SiparisController {

    private final SiparisService siparisService;
    private final StokService stokService;

    public SiparisController(SiparisService siparisService, StokService stokService) {
        this.siparisService = siparisService;
        this.stokService = stokService;
    }

    @GetMapping("/siparis/{id}")
    public String siparisVer(@PathVariable int id,
                             @RequestParam String urun,
                             @RequestParam int adet,
                             @RequestParam int tutar) throws ExecutionException, InterruptedException {
        System.out.println("İstek geldi, thread: " + Thread.currentThread().getName());
        return siparisService.siparisIsle(id, urun, adet, tutar);
    }

    @GetMapping("/stok")
    public String stokGoster() {
        return "Mevcut stok: " + stokService.getStok();
    }
}
