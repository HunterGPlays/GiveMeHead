package net.terrocidepvp.givemehead.utils;

import java.text.*;
import java.math.*;
import java.util.*;

public class NumberUtil
{
    static final DecimalFormat currencyFormat;
    static final NumberFormat commaSeparatedCurrencyFormat;
    
    public static String formatAsCurrency(final double value) {
        NumberUtil.commaSeparatedCurrencyFormat.setRoundingMode(RoundingMode.FLOOR);
        NumberUtil.commaSeparatedCurrencyFormat.setGroupingUsed(true);
        NumberUtil.commaSeparatedCurrencyFormat.setMinimumFractionDigits(2);
        NumberUtil.commaSeparatedCurrencyFormat.setMaximumFractionDigits(2);
        String str = NumberUtil.commaSeparatedCurrencyFormat.format(value);
        if (str.endsWith(".00")) {
            str = str.substring(0, str.length() - 3);
        }
        return str;
    }
    
    static {
        currencyFormat = new DecimalFormat("#0.00");
        commaSeparatedCurrencyFormat = NumberFormat.getInstance(Locale.US);
    }
}
