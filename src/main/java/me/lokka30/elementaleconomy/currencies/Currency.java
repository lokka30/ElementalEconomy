package me.lokka30.elementaleconomy.currencies;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Currency {

    public Currency(int id, String name, BigDecimal startingBalance, DecimalFormat decimalFormat, String displayFormat, int truncateDigits, String[] wordFormat) {
        this.id = id;
        this.name = name;
        this.startingBalance = startingBalance;
        this.decimalFormat = decimalFormat;
        this.displayFormat = displayFormat;
        this.truncateDigits = truncateDigits;
        this.wordFormat = wordFormat;
    }

    public final int id; // e.g. 0
    public final String name; // e.g. 'dollars'
    public final BigDecimal startingBalance; // e.g. 50.00
    public final DecimalFormat decimalFormat; // e.g. ###,##0.00
    public final String displayFormat; // e.g. $%decimalFormat%
    public final int truncateDigits; // e.g. 2 (50.12 instead of 50.123456)
    public final String[] wordFormat; // [0] = singular, [1] = plural; can be null if unspecified.

    public String getFormattedAmount(BigDecimal amount) {
        String formattedAmount = displayFormat
                .replace("%decimalFormat%", decimalFormat.format(amount.doubleValue()));

        if (formattedAmount.contains("%wordFormat%"))
            formattedAmount = formattedAmount.replace("%wordFormat%", amount.doubleValue() == 1.00 ? wordFormat[0] : wordFormat[1]);

        return formattedAmount;
    }
}
