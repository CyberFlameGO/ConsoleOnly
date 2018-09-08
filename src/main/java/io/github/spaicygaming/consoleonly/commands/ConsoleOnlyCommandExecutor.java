package io.github.spaicygaming.consoleonly.commands;

import io.github.spaicygaming.consoleonly.ConsoleOnly;
import io.github.spaicygaming.consoleonly.Permission;
import io.github.spaicygaming.consoleonly.restrictions.CommandsManager;
import io.github.spaicygaming.consoleonly.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ConsoleOnlyCommandExecutor implements CommandExecutor {

    private ConsoleOnly main;
    private CommandsManager cmdsManager;

    private String noPermissionsMsg, currentPluginVersion;

    public ConsoleOnlyCommandExecutor(ConsoleOnly main) {
        this.main = main;
        this.cmdsManager = main.getCommandsManager();

        this.noPermissionsMsg = ChatUtil.c("Messages.noPermissions");
        this.currentPluginVersion = main.getDescription().getVersion();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        // Args length 1
        if (args.length == 1) {
            // Help
            if (args[0].equalsIgnoreCase("help")) {
                printHelpMenu(sender);
            }
            // Info
            else if (args[0].equalsIgnoreCase("info")) {
                sender.sendMessage(ChatColor.DARK_GREEN + "     --=-=-=-=-=-=-=-=-=--");
                sender.sendMessage(ChatColor.GOLD + "          ConsoleOnly " + ChatColor.GRAY + "v" + currentPluginVersion);
                sender.sendMessage(ChatColor.RED + "    Project: " + ChatColor.ITALIC + "http://bit.ly/ConsoleOnly");
                sender.sendMessage(ChatColor.RED + "    SourceCode: " + ChatColor.ITALIC + "http://bit.ly/ConsoleOnlysource");
                sender.sendMessage(ChatColor.DARK_GREEN + "       --=-=-=-=-=-=-=--");
                sender.sendMessage("");
            }
            // Reload
            else if (args[0].equalsIgnoreCase("reload")) {
                if (!Permission.CMD_RELOAD.has(sender)) {
                    sender.sendMessage(noPermissionsMsg);
                }
                main.reloadConfig();
                sender.sendMessage(ChatUtil.getPrefix() + "§7Config Reloaded.");
            }
            // List help
            else if (args[0].equalsIgnoreCase("list")) {
                printListHelp(sender);
            }
            // Wrong arg
            else {
                printHelpMenu(sender);
            }
            return true;
        }

        // Args length 2
        else if (args.length == 2) {
            // List
            if (args[0].equalsIgnoreCase("list")) {
                if (!Permission.CMD_LIST.has(sender)) {
                    sender.sendMessage(noPermissionsMsg);
                    return false;
                }

                // ConsoleOnly
                if (args[1].equalsIgnoreCase("consoleonly")) {
                    displayCmds(sender, "ConsoleOnly", cmdsManager.getConsoleOnlyCmds());
                }
                // Blocked Commands
                else if (args[1].equalsIgnoreCase("blockedcmds")) {
                    displayCmds(sender, "BlockedCmds", cmdsManager.getBlockedCmds());
                }
                // Anti-tab
                else if (args[1].equalsIgnoreCase("antitab")) {
                    displayCmds(sender, "Anti-TAB", main.getConfig().getStringList("Settings.AntiTab.Commands"));
                }
                // Wrong arg
                else {
                    printListHelp(sender);
                }
            }
            // Wrong args
            else {
                printHelpMenu(sender);
            }
            return true;
        }
        // Wrong args length
        else {
            printHelpMenu(sender);
        }
        return false;
    }

    // TODO move in the config.yml
    private void printHelpMenu(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(ChatColor.RED + "   --=-=" + ChatColor.GOLD + " ConsoleOnly " + ChatColor.GRAY + currentPluginVersion + ChatColor.RED + " =-=--");
        sender.sendMessage(ChatColor.AQUA + "   /co info " + ChatColor.GREEN + "->" + ChatColor.GRAY + " Shows Info");
        sender.sendMessage(ChatColor.AQUA + "   /co reload " + ChatColor.GREEN + "->" + ChatColor.GRAY + " Reloads the Config");
        sender.sendMessage(ChatColor.AQUA + "   /co list consoleonly" + ChatColor.GREEN + "->" + ChatColor.GRAY + " Shows ConsoleOnly list.");
        sender.sendMessage(ChatColor.AQUA + "   /co list blockedcmds" + ChatColor.GREEN + "->" + ChatColor.GRAY + " Shows BlockedCommands list.");
        sender.sendMessage(ChatColor.AQUA + "   /co list antitab" + ChatColor.GREEN + "->" + ChatColor.GRAY + " Shows Anti-TAB list.");
        sender.sendMessage(ChatColor.RED + "         --=-=-=-=-=-=--");
        sender.sendMessage("");
    }

    // TODO move in the config.yml
    private void printListHelp(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(ChatColor.RED + "   --=-=" + ChatColor.GOLD + " Lists " + ChatColor.GRAY + "Help" + ChatColor.RED + " =-=--");
        sender.sendMessage(ChatColor.AQUA + "   /co list consoleonly" + ChatColor.GREEN + "->" + ChatColor.GRAY + " Shows ConsoleOnly list.");
        sender.sendMessage(ChatColor.AQUA + "   /co list blockedcmds" + ChatColor.GREEN + "->" + ChatColor.GRAY + " Shows BlockedCommands list.");
        sender.sendMessage(ChatColor.AQUA + "   /co list antitab" + ChatColor.GREEN + "->" + ChatColor.GRAY + " Shows Anti-TAB list.");
        sender.sendMessage(ChatColor.RED + "         --=-=-=-=-=-=--");
        sender.sendMessage("");
    }

    private void displayCmds(CommandSender sender, String listName, List<String> commandsList) {
        sender.sendMessage(ChatColor.RED + "  --= " + ChatColor.GOLD + listName + ChatColor.GRAY + " List" + ChatColor.RED + " =--");
        int index = 1;
        for (String cmd : commandsList) {
            sender.sendMessage(ChatColor.GRAY + " " + index + "." + ChatColor.AQUA + " " + cmd);
            index++;
        }
        sender.sendMessage(ChatColor.RED + "      --=-=-=-=-=-=--");
    }

}
