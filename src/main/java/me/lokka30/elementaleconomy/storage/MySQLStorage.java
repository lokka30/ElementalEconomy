package me.lokka30.elementaleconomy.storage;

import co.aikar.idb.DB;
import co.aikar.idb.Database;
import co.aikar.idb.DatabaseOptions;
import co.aikar.idb.PooledDatabaseOptions;
import me.lokka30.elementaleconomy.ElementalEconomy;
import me.lokka30.elementaleconomy.accounts.Account;
import me.lokka30.elementaleconomy.currencies.Currency;
import me.lokka30.elementaleconomy.utils.Utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

/**
 * This is the MySQL implementation for ElemEcon.
 */
public class MySQLStorage implements Storage {

    /*
    TODO Tables:

    elemecon_accounts (id, uuid)
    elemecon_balances (id, currency, balance)
     */

    private final ElementalEconomy main;
    public MySQLStorage(final ElementalEconomy main) { this.main = main; }

    @Override
    public void enable() {
        Utils.logger.info("&f(Storage) &7Using MySQL.");
        final String username = main.settings.getConfig().getString("storage.mysql-settings.username");
        final String password = main.settings.getConfig().getString("storage.mysql-settings.password");
        final String databaseName = main.settings.getConfig().getString("storage.mysql-settings.database");
        final String host = main.settings.getConfig().getString("storage.mysql-settings.host");

        DatabaseOptions options = DatabaseOptions.builder().mysql(username, password, databaseName, host).build();
        Database db = PooledDatabaseOptions.builder().options(options).createHikariDatabase();
        DB.setGlobalDatabase(db);
        Utils.logger.info("&f(Storage) &7Storage system loaded.");
    }

    @Override
    public void disable() {
        DB.close();
    }

    @Override
    public Account getAccount(UUID uuid) {

        final int id = -1;
        final HashMap<Currency, BigDecimal> currencyBalanceMap = new HashMap<>();

        //TODO get id and currencyMap from database

        return new Account(id, uuid, currencyBalanceMap);
    }

    @Override
    public boolean accountExists(UUID uuid) {

        //TODO check if uuid is in database

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
