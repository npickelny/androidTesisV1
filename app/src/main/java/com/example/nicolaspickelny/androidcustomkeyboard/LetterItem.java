package com.example.nicolaspickelny.androidcustomkeyboard;

import java.io.Serializable;

/**
 * Created by Nico on 22/02/2017.
 */

public class LetterItem implements Serializable{
    private int cant;
    private long timer;
    private String letter;
    private String letra2;

    public String getLetra2() {
        return letra2;
    }

    public void setLetra2(String letra2) {
        this.letra2 = letra2;
    }

    public void setCant(int cant) {
        this.cant = cant;
    }

    public void setTimer(long timer) {
        this.timer = timer;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public LetterItem() {
        this.cant = 0;
        this.timer = 0;
    }

    public int getCant() {
        return cant;
    }

    public long getTimer() {
        return timer;
    }

    public void addValue(long x){
        this.cant++;
        this.timer += x;
    }

    public float getAverage(){
        return (timer / cant);
    }
}
