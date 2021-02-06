package me.lokka30.elementaleconomy.hooks;

import me.lokka30.elementaleconomy.ElementalEconomy;
import me.lokka30.elementaleconomy.currencies.Currency;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.util.List;

public class VaultHook implements Economy {

    private final ElementalEconomy main;

    public VaultHook(ElementalEconomy main) {
        this.main = main;
    }

    /**
     * @return the 'external currency' configured by the server owner, i.e. the one used by Vault and possibly other APIs
     */
    private Currency getExternalCurrency() {
        return main.currencyManager.getExternalCurrency();
    }

    /**
     * @return if the plugin is enabled
     */
    @Override
    public boolean isEnabled() {
        return main.isEnabled();
    }

    /**
     * @return the name of this plugin
     */
    @Override
    public String getName() {
        return main.getDescription().getName();
    }

    /**
     * @return false, ElementalEconomy does not have Vault bank support.
     */
    @Override
    public boolean hasBankSupport() {
        return false;
    }

    /**
     * @return how many digits of decimals this plugin truncates to with the external currency
     */
    @Override
    public int fractionalDigits() {
        return getExternalCurrency().truncateDigits;
    }

    /**
     * @param unformatted the amount to format
     * @return formatted string of the unformatted amount
     */
    @Override
    public String format(double unformatted) {
        return getExternalCurrency().decimalFormat.format(unformatted);
    }

    /**
     * @return plural word for external currency (can be null).
     */
    @Override
    public String currencyNamePlural() {
        return getExternalCurrency().wordFormat[1];
    }

    /**
     * @return singular word for external currency (can be null).
     */
    @Override
    public String currencyNameSingular() {
        return getExternalCurrency().wordFormat[0];
    }

    /**
     * @param playerName player name to lookup
     * @deprecated use hasAccount(OfflinePlayer) instead
     */
    @Override
    public boolean hasAccount(String playerName) {
        return hasAccount(Bukkit.getOfflinePlayer(playerName));
    }

    /**
     * Check if the player has an account in ElemEcon's storage
     *
     * @param offlinePlayer player to check
     * @return if the player has an account in ElemEcon's storage
     */
    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return main.accountManager.accountExists(offlinePlayer.getUniqueId());
    }

    /**
     * This method is deprecated. Use hasAccount(OfflinePlayer) instead.
     * ElementalEconomy does not have per-world support. Use hasAccount(OfflinePlayer) instead.
     *
     * @param playerName player
     * @param worldName  world name
     * @return if the player has an account in ElemEcon's storage
     * @deprecated use hasAccount(OfflinePlayer)
     */
    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(Bukkit.getOfflinePlayer(playerName));
    }

    /**
     * ElementalEconomy does not have per-world support. Use hasAccount(OfflinePlayer) instead.
     *
     * @param offlinePlayer player
     * @param worldName     world name
     * @return if the player has an account in ElemEcon's storage
     */
    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String worldName) {
        return hasAccount(offlinePlayer);
    }

    /**
     * @param playerName player name
     * @return balance of the player
     * @deprecated use getBalance(OfflinePlayer) instead
     */
    @Override
    public double getBalance(String playerName) {
        return getBalance(Bukkit.getOfflinePlayer(playerName));
    }

    /**
     * Gets the balance of the player.
     *
     * @param offlinePlayer player
     * @return balance of player
     */
    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return main.accountManager.getAccount(offlinePlayer.getUniqueId()).getBalance(getExternalCurrency()).doubleValue();
    }

    /**
     * This method is deprecated. Use getBalance(OfflinePlayer) instead.
     * ElementalEconomy does not have per-world support. Use getBalance(OfflinePlayer) instead.
     *
     * @param playerName player name
     * @param worldName  world name
     * @return balance of player
     * @deprecated use getBalance(OfflinePlayer) instead
     */
    @Override
    public double getBalance(String playerName, String worldName) {
        return getBalance(Bukkit.getOfflinePlayer(playerName));
    }

    /**
     * ElementalEconomy does not have per-world support. Use getBalance(OfflinePlayer) instead.
     *
     * @param offlinePlayer player
     * @param worldName     world name
     * @return balance of player
     */
    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String worldName) {
        return getBalance(offlinePlayer);
    }

    /**
     * This method is deprecated. Use has(OfflinePlayer, double) instead.
     *
     * @param playerName player name
     * @param amount     amount
     * @return if the player's balance >= amount
     * @deprecated use has(OfflinePlayer, double) instead
     */
    @Override
    public boolean has(String playerName, double amount) {
        return has(Bukkit.getOfflinePlayer(playerName), amount);
    }

    /**
     * Check if the player can afford amount
     *
     * @param offlinePlayer player
     * @param amount        amount
     * @return if the player's balance >= amount
     */
    @Override
    public boolean has(OfflinePlayer offlinePlayer, double amount) {
        return main.accountManager.getAccount(offlinePlayer.getUniqueId()).has(getExternalCurrency(), BigDecimal.valueOf(amount));
    }

    /**
     * This method is deprecated. Use has(OfflinePlayer, double) instead.
     * ElementalEconomy does not have per-world support. Use has(OfflinePlayer, double) instead.
     *
     * @param playerName player name
     * @param worldName  world name
     * @param amount     amount
     * @return if the player's balance >= amount
     * @deprecated use has(OfflinePlayer, double) instead
     */
    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    /**
     * ElementalEconomy does not have per-world support. Use has(OfflinePlayer, double) instead.
     *
     * @param offlinePlayer player
     * @param worldName     world name
     * @param amount        amount
     * @return if the player's balance >= amount
     */
    @Override
    public boolean has(OfflinePlayer offlinePlayer, String worldName, double amount) {
        return has(offlinePlayer, amount);
    }

    /**
     * This method is deprecated. Use withdrawPlayer(OfflinePlayer, double) instead.
     *
     * @param playerName player name
     * @param amount     amount
     * @return Vault economy response
     * @deprecated use withdrawPlayer(OfflinePlayer, double) instead.
     */
    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return withdrawPlayer(Bukkit.getOfflinePlayer(playerName), amount);
    }

    /**
     * @param offlinePlayer player
     * @param amount        amount
     * @return Vault economy response
     */
    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {

        if (hasAccount(offlinePlayer) && has(offlinePlayer, amount)) {
            main.accountManager.getAccount(offlinePlayer.getUniqueId()).withdraw(getExternalCurrency(), BigDecimal.valueOf(amount));
            return new EconomyResponse(amount, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, null);
        } else {
            return new EconomyResponse(amount, getBalance(offlinePlayer), EconomyResponse.ResponseType.FAILURE, "Account non-existent or oversized withdrawal");
        }
    }

    /**
     * This method is deprecated and ElementalEconomy does not have per-world support. Use withdrawPlayer(OfflinePlayer, double) instead.
     *
     * @param playerName player name
     * @param worldName  world name
     * @param amount     amount
     * @return Vault economy response
     * @deprecated see notice above.
     */
    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(Bukkit.getOfflinePlayer(playerName), amount);
    }

    /**
     * ElementalEconomy does not have per-world support. Use withdrawPlayer(OfflinePlayer, double) instead.
     *
     * @param offlinePlayer player
     * @param worldName     world name
     * @param amount        amount
     * @return Vault economy response
     */
    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String worldName, double amount) {
        return withdrawPlayer(offlinePlayer, amount);
    }

    /**
     * This method is deprecated. Use depositPlayer(OfflinePlayer, dobule) instead.
     *
     * @param playerName player name
     * @param amount     amount
     * @return Vault economy response
     * @deprecated see notice above.
     */
    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return depositPlayer(Bukkit.getOfflinePlayer(playerName), amount);
    }

    /**
     * @param offlinePlayer player
     * @param amount        amount
     * @return Vault economy response
     */
    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
        if (hasAccount(offlinePlayer)) {
            main.accountManager.getAccount(offlinePlayer.getUniqueId()).deposit(getExternalCurrency(), BigDecimal.valueOf(amount));
            return new EconomyResponse(amount, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, null);
        } else {
            return new EconomyResponse(amount, 0.0, EconomyResponse.ResponseType.FAILURE, "Attempted to deposit " + amount + " but " + offlinePlayer.getUniqueId().toString() + " does not have an economy account");
        }
    }

    /**
     * This method is deprecated and ElementalEconomy does not have per-world support. Use depositPlayer(OfflinePlayer, double) instead.
     *
     * @param playerName player name
     * @param worldName  world name
     * @param amount     amount
     * @return Vault economy response
     * @deprecated see notice above.
     */
    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    /**
     * ElementalEconomy does not have per-world support. Use depositPlayer(OfflinePlayer, double) instead.
     *
     * @param offlinePlayer player
     * @param worldName     world name
     * @param amount        amount
     * @return Vault economy response
     */
    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String worldName, double amount) {
        return depositPlayer(offlinePlayer, amount);
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "ElementalEconomy does not have Vault bank support.");
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "ElementalEconomy does not have Vault bank support.");
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "ElementalEconomy does not have Vault bank support.");
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "ElementalEconomy does not have Vault bank support.");
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "ElementalEconomy does not have Vault bank support.");
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "ElementalEconomy does not have Vault bank support.");
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "ElementalEconomy does not have Vault bank support.");
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "ElementalEconomy does not have Vault bank support.");
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "ElementalEconomy does not have Vault bank support.");
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "ElementalEconomy does not have Vault bank support.");
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "ElementalEconomy does not have Vault bank support.");
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    /**
     * ElementalEconomy handles player account creation automatically.
     *
     * @param playerName player name
     * @deprecated see notice above.
     */
    @Override
    public boolean createPlayerAccount(String playerName) {
        return false;
    }

    /**
     * ElementalEconomy handles player account creation automatically.
     *
     * @param offlinePlayer player
     * @return false
     */
    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    /**
     * ElementalEconomy handles player account creation automatically.
     *
     * @param playerName player name
     * @param worldName  world name
     * @return false
     * @deprecated see notice above.
     */
    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return false;
    }

    /**
     * ElementalEconomy handles player account creation automatically.
     *
     * @param offlinePlayer player
     * @param worldName     world name
     * @return false
     */
    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String worldName) {
        return false;
    }
}
