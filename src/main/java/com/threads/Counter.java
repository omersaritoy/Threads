package com.threads;

public class Counter {
    static int counter=0;

    public static void main(String[] args) throws InterruptedException {
        int threads=4,her=5_000_000;
        Thread[] ts=new Thread[threads];
        for (int i=0;i<threads;i++){
            ts[i]=new Thread(()->{for(int j=0;j<her;j++)counter++;});
        }
        for (Thread t:ts)
            t.start();

        for (Thread t:ts)
            t.join();
        long expect=(long) threads*her;
        System.out.println("Wait:"+expect+", real:"+counter+", lost: "+(expect-counter));



    }

}
