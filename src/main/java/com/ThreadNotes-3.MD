# Deadlock, Livelock, Starvation

Bunlar, senkronizasyon (kilit/lock) kullanırken ortaya çıkabilecek yan etkilerdir. Yani synchronized veya Lock kullanarak race condition'ı çözersin, 
ama yanlış kullanırsan bu üç yeni sorunla karşılaşabilirsin.

1) Deadlock (Kilitlenme)
   Tanım: İki (veya daha fazla) thread, birbirinin elindeki kilidi (lock) beklerken, sonsuza kadar birbirini bekler durumda kalır. Hiçbiri ilerleyemez.

       Thread 1 ──sahip──> kilitA ──istiyor──> kilitB ──sahip──> Thread 2 ──istiyor──> kilitA
       ↑
       (döngü kapanıyor, deadlock!)
Deadlock'tan kaçınma yolu

En basit ve en yaygın çözüm: kilitleri her zaman aynı sırada al.

    // Her iki thread de ÖNCE kilitA, SONRA kilitB alsın
    synchronized (kilitA) {
        synchronized (kilitB) {
    // ...
        }
    }
Eğer tüm thread'ler kilitleri hep aynı sırayla alıyorsa, "birbirini bekleme döngüsü" oluşamaz.

2) Livelock (Canlı Kilitlenme)

Tanım: Thread'ler bloklanmamıştır, aktif olarak çalışıyorlardır — ama yaptıkları şey sürekli birbirine tepki vermekten ibarettir, hiçbiri gerçek ilerleme kaydedemez.

Benzetme
Dar bir koridorda karşılıklı yürüyen iki kişi düşün. İkisi de nazik davranıp yol vermeye çalışıyor: biri sağa kayıyor, diğeri de aynı anda sola kayıyor (çünkü o da yol vermeye çalışıyor) — hâlâ karşı karşıyalar. Sonra ikisi de ters yöne kayıyor — yine karşı karşıya. 
Bu sonsuza kadar sürebilir. Her ikisi de "hareket ediyor" (deadlock gibi donmuyorlar) ama hiçbiri geçemiyor.

3) Starvation (Açlık)

Tanım: Bir thread, ihtiyaç duyduğu kaynağa (kilit, CPU zamanı vb.) çok uzun süre veya hiçbir zaman erişemez — çünkü diğer thread'ler (genelde daha yüksek öncelikli olanlar) sürekli o kaynağı kapıyor.
Örnek senaryo

Diyelim ki bir kilide erişmeye çalışan 10 thread var. Java'da synchronized bloklarının kilit alma sırası garanti değildir (adil/fair değildir varsayılan olarak). Eğer bazı thread'ler sürekli "şanslı" çıkıp kilidi alıyorsa, bir thread hep en son sırada kalıp hiç kilit alamayabilir — bu starvation'dır.

# Neden olur?
- Yüksek öncelikli (Thread.setPriority()) thread'ler, düşük öncelikli olanları sürekli CPU'dan mahrum bırakabilir
- Adil olmayan (non-fair) kilit mekanizmaları, bazı thread'leri sürekli sıra dışı bırakabilir

Çözüm

ReentrantLock kullanırken fair mode (adil mod) açılabilir:

        Lock lock = new ReentrantLock(true); // true = fair mode

Fair mode, kilidi isteyen sıraya göre (FIFO) dağıtır — yani hiçbir thread sürekli atlanmaz. Ama dezavantajı: fair mode, normal moda göre daha yavaştır (performans kaybı).


Üçünün karşılaştırması
#                           Thread durumu	            Belirti	                                Tipik sebep
Deadlock	                Bloklanmış (donmuş)	        Program tamamen durur	                Kilitleri farklı sırada almak
Livelock	                Aktif ama ilerlemiyor	    CPU kullanılıyor ama iş bitmiyor        Thread'lerin birbirine aşırı "nazik" tepki vermesi
Starvation	    Bazıları çalışıyor, biri hiç sıra bulamıyor	Bir thread hep geride kalıyor	        Adil olmayan zamanlama