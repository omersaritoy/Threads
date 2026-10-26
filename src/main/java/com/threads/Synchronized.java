package com.threads;

public class Synchronized {
    static int counter=0;

    // 1. Metot: kilit = this (bu nesne)
    synchronized void increase() { counter++; }

    // 2. Static metot: kilit = Sinif.class nesnesi
    static synchronized void increase2() { counter++; }

    // 3. Blok: kilidi sen seçersin (genelde tercih edilen)
    private final Object kilit = new Object();
    void artir() {
        synchronized (kilit) { counter++; }
    }
}
