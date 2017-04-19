package com.example.nicolaspickelny.androidcustomkeyboard;

import java.io.Serializable;

/**
 * Created by Nico on 22/02/2017.
 */

public class LetterItem implements Serializable{
    private int count;
    private long total;

    public LetterItem() {
        this.count = 0;
        this.total = 0;
    }

    public int getCount() {
        return count;
    }

    public long getTotal() {
        return total;
    }

    public void addValue(long x){
        this.count++;
        this.total += x;
    }

    public float getAverage(){
        return (total/count);
    }
}
