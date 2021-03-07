package me.lokka30.elementaleconomy.storage;

import co.aikar.idb.DB;
import co.aikar.idb.Database;
import co.aikar.idb.DatabaseOptions;
import co.aikar.idb.PooledDatabaseOptions;
import me.lokka30.elementaleconomy.ElementalEconomy;
import me.lokka30.elementaleconomy.accounts.Account;
import me.lokka30.elementaleconomy.utils.Utils;

import java.math.BigDecimal;
import java.util.Map;
import java.util.SortedMap;
import java.util.UUID;

public class SQLiteStorage implements Storage {

    /*
    TODO Tables:
    elemecon_accounts (id, uuid)
    elemecon_balances (id, currency, balance)
     */

    @SuppressWarnings("FieldCanBeLocal")
    private final ElementalEconomy main;

    public SQLiteStorage(final ElementalEconomy main) {
        this.main = main;
    }

    @Override
    public void enable() {
        Utils.logger.info("&f(Storage) &7Loading SQLite storage...");

        DatabaseOptions options = DatabaseOptions.builder().sqlite("data.db").build();
        Database db = PooledDatabaseOptions.builder().options(options).createHikariDatabase();
        DB.setGlobalDatabase(db);

        Utils.logger.info("&f(Storage) &7SQLite storage loaded.");
    }

    @Override
    public void disable() {
        DB.close();

        Utils.logger.info("&f(Storage) &7SQLite storage disabled.");
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
    public void setBalance(int accountId, int currencyId, BigDecimal balance) {

    }

    @Override
    public BigDecimal getBalance(int accountId, int currencyId) {
        return null;
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
        return null;
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
        return null;
    }
}
