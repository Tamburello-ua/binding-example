package com.dumptruckloads.truckcalc.util;


import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class InputValidator {

    public static boolean isCountValid(String input) {
        return isValidNumber(input, false);
    }

    public static boolean isPriceValid(String input) {
        if(null != input && input.length()>0) {
            input = cleanReplaceSeparators(input);
        }

        return isValidNumber(input, true);
    }

    private static boolean isValidNumber(String input, boolean isNeedBePositive) {
        boolean ret = true;
        if(null != input && input.length()>0) {
            input = cleanReplaceSeparators(input);
        }

        if (TextUtils.isEmpty(input)) {
            ret = false;
        } else if (!isNumeric(input)) {
            ret = false;
        } else {
            Float parseNum = 0f;
            try {
                parseNum = Float.parseFloat(input);
            } catch (Exception e) {
                parseNum = -1f;
            }
            if (parseNum <= 0 && isNeedBePositive) {
                ret = false;
            }
        }
        return ret;
    }

    private static boolean isNumeric(String input) {
        if(null != input && input.length()>0) {
            input = cleanReplaceSeparators(input);
        }

        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(input, pos);
        return input.length() == pos.getIndex();
    }

    public static String getFormatedAmount(String amount){
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        String numberAsString = decimalFormat.format(Double.parseDouble(amount));
        return numberAsString;
    }

    public static String cleanReplaceSeparators(String input) {
        input = input.replace(",", "").replace("$", "").replace("" + DecimalFormatSymbols.getInstance().getGroupingSeparator(), "");
        return input;
    }

}
