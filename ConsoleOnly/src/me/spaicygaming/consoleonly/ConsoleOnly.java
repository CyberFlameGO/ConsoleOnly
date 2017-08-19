package me.spaicygaming.consoleonly;


import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;

public class ConsoleOnly extends JavaPlugin{

	private static ConsoleOnly instance;
	public static ConsoleOnly getInstance(){
		return instance;
	}
	
	List<String> consoleonly;
	List<String> blockedcmds;
	
	public Object[] updates;
	boolean checkupdates = getConfig().getBoolean("UpdateChecker");
	
	private String ver = getDescription().getVersion();
	//CHANGE CONFIG VERSION (59)
	String prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix")) + ChatColor.RESET + " ";
	boolean antitabActive = false;
	
	public void onEnable() {
		instance = this;
		
		Server server = getServer();
		ConsoleCommandSender console = server.getConsoleSender();
		
		/*
	    console.sendMessage(ChatColor.GREEN + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
	    console.sendMessage(ChatColor.AQUA + "         Console" + ChatColor.RED + "Only");
	    console.sendMessage(ChatColor.AQUA + "          Version " + this.ver);
		console.sendMessage(ChatColor.GOLD + " Project: " + ChatColor.RED + ChatColor.ITALIC +projectlink);
	    console.sendMessage(ChatColor.GREEN + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
		*/

		getLogger().info("ConsoleOnly v" + ver + " has been enabled!");
		
		Bukkit.getServer().getPluginManager().registerEvents(new ConsoleOnlyListener(), this);
		getCommand("consoleonly").setExecutor(new ConsoleOnlyCommands());
		
		saveDefaultConfig();
		refreshLists();
		
		if (!getConfig().getString("ConfigVersion").equals("1.3")) {
	        console.sendMessage("[ConsoleOnly] " + ChatColor.RED + "OUTDATED CONFIG FILE DETECTED, PLEASE DELETE THE OLD ONE!");
	    }
		
		// ANTI TAB
		antitabActive = getConfig().getBoolean("Settings.AntiTab.active");
		
		if (antitabActive && getServer().getPluginManager().getPlugin("ProtocolLib") == null) {
			console.sendMessage("Plugin dependency ProtocolLib not found, disabling anti-tab protections!");
			antitabActive = false;
		}
		
		if (antitabActive){
			ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
			new AntiTab(protocolManager);
			getLogger().info("Anti-Tab enabled");
		}else{
			getLogger().info("Anti-Tab disabled");
		}
		
		// LOGS UPDATES
		updates = UpdateChecker.getLastUpdate();
		
		if (checkupdates){
			getLogger().info("Checking for updates...");
			
			if (updates.length == 2){
				getLogger().info(Separatori(70));
				getLogger().info("Update found! Download here: " + "https://www.spigotmc.org/resources/consoleonly.40873/");
				getLogger().info("New version: " + updates[0]);
				getLogger().info("What's new: " + updates[1]);
				getLogger().info(Separatori(70));
			} else {
				getLogger().info("No new version available." );
			}
		}
		
	}
	
	public void onDisable() {
		getLogger().info("ConsoleOnly has been Disabled!");
	}
		
	
	public void refreshLists(){
		consoleonly = getConfig().getStringList("Settings.ConsoleOnly.Commands");
		blockedcmds = getConfig().getStringList("Settings.BlockedCommands.Commands");
	}
	
	public String Separatori(int value){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < value; i++){
			sb.append("=");
		}
		return sb.toString();
	}
	

	/*
	private boolean alreadyExist(List<String> list, String arg, CommandSender s){
		for (String input : list){
			if (input.equalsIgnoreCase(arg.replaceAll("_", " "))){
				s.sendMessage(prefix + ChatColor.RED + "The command " + ChatColor.ITALIC + arg + ChatColor.RESET + ChatColor.RED + " is already blocked.");
				return true;
			}
		}
		return false;
	}
	
	private void addCommand(CommandSender s, String command){
		String successAdded = prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.SuccesfullyAdded").replaceAll("%command%", String.valueOf(command)));
		List<String> addlist = new ArrayList<String>();
		
		for (String k : getConfig().getStringList("Settings.ConsoleOnly.Commands")){
			addlist.add(k);
		}
		addlist.add(command);
		getConfig().set("Settings.ConsoleOnly.Commands", addlist);
		saveConfig();
		s.sendMessage(successAdded);
		refreshLists();
	}
	*/
	
	public String getPrefix(){
		return this.prefix;
	}
	
	public String getVer(){
		return ver;
	}
	
}
