package com.threads;

public class WrongExample {
    public static void main(String[] args) {

        Thread t = new Thread(() -> System.out.println(Thread.currentThread().getName()));
        t.run();//write main
        t.start();//write thread-0




    }
}
