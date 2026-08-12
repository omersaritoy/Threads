package com.threads;

import java.awt.image.renderable.RenderableImage;

import static com.threads.Example9.sleep;

public class Example10 {
    public static void main(String[] args) {
        Runnable runnable=()->{
            for (int i =0 ;i<5;i++)
            {
                sleep(1000L);
                System.out.println("Running");
            }
        };
        Thread thread=new Thread(runnable);
        thread.setDaemon(true);
        thread.start();

//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }
}
