package io.github.spaicygaming.consoleonly.restrictions;

import io.github.spaicygaming.consoleonly.ConsoleOnly;
import io.github.spaicygaming.consoleonly.util.ChatUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Optional;

public class ConsoleOnlyListeners implements Listener {

    private ConsoleOnly main;
    private CommandsManager cmdsManager;

    // Sounds name
    private Optional<Sound> consoleOnlySound, blockedCmdsSound, colonSound;

    public ConsoleOnlyListeners(ConsoleOnly main) {
        this.main = main;
        this.cmdsManager = main.getCommandsManager();

        // Loaad sounds
        this.consoleOnlySound = loadSound("Settings.ConsoleOnly.effects.sound");
        this.blockedCmdsSound = loadSound("Settings.BlockedCommands.effects.sound");
        this.colonSound = loadSound("Settings.BlocksCmdsWColons.effects.sound");
    }

    private Optional<Sound> loadSound(String configPath) {
        String soundName = main.getConfig().getString(configPath);
        try {
            if (!soundName.isEmpty())
                return Optional.of(Sound.valueOf(soundName));
        } catch (IllegalArgumentException ex) {
            ChatUtil.alertConsole("The sound \"" + soundName + "\" specified in \"" + configPath + "\" doesn't exist!");
        }
        return Optional.empty();
    }

    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().replace("/", "").toLowerCase().split(" ")[0];
        Player player = event.getPlayer();

        // Console Only
        if (main.getConfig().getBoolean("Settings.ConsoleOnly.active")) {
            if (cmdsManager.getConsoleOnlyCmds().contains(command)) {
                event.setCancelled(true);
                player.sendMessage(ChatUtil.c("Settings.ConsoleOnly.Message"));

                // Play sound effect if enabled
                playSoundEffect(player, consoleOnlySound);
                return;
            }
        }
        // Blocked Commands
        if (main.getConfig().getBoolean("Settings.BlockedCommands.active") && !player.hasPermission("consoleonly.bypass")) {
            if (cmdsManager.getBlockedCmds().contains(command)) {
                event.setCancelled(true);
                player.sendMessage(ChatUtil.c("Settings.BlockedCommands.Message"));

                // Play sound effect if enabled
                playSoundEffect(player, blockedCmdsSound);
                return;
            }
        }
        // WorldEdit commands crash fix
        if (main.getConfig().getBoolean("Settings.WorldEditCrashFix.active") && !player.hasPermission("consoleonly.wefix.bypass")) {
            if (cmdsManager.getWorldEditCmds().contains(command)) {
                event.setCancelled(true);

                player.sendMessage(ChatUtil.c("Settings.WorldEditCrashFix.Message"));

                if (!main.getConfig().getBoolean("Settings.WorldEditCrashFix.Broadcast.active")) {
                    return;
                }

                main.getServer().broadcastMessage(ChatUtil.c("Settings.WorldEditCrashFix.Broadcast.message")
                        .replace("{player}", player.getName()));
            }
        }
        // Block command with colons
        if (command.contains(":") && main.getConfig().getBoolean("Settings.BlocksCmdsWColons.active")) {
            event.setCancelled(true);
            player.sendMessage(ChatUtil.c("Settings.BlocksCmdsWColons.message"));

            // Play sound effect if enabled
            playSoundEffect(player, colonSound);
        }
    }

    /**
     * Play the sound if present
     *
     * @param player The player whose location get
     * @param sound  The sound
     */
    private void playSoundEffect(Player player, Optional<Sound> sound) {
        if (sound.isPresent())
            player.getWorld().playSound(player.getLocation(), sound.get(), 0.6F, 0.6F);
    }

}
