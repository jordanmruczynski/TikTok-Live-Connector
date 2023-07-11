package pl.jordanmruczynski.mctiktok;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitScheduler;
import pl.jordanmruczynski.mctiktok.configuration.GiftCodesConfig;
import pl.jordanmruczynski.mctiktok.giftactions.GiftActionHandler;
import pl.jordanmruczynski.mctiktok.model.StreamStatus;
import pl.jordanmruczynski.mctiktok.model.Streamer;
import pl.jordanmruczynski.mctiktok.rest.requests.ChatMessageRequest;
import pl.jordanmruczynski.mctiktok.rest.requests.GiftRequest;
import pl.jordanmruczynski.mctiktok.rest.requests.HandleStatusRequest;
import pl.jordanmruczynski.mctiktok.rest.requests.StatusType;
import pl.jordanmruczynski.mctiktok.util.ServerMainThread;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * author jordanmruczynski
 * date 09.2022
 */

public class UserRepository implements Listener {

    private final ScheduledExecutorService scheduledExecutorService;
    private final BukkitScheduler bukkitScheduler;
    private final GiftCodesConfig giftCodesConfig;
    private GiftActionHandler giftActionHandler = new GiftActionHandler();

    private GiftRequest giftRequest = new GiftRequest();
    private ChatMessageRequest chatMessageRequest = new ChatMessageRequest();
    private HandleStatusRequest handleStatusRequest = new HandleStatusRequest();

    private Map<UUID, Streamer> streamers = new HashMap<>();
    private long lastMessage = System.currentTimeMillis();
    private BossBar bossBar = Bukkit.createBossBar("§eMcTiktok 2.1 §7| §fDownload plugin on GitHub: jordanmruczynski", BarColor.YELLOW, BarStyle.SOLID);

    public UserRepository(ScheduledExecutorService scheduledExecutorService, BukkitScheduler bukkitScheduler, GiftCodesConfig giftCodesConfig) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.bukkitScheduler = bukkitScheduler;
        this.giftCodesConfig = giftCodesConfig;
    }

    @EventHandler
    public void handleJoin(PlayerJoinEvent event) {

    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        if (streamers.containsKey(player.getUniqueId())) {
            final Streamer streamer = streamers.get(player.getUniqueId());
            if (streamer.getStreamStatus() == StreamStatus.ON) {
                handleStatusRequest.sendRequest(streamer.getTiktokName(), StatusType.STOP, callback -> {
                    System.out.println("STREAMER QUIT WITH ENABLED LIVE, TRYING TURN OFF LIVE.. CALLBACK: " + callback);
                });
            }
            streamers.remove(player.getUniqueId());
        }
    }

    public void start() {
        this.scheduledExecutorService.scheduleWithFixedDelay(() -> {
            if (streamers.isEmpty()) return;

            for (Streamer streamer : streamers.values()) {
                Player player = Bukkit.getPlayer(streamer.getPlayerUuid());

                if (streamer.getStreamStatus() == StreamStatus.ON) {
                    giftRequest.sendRequest(streamer.getTiktokName(), callback -> {
                        callback.values().forEach(g -> {
                            ServerMainThread.RunParallel.run(() -> {
                                System.out.println("GIFT " + g.data.giftId + " " + player);
                                giftActionHandler.handleGift(giftCodesConfig.getNumberFromCode(""+g.data.giftId), player, g);
                                streamer.setLastGifter(g.uniqueId);
                            });
                        });

                    });

                    chatMessageRequest.sendRequest(streamer.getTiktokName(), callback -> {
                        callback.values().forEach(g -> {
                            player.sendMessage(ChatColor.GRAY + g.uniqueId + ChatColor.DARK_GRAY + ": " + ChatColor.WHITE + g.data.comment);
                        });
                    });

                    if (System.currentTimeMillis() - lastMessage >= 30_000) {
                        player.sendMessage("");
                        player.sendMessage("§eMcTiktok §f2.1 §7by §8Jordan \"Jordii\" Mruczynski §9§o(jordanmruczynski#7622)");
                        player.sendMessage("§7Download: https://github.com/jordanmruczynski/TikTok-Live-Connector");
                        bossBar.setProgress(1.0);
                        bossBar.addPlayer(player);

                        Bukkit.getScheduler().runTaskLater(McTiktok.getMcTiktok(), () -> {
                            bossBar.removePlayer(player);
                        }, 20*5);
                        lastMessage = System.currentTimeMillis();
                    }

                }

            }
        }, 0, 3, TimeUnit.SECONDS);
    }

    public GiftRequest getGiftRequest() {
        return giftRequest;
    }

    public ChatMessageRequest getChatMessageRequest() {
        return chatMessageRequest;
    }

    public HandleStatusRequest getHandleStatusRequest() {
        return handleStatusRequest;
    }

    public void addStreamer(Streamer streamer) {
        streamers.put(streamer.getPlayerUuid(), streamer);
    }

    public void removeStreamer(UUID uuid) {
        streamers.remove(uuid);
    }

    public void replaceStreamer(UUID uuid, Streamer streamer) {
        streamers.replace(uuid, streamer);
    }

    public Streamer getStreamer(UUID uuid) {
        if (streamers.containsKey(uuid)) return streamers.get(uuid);
        else return null;
    }

    public int getStreamersAmount() {
        return streamers.size();
    }
}
