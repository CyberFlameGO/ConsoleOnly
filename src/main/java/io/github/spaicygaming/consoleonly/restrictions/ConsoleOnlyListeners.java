package io.github.spaicygaming.consoleonly.restrictions;

import io.github.spaicygaming.consoleonly.ConsoleOnly;
import io.github.spaicygaming.consoleonly.Permission;
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
    // WorldEdit crash fix broadcast message
    private String weCrashFixBroadcast;

    public ConsoleOnlyListeners(ConsoleOnly main) {
        this.main = main;
        this.cmdsManager = main.getCommandsManager();

        loadConfigValues();
    }

    /**
     * Load needed values from the configuration file
     */
    public void loadConfigValues() {
        // Load sounds
        this.consoleOnlySound = parseSound("Settings.ConsoleOnly.effects.sound");
        this.blockedCmdsSound = parseSound("Settings.BlockedCommands.effects.sound");
        this.colonSound = parseSound("Settings.BlocksCmdsWColons.effects.sound");
        // Load message
        this.weCrashFixBroadcast = ChatUtil.c("Settings.WorldEditCrashFix.Broadcast.message");
    }

    /**
     * Get the sound name and try to parse it as a {@link Sound}.
     * Print alert messages if a IllegalArgumentException is thrown
     *
     * @param configPath The path to the sounds
     * @return en empty optional if the sound name is invalid
     */
    private Optional<Sound> parseSound(String configPath) {
        String soundName = main.getConfig().getString(configPath);
        try {
            if (!soundName.isEmpty())
                return Optional.of(Sound.valueOf(soundName));
        } catch (IllegalArgumentException ex) {
            ChatUtil.alertConsole("The sound \"" + soundName + "\" specified in \"" + configPath + "\" doesn't exist!");
        }
        return Optional.empty();
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().replace("/", "").toLowerCase().split(" ")[0];
        Player player = event.getPlayer();

        // Console Only
        if (main.getConfig().getBoolean("Settings.ConsoleOnly.active")) {
            if (cmdsManager.getConsoleOnlyCmds().contains(command)) {
                event.setCancelled(true);
                player.sendMessage(ChatUtil.c("Settings.ConsoleOnly.message"));

                // Play sound effect if enabled
                playSoundEffect(player, consoleOnlySound);
                return;
            }
        }
        // Blocked Commands
        if (main.getConfig().getBoolean("Settings.BlockedCommands.active") && !Permission.BYPASS_CMDS.has(player)) {
            if (cmdsManager.getBlockedCmds().contains(command)) {
                event.setCancelled(true);
                player.sendMessage(ChatUtil.c("Settings.BlockedCommands.message"));

                // Play sound effect if enabled
                playSoundEffect(player, blockedCmdsSound);
                return;
            }
        }
        // WorldEdit commands crash fix
        if (main.getConfig().getBoolean("Settings.WorldEditCrashFix.active") && !Permission.BYPASS_WEFIX.has(player)) {
            if (cmdsManager.getWorldEditCmds().contains(command)) {
                event.setCancelled(true);
                player.sendMessage(ChatUtil.c("Settings.WorldEditCrashFix.message"));

                // Broadcast message if not empty
                if (!weCrashFixBroadcast.isEmpty()) {
                    main.getServer().broadcastMessage(weCrashFixBroadcast.replace("{player}", player.getName()));
                }
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
     * @param player        The player whose location get
     * @param optionalSound The sound
     */
    private void playSoundEffect(Player player, Optional<Sound> optionalSound) {
        optionalSound.ifPresent(sound -> player.getWorld().playSound(player.getLocation(), sound, 0.6F, 0.6F));
    }

}
