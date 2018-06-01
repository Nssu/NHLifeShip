package com.example.jinsu.nh_life.common;

public class Constants {
    private static Constants instance= null;
    private int Step = 0;
    public String PREF_LOCAL_ID = "pref_id";
    public String PREF_USER_KEY = "user_key";

    private Constants(){}

    public static Constants getInstance()
    {
        if(instance == null)
        {
            instance = new Constants();
        }
        return instance;
    }

    public int getStep() {
        return Step;
    }

    public String getPREF_LOCAL_ID() {
        return PREF_LOCAL_ID;
    }

    public String getPREF_USER_KEY() {
        return PREF_USER_KEY;
    }

    public void setStep(int step) {
        Step = step;
    }
}
