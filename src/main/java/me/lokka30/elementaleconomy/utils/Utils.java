package me.lokka30.elementaleconomy.utils;

import me.lokka30.microlib.MicroLogger;

import java.util.Arrays;
import java.util.HashSet;

public class Utils {

    private Utils() {
        throw new UnsupportedOperationException("Must use static methods here.");
    }

    public static final MicroLogger logger = new MicroLogger("&b&lElementalEconomy: &7");

    public static final HashSet<String> supportedServerVersions = new HashSet<>(Arrays.asList("1.16", "1.15", "1.14", "1.13", "1.12", "1.11", "1.10", "1.9", "1.8", "1.7"));

}