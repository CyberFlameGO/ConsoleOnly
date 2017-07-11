package me.spaicygaming.consoleonly;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ConsoleOnlyCommands implements CommandExecutor{

	private ConsoleOnly main = ConsoleOnly.getInstance();
	
	private String prefix = main.getPrefix();
	private String projectlink = "http://bit.ly/ConsoleOnly";
	private String sourcecodelink = "http://bit.ly/ConsoleOnlysource";
	private String ver = main.getVer();
	
	public boolean onCommand(CommandSender s, Command cmd, String alias, String[] args){
		
		String unkncmd = prefix + "Unknown command, use /consoleonly help";
		String noperms = prefix + ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("Messages.NoPermissions"));
		
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
							main.reloadConfig();
							main.refreshLists();
							s.sendMessage(prefix + "§7Config Reloaded.");
							main.getLogger().info("Config Reloaded.");
						}
						else {
							s.sendMessage(noperms);
						}
					}
					else {
						main.reloadConfig();
						main.getLogger().info("Config Reloaded.");
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
						displayCmds(s, "ConsoleOnly", main.consoleonly);
					}
					//BLOCKED COMMANDS
					else if(args[1].equalsIgnoreCase("blockedcmds")){
						displayCmds(s, "BlockedCmds", main.blockedcmds);
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
			 * ADD CMD
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
			}*/
			
			/*
			 * if args >
			 */
			else {
				s.sendMessage(unkncmd);
			}
		}
		return false;
	}
	
	private void printListHelp(CommandSender s){
		s.sendMessage("");
		s.sendMessage(ChatColor.RED + "   --=-=" + ChatColor.GOLD  + " Lists " + ChatColor.GRAY + "Help" + ChatColor.RED + " =-=--");
		s.sendMessage(ChatColor.AQUA + "   /co list consoleonly" + ChatColor.GREEN + "->" + ChatColor.GRAY + " Shows ConsoleOnly list.");
		s.sendMessage(ChatColor.AQUA + "   /co list blockedcmds" + ChatColor.GREEN + "->" + ChatColor.GRAY + " Shows BlockedCommands list.");
		s.sendMessage(ChatColor.RED + "         --=-=-=-=-=-=--");
		s.sendMessage("");
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
	

}
