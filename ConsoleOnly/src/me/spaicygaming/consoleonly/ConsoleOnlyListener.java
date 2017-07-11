package me.spaicygaming.consoleonly;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class ConsoleOnlyListener implements Listener{

	private ConsoleOnly main = ConsoleOnly.getInstance();
	private String prefix = main.getPrefix();
	
	@EventHandler
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent e) {
	  Player p = e.getPlayer();
	  Configuration config = main.getConfig();
	  String sounderror = "[ConsoleOnly] " + ChatColor.RED + "ConsoleOnly | entered sound does not exist.";
	  
	  	if (config.getBoolean("Settings.ConsoleOnly.Active")){
	  		//List<String> consoleonly = config.getStringList("Settings.ConsoleOnly.Commands");
	  		for (String command : main.consoleonly) {
	  			if ((e.getMessage().toLowerCase().startsWith("/" + command))) {
	  				e.setCancelled(true);
	  				p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', config.getString("Settings.ConsoleOnly.Message")));
	  				
	  				if (config.getBoolean("Settings.ConsoleOnly.Effects.sounds.active")){
	  					String sound = config.getString("Settings.ConsoleOnly.Effects.sounds.type");
	  					try {
	  						p.getWorld().playSound(p.getLocation(), Sound.valueOf(sound), 0.6F, 0.6F);
	  					} catch (Exception ex){
	  						main.getServer().getConsoleSender().sendMessage(sounderror);
	  					}
	  				}
	  				break;
	  			}
	  		}
	  	}
	  
	  	if (config.getBoolean("Settings.BlockedCommands.Active")) {
	  		//List<String> blockedcmds = config.getStringList("Settings.BlockedCommands.Commands");
	  		for (String command : main.blockedcmds) {
	  			if (!p.hasPermission("consoleonly.bypass")) {
	  				if (e.getMessage().toLowerCase().startsWith("/" + command)){
	  					e.setCancelled(true);
	  					p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', config.getString("Settings.BlockedCommands.Message")));
	  					
	  					if (config.getBoolean("Settings.BlockedCommands.Effects.sounds.active")){
	  						String sound = config.getString("Settings.BlockedCommands.Effects.sounds.type");
	  						try{
	  							p.getWorld().playSound(p.getLocation(), Sound.valueOf(sound), 0.6F, 0.6F);
	  						}catch (Exception ex){
	  							main.getServer().getConsoleSender().sendMessage(sounderror);
	  						}
		  				}
	  					break;
	  				}			  
	  			}
	  		}
	  	}
	  	
	  	/*
	  	 * BLOCK COMMANDS WITH :
	  	 */	  	
	  	if (main.getConfig().getBoolean("Settings.BlocksCmdsW/Colons.active")){
	  		if (e.getMessage().contains(":")){
	  			e.setCancelled(true);
	  			p.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', config.getString("Settings.BlocksCmdsW/Colons.message")));
	  			if (config.getBoolean("Settings.BlocksCmdsW/Colons.Effects.sounds.active")){
	  				String sound = config.getString("Settings.BlocksCmdsW/Colons.Effects.sounds.type");
	  				try{
						p.getWorld().playSound(p.getLocation(), Sound.valueOf(sound), 0.6F, 0.6F);
					}catch (Exception ex){
						main.getServer().getConsoleSender().sendMessage(sounderror);
					}
	  			}
	  		}
	  	}
	}
	
	//NOTIFICA AGGIORNAMENTO DISPONIBILE
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		
		if (main.checkupdates && (p.isOp() || p.hasPermission("*") || p.hasPermission("consoleonly.notify"))){
			if(main.updates.length == 2){
				p.sendMessage(ChatColor.GREEN + main.Separatori(31));
				p.sendMessage("§6§l[§cConsoleOnly§6] New update available:");
				p.sendMessage("§6Current version: §e" + main.getVer());
				p.sendMessage("§6New version: §e" + main.updates[0]);
				p.sendMessage("§6What's new: §e" + main.updates[1]);
				p.sendMessage(ChatColor.GREEN + main.Separatori(31));
			}
		}
	}
	
	
}
