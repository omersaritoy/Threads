package com.threads;

public class RaceConditionTest {
    private static int counter = 0;

    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            for (int i = 0; i < 100_000; i++) {
                counter++;
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();

        t1.join(); // t1 bitene kadar bekle
        t2.join(); // t2 bitene kadar bekle

        System.out.println("Beklenen: 200000, Gerçek: " + counter);
    }
}