package me.lokka30.elementaleconomy.storage;

import me.lokka30.elementaleconomy.ElementalEconomy;
import me.lokka30.elementaleconomy.accounts.Account;
import me.lokka30.elementaleconomy.currencies.Currency;

import java.math.BigDecimal;
import java.util.UUID;

public class SQLiteStorage implements Storage {

    private final ElementalEconomy main;
    public SQLiteStorage(final ElementalEconomy main) { this.main = main; }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

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
