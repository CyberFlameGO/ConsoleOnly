package io.github.spaicygaming.consoleonly.util;

import io.github.spaicygaming.consoleonly.ConsoleOnly;
import org.bukkit.ChatColor;

public class ChatUtil {

    /**
     * Main class instance
     */
    private static ConsoleOnly main = ConsoleOnly.getInstance();

    /**
     * Chat prefix
     */
    private static String prefix = main.getConfig().getBoolean("Prefix.active")
            ? colorString(main.getConfig().getString("Prefix.prefix")) + ChatColor.RESET + " " : "";

    /**
     * Get the chat prefix specified in the configuration file
     *
     * @return an empty string if the prefix is disabled
     */
    public static String getPrefix() {
        return prefix;
    }

    /**
     * Color the String translating it to ChatColor using the character '&'
     *
     * @param str the String to color
     * @return the colored string
     */
    public static String colorString(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    /**
     * Get the string from the configuration file, color it using {@link #colorString(String)}
     * and append it to the prefix
     *
     * @param configPath The subkey of config.yml's ConfigurationSection "Messages"
     * @return the prefix + colored message
     */
    public static String c(String configPath) {
        return prefix + colorString(main.getConfig().getString(configPath));
    }

    /**
     * Send a red message to the console
     *
     * @param message the message to send
     */
    public static void alertConsole(String message) {
        main.getServer().getConsoleSender().sendMessage("[ConsoleOnly] " + ChatColor.RED + message);
    }

    public static String createSeparators(char character, int amount) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= amount; i++) {
            stringBuilder.append(character);
        }
        return stringBuilder.toString();
    }

}
