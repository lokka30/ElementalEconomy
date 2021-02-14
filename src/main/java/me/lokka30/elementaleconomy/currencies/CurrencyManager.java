package me.lokka30.elementaleconomy.currencies;

import me.lokka30.elementaleconomy.ElementalEconomy;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Objects;

public class CurrencyManager {

    private final ElementalEconomy main;

    public CurrencyManager(final ElementalEconomy main) {
        this.main = main;
    }

    public HashMap<Integer, Currency> currencyIdMap = new HashMap<>();
    public HashMap<String, Integer> currencyNameIdMap = new HashMap<>();

    /**
     * Read the currencies.yml file and insert all the currency data into the 3 HashMaps above.
     */
    public void loadCurrencies() {
        currencyIdMap.clear();
        currencyNameIdMap.clear();

        for (String currencyName : Objects.requireNonNull(main.currencies.getConfig().getConfigurationSection("currencies")).getKeys(false)) {
            final String path = "currencies." + currencyName + ".";
            final int id = main.currencies.getConfig().getInt(path + "id", 0);
            final BigDecimal startingBalance = BigDecimal.valueOf(main.currencies.getConfig().getDouble(path + "starting-balance", 50.00));
            final DecimalFormat decimalFormat = new DecimalFormat(Objects.requireNonNull(main.currencies.getConfig().getString(path + "decimal-format", "0.00")));
            final String displayFormat = main.currencies.getConfig().getString(path + "display-format", "$%balance%");
            final int truncateDigits = main.currencies.getConfig().getInt(path + "truncate-digits", 2);
            final String[] wordFormat = {main.currencies.getConfig().getString(path + "word-format.singular", ""), main.currencies.getConfig().getString(path + "word-format.plural", "")};

            final Currency currency = new Currency(id, currencyName, startingBalance, decimalFormat, displayFormat, truncateDigits, wordFormat);

            currencyIdMap.put(id, currency);
            currencyNameIdMap.put(currencyName, id);
        }
    }

    /**
     * Retrieve the currency by its name
     *
     * @param name name of the currency
     * @return the currency
     */
    public Currency getCurrency(String name) {
        return getCurrency(currencyNameIdMap.get(name));
    }

    public Currency getCurrency(int currencyId) {
        return currencyIdMap.get(currencyId);
    }

    /**
     * The default currency used by ElementalEconomy's commands such as /balance and /economy
     *
     * @return default currency
     */
    @SuppressWarnings("unused")
    public Currency getDefaultCurrency() {
        return getCurrency(main.currencies.getConfig().getString("default-currency"));
    }

    /**
     * The currency used by external plugins without multi-currency support, such as Vault
     *
     * @return external currency
     */
    public Currency getExternalCurrency() {
        return getCurrency(main.currencies.getConfig().getString("external-currency"));
    }
}
