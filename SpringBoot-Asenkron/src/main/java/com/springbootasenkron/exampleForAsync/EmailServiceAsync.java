package com.springbootasenkron.exampleForAsync;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
public class EmailServiceAsync {

    @Async
    public void mailGonder(String adres) {
        System.out.println("Mail gönderiliyor, thread: " + Thread.currentThread().getName());
        try {
            Thread.sleep(3000); // 3 saniyelik iş simülasyonu
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Mail gönderildi: " + adres);
    }
}

@RestController
class TestController {
    private final EmailServiceAsync emailService;

    public TestController(EmailServiceAsync emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/kayit")
    public String kayitOl() {
        System.out.println("İstek geldi, thread: " + Thread.currentThread().getName());
        emailService.mailGonder("user@example.com");
        System.out.println("Controller metodu bitiyor");
        return "Kayıt tamam";

    }
}
