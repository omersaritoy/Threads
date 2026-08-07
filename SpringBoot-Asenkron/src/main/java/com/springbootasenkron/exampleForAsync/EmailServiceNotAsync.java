package com.springbootasenkron.exampleForAsync;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
public class EmailServiceNotAsync {
    public void mailGonder(String adres) {
        // 3 saniye sürüyor (SMTP sunucusuna bağlanma vb.)
    }
}

@RestController
class  UserController{
    private final EmailServiceNotAsync emailService;

    UserController(EmailServiceNotAsync emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/kayit")
    public String kayitOl() {
        emailService.mailGonder("user@example.com"); // 3 saniye burada BLOKLAR
        return "Kayıt tamam";  // kullanıcı 3 saniye bekledi
    }
}