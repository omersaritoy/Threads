package com.demo;

public class Interrupt {
    private static int counter=0;

    public static void main(String[] args) {

        final var mainThread=Thread.currentThread();
        new Thread(()->{
            for (int i=0;i<1000000;i++) counter++;
            mainThread.interrupt();
        }).start();
        while (counter<1000000){
            System.out.println("Not reached yet");
            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }
        System.out.println("Reached: "+counter);

    }
}