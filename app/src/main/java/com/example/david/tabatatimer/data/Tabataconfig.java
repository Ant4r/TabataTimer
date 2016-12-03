package com.example.david.tabatatimer.data;


import com.orm.SugarRecord;

/**
 * Created by David on 31/10/2016.
 * Object to save the full configuration of a tabatatimer
 */

public class Tabataconfig extends SugarRecord {

    private String name;
    private int prepare;
    private int work;
    private int rest;
    private int cycles;
    private int tabatas;

    //// You must provide an empty constructor for SugarORM
    public Tabataconfig() {
    }

    public Tabataconfig(Tabataconfig tabataconfig){
        this.name = tabataconfig.getName();
        this.prepare = tabataconfig.getPrepare();
        this.work = tabataconfig.getWork();
        this.rest = tabataconfig.getRest();
        this.cycles = tabataconfig.getCycles();
        this.tabatas = tabataconfig.getTabatas();
    }

    public Tabataconfig(String name, int prepare, int work, int rest, int cycles, int tabatas) {
        this.name = name;
        this.prepare = prepare;
        this.work = work;
        this.rest = rest;
        this.cycles = cycles;
        this.tabatas = tabatas;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrepare() {
        return prepare;
    }

    public void setPrepare(int prepare) {
        this.prepare = prepare;
    }

    public int getWork() {return work;}

    public void setWork(int work) {
        this.work = work;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public int getCycles() {return cycles;}

    public void setCycles(int cycles) {
        this.cycles = cycles;
    }

    public int getTabatas() {
        return tabatas;
    }

    public void setTabatas(int tabatas) {
        this.tabatas = tabatas;
    }
}
