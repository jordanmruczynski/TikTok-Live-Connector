package pl.jordanmruczynski.mctiktok.giftactions;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;

public class GiftActionListeners implements Listener {

    protected static Map<UUID, Long> freezedPlayers = new HashMap<>();

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (freezedPlayers.containsKey(e.getPlayer().getUniqueId())) {
            if ((freezedPlayers.get(e.getPlayer().getUniqueId()) + 5) >= System.currentTimeMillis() / 1000) {
                e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§cYou can't move! §7(" + (freezedPlayers.get(e.getPlayer().getUniqueId()) + 5 - System.currentTimeMillis() / 1000) + ")"));
                e.setCancelled(true);
            } else {
                freezedPlayers.remove(e.getPlayer().getUniqueId());
            }
        }
    }
}
