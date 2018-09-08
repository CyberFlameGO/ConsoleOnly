package io.github.spaicygaming.consoleonly.updater;

import io.github.spaicygaming.consoleonly.Permission;
import io.github.spaicygaming.consoleonly.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private UpdateChecker updateChecker;

    PlayerJoinListener(UpdateChecker updateChecker) {
        this.updateChecker = updateChecker;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        // If this listener is registered means that there is an update available

        if (Permission.NOTIFY_UPDATES.has(player)) {
            player.sendMessage(ChatColor.GREEN + ChatUtil.createSeparators('=', 31));
            player.sendMessage("§6§l[§cConsoleOnly§6] New update available:");
            player.sendMessage("§6Current version: §e" + updateChecker.getCurrentVersion());
            player.sendMessage("§6New version: §e" + updateChecker.getLatestVersion());
            player.sendMessage("§6What's new: §e" + updateChecker.getLatestUpdateTitle());
            player.sendMessage(ChatColor.GREEN + ChatUtil.createSeparators('=', 31));
        }
    }

}
