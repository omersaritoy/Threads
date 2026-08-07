# ExecutorService ve Thread Pool Mantığı

Neden thread pool'a ihtiyaç var?

Diyelim ki bir web sunucun var ve her gelen istek için new Thread(...) oluşturup çalıştırıyorsun:

    // KÖTÜ yaklaşım
    serverSocket.accept(); // istek geldi
    new Thread(() -> islemYap(istek)).start(); // her istek için YENİ thread

Bunun sorunları:
1) Thread oluşturma maliyetlidir — işletim sisteminden yeni bir stack alanı, kaynak tahsisi ister. Saniyede binlerce istek gelirse, binlerce thread oluşturmak sistemi çökertebilir.
2) Sınırsız kaynak tüketimi — çok fazla thread aynı anda çalışırsa, CPU context switching'e boğulur, bellek tükenir (OutOfMemoryError bile alabilirsin).
3) Thread'lerin ölmesi/yeniden oluşturulması — her görev bitince thread ölür, yeni görev gelince yeniden oluşturulur. Bu sürekli yaratma/yok etme maliyeti performansı düşürür.


Çözüm: Thread Pool (İş Parçacığı Havuzu)

Fikir: Sabit sayıda (veya sınırlı büyüyebilen) thread'i önceden oluştur, bir havuzda (pool) tut. Gelen görevleri bu havuzdaki mevcut thread'lere dağıt. Görev bitince thread ölmez, havuza geri döner, yeni bir görev bekler.


                        ┌─────────────────────────┐
    Görev 1 ──┐         │      THREAD POOL         │
    Görev 2 ──┼──kuyruğa─►  [T1] [T2] [T3] [T4]    │
    Görev 3 ──┤         │   (sabit sayıda thread,   │
    Görev 4 ──┘         │    tekrar tekrar kullanılır)│
                        └─────────────────────────┘

Bu, tam olarak restoran benzetmesindeki gibi: garsonlar (thread'ler) sabit sayıda, müşteriler (görevler) gelip gider, garson her müşteriden sonra işten çıkmaz, sırada bekleyen bir sonraki müşteriye hizmet eder.


# Executors sınıfının sunduğu hazır thread pool türleri

ExecutorService executor = Executors.newFixedThreadPool(4);

1) newFixedThreadPool(n) — sabit sayıda thread
             
        ExecutorService executor = Executors.newFixedThreadPool(4);

- Her zaman tam olarak n thread vardır.
- Eğer n thread de doluysa, yeni gelen görevler bir kuyrukta (queue) bekler.
- Kullanım alanı: CPU-yoğun işler, sabit ve öngörülebilir kaynak kullanımı istediğinde (mesela CPU çekirdek sayısına göre boyutlandırma: Runtime.getRuntime().availableProcessors()).

2) newSingleThreadExecutor() — tek thread 
    
        ExecutorService executor = Executors.newSingleThreadExecutor();

- Sadece 1 thread vardır, görevler sırayla (FIFO) işlenir.
- Kullanım alanı: Görevlerin kesinlikle sırayla ve tek thread'de işlenmesi gerektiğinde (mesela bir log dosyasına sırayla yazma, ya da bir kaynağa tek noktadan erişim garantisi istediğinde — böylece senkronizasyon problemleri baştan ortadan kalkar).

3) newCachedThreadPool() — dinamik, esnek

        ExecutorService executor = Executors.newCachedThreadPool();

- Gerektiği kadar thread oluşturur, boşta kalan thread'leri 60 saniye sonra kapatır.
- Risk: Çok fazla görev aynı anda gelirse, sınırsız thread oluşabilir — bu da newFixedThreadPool'un çözmeye çalıştığı "kontrolsüz thread patlaması" sorununu geri getirebilir. Dikkatli kullanılmalı, production'da genelde tercih edilmez.

4) newScheduledThreadPool(n) — zamanlanmış görevler

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

        executor.schedule(() -> System.out.println("5 saniye sonra çalış"), 5, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(() -> System.out.println("her 3 saniyede bir"), 0, 3, TimeUnit.SECONDS);
- Belirli bir gecikmeyle bir kere, ya da periyodik olarak tekrar tekrar çalışacak görevler için.
- Spring Boot'taki @Scheduled annotation'ının arka planda kullandığı mantık aslında buna çok benzer (ileride göreceğiz).

execute() vs submit() — tekrar hatırlatma + derinleştirme

    executor.execute(runnable);              // void döner, sonuç/hata bilgisi alamazsın
    Future<?> f = executor.submit(runnable); // Future<?> döner, get() null verir ama "bitti mi" bilgisi alırsın
    Future<T> f = executor.submit(callable); // Future<T> döner, gerçek bir SONUÇ alırsın

