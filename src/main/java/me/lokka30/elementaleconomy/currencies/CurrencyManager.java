package me.lokka30.elementaleconomy.currencies;

import me.lokka30.elementaleconomy.ElementalEconomy;

import java.math.BigDecimal;
import java.util.HashMap;

public class CurrencyManager {

    private final ElementalEconomy main;
    public CurrencyManager(final ElementalEconomy main) { this.main = main; }

    private HashMap<Integer, Currency> currencyIdMap = new HashMap<>();
    private HashMap<String, Integer> currencyNameIdMap = new HashMap<>();
    private HashMap<Integer, BigDecimal> currencyIdStartingBalanceMap = new HashMap<>();

    public void loadCurrencies() {
        currencyIdMap.clear();
        currencyNameIdMap.clear();
        currencyIdStartingBalanceMap.clear();

        /*
        TODO
        set currencyIdMap
        set currencyNameIdMap
        set currencyIdStartingBalanceMap
        set defaultCurrencyId
        set defaultExternalCurrency (vault currency)
         */
    }

    public Currency getCurrency(String name) {
        return currencyIdMap.get(currencyNameIdMap.get(name));
    }

    public Currency getDefaultCurrency() {
        return getCurrency(main.currencies.getConfig().getString("default-currency"));
    }

    public Currency getExternalCurrency() {
        return getCurrency(main.currencies.getConfig().getString("external-currency"));
    }
}
