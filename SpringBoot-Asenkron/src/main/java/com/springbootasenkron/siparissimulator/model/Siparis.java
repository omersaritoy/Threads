package com.springbootasenkron.siparissimulator.model;

public class Siparis {
    private final int id;
    private final String urunAdi;
    private final int adet;


    public Siparis(int id, String urunAdi, int adet) {
        this.id = id;
        this.urunAdi = urunAdi;
        this.adet = adet;
    }

    public int getId() {
        return id;
    }

    public String getUrunAdi() {
        return urunAdi;
    }

    public int getAdet() {
        return adet;
    }
}
