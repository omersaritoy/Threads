package com.threads;

public class BankaHesabiGenisKilit {
    private int bakiye;
    private final Object lock = new Object();

    public BankaHesabiGenisKilit(int baslangicBakiye) {
        this.bakiye = baslangicBakiye;
    }

    public boolean paraCek(int miktar) {
        System.out.println(Thread.currentThread().getName() + " işlemi başlattı...");

        simuleEtLoglama(); // KİLİT DIŞINDA

        boolean basarili;
        synchronized (lock) {
            if (bakiye >= miktar) {
                bakiye -= miktar;
                basarili = true;
            } else {
                basarili = false;
            }
        }

        if (basarili) {
            simuleEtIslemKaydi(); // KİLİT DIŞINDA
            System.out.println(Thread.currentThread().getName() + " çekti: " + miktar);
        }

        return basarili;
    }

    private void simuleEtLoglama() {
        try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    private void simuleEtIslemKaydi() {
        try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    public static void main(String[] args) throws InterruptedException {
        BankaHesabiGenisKilit hesap = new BankaHesabiGenisKilit(1000);
        Thread[] threadler = new Thread[10];

        long baslangic = System.currentTimeMillis();

        for (int i = 0; i < 10; i++) {
            threadler[i] = new Thread(() -> hesap.paraCek(20));
        }
        for (Thread t : threadler) t.start();
        for (Thread t : threadler) t.join();

        long sure = System.currentTimeMillis() - baslangic;
        System.out.println("Toplam süre: " + sure + "ms, Son bakiye: " + hesap.bakiye);
    }
}