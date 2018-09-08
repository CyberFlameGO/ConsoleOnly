package io.github.spaicygaming.consoleonly.restrictions;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import io.github.spaicygaming.consoleonly.ConsoleOnly;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class AntiTab {

    public AntiTab(ConsoleOnly main, CommandsManager commandsManager, ProtocolManager protocolManager) {
        protocolManager.addPacketListener(new PacketAdapter(main, ListenerPriority.NORMAL, PacketType.Play.Client.TAB_COMPLETE) {

            @EventHandler(priority = EventPriority.HIGHEST)
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType() != PacketType.Play.Client.TAB_COMPLETE || event.getPlayer().hasPermission("consoleonly.antitab.bypass")) {
                    return;
                }

                try {
                    PacketContainer pack = event.getPacket();
                    String message = pack.getSpecificModifier(String.class).read(0).toLowerCase();

                    if (message.equals("/") || message.contains(":") || commandsManager.getAntiTabCmds().stream().anyMatch(message::startsWith)) {
                        event.setCancelled(true);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

}
