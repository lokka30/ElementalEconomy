package me.lokka30.elementaleconomy.storage;

import me.lokka30.elementaleconomy.ElementalEconomy;
import me.lokka30.elementaleconomy.accounts.Account;
import me.lokka30.elementaleconomy.currencies.Currency;
import me.lokka30.elementaleconomy.utils.Utils;

import java.math.BigDecimal;
import java.util.UUID;

public class YAMLStorage implements Storage {

    private final ElementalEconomy main;

    public YAMLStorage(final ElementalEconomy main) {
        this.main = main;
    }

    /*
    Developers, if interested, see /src/main/resources/data.yml
    for an example file layout used by this class. Similar to DB's.
     */

    @Override
    public void enable() {
        Utils.logger.info("&f(Storage) &7Loading YAML storage...");

        //TODO

        Utils.logger.info("&f(Storage) &7YAML storage loaded.");
    }

    @Override
    public void disable() {
        Utils.logger.info("&f(Storage) &7YAML storage disabled.");
    }

    @Override
    public Account getAccount(UUID uuid) {
        return null;
    }

    @Override
    public boolean accountExists(UUID uuid) {
        return false;
    }

    @Override
    public void saveAccount(Account account) {

    }

    @Override
    public int getNextId() {
        return 0;
    }

    @Override
    public void setBalance(int id, Currency currency, BigDecimal balance) {

    }

    @Override
    public BigDecimal getBalance(int id, Currency currency) {
        return null;
    }
}
