package com.example.NatureCalling;

import java.util.concurrent.atomic.AtomicBoolean;

//THIS CLASS KEEPS TRACK OF WHETHER THE PHONE IS IN MOTION
public abstract class GlobalData {

    private GlobalData() {
    };

    private static final AtomicBoolean phoneInMotion = new AtomicBoolean(false);

    public static boolean isPhoneInMotion() {
        return phoneInMotion.get();
    }

    public static void setPhoneInMotion(boolean bool) {
        phoneInMotion.set(bool);
    }
}