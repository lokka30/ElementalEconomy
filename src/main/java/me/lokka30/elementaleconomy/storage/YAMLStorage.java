package me.lokka30.elementaleconomy.storage;

import me.lokka30.elementaleconomy.ElementalEconomy;
import me.lokka30.elementaleconomy.accounts.Account;
import me.lokka30.elementaleconomy.currencies.Currency;
import me.lokka30.elementaleconomy.utils.Utils;
import me.lokka30.microlib.YamlConfigFile;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class YAMLStorage implements Storage {

    private final ElementalEconomy main;

    public YAMLStorage(final ElementalEconomy main) {
        this.main = main;
        this.dataFile = new YamlConfigFile(main, new File(main.getDataFolder(), "data.yml"));
    }

    /*
    Developers, if interested, see /src/main/resources/data.yml
    for an example file layout used by this class. Similar to DB's.
     */

    final YamlConfigFile dataFile;

    @Override
    public void enable() {
        Utils.logger.info("&f(Storage) &7Loading YAML storage...");

        try {
            dataFile.load();
        } catch (IOException exception) {
            exception.printStackTrace();
        }


        Utils.logger.info("&f(Storage) &7YAML storage loaded.");
    }

    @Override
    public void disable() {
        Utils.logger.info("&f(Storage) &7YAML storage disabled.");
    }

    @Override
    public Account getAccount(UUID uuid) {
        final int accountId = dataFile.getConfig().getInt("accounts." + uuid.toString() + ".id");

        HashMap<Integer, BigDecimal> balanceMap = new HashMap<>();
        for (Currency currency : main.currencyManager.currencyIdMap.values()) {
            if (dataFile.getConfig().contains("balances." + accountId + "." + currency.id)) {
                final BigDecimal balance = BigDecimal.valueOf(dataFile.getConfig().getDouble("balances." + accountId + "." + currency.id));
                balanceMap.put(currency.id, balance);
            }
        }

        return new Account(accountId, uuid, balanceMap);
    }

    @Override
    public boolean accountExists(UUID uuid) {
        return dataFile.getConfig().contains("accounts." + uuid.toString());
    }

    @Override
    public void saveAccount(Account account) {
        dataFile.getConfig().set("accounts.id-uuid-map." + account.getId(), account.getUUID().toString());
        dataFile.getConfig().set("accounts." + account.getUUID().toString() + ".id", account.getId());
        dataFile.getConfig().set("accounts." + account.getUUID().toString() + ".lastUsername", Bukkit.getOfflinePlayer(account.getUUID()).getName());

        for (Integer currencyId : account.getCurrencyBalanceMap().keySet()) {
            dataFile.getConfig().set("balances." + account.getId() + "." + currencyId, account.getCurrencyBalanceMap().get(currencyId).doubleValue());
        }

        saveDataCfg();
    }

    @Override
    public int getNextId() {
        if (dataFile.getConfig().contains("currentId")) {
            final int nextId = dataFile.getConfig().getInt("currentId") + 1;
            dataFile.getConfig().set("currentId", nextId);
            saveDataCfg();
            return nextId;
        } else {
            int nextId = 1;

            while (true) {
                if (dataFile.getConfig().contains("balances." + nextId)) {
                    nextId++;
                } else {
                    dataFile.getConfig().set("currentId", nextId);
                    saveDataCfg();
                    return nextId;
                }
            }
        }
    }

    @Override
    public void setBalance(int accountId, int currencyId, BigDecimal balance) {
        dataFile.getConfig().set("balances." + accountId + "." + currencyId, balance.doubleValue());
        saveDataCfg();

        Utils.sendDebugMessage(Utils.DebugMessageType.BALANCE_METHODS, "YAMLStorage#setBalance ran:");
        Utils.sendDebugMessage(Utils.DebugMessageType.BALANCE_METHODS, "&8 - &7Account ID: " + accountId);
        Utils.sendDebugMessage(Utils.DebugMessageType.BALANCE_METHODS, "&8 - &7Currency ID: " + currencyId);
        Utils.sendDebugMessage(Utils.DebugMessageType.BALANCE_METHODS, "&8 - &7New balance: ~" + balance.floatValue());
    }

    @Override
    public BigDecimal getBalance(int accountId, int currencyId) {
        final String path = "balances." + accountId + "." + currencyId;

        if (dataFile.getConfig().contains(path)) {
            return BigDecimal.valueOf(dataFile.getConfig().getDouble(path));
        } else {
            return null;
        }
    }

    /**
     * A map of each currency and the top balances of every player of that currency
     * <p>
     * Layout:
     * Map<CurrencyID, SortedMap<AccountID, Balance>>
     *
     * @return map of the highest balances for each currency (sorted in order).
     */
    @Override
    public Map<Integer, SortedMap<Integer, BigDecimal>> getTopBalances() {
        final HashMap<Integer, SortedMap<Integer, BigDecimal>> topBalances = new HashMap<>();

        //noinspection ConstantConditions
        for (String accountId : dataFile.getConfig().getConfigurationSection("balances").getKeys(false)) {
            //noinspection ConstantConditions
            for (String currencyId : dataFile.getConfig().getConfigurationSection("balances." + accountId).getKeys(false)) {
                topBalances.getOrDefault(Integer.valueOf(currencyId), new TreeMap<>()).put(Integer.valueOf(accountId), BigDecimal.valueOf(dataFile.getConfig().getDouble("balances." + accountId + "." + currencyId, 0.0)));
            }
        }

        return topBalances;
    }

    /**
     * This method is used by the baltop command to get the username of players
     * from their account ID.
     *
     * @param accountId account id of the player
     * @return their last known username
     */
    @Override
    public String getUsernameFromAccountId(int accountId) {
        //noinspection ConstantConditions
        UUID uuid = UUID.fromString(dataFile.getConfig().getString("accounts.id-uuid-map." + accountId));

        return dataFile.getConfig().getString("accounts." + uuid.toString() + ".lastUsername");
    }

    private void saveDataCfg() {
        try {
            dataFile.save();
        } catch (IOException ex) {
            Utils.logger.error("Unable to save data.yml! Stack trace:");
            ex.printStackTrace();
        }
    }
}
