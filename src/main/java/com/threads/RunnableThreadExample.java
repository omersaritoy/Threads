package com.threads;

public class RunnableThreadExample {
    public static void main(String[] args) {
        Thread one=new Thread(new ThreadOne());
        Thread two=new Thread(new ThreadTwo());

        one.start();
        two.start();


    }
}
class ThreadOne implements Runnable{

    @Override
    public void run() {
        for (int i=0;i<200;i++)
            System.out.println("Thread One: "+i);
    }
}
class ThreadTwo implements Runnable{

    @Override
    public void run() {
        for (int i=0;i<200;i++){
            System.out.println("Thread Two:"+i);
        }
    }
}
