package com.threads;

public class HesapTransfer {
    private int bakiye;
    private final String isim;

    public HesapTransfer(String isim, int baslangicBakiye) {
        this.isim = isim;
        this.bakiye = baslangicBakiye;
    }

    // BİLEREK YANLIŞ - deadlock'a açık
    public static void transferYap(HesapTransfer gonderen, HesapTransfer alan, int miktar) {
        synchronized (gonderen) {
            System.out.println(Thread.currentThread().getName() + ": " + gonderen.isim + " kilidini aldı");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            synchronized (alan) {
                System.out.println(Thread.currentThread().getName() + ": " + alan.isim + " kilidini de aldı");
                gonderen.bakiye -= miktar;
                alan.bakiye += miktar;
                System.out.println(Thread.currentThread().getName() + ": Transfer tamam - "
                        + gonderen.isim + "→" + alan.isim + ": " + miktar);

            }

        }

    }
    public static void main(String[] args) throws InterruptedException {
        HesapTransfer hesapA = new HesapTransfer("Hesap-A", 1000);
        HesapTransfer hesapB = new HesapTransfer("Hesap-B", 1000);

        // Thread 1: A'dan B'ye transfer
        Thread t1 = new Thread(() -> {
            transferYap(hesapA, hesapB, 100);
        }, "Thread-1(A->B)");

        // Thread 2: B'den A'ya transfer (TERS SIRA!)
        Thread t2 = new Thread(() -> {
            transferYap(hesapB, hesapA, 50);
        }, "Thread-2(B->A)");

        t1.start();
        t2.start();

        t1.join(5000); // en fazla 5 saniye bekle
        t2.join(5000);

        if (t1.isAlive() || t2.isAlive()) {
            System.out.println("!!! DEADLOCK OLUŞTU - thread'ler hâlâ çalışıyor/bekliyor !!!");
        } else {
            System.out.println("İşlem tamamlandı. Hesap-A: " + hesapA.bakiye + ", Hesap-B: " + hesapB.bakiye);
        }
    }
}