package me.spaicygaming.consoleonly;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ConsoleOnlyCommands implements CommandExecutor {

    private ConsoleOnly main = ConsoleOnly.getInstance();
    private CommandsManager cmdsManager = main.getCommandsManager();

    private String prefix = main.getPrefix();
    private String unknownCmd = prefix + "Unknown command, use /consoleonly help";
    private String noPerms = prefix + ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("Messages.NoPermissions"));

    private String projectLink = "http://bit.ly/ConsoleOnly";
    private String sourceCodeLink = "http://bit.ly/ConsoleOnlysource";
    private String ver = main.getVer();

    public boolean onCommand(CommandSender s, Command cmd, String alias, String[] args) {

        if (args.length == 1) {
            // Help
            if (args[0].equalsIgnoreCase("help")) {
                s.sendMessage("");
                s.sendMessage(ChatColor.RED + "   --=-=" + ChatColor.GOLD + " ConsoleOnly " + ChatColor.GRAY + ver + ChatColor.RED + " =-=--");
                s.sendMessage(ChatColor.AQUA + "   /co info " + ChatColor.GREEN + "->" + ChatColor.GRAY + " Shows Info");
                s.sendMessage(ChatColor.AQUA + "   /co reload " + ChatColor.GREEN + "->" + ChatColor.GRAY + " Reloads the Config");
                s.sendMessage(ChatColor.AQUA + "   /co list consoleonly" + ChatColor.GREEN + "->" + ChatColor.GRAY + " Shows ConsoleOnly list.");
                s.sendMessage(ChatColor.AQUA + "   /co list blockedcmds" + ChatColor.GREEN + "->" + ChatColor.GRAY + " Shows BlockedCommands list.");
                s.sendMessage(ChatColor.AQUA + "   /co list antitab" + ChatColor.GREEN + "->" + ChatColor.GRAY + " Shows Anti-TAB list.");
                // s.sendMessage(ChatColor.AQUA + " /co addcmd <cmd>" + ChatColor.GREEN + "->" + ChatColor.GRAY + " Add Commands");
                s.sendMessage(ChatColor.RED + "         --=-=-=-=-=-=--");
                s.sendMessage("");
                return true;
            }

            // Info
            else if (args[0].equalsIgnoreCase("info")) {
                s.sendMessage(ChatColor.DARK_GREEN + "     --=-=-=-=-=-=-=-=-=--");
                s.sendMessage(ChatColor.GOLD + "          ConsoleOnly " + ChatColor.GRAY + "v" + ver);
                s.sendMessage(ChatColor.RED + "    Project: " + ChatColor.ITALIC + projectLink);
                s.sendMessage(ChatColor.RED + "    SourceCode: " + ChatColor.ITALIC + sourceCodeLink);
                s.sendMessage(ChatColor.DARK_GREEN + "       --=-=-=-=-=-=-=--");
                s.sendMessage("");
                return true;
            }

            // Reload
            else if (args[0].equalsIgnoreCase("reload")) {
                if (!s.hasPermission("consoleonly.reload")) {
                    s.sendMessage(noPerms);
                }
                main.reloadConfig();
                cmdsManager.refreshLists();
                s.sendMessage(prefix + "�7Config Reloaded.");
                return true;
            }

            // List help
            else if (args[0].equalsIgnoreCase("list")) {
                printListHelp(s);
                return true;
            }

            // Wrong arg
            else {
                s.sendMessage(unknownCmd);
            }
            return true;
        }

        /*
         * ARGS 2
         */
        else if (args.length == 2) {
            // List
            if (args[0].equalsIgnoreCase("list")) {
                if (!s.hasPermission("consoleonly.list")) {
                    s.sendMessage(noPerms);
                    return true;
                }

                // ConsoleOnly
                if (args[1].equalsIgnoreCase("consoleonly")) {
                    displayCmds(s, "ConsoleOnly", cmdsManager.getConsoleOnlyCmds());
                }
                // Blocked Commands
                else if (args[1].equalsIgnoreCase("blockedcmds")) {
                    displayCmds(s, "BlockedCmds", cmdsManager.getBlockedCmds());
                }
                // Anti-tab
                else if (args[1].equalsIgnoreCase("antitab")) {
                    displayCmds(s, "Anti-TAB", main.getConfig().getStringList("Settings.AntiTab.Commands"));
                }
                // Wrong arg
                else {
                    printListHelp(s);
                }
            }

            // Wrong args
            else {
                s.sendMessage(unknownCmd);
            }
            return true;
        }

        // Wrong args length
        else {
            s.sendMessage(unknownCmd);
        }
        return false;
    }

    private void printListHelp(CommandSender s) {
        s.sendMessage("");
        s.sendMessage(ChatColor.RED + "   --=-=" + ChatColor.GOLD + " Lists " + ChatColor.GRAY + "Help" + ChatColor.RED + " =-=--");
        s.sendMessage(ChatColor.AQUA + "   /co list consoleonly" + ChatColor.GREEN + "->" + ChatColor.GRAY + " Shows ConsoleOnly list.");
        s.sendMessage(ChatColor.AQUA + "   /co list blockedcmds" + ChatColor.GREEN + "->" + ChatColor.GRAY + " Shows BlockedCommands list.");
        s.sendMessage(ChatColor.AQUA + "   /co list antitab" + ChatColor.GREEN + "->" + ChatColor.GRAY + " Shows Anti-TAB list.");
        s.sendMessage(ChatColor.RED + "         --=-=-=-=-=-=--");
        s.sendMessage("");
    }

    private void displayCmds(CommandSender s, String name, List<String> list) {
        s.sendMessage(ChatColor.RED + "  --= " + ChatColor.GOLD + name + ChatColor.GRAY + " List" + ChatColor.RED + " =--");
        int index = 1;
        for (String cmd : list) {
            s.sendMessage(ChatColor.GRAY + " " + index + "." + ChatColor.AQUA + " " + cmd);
            index++;
        }
        s.sendMessage(ChatColor.RED + "      --=-=-=-=-=-=--");
    }

}
