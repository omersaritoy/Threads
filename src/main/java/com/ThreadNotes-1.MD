# Process vs Thread

Process: Bilgisayarında bir program çalıştırdığında (mesela IntelliJ IDEA'yı açtığında, ya da java Main komutunu çalıştırdığında), işletim sistemi o program için bağımsız bir çalışma alanı oluşturur. Buna process denir.

Her process'in kendine ait:
- Kendi bellek alanı (memory space) — başka process'ler bu belleğe direkt erişemez
- Kendi kaynakları (açık dosyalar, network bağlantıları vb.)
- En az bir thread'i vardır (her process en az bir thread ile başlar)


Örnek: Aynı anda Chrome açık, IntelliJ açık, Spotify açık. Bunların her biri ayrı bir process. Chrome çökerse, IntelliJ'i etkilemez — çünkü bellekleri tamamen izole.

Thread:Bir process'in içinde çalışan, o process'in kaynaklarını paylaşan bir yürütme birimidir. Yani thread, process'in "alt işçisi" gibi düşünülebilir.
Senin Main.java dosyanı çalıştırdığında, JVM bir process başlatır ve bu process içinde en az bir thread (main thread) otomatik oluşur.
Sen new Thread(() -> {...}).start() dediğinde, aynı process içinde ikinci bir thread daha oluşturmuş oluyorsun.


# Thread Nasıl Oluşturulur ve Yaşam Döngüsü
1) Thread sınıfını extend etmek
    class MyThread extends Thread {
        public void run() {
            System.out.println("Çalışıyorum");
        }
    }
    new MyThread().start();
2) Runnable implement etmek (daha çok tercih edilir)
   
         Runnable r = () -> System.out.println("Çalışıyorum");
         new Thread(r).start();
3) ExecutorService ile (modern, tercih edilen yöntem)

         executorService.execute(() -> System.out.println("Çalışıyorum"));
Runnable'ı tercih etme sebebi: Java'da tek sınıftan kalıtım (extends) alabilirsin, ama birden fazla interface implement edebilirsin.
Eğer Thread'i extend edersen, başka bir sınıftan kalıtım alma hakkını kaybedersin. Runnable kullanırsan bu esnekliği korursun.

# start() vs run() — çok kritik bir fark
    
    Thread t = new Thread(() -> System.out.println("Merhaba"));

    t.run();   // YANLIŞ kullanım şekli
    t.start(); // DOĞRU kullanım şekli
- t.start() → gerçekten yeni bir thread oluşturur, işletim sistemine "bana yeni bir iş parçacığı ayır" der, ve run() metodunun içeriği o yeni thread'de çalışır.
- t.run() → hiçbir yeni thread oluşturmaz! Sadece normal bir metod çağrısı gibi, mevcut thread (örneğin main thread) üzerinde çalışır. Yani t.run() yazarsan, paralellik falan olmaz, sıradan bir fonksiyon çağırmış gibi olursun.


# Thread Yaşam Döngüsü (Lifecycle)

Bir thread'in ömrü boyunca geçtiği durumlar:

    NEW → RUNNABLE → (BLOCKED / WAITING / TIMED_WAITING) → TERMINATED

# Durum	            Ne anlama gelir
NEW	                new Thread(...) ile oluşturuldu ama start() henüz çağrılmadı
RUNNABLE	        start() çağrıldı, thread çalışmaya hazır veya çalışıyor (CPU'yu bekliyor ya da kullanıyor)
BLOCKED	            Bir kilidi (lock) beklerken (mesela synchronized bloğuna girmeye çalışıyor ama başka thread o kilidi tutuyor)
WAITING	            Süresiz olarak başka bir thread'in bir sinyal vermesini bekliyor (wait(), join() gibi)
TIMED_WAITING	    Belirli bir süre bekliyor (senin kodundaki Thread.sleep(1000) tam olarak bu durum)
TERMINATED	        run() metodu tamamlandı, thread işini bitirdi, artık geri döndürülemez

# Concurrency vs Parallelism

Concurrency: Aynı anda birden fazla işi "yönetmek" demektir — ama bunların gerçekten aynı anda çalışması şart değil.
Tek bir CPU çekirdeği bile, işler arasında çok hızlı geçiş yaparak (context switching) "sanki aynı anda çalışıyorlarmış" hissi verebilir.

Parallelism: Gerçekten aynı anda, aynı zaman diliminde birden fazla işin çalışması demektir. Bunun için birden fazla CPU çekirdeği gerekir.
