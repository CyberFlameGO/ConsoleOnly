package me.spaicygaming.consoleonly;


import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CmdsBlocker extends JavaPlugin implements Listener{

	String ver = "1.0";
	String projectlink = "http://bit.ly/ConsoleOnly";
	String prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix")) + ChatColor.RESET + " ";
	
	public void onEnable() {
		Server server = getServer();
		ConsoleCommandSender console = server.getConsoleSender();
		
	    //console.sendMessage(ChatColor.GREEN + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
	    //console.sendMessage(ChatColor.AQUA + "         Console" + ChatColor.RED + "Only");
	    //console.sendMessage(ChatColor.AQUA + "          Version " + this.ver);
		//console.sendMessage(ChatColor.GOLD + " Project: " + ChatColor.RED + ChatColor.ITALIC +projectlink);
	    //console.sendMessage(ChatColor.GREEN + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
		
		getLogger().info("ConsoleOnly v" + ver + " has been enabled!");
		
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		if (!getConfig().getString("ConfigVersion").equals("1.0")) {
	        console.sendMessage("[ConsoleOnly] " + ChatColor.RED + "OUTDATED CONFIG FILE DETECTED, PLEASE DELETE THE OLD ONE!");
	    }
		saveDefaultConfig();
	}
	
	public void onDisable() {
		getLogger().info("ConsoleOnly has been Disabled!");
	}
		
	@EventHandler
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent e) {
	  Player p = e.getPlayer();
	  Configuration config = getConfig();
	  
	  	if (config.getBoolean("Settings.ConsoleOnly.Active")){
	  		List<String> consolenoly = config.getStringList("Settings.ConsoleOnly.Commands");
	  		for (String command : consolenoly) {
	  			if (e.getMessage().toLowerCase().startsWith("/" + command)) {
	  				e.setCancelled(true);
	  				p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', config.getString("Settings.ConsoleOnly.Message")));
	  			}
	  		}
	  	}
	  
	  	if (config.getBoolean("Settings.BlockedCommands.Active")) {
	  		List<String> blockedcmds = config.getStringList("Settings.BlockedCommands.Commands");
	  		for (String command : blockedcmds) {
	  			if (!p.hasPermission("consoleonly.bypass")) {
	  				if (e.getMessage().toLowerCase().startsWith("/" + command)){
	  					e.setCancelled(true);
	  					p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', config.getString("Settings.BlockedCommands.Message"))); 
	  				}			  
	  			}
	  		}
	  	}	  	
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args){
		if ((alias.equalsIgnoreCase("consoleonly")) || (alias.equalsIgnoreCase("co"))){
			if (args.length == 0){
				sender.sendMessage("");
				sender.sendMessage(ChatColor.RED + "   --=-=" + ChatColor.GOLD  + " ConsoleOnly " + ChatColor.GRAY + ver + ChatColor.RED + " =-=--");
				sender.sendMessage(ChatColor.AQUA + "   /co info " + ChatColor.GREEN + "->" + ChatColor.GRAY + " Show Info");
				sender.sendMessage(ChatColor.AQUA + "   /co reload " + ChatColor.GREEN + "->" + ChatColor.GRAY + " Reload the Config");
				sender.sendMessage(ChatColor.RED + "         --=-=-=-=-=-=--");
				sender.sendMessage("");
			}
			
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload")) {
					if (sender instanceof Player){
						if (sender.hasPermission("consoleonly.reload")){
							reloadConfig();
							sender.sendMessage(prefix + "§7Config Reloaded");
							getLogger().info("Config Reloaded");
						}
						else {
							sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.NoPermissions")));
						}
					}
					else {
						reloadConfig();
						getLogger().info("Config Reloaded");
					}
				}
				else if (args[0].equalsIgnoreCase("info")) {
					sender.sendMessage(ChatColor.DARK_GREEN + "    --=-=-=-=-=-=-=-=-=--");
					sender.sendMessage(ChatColor.GOLD + "       ConsoleOnly " + ChatColor.GRAY + "v" + ver);
					sender.sendMessage(ChatColor.RED + "    Project: " + ChatColor.ITALIC + projectlink);
					sender.sendMessage(ChatColor.DARK_GREEN + "      --=-=-=-=-=-=-=--");
					sender.sendMessage("");
				}
				else{
					if (sender instanceof Player){
						sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.WrongCommand")));
					}
					else {
						getLogger().info(getConfig().getString("Messages.WrongCommand"));
					}
				}
			}
			
		}
		if (args.length > 1) {
			if (sender instanceof Player) {
				sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.TooManyArgs")));
			}
			else {
				getLogger().info(getConfig().getString("Messages.TooManyArgs"));
			}
		}
		return false;
	}
		
}

