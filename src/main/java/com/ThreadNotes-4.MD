# synchronized

Bir kod bloğuna veya metoda aynı anda sadece bir thread'in girebilmesini sağlar.
Bu mekanizmaya mutual exclusion (karşılıklı dışlama), kısaca mutex denir.
Ayrıca hem visibility hem atomicity garantisi sağlar.

Her Java nesnesinin bir "kilidi" (monitor) vardır
Java'da her nesne, arka planda kendine ait bir monitor lock (izleyici kilit) taşır. synchronized kullanıldığında, sen aslında "bu nesnenin kilidini al, işini yap, sonra bırak" diyorsun.

Kullanım şekli 1: Senkronize blok

    private final Object lock = new Object();
    private int counter = 0;
    
    public void increment() {
        synchronized (lock) {       // lock nesnesinin kilidini al
             counter++;           // sadece 1 thread bu satıra girebilir
        }                        // kilit otomatik bırakılır
    }

Nasıl çalışır:
1) Bir thread synchronized (lock) bloğuna girmek istediğinde, önce lock nesnesinin kilidinin boş olup olmadığını kontrol eder.
2) Boşsa kilidi alır, bloğa girer, işini yapar.
3) Blok bitince kilit otomatik olarak bırakılır (exception fırlasa bile — bu önemli, try-finally gibi güvenli).
4) Eğer kilit doluysa (başka bir thread tutuyorsa), gelen thread BLOCKED durumuna geçer ve kilit boşalana kadar bekler.


Kullanım şekli 2: Senkronize metod

        public synchronized void increment() {
            counter++;
        }
Bu, yukarıdakiyle aynı anlama gelir, sadece kısayoldur. Fark: burada kilitlenen nesne, metodun ait olduğu this nesnesidir (instance metodlarında). Yani:

        public synchronized void increment() {
            counter++;
        }
        // yukarıdaki, aşağıdakiyle birebir aynı:
        public void increment() {
            synchronized (this) {
                counter++;
            }
        }


Static senkronize metod — dikkat, farklı kilit!

    public static synchronized void increment() {
         counter++;
    }

Deadlock örneğiyle bağlantı

Az önceki Madde 6'da gördüğün deadlock örneğinde, synchronized (kilitA) ve synchronized (kilitB) iki farklı nesnenin kilidini temsil ediyordu. 
İşte bu yüzden iki thread farklı sırayla kilit almaya çalışınca birbirini bekleyip kilitlendiler.


# synchronized'ın maliyeti — neden her yere koymuyoruz?
1) Context switching maliyeti — bir thread kilidi bekleyip BLOCKED olduğunda, işletim sistemi o thread'i "uyutur", kilit boşalınca tekrar "uyandırır". Bu geçişler CPU açısından maliyetlidir.
2) Paralellik kaybı — synchronized bloğuna aynı anda sadece 1 thread girebildiği için, o blok içindeki kod hiç paralel çalışmaz, sırayla (seri) çalışır. Bu, çok thread'li programın avantajını bir noktada "iptal eder".
3) Contention (rekabet) — çok fazla thread aynı kilidi bekliyorsa, hepsi sırada bekler, program yavaşlar.


Kural: synchronized bloğunu mümkün olduğunca küçük ve kısa tut. Sadece gerçekten paylaşılan veriye dokunan satırları kilitle, gereksiz kodu (I/O işlemleri, loglar, hesaplamalar) kilidin dışında bırak.


Kötü örnek (gereksiz uzun kilit)

    public synchronized void islemYap() {
        veritabaninaBaglan();     // yavaş, ama paylaşılan veriyle ilgisi yok
        counter++;                 // sadece bu satır kritik
        dosyayaLogYaz();           // yavaş, paylaşılan veriyle ilgisi yok
    }

İyi örnek (dar kapsamlı kilit)

    public void islemYap() {
        veritabaninaBaglan();      // kilit DIŞINDA
        synchronized (this) {
            counter++;               // sadece kritik satır kilitli
        }
        dosyayaLogYaz();           // kilit DIŞINDA
    }

# volatile
- Ne verir: Sadece visibility (görünürlük) garantisi
- Ne vermez: Atomicity — yani volatile int x olsa da x++ hâlâ race condition'a açıktır
- Ne zaman kullanılır: Tek thread yazıyor, diğerleri sadece okuyorsa (durdur bayrağı gibi)

        private static volatile boolean running = true; // ideal kullanım
        private static volatile int counter = 0;         // x++ için YETERSİZ

# Atomic Sınıflar — Neden Var?

Sorun: synchronized hem visibility hem atomicity verir ama maliyetlidir (thread'i bloklar, context switching yapar). Basit bir sayaç artırma işlemi için tüm bu maliyeti ödemek gerekli mi?

Cevap: Hayır. Java, java.util.concurrent.atomic paketinde, kilitlemeden (lock-free) atomicity sağlayan sınıflar sunar: AtomicInteger, AtomicLong, AtomicBoolean, AtomicReference vb.

# Nasıl çalışıyor? — CAS (Compare-And-Swap) mekanizması

Atomic sınıflar, synchronized gibi thread'i bloklamaz. Bunun yerine CPU'nun donanım seviyesinde sunduğu atomik bir işlem kullanır: CAS (Compare-And-Swap).


CAS mantığı:

    CAS(bellekAdresi, beklenenDeğer, yeniDeğer):
    eğer bellekteki değer == beklenenDeğer ise:
        belleğe yeniDeğer'i yaz
        başarılı (true) dön
    değilse:
        hiçbir şey yapma
        başarısız (false) dön

Bu tek bir CPU komutuyla (CMPXCHG gibi) atomik olarak gerçekleşir — yani bölünemez, kesintiye uğramaz.

AtomicInteger.incrementAndGet() içeride nasıl çalışır?

    public int incrementAndGet() {
    while (true) {
        int eskiDeger = get();               // şu anki değeri oku
        int yeniDeger = eskiDeger + 1;       // hesapla
        if (compareAndSet(eskiDeger, yeniDeger)) { // CAS dene
            return yeniDeger;                 // başarılıysa dön
            }
        // başarısızsa (başka thread araya girdiyse), TEKRAR DENE (döngü)
        }
    }

Ne zaman synchronized, ne zaman Atomic kullanılır?
Tek bir değişkeni basitçe artır/azalt/değiştir gibi işlemler → Atomic (daha hızlı, daha basit)
Birden fazla değişkeni birlikte, tutarlı şekilde değiştirmen gerekiyorsa (mesela banka hesabından para çekip başka hesaba yatırma gibi "ya hep ya hiç" işlemler) → synchronized veya Lock (çünkü Atomic sınıflar sadece tek bir değişken için atomicity garantisi verir, birden fazla değişkenin birlikte tutarlı olmasını garanti edemez)

