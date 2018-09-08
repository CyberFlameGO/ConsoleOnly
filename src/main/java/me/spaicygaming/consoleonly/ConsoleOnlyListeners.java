package me.spaicygaming.consoleonly;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class ConsoleOnlyListeners implements Listener {

    private ConsoleOnly main = ConsoleOnly.getInstance();
    private UpdateChecker updateChecker = main.getUpdateChecker();
    private CommandsManager cmdsManager = main.getCommandsManager();

    // Sounds name
    private String consoleOnlySoundName = main.getConfig().getString("Settings.ConsoleOnly.Effects.sounds.type");
    private String blockedCmdsSoundName = main.getConfig().getString("Settings.BlockedCommands.Effects.sounds.type");
    private String colonSoundName = main.getConfig().getString("Settings.BlocksCmdsWColons.Effects.sounds.type");

    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent e) {
        String message = e.getMessage();
        Player p = e.getPlayer();

        /*
         * Whitelisted Commands
         */
        for (String whitelistedCmd : main.getConfig().getStringList("Settings.WhitelistedCommands")) {
            if (message.equalsIgnoreCase("/" + whitelistedCmd)) {
                return;
            }
        }

        /*
         * Console Only
         */
        if (main.getConfig().getBoolean("Settings.ConsoleOnly.Active")) {
            for (String command : cmdsManager.getConsoleOnlyCmds()) {
                if (message.toLowerCase().startsWith("/" + command)) {
                    e.setCancelled(true);
                    p.sendMessage(c("Settings.ConsoleOnly.Message"));

                    if (!main.getConfig().getBoolean("Settings.ConsoleOnly.Effects.sounds.active")) {
                        return;
                    }
                    playSoundEffect(p, consoleOnlySoundName);
                    return;
                }
            }

        }
        /*
         * Blocked Commands
         */
        if (main.getConfig().getBoolean("Settings.BlockedCommands.Active")) {
            for (String command : cmdsManager.getBlockedCmds()) {
                if (p.hasPermission("consoleonly.bypass")) {
                    break;
                }
                if (message.toLowerCase().startsWith("/" + command)) {
                    e.setCancelled(true);
                    p.sendMessage(c("Settings.BlockedCommands.Message"));

                    if (!main.getConfig().getBoolean("Settings.BlockedCommands.Effects.sounds.active")) {
                        return;
                    }
                    playSoundEffect(p, blockedCmdsSoundName);
                    return;
                }
            }
        }

        /*
         * World Edit Fix
         */
        if (main.getConfig().getBoolean("Settings.WorldEditCrashFix.Active")) {
            for (String command : main.getConfig().getStringList("Settings.WorldEditCrashFix.Commands")) {
                if (p.hasPermission("consoleonly.wefix.bypass")) {
                    break;
                }
                if (message.toLowerCase().startsWith("/" + command)) {
                    e.setCancelled(true);

                    p.sendMessage(c("Settings.WorldEditCrashFix.Message"));

                    if (!main.getConfig().getBoolean("Settings.WorldEditCrashFix.Broadcast.active")) {
                        return;
                    }
                    main.getServer().broadcastMessage(c("Settings.WorldEditCrashFix.Broadcast.message")
                            .replace("{player}", p.getName()));
                    return;
                }
            }
        }


        /*
         * BLOCK COMMANDS WITH :
         */
        if (e.getMessage().contains(":")) {
            if (main.getConfig().getBoolean("Settings.BlocksCmdsWColons.active")) {
                // To allow ':' in WorldEdit commands (//)
                if (message.startsWith("//") && main.getConfig().getBoolean("Settings.BlocksCmdsWColons.ignoreWorldEditCmds")) {
                    return;
                }

                e.setCancelled(true);
                p.sendMessage(c("Settings.BlocksCmdsWColons.message"));
                if (!main.getConfig().getBoolean("Settings.BlocksCmdsWColons.Effects.sounds.active")) {
                    return;
                }
                playSoundEffect(p, colonSoundName);
                return;
            }
        }


    }

    private void playSoundEffect(Player p, String soundName) {
        try {
            p.getWorld().playSound(p.getLocation(), Sound.valueOf(soundName), 0.6F, 0.6F);
        } catch (Exception ex) {
            main.getServer().getConsoleSender().sendMessage("[ConsoleOnly] " + ChatColor.RED + "ConsoleOnly | entered sound does not exist.");
        }
    }

    // Available Update notification
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (!main.checkForUpdates())
            return;

        if (!p.isOp() && !p.hasPermission("*") && !p.hasPermission("consoleonly.notify")) {
            return;
        }

        if (updateChecker.availableUpdate()) {
            p.sendMessage(ChatColor.GREEN + main.Separatori(31));
            p.sendMessage("�6�l[�cConsoleOnly�6] New update available:");
            p.sendMessage("�6Current version: �e" + main.getVer());
            p.sendMessage("�6New version: �e" + updateChecker.getLatestVersion());
            p.sendMessage("�6What's new: �e" + updateChecker.getUpdateTitle());
            p.sendMessage(ChatColor.GREEN + main.Separatori(31));
        }
    }

    private String c(String configPath) {
        return main.getPrefix() + ChatColor.translateAlternateColorCodes('&', main.getConfig().getString(configPath));
    }


}
