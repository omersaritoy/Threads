package com.demo;


import java.util.concurrent.Executors;

public class RunnableTest {
    public static void main(String[] args) {
        Runnable gorev = () -> System.out.println("Görev çalıştı");

        Runnable gorev2 = () -> {
            for (int i = 0; i < 3; i++)
                System.out.println("Görev 2 çalıştı");
        };

        java.util.concurrent.ExecutorService executorService = Executors.newSingleThreadExecutor();

        try {
            System.out.println("Başladı");
            executorService.execute(gorev);
            executorService.execute(gorev2);
            executorService.execute(gorev);
            System.out.println("Bitti");
        } finally {
            executorService.shutdown();
        }
    }
}
