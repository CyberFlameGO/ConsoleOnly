package me.spaicygaming.consoleonly;

import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ConsoleOnly extends JavaPlugin {

    private static ConsoleOnly instance;

    // Stuff
    private UpdateChecker updateChecker;
    private CommandsManager commandsManager;

    private String ver = getDescription().getVersion();

    public void onEnable() {
        instance = this;
        ConsoleCommandSender console = getServer().getConsoleSender();
		
		/*
	    console.sendMessage(ChatColor.GREEN + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
	    console.sendMessage(ChatColor.AQUA + "         Console" + ChatColor.RED + "Only");
	    console.sendMessage(ChatColor.AQUA + "          Version " + this.ver);
		console.sendMessage(ChatColor.GOLD + " Project: " + ChatColor.RED + ChatColor.ITALIC +projectlink);
	    console.sendMessage(ChatColor.GREEN + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
		*/

        saveDefaultConfig();
        commandsManager = new CommandsManager();
        updateChecker = new UpdateChecker();

        getServer().getPluginManager().registerEvents(new ConsoleOnlyListeners(), this);
        getCommand("consoleonly").setExecutor(new ConsoleOnlyCommands());

        // Config Version
        if (getConfig().getDouble("ConfigVersion") < 1.7) {
            console.sendMessage("[ConsoleOnly] " + ChatColor.RED + "OUTDATED CONFIG FILE DETECTED, PLEASE DELETE THE OLD ONE!");
            console.sendMessage("[ConsoleOnly] " + ChatColor.RED + "You can also update it manually. Updated config: https://github.com/SpaicyGaming/ConsoleOnly/blob/master/ConsoleOnly/config.yml");
        }

        // Anti-TAB
        boolean useAntitab = getConfig().getBoolean("Settings.AntiTab.active");
        if (useAntitab && getServer().getPluginManager().getPlugin("ProtocolLib") == null) {
            console.sendMessage("Plugin dependency ProtocolLib not found, disabling anti-tab protections!");
        } else {
            new AntiTab(ProtocolLibrary.getProtocolManager());
            getLogger().info("Anti-Tab enabled");
        }

        getLogger().info("ConsoleOnly v" + ver + " has been enabled!");

        // Update Checker
        if (!checkForUpdates()) {
            return;
        }
        getLogger().info("Checking for updates...");

        if (updateChecker.availableUpdate()) {
            console.sendMessage(ChatColor.GREEN + Separatori(70));
            console.sendMessage(ChatColor.AQUA + "Update found! Download here: " + "https://www.spigotmc.org/resources/consoleonly.40873/");
            console.sendMessage(ChatColor.AQUA + "Current version: " + ver);
            console.sendMessage(ChatColor.AQUA + "Latest version: " + updateChecker.getLatestVersion());
            console.sendMessage(ChatColor.AQUA + "What's new: " + updateChecker.getUpdateTitle());
            console.sendMessage(ChatColor.GREEN + Separatori(70));
            return;
        }
        getLogger().info("No new version available.");

    }

    public static ConsoleOnly getInstance() {
        return instance;
    }

    public String Separatori(int value) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value; i++) {
            sb.append("=");
        }
        return sb.toString();
    }

    public CommandsManager getCommandsManager() {
        return commandsManager;
    }

    /**
     * Return the UpdateChecker
     *
     * @return
     */
    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    /**
     * Check if the update checker is enabled in the config.yml
     *
     * @return false -> don't check
     */
    public boolean checkForUpdates() {
        return getConfig().getBoolean("UpdateChecker");
    }

    /**
     * Return the chat prefix
     *
     * @return
     */
    public String getPrefix() {
        if (getConfig().getBoolean("Prefix.active")) {
            return ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix.prefix")) + ChatColor.RESET + " ";
        }
        return "";
    }

    /**
     * Return the current plugin version
     *
     * @return
     */
    public String getVer() {
        return ver;
    }

}
