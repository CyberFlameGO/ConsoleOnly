package me.spaicygaming.consoleonly;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

public class AntiTab{

	private ConsoleOnly main = ConsoleOnly.getInstance();

	public AntiTab(ProtocolManager protocolManager) {
		protocolManager.addPacketListener(new PacketAdapter(main, ListenerPriority.NORMAL, new PacketType[] { PacketType.Play.Client.TAB_COMPLETE }){
			@EventHandler(priority = EventPriority.HIGHEST)
			public void onPacketReceiving(PacketEvent e){
				
				if (e.getPacketType() != PacketType.Play.Client.TAB_COMPLETE){
					return;
				}
				
				if (e.getPlayer().hasPermission("consoleonly.antitab.bypass")){
					return;
				}
				
				try{
					PacketContainer pack = e.getPacket();
					String message = pack.getSpecificModifier(String.class).read(0).toLowerCase();
					
					if (message.equals("/")){
						e.setCancelled(true);
					}
					
					if (message.contains(":")){
						e.setCancelled(true);
					}
					
					for (String blockedCmdsTabs : main.getConfig().getStringList("Settings.AntiTab.Commands")) {
						if (message.startsWith("/" + blockedCmdsTabs)) {
							e.setCancelled(true);
						}
					}
					
				}catch(Exception ex){
					ex.printStackTrace();
				}
				
				
			}
	    });
	}
    
	
}
