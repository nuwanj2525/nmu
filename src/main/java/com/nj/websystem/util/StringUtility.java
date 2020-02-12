package com.nj.websystem.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtility {

    final static public String YY = "yy";
    final static public String MM = "MM";
    final static public String DD = "dd";
    private static SimpleDateFormat simpleDateFormat = null;

    public static String getCustDateByPatten(String pattern) {
        simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(new Date());
    }

    public static String getCustDateByPatten(String pattern, Date givenDate) {
        simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(givenDate);
    }

    public static String getFilledNumber(int number, Long filCount) {
        String format = "%0" + "" + filCount + "d";
        return String.format(format, (number));
    }

    public static boolean isEmpty(String value) {
        if (value != null) {
            return value.isEmpty();
        }
        return false;
    }

    public static boolean get(String val) {
        if (val != null)
            if (!val.isEmpty() && !val.equals("?")) {
                return true;
            } else {
                return false;
            }
        else
            return false;
    }
}
