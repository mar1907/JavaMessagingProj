package com.ds.assigthree.common.entity;

public class DVDBuilder {

    private DVD dvd;

    public DVDBuilder() {
        dvd = new DVD();
    }

    public DVDBuilder setTitle(String title){
        dvd.setTitle(title);
        return this;
    }

    public DVDBuilder setYear(int year){
        dvd.setYear(year);
        return this;
    }

    public DVDBuilder setPrice(double price){
        dvd.setPrice(price);
        return this;
    }

    public DVD build(){
        return dvd;
    }
}
