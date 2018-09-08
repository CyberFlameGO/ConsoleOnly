package io.github.spaicygaming.consoleonly;

import io.github.spaicygaming.consoleonly.restrictions.CommandsManager;
import io.github.spaicygaming.consoleonly.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ConsoleOnlyCommands implements CommandExecutor {

    private ConsoleOnly main;
    private CommandsManager cmdsManager;

    private String noPerms;

    private String projectLink = "http://bit.ly/ConsoleOnly";
    private String sourceCodeLink = "http://bit.ly/ConsoleOnlysource";
    private String ver = main.getRunningVersion();

    ConsoleOnlyCommands(ConsoleOnly main) {
        this.main = main;
        this.cmdsManager = main.getCommandsManager();

        this.noPerms = ChatUtil.c("Messages.NoPermissions");
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
                sender.sendMessage(ChatColor.GOLD + "          ConsoleOnly " + ChatColor.GRAY + "v" + ver);
                sender.sendMessage(ChatColor.RED + "    Project: " + ChatColor.ITALIC + projectLink);
                sender.sendMessage(ChatColor.RED + "    SourceCode: " + ChatColor.ITALIC + sourceCodeLink);
                sender.sendMessage(ChatColor.DARK_GREEN + "       --=-=-=-=-=-=-=--");
                sender.sendMessage("");
            }
            // Reload
            else if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("consoleonly.reload")) {
                    sender.sendMessage(noPerms);
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
                if (!sender.hasPermission("consoleonly.list")) {
                    sender.sendMessage(noPerms);
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
        sender.sendMessage(ChatColor.RED + "   --=-=" + ChatColor.GOLD + " ConsoleOnly " + ChatColor.GRAY + ver + ChatColor.RED + " =-=--");
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
