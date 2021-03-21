package me.lokka30.elementaleconomy.accounts;

import me.lokka30.elementaleconomy.ElementalEconomy;
import me.lokka30.elementaleconomy.utils.Utils;
import org.apache.commons.lang.Validate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

public class Account {

    public Account(int id, UUID uuid, HashMap<Integer, BigDecimal> currencyBalanceMap) {
        this.id = id;
        this.uuid = uuid;
        this.currencyBalanceMap = currencyBalanceMap;
    }

    private final int id;
    private final UUID uuid;
    @SuppressWarnings("FieldMayBeFinal")
    private final HashMap<Integer, BigDecimal> currencyBalanceMap;



    /*
    Methods to get values associated with account
     */

    public int getId() { return id; }

    public UUID getUUID() {
        return uuid;
    }

    public HashMap<Integer, BigDecimal> getCurrencyBalanceMap() {
        return currencyBalanceMap;
    }



    /*
    Methods to manage the account's balances
     */

    public BigDecimal getBalance(int currencyId) {
        if (!currencyBalanceMap.containsKey(currencyId)) {
            currencyBalanceMap.put(currencyId, ElementalEconomy.getInstance().currencyManager.getCurrency(currencyId).startingBalance);
        }

        return currencyBalanceMap.get(currencyId);
    }

    public void setBalance(int currencyId, BigDecimal balance) {
        Validate.isTrue(balance.doubleValue() >= 0, "Amount must be greater than or equal to 0.");
        currencyBalanceMap.remove(currencyId);
        currencyBalanceMap.put(currencyId, balance);
        saveAccountToAccountMap();
        ElementalEconomy.getInstance().storageManager.storage.setBalance(id, currencyId, balance);

        Utils.sendDebugMessage(Utils.DebugMessageType.BALANCE_METHODS, "Account#setBalance ran:");
        Utils.sendDebugMessage(Utils.DebugMessageType.BALANCE_METHODS, "&8 - &7Currency ID: " + currencyId);
        Utils.sendDebugMessage(Utils.DebugMessageType.BALANCE_METHODS, "&8 - &7New balance: ~" + balance.floatValue());
    }

    public void deposit(int currencyId, BigDecimal amount) {
        Validate.isTrue(amount.doubleValue() > 0, "Amount must be greater than 0.");
        setBalance(currencyId, getBalance(currencyId).add(amount));
    }

    public void withdraw(int currencyId, BigDecimal amount) {
        BigDecimal balance = getBalance(currencyId).subtract(amount);
        Validate.isTrue(balance.doubleValue() >= 0, "Balance after withdrawal must be greater than or equal to 0.");
        setBalance(currencyId, balance);
    }

    public boolean has(int currencyId, BigDecimal amount) {
        Validate.isTrue(amount.doubleValue() > 0, "Amount must be greater than 0.");
        return amount.compareTo(getBalance(currencyId)) <= 0;
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
