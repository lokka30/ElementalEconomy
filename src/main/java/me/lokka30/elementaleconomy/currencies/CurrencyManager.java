package me.lokka30.elementaleconomy.currencies;

import me.lokka30.elementaleconomy.ElementalEconomy;

import java.util.HashMap;

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

        /*
        TODO
        set currencyIdMap
        set currencyNameIdMap
        set currencyIdStartingBalanceMap
         */
    }

    /**
     * Retrieve the currency by its name
     *
     * @param name name of the currency
     * @return the currency
     */
    public Currency getCurrency(String name) {
        return currencyIdMap.get(currencyNameIdMap.get(name));
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
