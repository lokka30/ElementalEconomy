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
import java.util.HashMap;
import java.util.UUID;

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
        final int id = dataFile.getConfig().getInt("accounts." + uuid.toString() + ".id");

        HashMap<Currency, BigDecimal> balanceMap = new HashMap<>();
        for (Currency currency : main.currencyManager.currencyIdMap.values()) {
            if (dataFile.getConfig().contains("balances." + id + "." + currency.id)) {
                final BigDecimal balance = BigDecimal.valueOf(dataFile.getConfig().getDouble("balances." + id + "." + currency.id));
                balanceMap.put(currency, balance);
            }
        }

        return new Account(id, uuid, balanceMap);
    }

    @Override
    public boolean accountExists(UUID uuid) {
        return dataFile.getConfig().contains("accounts." + uuid.toString());
    }

    @Override
    public void saveAccount(Account account) {
        dataFile.getConfig().set("accounts." + account.getUUID().toString() + ".id", account.getId());
        dataFile.getConfig().set("accounts." + account.getUUID().toString() + ".lastUsername", Bukkit.getOfflinePlayer(account.getUUID()).getName());

        for (Currency currency : account.getCurrencyBalanceMap().keySet()) {
            dataFile.getConfig().set("balances." + account.getId() + "." + currency.id, account.getCurrencyBalanceMap().get(currency).doubleValue());
        }
    }

    @Override
    public int getNextId() {
        if (dataFile.getConfig().contains("currentId")) {
            final int nextId = dataFile.getConfig().getInt("currentId") + 1;
            dataFile.getConfig().set("currentId", nextId);
            return nextId;
        } else {
            int nextId = 1;

            while (true) {
                if (dataFile.getConfig().contains("balances." + nextId)) {
                    nextId++;
                } else {
                    dataFile.getConfig().set("currentId", nextId);
                    return nextId;
                }
            }
        }
    }

    @Override
    public void setBalance(int accountId, Currency currency, BigDecimal balance) {
        dataFile.getConfig().set("balances." + accountId + "." + currency.id, balance.doubleValue());
    }

    @Override
    public BigDecimal getBalance(int accountId, Currency currency) {
        final String path = "balances." + accountId + "." + currency.id;

        if (dataFile.getConfig().contains(path)) {
            return BigDecimal.valueOf(dataFile.getConfig().getDouble(path));
        } else {
            return null;
        }
    }
}
