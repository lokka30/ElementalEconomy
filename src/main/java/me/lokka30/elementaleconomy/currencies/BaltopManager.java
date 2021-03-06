package me.lokka30.elementaleconomy.currencies;

import me.lokka30.elementaleconomy.ElementalEconomy;
import me.lokka30.elementaleconomy.misc.DebugMessageType;
import me.lokka30.elementaleconomy.utils.Utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class BaltopManager {

    private final ElementalEconomy main;

    public BaltopManager(final ElementalEconomy main) {
        this.main = main;
    }

    /**
     * AccountID, <CurrencyID, Balance>
     */
    public Map<Integer, Map<Currency, BigDecimal>> baltopMap = new HashMap<>();

    public void resetCache() {
        Utils.sendDebugMessage(DebugMessageType.BALTOP_CACHING, "Resetting baltop cache");
        //TODO
    }
}
