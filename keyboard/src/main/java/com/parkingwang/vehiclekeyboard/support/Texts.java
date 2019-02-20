package com.parkingwang.vehiclekeyboard.support;

import java.util.regex.Pattern;

/**
 * 陈渝金
 * 645503254@qq.com
 * 四川星盾科技股份有限公司
 * @since 0.1
 * @version 0.3-ALPHA
 */
public class Texts {
    private static Pattern ENGLISH_LETTER_DIGITS = Pattern.compile("[^a-zA-Z0-9]");

    public static boolean isEnglishLetterOrDigit(String str) {
        return !ENGLISH_LETTER_DIGITS.matcher(str).find();
    }

    public static boolean isNewEnergyType(String number) {
        if (number != null && number.length() > 2) {
            final int size = 8 - number.length();
            for (int i = 0; i < size; i++) {
                number += "0";
            }
            if (Pattern.matches("\\w[A-Z][0-9DF][0-9A-Z]\\d{3}[0-9DF]", number)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }
}
