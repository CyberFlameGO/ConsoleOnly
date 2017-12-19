package me.spaicygaming.consoleonly;

import java.util.List;

public class CommandsManager {

	private ConsoleOnly main = ConsoleOnly.getInstance();
	
	private List<String> consoleonly;
	private List<String> blockedcmds;
	
	public CommandsManager(){
		refreshLists();
	}
	
	public void refreshLists(){
		consoleonly = main.getConfig().getStringList("Settings.ConsoleOnly.Commands");
		blockedcmds = main.getConfig().getStringList("Settings.BlockedCommands.Commands");
	}
	
	public List<String> getBlockedCmds(){
		return blockedcmds;
	}
	
	public List<String> getConsoleOnlyCmds(){
		return consoleonly;
	}
	
}
