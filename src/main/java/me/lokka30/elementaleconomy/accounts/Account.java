package me.lokka30.elementaleconomy.accounts;

import me.lokka30.elementaleconomy.ElementalEconomy;
import me.lokka30.elementaleconomy.currencies.Currency;
import org.apache.commons.lang.Validate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

public class Account {

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
        Validate.isTrue(balance.doubleValue() >= 0, "Amount must be greater than or equal to 0.");
        currencyBalanceMap.remove(currency);
        currencyBalanceMap.put(currency, balance);
        saveAccountToAccountMap();
        ElementalEconomy.getInstance().storageManager.storage.setBalance(id, currency, balance);
    }

    public void deposit(Currency currency, BigDecimal amount) {
        Validate.isTrue(amount.doubleValue() > 0, "Amount must be greater than 0.");
        setBalance(currency, getBalance(currency).add(amount));
    }

    public void withdraw(Currency currency, BigDecimal amount) {
        BigDecimal balance = getBalance(currency).subtract(amount);
        Validate.isTrue(balance.doubleValue() >= 0, "Balance after withdrawal must be greater than or equal to 0.");
        setBalance(currency, balance);
    }

    public boolean has(Currency currency, BigDecimal amount) {
        Validate.isTrue(amount.doubleValue() > 0, "Amount must be greater than 0.");
        return amount.compareTo(getBalance(currency)) <= 0;
    }



    /*
    Other methods
     */

    public void saveAccountToAccountMap() {
        ElementalEconomy.getInstance().accountManager.cachedAccounts.remove(uuid);
        ElementalEconomy.getInstance().accountManager.cachedAccounts.put(uuid, this);
    }

    public void saveBalancesToStorage() {
        ElementalEconomy.getInstance().storageManager.storage.saveAccount(this);
    }
}
