package com.olivercatling.hasamishogi.Database;

/**
 * Created by Oliver on 25/04/2018.
 */

public class Settings {
    private boolean daiHasamiShogi;
    private boolean daiHasamiShogiFiveWinRule;
    private int winNumber;
    private int reference;

    public Settings(boolean daiHasamiShogi, boolean daiHasamiShogiFiveWinRule, int winNumber, int reference){
        this.daiHasamiShogi = daiHasamiShogi;
        this.daiHasamiShogiFiveWinRule = daiHasamiShogiFiveWinRule;
        this.winNumber = winNumber;
        this.reference = reference;
    }

    public boolean isDaiHasamiShogi(){
        return this.daiHasamiShogi;
    }

    public boolean isDaiHasamiShogiFiveWinRule(){
        return this.daiHasamiShogiFiveWinRule;
    }

    public int getWinNumber(){
        return this.winNumber;
    }

    public int getReference(){
        return this.reference;
    }

    public void setDaiHasamiShogi(boolean daiHasamiShogi){
        this.daiHasamiShogi = daiHasamiShogi;
    }

    public void setDaiHasamiShogiFiveWinRule(boolean daiHasamiShogiFiveWinRule){
        this.daiHasamiShogiFiveWinRule = daiHasamiShogiFiveWinRule;
    }

    public void setWinNumber(int winNumber){
        this.winNumber = winNumber;
    }

    public void setReference(int reference){
        this.reference = reference;
    }
}
