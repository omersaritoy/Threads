# Future ve CompletableFuture


Future, submit() çağrıldığında dönen, "ileride gelecek bir sonucun yer tutucusu" nesnesiydi. Ama Future'ın büyük bir eksikliği var — şimdi onu görelim.

Future'ın sınırlamaları

1) Zincirleme (chaining) yapamazsın
   Diyelim ki şu akışı istiyorsun: "Bir sayı hesapla → sonucu 2 ile çarp → sonucu ekrana yazdır." Her adım bir öncekinin sonucuna bağlı. Future ile bunu yapmak çok can sıkıcı:

       Future<Integer> f1 = executor.submit(() -> 10 + 20);
       Integer sonuc1 = f1.get();               // BLOKLA, bekle
       Future<Integer> f2 = executor.submit(() -> sonuc1 * 2);
       Integer sonuc2 = f2.get();               // TEKRAR BLOKLA, bekle
       System.out.println(sonuc2);
Her .get() çağrısı thread'i bloklar. Yani asenkron başladık ama aslında zincirleme yaparken tekrar senkron (bloklayan) bir yapıya dönüyoruz — asenkron programlamanın amacı bu değildi!

2) Birden fazla Future'ı birleştirmek zor
   "İki farklı hesaplamayı paralel yap, ikisi de bitince sonuçlarını birleştir" gibi bir şeyi Future ile yapmak çok manuel ve karmaşık kod gerektirir.
3) Hata yönetimi klunky
   Future.get() bir ExecutionException fırlatabilir, ama "hata olursa şunu yap, olmazsa bunu yap" gibi akıcı bir yapı sunmaz — hep try-catch ile elle yönetmen gerekir.

# Çözüm: CompletableFuture (Java 8+)
CompletableFuture<T>, Future'ın çok daha güçlü, zincirleme (chaining) destekli, non-blocking (bloklamayan) bir versiyonudur.

    CompletableFuture<Integer> cf = CompletableFuture.supplyAsync(() -> {
    // arka planda, ayrı bir thread'de çalışır
        return 10 + 20;
    });

supplyAsync(), bir Supplier<T> (değer üreten fonksiyon) alır ve bunu arka planda bir thread'de (varsayılan olarak JVM'in ortak thread pool'unda — ForkJoinPool.commonPool()) çalıştırır, hemen bir CompletableFuture döner (bloklamaz).

    CompletableFuture<Integer> sonuc = CompletableFuture
    .supplyAsync(() -> 10 + 20)           // (1) hesapla: 30
    .thenApply(x -> x * 2)                // (2) 30 * 2 = 60
    .thenApply(x -> x + 5);               // (3) 60 + 5 = 65

    sonuc.thenAccept(deger -> System.out.println("Sonuç: " + deger)); // 65

Her adım öncekinin bitmesini bekler, ama bunu bloklamadan yapar — .thenApply() sadece "bu iş bitince, şunu yap" demek, bloklayan bir .get() çağırmadan.


Önemli metodlar
# Metod	                        Ne yapar
supplyAsync(Supplier<T>)	    Asenkron başlat, bir değer üret
runAsync(Runnable)	            Asenkron başlat, değer üretme (sonuç yok)
thenApply(Function)	            Sonucu dönüştür (senkron devam, ama zincir devam eder)
thenApplyAsync(Function)	    Sonucu dönüştür, ama başka bir thread'de çalıştır
thenAccept(Consumer)	        Sonucu kullan ama başka bir şey döndürme (örnek: yazdırma)
thenCompose(Function)	        Sonucu, başka bir CompletableFuture döndüren bir fonksiyona bağla (zincirleme async işlemler)
thenCombine(other, BiFunction)	İki farklı CompletableFuture'ın sonucunu birleştir
exceptionally(Function)	        Hata olursa, alternatif bir değer döndür
handle(BiFunction)	            Hem başarı hem hata durumunu tek yerde ele al
join()	                        get() gibi ama checked exception fırlatmaz (unchecked fırlatır)

thenApply vs thenCompose — çok karıştırılan bir fark

    // thenApply: fonksiyon DÜZ bir değer döner
    CompletableFuture<Integer> cf1 = CompletableFuture
    .supplyAsync(() -> 10)
    .thenApply(x -> x * 2); // Integer döner → CompletableFuture<Integer>
    
    // thenCompose: fonksiyon BAŞKA BİR CompletableFuture döner (zincirleme async işlemler)
    CompletableFuture<Integer> cf2 = CompletableFuture
    .supplyAsync(() -> 10)
    .thenCompose(x -> CompletableFuture.supplyAsync(() -> x * 2)); // CompletableFuture<Integer> döner

Bunu Optional'daki map (≈thenApply) ile flatMap (≈thenCompose) farkına benzetebilirsin — eğer fonksiyonun kendisi zaten bir CompletableFuture döndürüyorsa ve thenApply kullanırsan, 
elinde CompletableFuture<CompletableFuture<Integer>> gibi iç içe (nested) bir yapı kalır, bu genelde istenmez. thenCompose bunu "düzleştirir" (flatten eder).

İki asenkron işi birleştirme — thenCombine

        CompletableFuture<Integer> hesap1 = CompletableFuture.supplyAsync(() -> {
        // uzun sürüyor, mesela 2 saniye
             return 10;
        });
        
        CompletableFuture<Integer> hesap2 = CompletableFuture.supplyAsync(() -> {
        // uzun sürüyor, mesela 3 saniye
        return 20;
        });
        
        CompletableFuture<Integer> toplam = hesap1.thenCombine(hesap2, (a, b) -> a + b);
        
        System.out.println(toplam.join()); // 30

ritik nokta: hesap1 ve hesap2 paralel çalışır (ikisi de aynı anda başlar), toplam süre max(2s, 3s) = 3 saniye civarındadır — sırayla yapılsaydı 2+3=5 saniye sürerdi. Bu, CompletableFuture'ın gerçek gücünü gösteren bir örnek.


# Hata yönetimi

    CompletableFuture<Integer> cf = CompletableFuture
    .supplyAsync(() -> {
        if (true) throw new RuntimeException("Bir hata oluştu!");
        return 10;
    })
    .exceptionally(hata -> {
        System.out.println("Hata yakalandı: " + hata.getMessage());
        return -1; // hata durumunda kullanılacak varsayılan değer
    });

    System.out.println(cf.join()); // -1
exceptionally, zincirdeki herhangi bir adımda hata olursa devreye girer — bloklayan bir try-catch yazmana gerek kalmaz, akış bozulmadan devam eder.


Birden fazla CompletableFuture'ı toplu bekleme

    CompletableFuture<Void> hepsi = CompletableFuture.allOf(hesap1, hesap2, hesap3);
    hepsi.join(); // hepsi bitene kadar bekle
    
    CompletableFuture<Object> herhangiBiri = CompletableFuture.anyOf(hesap1, hesap2, hesap3);
    herhangiBiri.join(); // hangisi ÖNCE biterse, o sonucu al

# Hangi thread'de çalışıyor? — Önemli detay

Varsayılan olarak supplyAsync, thenApply gibi metodlar, JVM'in ortak fork-join pool'unu kullanır (ForkJoinPool.commonPool()) — bu, senin oluşturduğun bir
ExecutorService değil, JVM genelinde paylaşılan bir havuz. Eğer kendi thread pool'unu kullanmak istersen, Async ekli metodların ikinci parametresine kendi executor'ını verebilirsin:

    ExecutorService myExecutor = Executors.newFixedThreadPool(4);

    CompletableFuture.supplyAsync(() -> {
        return 10 + 20;
    }, myExecutor);  // <-- kendi thread pool'unu belirt


# Future vs CompletableFuture — özet karşılaştırma


	                                            Future	                                CompletableFuture
Zincirleme (chaining)	                        Yok, manuel yönetim gerekir	            thenApply, thenCompose vb. ile doğal
Bloklamadan sonuç işleme	                    Yok, .get() hep bloklar	                thenAccept, thenApply bloklamadan devam eder
Birden fazla işi birleştirme	                Manuel, karmaşık	                    thenCombine, allOf, anyOf ile kolay
Hata yönetimi	                                try-catch ile manuel	                exceptionally, handle ile akış içinde
Manuel tamamlama	                            Yok	                                    complete(value) ile dışarıdan elle tamamlanabilir