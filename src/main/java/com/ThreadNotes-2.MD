# Race Condition (Yarış Durumu)
Race condition, birden fazla thread'in aynı paylaşılan veriye, senkronize olmadan, aynı anda erişip değiştirmeye çalışmasıyla oluşan bir durumdur.
onuç, thread'lerin hangi sırayla çalıştığına bağlı olarak her seferinde değişebilir — yani programın davranışı öngörülemez (non-deterministic) hale gelir.

Klasik örnek: counter++ neden tehlikeli?

        private static int counter = 0;
        2 thread aynı anda çalışıyor, her biri counter++ yapıyor 100000 kere

Sen counter++ yazınca bunun tek bir işlem olduğunu düşünürsün. Ama aslında bu, CPU seviyesinde 3 ayrı adıma ayrılır:
1. READ:  counter değerini oku (belleğe git, mesela 5 al)
2. ADD:   okunan değere 1 ekle (5 + 1 = 6)
3. WRITE: yeni değeri (6) geri belleğe yaz

Buna "read-modify-write" denir ve bu 3 adım atomik değildir (yani bölünmez, kesintisiz bir tek işlem değildir). İki thread arasında bu adımlar iç içe geçebilir.

Somut senaryo — nasıl veri kaybolur?
counter = 5 diyelim, iki thread aynı anda counter++ çalıştırıyor:

| Zaman | Thread A           | Thread B                                      |
|-------|--------------------|-----------------------------------------------|
| t1    | READ counter → 5   |                                               |
| t2    |                    | READ counter → 5   (henüz A yazmadı!)         |
| t3    | ADD: 5+1=6         |                                               |
| t4    | WRITE: counter = 6 |                                               |
| t5    |                    | ADD: 5+1=6  (hala eski değerle işlem yapıyor) |
| t6    |                    | WRITE: counter = 6                            |

Sonuç: İki thread de counter++ çalıştırdı ama counter sadece 6 oldu, 7 olması gerekirken! Bir artırma işlemi kayboldu, çünkü Thread B, Thread A'nın henüz yazmadığı eski değeri (5) okudu.

Bunu 1 milyon kere tekrarlarsan (senin ilk kodundaki gibi), binlerce artırma işlemi böyle "kaybolabilir" — sonuç 1.000.000 yerine 999.847 gibi garip bir sayı çıkabilir.


# Visibility Problemi
Visibility problemi, bir thread'in yaptığı değişikliği başka bir thread'in ne zaman göreceğinin garanti olmaması durumudur.
Race condition'dan farklı bir problemdir — race condition "aynı anda yazma" ile ilgiliyken, visibility "yazılan değerin görülüp görülmemesi" ile ilgilidir.

# Neden bu problem var? (Donanım seviyesi)
Modern CPU'larda her çekirdeğin kendi cache'i vardır (L1, L2 cache gibi). Bir thread bir değişkeni değiştirdiğinde, bu değişiklik önce o çekirdeğin kendi cache'ine yazılır. 
Ana belleğe (RAM) hemen yansımayabilir — performans için CPU bunu geciktirebilir.

    CPU Çekirdek 1 (Thread A)          CPU Çekirdek 2 (Thread B)
        [L1 Cache: x=1]                    [L1 Cache: x=0]  ← hala eski değer!
        \                                   /
        \                                 /
        \---------- RAM: x=1 ----------/
Thread A, x=1 yazdı ve bu kendi cache'inde. RAM'e henüz yansımamış olabilir. Thread B aynı anda x'i okursa, 
ya RAM'deki eski değeri (henüz güncellenmemiş) ya da kendi cache'indeki bambaşka bir değeri görebilir. Garantisi yok.

# Neden bu problem var? (JVM/derleyici seviyesi)
Donanım seviyesinin üstüne bir de şu eklenir: JIT compiler (derleyici) optimizasyonu. JVM, kodu çalıştırırken performans için bazı optimizasyonlar yapar. Örneğin bir thread aynı değişkeni döngü içinde defalarca okuyorsa, derleyici şöyle düşünebilir:

    "Bu thread'in kendi bakış açısından bu değişken hiç değişmiyor (çünkü bu thread'in kendi kodu onu değiştirmiyor). 
    O zaman her seferinde belleğe gitmeye gerek yok, bir kere okuyup register'da (çok hızlı erişilen CPU deposu) tutayım."


Bu optimizasyon tek thread'li kod için tamamen doğrudur ve performansı artırır. Ama çok thread'li ortamda tehlikelidir: derleyici, "bu değişkeni başka bir thread değiştirebilir"
ihtimalini hesaba katmaz (katmasını sağlamak senin işin — volatile ile).


volatile değişkene yazma → sonra o değişkeni okuma
synchronized bloğundan çıkma → sonra aynı kilide girme
Bir thread'i start() etme → başlatılan thread'in içindeki her şey
Bir thread'in bitmesi (join() ile beklenmesi) → join()'den sonraki kod

    private static volatile boolean running = true;

volatile şunu garanti eder:
1. Bir thread volatile değişkene yazdığında, bu değişiklik direkt ana belleğe yazılır (cache'te takılı kalmaz)
2. Bir thread volatile değişkeni okuduğunda, her zaman ana bellekten okur (kendi cache'inden değil)
3. Derleyici bu değişkeni register'a alıp "donduramaz" — her okuma gerçek bir bellek erişimidir

Yani volatile, happens-before ilişkisi kurar: bir thread'in volatile değişkene yazması, 
başka bir thread'in o değişkeni okumasından önce gerçekleşmiş sayılır ve görünürlük garanti edilir.

Önemli: volatile sadece visibility garantisi verir, atomicity (bölünmezlik) garantisi vermez! Yani volatile int counter olsa bile counter++ hâlâ 3 adımlık (read-modify-write) bir işlemdir ve race condition'a karşı korumasızdır. volatile, tek bir thread'in yazdığı,
diğerlerinin sadece okuduğu senaryolar için idealdir (örnek: bir "durdur" bayrağı).

# synchronized

Hem visibility hem atomicity garantisi verir (bir sonraki başlıkta detaylı işleyeceğiz).

Atomic sınıflar

AtomicInteger, AtomicBoolean gibi sınıflar, hem visibility hem atomicity garantisi verir, synchronized'a göre daha performanslıdır (bir sonraki başlıklarda göreceğiz).
