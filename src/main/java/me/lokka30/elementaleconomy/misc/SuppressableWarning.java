package me.lokka30.elementaleconomy.misc;

public enum SuppressableWarning {
    DEPRECATED_VAULT_METHOD_CALL("A plugin (not ElementalEconomy) made a deprecated method call using the Vault API. &8(&7%extraInformation%&8)"),
    UNSUPPORTED_VAULT_METHOD_CALL("A plugin (not ElementalEconomy) made a method call to the Vault API but ElementalEconomy does not support it. &8(&7%extraInformation%&8)");

    public final String warningMsg;

    SuppressableWarning(String warningMsg) {
        this.warningMsg = warningMsg;
    }
}
