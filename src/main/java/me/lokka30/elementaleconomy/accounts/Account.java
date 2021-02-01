package me.lokka30.elementaleconomy.accounts;

import me.lokka30.elementaleconomy.ElementalEconomy;
import me.lokka30.elementaleconomy.currencies.Currency;
import me.lokka30.elementaleconomy.utils.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.UUID;

public class Account {

    //TODO

    public Account(int id, UUID uuid, HashMap<Currency, BigDecimal> currencyBalanceMap) {
        this.id = id;
        this.uuid = uuid;
        this.currencyBalanceMap = currencyBalanceMap;
    }

    private final int id;
    private final UUID uuid;
    @SuppressWarnings("FieldMayBeFinal")
    private HashMap<Currency, BigDecimal> currencyBalanceMap;



    /*
    Methods to get values associated with account
     */

    public int getId() { return id; }

    public UUID getUUID() {
        return uuid;
    }

    public HashMap<Currency, BigDecimal> getCurrencyBalanceMap() { return currencyBalanceMap; }



    /*
    Methods to manage the account's balances
     */

    public BigDecimal getBalance(Currency currency) {
        if(!currencyBalanceMap.containsKey(currency)) {
            currencyBalanceMap.put(currency, currency.startingBalance);
        }

        return currencyBalanceMap.get(currency);
    }

    public void setBalance(Currency currency, BigDecimal balance) {
        ElementalEconomy.getInstance().storageManager.storage.setBalance(id, currency, balance);
    }

    public void deposit(Currency currency, BigDecimal amount) {
        setBalance(currency, getBalance(currency).add(amount));
    }

    public void withdraw(Currency currency, BigDecimal amount) {
        if(has(currency, amount)) {
            setBalance(currency, getBalance(currency).subtract(amount));
        } else {
            Utils.logger.error("&c[TRANSACTION SKIPPED!] &7A plugin attempted to withdraw an amount from a player's balance without ensuring that they can afford it in the first place. Currency: &b" + currency.name + "&7, amount: &b" + amount.setScale(2, RoundingMode.DOWN).doubleValue() + "&7.");
        }
    }

    public boolean has(Currency currency, BigDecimal amount) {
        return amount.compareTo(getBalance(currency)) <= 0;
    }



    /*
    Other methods
     */

    public void saveBalancesToStorage() {
        ElementalEconomy.getInstance().storageManager.storage.saveAccount(this);
    }
}
