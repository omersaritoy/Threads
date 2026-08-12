package com.threads;

public class BankaHesabiDuzeltilmis {
    private int bakiye;
    private final Object lock = new Object();

    public BankaHesabiDuzeltilmis(int baslangicBakiye) {
        this.bakiye = baslangicBakiye;
    }

    public boolean paraCek(int miktar) {
        synchronized (lock) {              // <-- kontrol VE güncelleme birlikte kilitli
            if (bakiye >= miktar) {
                try { Thread.sleep(1); } catch (InterruptedException e) {}
                bakiye -= miktar;
                System.out.println(Thread.currentThread().getName() + " çekti: " + miktar + ", kalan bakiye: " + bakiye);
                return true;
            } else {
                System.out.println(Thread.currentThread().getName() + " yetersiz bakiye, çekemedi. Bakiye: " + bakiye);
                return false;
            }
        }
    }
    public int getBakiye() {
        return bakiye;
    }
    public static void main(String[] args) throws InterruptedException {
        BankaHesabiDuzeltilmis hesap = new BankaHesabiDuzeltilmis(100);
        Thread[] threadler = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threadler[i] = new Thread(() -> hesap.paraCek(20));
        }

        for (Thread t : threadler) t.start();
        for (Thread t : threadler) t.join();

        System.out.println("SON BAKİYE: " + hesap.getBakiye());
    }
}
