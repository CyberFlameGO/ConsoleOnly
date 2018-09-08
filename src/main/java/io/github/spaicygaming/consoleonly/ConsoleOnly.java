package io.github.spaicygaming.consoleonly;

import com.comphenix.protocol.ProtocolLibrary;
import io.github.spaicygaming.consoleonly.restrictions.AntiTab;
import io.github.spaicygaming.consoleonly.restrictions.CommandsManager;
import io.github.spaicygaming.consoleonly.restrictions.ConsoleOnlyListeners;
import io.github.spaicygaming.consoleonly.updater.UpdateChecker;
import io.github.spaicygaming.consoleonly.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ConsoleOnly extends JavaPlugin {

    private static ConsoleOnly instance;
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
        commandsManager = new CommandsManager(this);

        // Register listeners and commands
        getServer().getPluginManager().registerEvents(new ConsoleOnlyListeners(this), this);
        getCommand("consoleonly").setExecutor(new ConsoleOnlyCommands(this));

        // Notify if using an outdated configuration file version
        if (getConfig().getDouble("ConfigVersion") < 1.7) {
            ChatUtil.alertConsole("OUTDATED CONFIG FILE DETECTED, PLEASE DELETE THE OLD ONE!");
            ChatUtil.alertConsole("You can also update it manually. Updated config: https://github.com/SpaicyGaming/ConsoleOnly/blob/master/ConsoleOnly/config.yml");
        }

        // Anti-TAB
        if (getConfig().getBoolean("Settings.AntiTab.active"))
            if (getServer().getPluginManager().getPlugin("ProtocolLib") == null) {
                console.sendMessage("Plugin dependency ProtocolLib not found, can't enable anti-tab protections!");
            } else {
                new AntiTab(this, commandsManager, ProtocolLibrary.getProtocolManager());
                getLogger().info("Anti-Tab enabled");

            }

        getLogger().info("ConsoleOnly v" + ver + " has been enabled!");

        // Update Checker
        if (getConfig().getBoolean("UpdateChecker")) {
            getLogger().info("Checking for updates...");
            UpdateChecker updateChecker = new UpdateChecker(this, ver);

            if (updateChecker.availableUpdate()) {
                console.sendMessage(ChatColor.GREEN + ChatUtil.createSeparators('=', 70));
                console.sendMessage(ChatColor.AQUA + "Update found! Download here: " + "https://www.spigotmc.org/resources/consoleonly.40873/");
                console.sendMessage(ChatColor.AQUA + "Current version: " + updateChecker.getCurrentVersion());
                console.sendMessage(ChatColor.AQUA + "Latest version: " + updateChecker.getLatestVersion());
                console.sendMessage(ChatColor.AQUA + "What's new: " + updateChecker.getLatestUpdateTitle());
                console.sendMessage(ChatColor.GREEN + ChatUtil.createSeparators('=', 70));
            } else {
                getLogger().info("No new version available.");
            }
        }
    }

    /**
     * Get class instance
     *
     * @return the class instance
     */
    public static ConsoleOnly getInstance() {
        return instance;
    }

    public CommandsManager getCommandsManager() {
        return commandsManager;
    }


    @Override
    public void reloadConfig() {
        super.reloadConfig();
        commandsManager.initializeLists();
    }

    /**
     * @return the running plugin version
     */
    String getRunningVersion() {
        return ver;
    }

}
