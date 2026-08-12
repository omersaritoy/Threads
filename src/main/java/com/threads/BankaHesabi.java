package com.threads;

public class BankaHesabi {
    private int bakiye;

    public BankaHesabi(int baslangicBakiye) {
        this.bakiye = baslangicBakiye;
    }

    public boolean paraCek(int miktar) {

        if (bakiye >= miktar) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignored) {
            }

            bakiye -= miktar;
            System.out.println(Thread.currentThread().getName() + " çekti: " + miktar + ", kalan bakiye: " + bakiye);
            return true;
        } else {
            System.out.println(Thread.currentThread().getName() + " yetersiz bakiye, çekemedi. Bakiye: " + bakiye);
            return false;
        }

    }

    public int getBakiye() {
        return bakiye;
    }

    public static void main(String[] args) throws InterruptedException {
        BankaHesabi hesap = new BankaHesabi(100); // 100 TL bakiye
        Thread[] threadler = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threadler[i] = new Thread(() -> hesap.paraCek(20)); // her thread 20 TL çekmeye çalışıyor
        }

        for (Thread t : threadler) t.start();
        for (Thread t : threadler) t.join();

        System.out.println("SON BAKİYE: " + hesap.getBakiye());
    }
}
