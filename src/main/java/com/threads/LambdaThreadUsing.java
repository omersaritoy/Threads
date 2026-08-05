package com.threads;

import java.util.stream.IntStream;

public class LambdaThreadUsing {
    public static void main(String[] args) {

        System.out.println("Program Çalıştı");

        new Thread(()-> System.out.println("Selam ben ilk thread'im")).start();


        new Thread(()->{
            System.out.println("Önceki kodlama");
            for (int i=0;i<5;i++)
                System.out.println("Sayaç:"+i);
        }).start();


        Runnable runnable1=()-> System.out.println("3.Çalışan kod");
        Runnable runnable2=()->{
            System.out.println("2.öncü kodlama");
            for (int i=0;i<4;i++)
                System.out.println("Sayaç:"+i);
        };

        new Thread(runnable1).start();
        new Thread(runnable2).start();
        System.out.println("End.");


    }
}
