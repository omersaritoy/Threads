package com.threads;

public class HesapTransferDuzeltilmis {
    private int bakiye;
    private final String isim;
    private final int id; // benzersiz, sabit bir sıralama kriteri

    private static int sayac = 0;

    public HesapTransferDuzeltilmis(String isim, int baslangicBakiye) {
        this.isim = isim;
        this.bakiye = baslangicBakiye;
        this.id = sayac++;
    }

    public static void transferYap(HesapTransferDuzeltilmis gonderen, HesapTransferDuzeltilmis alan, int miktar) {
        // HER ZAMAN küçük id'li hesabı önce kilitle
        HesapTransferDuzeltilmis ilkKilit = gonderen.id < alan.id ? gonderen : alan;
        HesapTransferDuzeltilmis ikinciKilit = gonderen.id < alan.id ? alan : gonderen;

        synchronized (ilkKilit) {
            System.out.println(Thread.currentThread().getName() + ": " + ilkKilit.isim + " kilidini aldı");

            try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

            synchronized (ikinciKilit) {
                System.out.println(Thread.currentThread().getName() + ": " + ikinciKilit.isim + " kilidini de aldı");

                gonderen.bakiye -= miktar;
                alan.bakiye += miktar;
                System.out.println(Thread.currentThread().getName() + ": Transfer tamam - "
                        + gonderen.isim + "→" + alan.isim + ": " + miktar);
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {
        HesapTransferDuzeltilmis hesapA = new HesapTransferDuzeltilmis("Hesap-A", 1000); // id=0
        HesapTransferDuzeltilmis hesapB = new HesapTransferDuzeltilmis("Hesap-B", 1000); // id=1

        Thread t1 = new Thread(() -> transferYap(hesapA, hesapB, 100), "Thread-1(A->B)");
        Thread t2 = new Thread(() -> transferYap(hesapB, hesapA, 50), "Thread-2(B->A)");

        t1.start();
        t2.start();
        t1.join(5000);
        t2.join(5000);

        if (t1.isAlive() || t2.isAlive()) {
            System.out.println("!!! DEADLOCK OLUŞTU !!!");
        } else {
            System.out.println("İşlem tamamlandı. Hesap-A: " + hesapA.bakiye + ", Hesap-B: " + hesapB.bakiye);
        }
    }
}


