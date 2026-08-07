package com.springbootasenkron.exampleForAsync;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
public class EmailService {

   // @Async koyduğun metod çağrıldığında, Spring bu çağrıyı otomatik olarak bir thread pool'a devreder —
    // tıpkı senin executorService.execute(...) yazman gibi, ama Spring bunu senin için arka planda yapıyor.
    @Async
    public void mailGonder(String message){
        // artık bu metod ÇAĞRILDIĞI anda geri döner,
        // gerçek iş ARKA PLANDA başka bir thread'de çalışır
    }

}
//⚠️ Aynı sınıf içinden @Async metodu çağırırsan çalışmaz!
//@Service
//public class UserService {
//
//    public void kayitOl() {
//        mailGonder(); // AYNI SINIF İÇİNDEN çağrı — @Async DEVREye GİRMEZ, senkron çalışır!
//    }
//
//    @Async
//    public void mailGonder() {
//        // ...
//    }
//}

@RestController
class  UserController2{
    private final EmailService emailService;

    UserController2(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/kayit2")
    public String kayitOl() {
        emailService.mailGonder("user@example.com"); // 3 saniye burada BLOKLAR
        return "Kayıt tamam";  // kullanıcı 3 saniye bekledi
    }
}
