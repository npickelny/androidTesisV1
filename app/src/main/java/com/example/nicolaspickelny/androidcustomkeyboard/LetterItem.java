package com.example.nicolaspickelny.androidcustomkeyboard;

import java.io.Serializable;

/**
 * Created by Nico on 22/02/2017.
 */

public class LetterItem implements Serializable{
    private int counter;
    private long total;
    private String letra1;
    private String letra2;

    public String getLetra2() {
        return letra2;
    }

    public void setLetra2(String letra2) {
        this.letra2 = letra2;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getLetra1() {
        return letra1;
    }

    public void setLetra1(String letra1) {
        this.letra1 = letra1;
    }

    public LetterItem() {
        this.counter = 0;
        this.total = 0;
    }

    public int getCounter() {
        return counter;
    }

    public long getTotal() {
        return total;
    }

    public void addValue(long x){
        this.counter++;
        this.total += x;
    }

    public float getAverage(){
        return (total/ counter);
    }
}
