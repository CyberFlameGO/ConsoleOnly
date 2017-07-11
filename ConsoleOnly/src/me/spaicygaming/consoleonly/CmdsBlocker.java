package me.spaicygaming.consoleonly;


import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CmdsBlocker extends JavaPlugin implements Listener{

	String ver = getDescription().getVersion();
	//CHANGE CONFIG VERSION (43)
	String projectlink = "http://bit.ly/ConsoleOnly";
	String sourcecodelink = "http://bit.ly/ConsoleOnlysource";
	String prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Prefix")) + ChatColor.RESET + " ";
	
	List<String> consoleonly;
	List<String> blockedcmds;
	
	public static Object[] updates;
	
	public static CmdsBlocker instance;
	
	public static CmdsBlocker getInstance(){
		return instance;
	}
	
	Boolean checkupdates = getConfig().getBoolean("UpdateChecker");
	
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
		
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		if (!getConfig().getString("ConfigVersion").equals("1.3")) {
	        console.sendMessage("[ConsoleOnly] " + ChatColor.RED + "OUTDATED CONFIG FILE DETECTED, PLEASE DELETE THE OLD ONE!");
	    }
		saveDefaultConfig();
		refreshLists();
		
		//LOGS UPDATES
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
		
	@EventHandler
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent e) {
	  Player p = e.getPlayer();
	  Configuration config = getConfig();
	  String sounderror = "[ConsoleOnly] " + ChatColor.RED + "ConsoleOnly | entered sound does not exist.";
	  
	  	if (config.getBoolean("Settings.ConsoleOnly.Active")){
	  		//List<String> consoleonly = config.getStringList("Settings.ConsoleOnly.Commands");
	  		for (String command : consoleonly) {
	  			if ((e.getMessage().toLowerCase().startsWith("/" + command))) {
	  				e.setCancelled(true);
	  				p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', config.getString("Settings.ConsoleOnly.Message")));
	  				
	  				if (config.getBoolean("Settings.ConsoleOnly.Effects.sounds.active")){
	  					String sound = config.getString("Settings.ConsoleOnly.Effects.sounds.type");
	  					try {
	  						p.getWorld().playSound(p.getLocation(), Sound.valueOf(sound), 0.6F, 0.6F);
	  					} catch (Exception ex){
	  						getServer().getConsoleSender().sendMessage(sounderror);
	  					}
	  				}
	  			}
	  		}
	  	}
	  
	  	if (config.getBoolean("Settings.BlockedCommands.Active")) {
	  		//List<String> blockedcmds = config.getStringList("Settings.BlockedCommands.Commands");
	  		for (String command : blockedcmds) {
	  			if (!p.hasPermission("consoleonly.bypass")) {
	  				if (e.getMessage().toLowerCase().startsWith("/" + command)){
	  					e.setCancelled(true);
	  					p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', config.getString("Settings.BlockedCommands.Message")));
	  					
	  					if (config.getBoolean("Settings.BlockedCommands.Effects.sounds.active")){
	  						String sound = config.getString("Settings.BlockedCommands.Effects.sounds.type");
	  						try{
	  							p.getWorld().playSound(p.getLocation(), Sound.valueOf(sound), 0.6F, 0.6F);
	  						}catch (Exception ex){
	  							getServer().getConsoleSender().sendMessage(sounderror);
	  						}
		  				}
	  				}			  
	  			}
	  		}
	  	}
	  	
	  	/*
	  	 * BLOCK COMMANDS WITH :
	  	 */	  	
	  	if (getConfig().getBoolean("Settings.BlocksCmdsW/Colons.active")){
	  		if (e.getMessage().contains(":")){
	  			e.setCancelled(true);
	  			p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', config.getString("Settings.BlocksCmdsW/Colons.message")));
	  			if (config.getBoolean("Settings.BlocksCmdsW/Colons.Effects.sounds.active")){
	  				String sound = config.getString("Settings.BlocksCmdsW/Colons.Effects.sounds.type");
	  				try{
						p.getWorld().playSound(p.getLocation(), Sound.valueOf(sound), 0.6F, 0.6F);
					}catch (Exception ex){
						getServer().getConsoleSender().sendMessage(sounderror);
					}
	  			}
	  		}
	  	}
	}

	public boolean onCommand(CommandSender s, Command cmd, String alias, String[] args){
		
		String unkncmd = prefix + "Unknown command, use /consoleonly help";
		String noperms = prefix + ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.NoPermissions"));
		
		if ((alias.equalsIgnoreCase("consoleonly")) || (alias.equalsIgnoreCase("co"))){
			if (args.length == 0){
				s.sendMessage(unkncmd);
			}

			else if (args.length == 1) {
				/*
				 * shows subcommands
				 */
				if (args[0].equalsIgnoreCase("help")){
					s.sendMessage("");
					s.sendMessage(ChatColor.RED + "   --=-=" + ChatColor.GOLD  + " ConsoleOnly " + ChatColor.GRAY + ver + ChatColor.RED + " =-=--");
					s.sendMessage(ChatColor.AQUA + "   /co info " + ChatColor.GREEN + "->" + ChatColor.GRAY + " Shows Info");
					s.sendMessage(ChatColor.AQUA + "   /co reload " + ChatColor.GREEN + "->" + ChatColor.GRAY + " Reloads the Config");
					s.sendMessage(ChatColor.AQUA + "   /co list consoleonly" + ChatColor.GREEN + "->" + ChatColor.GRAY + " Shows ConsoleOnly list.");
					s.sendMessage(ChatColor.AQUA + "   /co list blockedcmds" + ChatColor.GREEN + "->" + ChatColor.GRAY + " Shows BlockedCommands list.");
					//s.sendMessage(ChatColor.AQUA + "   /co addcmd <cmd>" + ChatColor.GREEN + "->" + ChatColor.GRAY + " Add Commands");
					s.sendMessage(ChatColor.RED + "         --=-=-=-=-=-=--");
					s.sendMessage("");
				}
				
				/*
				 * shows info
				 */
				else if (args[0].equalsIgnoreCase("info")) {
					s.sendMessage(ChatColor.DARK_GREEN + "     --=-=-=-=-=-=-=-=-=--");
					s.sendMessage(ChatColor.GOLD + "          ConsoleOnly " + ChatColor.GRAY + "v" + ver);
					s.sendMessage(ChatColor.RED + "    Project: " + ChatColor.ITALIC + projectlink);
					s.sendMessage(ChatColor.RED + "    SourceCode: " + ChatColor.ITALIC + sourcecodelink);
					s.sendMessage(ChatColor.DARK_GREEN + "       --=-=-=-=-=-=-=--");
					s.sendMessage("");
				}
				
				/*
				 * reload config
				 */
				else if (args[0].equalsIgnoreCase("reload")) {
					if (s instanceof Player){
						if (s.hasPermission("consoleonly.reload")){
							reloadConfig();
							refreshLists();
							s.sendMessage(prefix + "§7Config Reloaded.");
							getLogger().info("Config Reloaded.");
						}
						else {
							s.sendMessage(noperms);
						}
					}
					else {
						reloadConfig();
						getLogger().info("Config Reloaded.");
					}
				}
				/*
				 * list help
				 */
				else if (args[0].equalsIgnoreCase("list")){
					printListHelp(s);
				}
				
				/*
				 * else
				 */
				else{
					s.sendMessage(unkncmd);
				}
			}
			
			/*
			 * ARGS 2
			 */
			else if (args.length == 2){ 
				
				/*
				 * SHOW COMMANDS
				 */
				if (args[0].equalsIgnoreCase("list")){
					if (!s.hasPermission("consoleonly.list")){
						s.sendMessage(noperms);
						return true;
					}
					
					//CONSOLEONLY
					if (args[1].equalsIgnoreCase("consoleonly")){
						displayCmds(s, "ConsoleOnly", consoleonly);
					}
					//BLOCKED COMMANDS
					else if(args[1].equalsIgnoreCase("blockedcmds")){
						displayCmds(s, "BlockedCmds", blockedcmds);
					}
					//else
					else{
						printListHelp(s);
					}
				}
				
				/*
				 * ELSE
				 */
				else {
					s.sendMessage(unkncmd);
				}
			}
			
			/*
			 * ARGS 3
			 */
			/*
			else if(args.length == 3){
				if (args[0].equalsIgnoreCase("addcmd")){
					if (!s.hasPermission("consoleonly.addcmds")){
						s.sendMessage(noperms);
						return true;
					}
					if (args[1].equalsIgnoreCase("consoleonly")){
						if (alreadyExist(consoleonly, args[2], s)){
							return true;
						}

						addCommand(s, args[2]);
					}
					else if (args[1].equalsIgnoreCase("blockedcommands")){
						if (alreadyExist(blockedcmds, args[2], s)){
							return true;
						}

						addCommand(s, args[2]);
					}
				}
			}
			*/
			/*
			 * if args >
			 */
			else {
				if (s instanceof Player) {
					s.sendMessage(unkncmd);
				}
				else {
					getLogger().info(getConfig().getString("Unknown command, use /consoleonly help"));
				}
			}
		}
		return false;
	}
	
	private void displayCmds(CommandSender s,String name, List<String> list){
		s.sendMessage(ChatColor.RED + "  --= " + ChatColor.GOLD  + name + ChatColor.GRAY + " List" + ChatColor.RED + " =--");
		int index = 1;
		for (String cmd : list){
			s.sendMessage(ChatColor.GRAY + " " + index + "." + ChatColor.AQUA + " " + cmd);
			index++;
		}
		s.sendMessage(ChatColor.RED + "      --=-=-=-=-=-=--");
	}
	
	
	//NOTIFICA AGGIORNAMENTO DISPONIBILE
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		
		if (checkupdates && (p.isOp() || p.hasPermission("*") || p.hasPermission("consoleonly.notify"))){
			if(updates.length == 2){
				p.sendMessage(ChatColor.GREEN + Separatori(31));
				p.sendMessage("§6§l[§cConsoleOnly§6] New update available:");
				p.sendMessage("§6Current version: §e" + ver);
				p.sendMessage("§6New version: §e" + updates[0]);
				p.sendMessage("§6What's new: §e" + updates[1]);
				p.sendMessage(ChatColor.GREEN + Separatori(31));
			}
		}
	}
	
	public static String Separatori(int value){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < value; i++){
			sb.append("=");
		}
		return sb.toString();
	}
	
	private void printListHelp(CommandSender s){
		s.sendMessage("");
		s.sendMessage(ChatColor.RED + "   --=-=" + ChatColor.GOLD  + " Lists " + ChatColor.GRAY + "Help" + ChatColor.RED + " =-=--");
		s.sendMessage(ChatColor.AQUA + "   /co list consoleonly" + ChatColor.GREEN + "->" + ChatColor.GRAY + " Shows ConsoleOnly list.");
		s.sendMessage(ChatColor.AQUA + "   /co list blockedcmds" + ChatColor.GREEN + "->" + ChatColor.GRAY + " Shows BlockedCommands list.");
		s.sendMessage(ChatColor.RED + "         --=-=-=-=-=-=--");
		s.sendMessage("");
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
	public void refreshLists(){
		consoleonly = getConfig().getStringList("Settings.ConsoleOnly.Commands");
		blockedcmds = getConfig().getStringList("Settings.BlockedCommands.Commands");
	}
	
}
