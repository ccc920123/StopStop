//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.kernal.plateid;

public class MathRandom {
    public static double rate0 = 0.5D;
    public static double rate1 = 0.2D;
    public static double rate2 = 0.15D;
    public static double rate3 = 0.06D;
    public static double rate4 = 0.04D;
    public static double rate5 = 0.05D;

    public MathRandom() {
    }

    public static int PercentageRandom() {
        double randomNumber = Math.random();
        return randomNumber >= 0.0D && randomNumber <= rate0?0:(randomNumber >= rate0 / 100.0D && randomNumber <= rate0 + rate1?1:(randomNumber >= rate0 + rate1 && randomNumber <= rate0 + rate1 + rate2?2:(randomNumber >= rate0 + rate1 + rate2 && randomNumber <= rate0 + rate1 + rate2 + rate3?3:(randomNumber >= rate0 + rate1 + rate2 + rate3 && randomNumber <= rate0 + rate1 + rate2 + rate3 + rate4?4:(randomNumber >= rate0 + rate1 + rate2 + rate3 + rate4 && randomNumber <= rate0 + rate1 + rate2 + rate3 + rate4 + rate5?5:-1)))));
    }
}
