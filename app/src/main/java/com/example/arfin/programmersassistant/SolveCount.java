package com.example.arfin.programmersassistant;

public class SolveCount {
    private String date;
    private int cf;
    private int uva;
    private int loj;
    private int hr;



    public SolveCount(){
        //this constructor is required
    }

    public SolveCount(int cf, String date, int uva, int loj, int hr) {
        this.date = date;
        this.cf = cf;
        this.uva = uva;
        this.loj = loj;
        this.hr = hr;
    }

    public String getDate() {
        return date;
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

    public int getHr() {return  hr; }
}
