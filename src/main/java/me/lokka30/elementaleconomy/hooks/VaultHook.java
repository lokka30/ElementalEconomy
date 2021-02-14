package me.lokka30.elementaleconomy.hooks;

import me.lokka30.elementaleconomy.ElementalEconomy;
import me.lokka30.elementaleconomy.currencies.Currency;
import me.lokka30.elementaleconomy.misc.DebugMessageType;
import me.lokka30.elementaleconomy.misc.SuppressableWarning;
import me.lokka30.elementaleconomy.utils.Utils;
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
        Utils.sendSuppressableWarning(SuppressableWarning.DEPRECATED_VAULT_METHOD_CALL, "method=hasAccount(String), username=" + playerName, main);
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
        Utils.sendSuppressableWarning(SuppressableWarning.DEPRECATED_VAULT_METHOD_CALL, "method=hasAccount(String, String), username=" + playerName + ", world=" + worldName, main);
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
        Utils.sendSuppressableWarning(SuppressableWarning.DEPRECATED_VAULT_METHOD_CALL, "method=getBalance(String), username=" + playerName, main);
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
        return main.accountManager.getAccount(offlinePlayer.getUniqueId()).getBalance(getExternalCurrency().id).doubleValue();
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
        Utils.sendSuppressableWarning(SuppressableWarning.DEPRECATED_VAULT_METHOD_CALL, "method=getBalance(String, String), username=" + playerName + ", world=" + worldName, main);
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
        Utils.sendSuppressableWarning(SuppressableWarning.DEPRECATED_VAULT_METHOD_CALL, "method=getBalance(OfflinePlayer, String), username=" + offlinePlayer.getName() + ", world=" + worldName, main);
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
        Utils.sendSuppressableWarning(SuppressableWarning.DEPRECATED_VAULT_METHOD_CALL, "method=has(String, double), username=" + playerName + ", amount=" + amount, main);
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
        return main.accountManager.getAccount(offlinePlayer.getUniqueId()).has(getExternalCurrency().id, BigDecimal.valueOf(amount));
    }

    /**
     * This method is deprecated. Use has(OfflinePlayer, double) instead.
     * ElementalEconomy does not have per-world support. Use has(OfflinePlayer, double) instead.
     *
     * @param playerName player name
     * @param worldName  world name
     * @param amount     amount
     * @return if the player's balance >= amount
     * @deprecated see notice above.
     */
    @Override
    public boolean has(String playerName, String worldName, double amount) {
        Utils.sendSuppressableWarning(SuppressableWarning.DEPRECATED_VAULT_METHOD_CALL, "method=has(String, String, amount), username=" + playerName + ", world=" + worldName + ", amount=" + amount, main);
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
     * @deprecated see notice above.
     */
    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        Utils.sendSuppressableWarning(SuppressableWarning.DEPRECATED_VAULT_METHOD_CALL, "method=withdrawPlayer(String, double), username=" + playerName + ", amount=" + amount, main);
        return withdrawPlayer(Bukkit.getOfflinePlayer(playerName), amount);
    }

    /**
     * @param offlinePlayer player
     * @param amount        amount
     * @return Vault economy response
     */
    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {

        if (hasAccount(offlinePlayer)) {
            if (has(offlinePlayer, amount)) {
                main.accountManager.getAccount(offlinePlayer.getUniqueId()).withdraw(getExternalCurrency().id, BigDecimal.valueOf(amount));
                return new EconomyResponse(amount, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, null);
            } else {
                Utils.logger.error("A plugin (NOT ElementalEconomy) tried to run Vault's withdrawPlayer but the player doesn't have enough money to withdraw. Player: " + offlinePlayer.getName());
                return new EconomyResponse(amount, getBalance(offlinePlayer), EconomyResponse.ResponseType.FAILURE, "Oversized withdrawal");
            }
        } else {
            Utils.logger.error("A plugin (NOT ElementalEconomy) tried to run Vault's withdrawPlayer but the player doesn't have an account. Player: " + offlinePlayer.getName());
            return new EconomyResponse(amount, getBalance(offlinePlayer), EconomyResponse.ResponseType.FAILURE, "Account non-existent");
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
        Utils.sendSuppressableWarning(SuppressableWarning.DEPRECATED_VAULT_METHOD_CALL, "method=withdrawPlayer(String, String, double), username=" + playerName + ", world=" + worldName + ", amount=" + amount, main);
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
        Utils.sendSuppressableWarning(SuppressableWarning.DEPRECATED_VAULT_METHOD_CALL, "method=depositPlayer(String, double), username=" + playerName + ", amount=" + amount, main);
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
            main.accountManager.getAccount(offlinePlayer.getUniqueId()).deposit(getExternalCurrency().id, BigDecimal.valueOf(amount));

            Utils.sendDebugMessage(DebugMessageType.BALANCE_METHODS, "VaultHook#depositPlayer ran:");
            Utils.sendDebugMessage(DebugMessageType.BALANCE_METHODS, "&8 - &7Username: " + offlinePlayer.getName());
            Utils.sendDebugMessage(DebugMessageType.BALANCE_METHODS, "&8 - &7Amount: " + amount);

            return new EconomyResponse(amount, getBalance(offlinePlayer), EconomyResponse.ResponseType.SUCCESS, null);
        } else {
            Utils.logger.error("A plugin (NOT ElementalEconomy) tried to run Vault's depositPlayer but the player doesn't have an account. Player: " + offlinePlayer.getName());
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
        Utils.sendSuppressableWarning(SuppressableWarning.DEPRECATED_VAULT_METHOD_CALL, "method=depositPlayer(String, String, amount), username=" + playerName + ", world=" + worldName + ", amount=" + amount, main);
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
        Utils.sendSuppressableWarning(SuppressableWarning.UNSUPPORTED_VAULT_METHOD_CALL, "method=createBank(String, String)", main);
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "ElementalEconomy does not have Vault bank support.");
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        Utils.sendSuppressableWarning(SuppressableWarning.UNSUPPORTED_VAULT_METHOD_CALL, "method=createBank(String, OfflinePlayer)", main);
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "ElementalEconomy does not have Vault bank support.");
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        Utils.sendSuppressableWarning(SuppressableWarning.UNSUPPORTED_VAULT_METHOD_CALL, "method=deleteBank(String)", main);
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "ElementalEconomy does not have Vault bank support.");
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        Utils.sendSuppressableWarning(SuppressableWarning.UNSUPPORTED_VAULT_METHOD_CALL, "method=bankBalance(String)", main);
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "ElementalEconomy does not have Vault bank support.");
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        Utils.sendSuppressableWarning(SuppressableWarning.UNSUPPORTED_VAULT_METHOD_CALL, "method=bankHas(String, double)", main);
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "ElementalEconomy does not have Vault bank support.");
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        Utils.sendSuppressableWarning(SuppressableWarning.UNSUPPORTED_VAULT_METHOD_CALL, "method=bankWithdraw(String, double)", main);
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "ElementalEconomy does not have Vault bank support.");
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        Utils.sendSuppressableWarning(SuppressableWarning.UNSUPPORTED_VAULT_METHOD_CALL, "method=bankDeposit(String, double)", main);
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "ElementalEconomy does not have Vault bank support.");
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        Utils.sendSuppressableWarning(SuppressableWarning.UNSUPPORTED_VAULT_METHOD_CALL, "method=isBankOwner(String, String)", main);
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "ElementalEconomy does not have Vault bank support.");
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        Utils.sendSuppressableWarning(SuppressableWarning.UNSUPPORTED_VAULT_METHOD_CALL, "method=isBankOwner(String, OfflinePlayer)", main);
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "ElementalEconomy does not have Vault bank support.");
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        Utils.sendSuppressableWarning(SuppressableWarning.UNSUPPORTED_VAULT_METHOD_CALL, "method=isBankMember(String, String)", main);
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "ElementalEconomy does not have Vault bank support.");
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        Utils.sendSuppressableWarning(SuppressableWarning.UNSUPPORTED_VAULT_METHOD_CALL, "method=isBankMember(String, OfflinePlayer)", main);
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "ElementalEconomy does not have Vault bank support.");
    }

    @Override
    public List<String> getBanks() {
        Utils.sendSuppressableWarning(SuppressableWarning.UNSUPPORTED_VAULT_METHOD_CALL, "method=getBanks()", main);
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
        Utils.sendSuppressableWarning(SuppressableWarning.UNSUPPORTED_VAULT_METHOD_CALL, "method=createPlayerAccount(String)", main);
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
        Utils.sendSuppressableWarning(SuppressableWarning.UNSUPPORTED_VAULT_METHOD_CALL, "method=createPlayerAccount(OfflinePlayer)", main);
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
        Utils.sendSuppressableWarning(SuppressableWarning.UNSUPPORTED_VAULT_METHOD_CALL, "method=createPlayerAccount(String, String)", main);
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
        Utils.sendSuppressableWarning(SuppressableWarning.UNSUPPORTED_VAULT_METHOD_CALL, "method=createPlayerAccount(OfflinePlayer, String)", main);
        return false;
    }
}
