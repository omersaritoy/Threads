package com.threads;

public class DeadlockTest {
    public static void main(String[] args) {
        Object kilitA = new Object();
        Object kilitB = new Object();

// Thread 1
        new Thread(() -> {
            synchronized (kilitA) {
                System.out.println("Thread 1: kilitA alındı");
                try { Thread.sleep(100); } catch (Exception e) {}
                synchronized (kilitB) {   // kilitB'yi bekliyor
                    System.out.println("Thread 1: kilitB de alındı");
                }
            }
        }).start();

// Thread 2
        new Thread(() -> {
            synchronized (kilitB) {
                System.out.println("Thread 2: kilitB alındı");
                try { Thread.sleep(100); } catch (Exception e) {}
                synchronized (kilitA) {   // kilitA'yı bekliyor
                    System.out.println("Thread 2: kilitA da alındı");
                }
            }
        }).start();
    }
}
