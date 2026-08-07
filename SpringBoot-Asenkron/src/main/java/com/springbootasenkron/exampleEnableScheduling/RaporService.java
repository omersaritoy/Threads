package com.springbootasenkron.exampleEnableScheduling;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RaporService {
    @Scheduled(fixedRate = 5000) // her 5000ms'de (5 saniyede) bir çalıştır
    public void raporOlustur() {
        System.out.println("Rapor oluşturuluyor: " + LocalDateTime.now());
    }
    @Scheduled(fixedDelay = 5000) // önceki iş bitince, 5 saniye BEKLE, sonra yeniden başlat
    public void temizlikYap() {
        System.out.println("Temizlik yapılıyor: " + LocalDateTime.now());
    }
    @Scheduled(fixedRate = 5000, initialDelay = 10000) // uygulama başladıktan 10 saniye sonra İLK çalışma, sonra her 5 saniyede
    public void ilkGecikmeliGorev() {
        System.out.println("Çalıştı: " + LocalDateTime.now());
    }
    @Scheduled(cron = "0 0 3 * * *") // her gün saat 03:00'te
    public void geceYarisiTemizlik() {
        System.out.println("Gece temizliği: " + LocalDateTime.now());
    }
    @Scheduled(fixedRate = 1000)
    public void gorev1() {
        // 5 saniye sürüyor
    }

    @Scheduled(fixedRate = 1000)
    public void gorev2() {
        // bu, gorev1 bitene kadar HİÇ ÇALIŞMAZ (aynı tek thread'i paylaşıyorlar)
    }
    @Async
    @Scheduled(fixedRate = 5000)
    public void gorev() {
        // artık @Async'in thread pool'unda paralel çalışır
    }
}
