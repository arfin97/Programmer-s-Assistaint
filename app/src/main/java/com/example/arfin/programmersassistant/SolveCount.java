package com.example.arfin.programmersassistant;

public class SolveCount {
    private int cf;
    private int uva;
    private int loj;


    public SolveCount(){
        //this constructor is required
    }

    public SolveCount(int cf, int uva, int loj) {
        this.cf = cf;
        this.uva = uva;
        this.loj = loj;
    }

    public int getCf() {
        return cf;
    }

    public int getUva() {
        return uva;
    }

    public int getLoj() {
        return loj;
    }
}
