package pl.jordanmruczynski.mctiktok.commands;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.jordanmruczynski.mctiktok.UserRepository;
import pl.jordanmruczynski.mctiktok.model.StreamStatus;
import pl.jordanmruczynski.mctiktok.model.Streamer;
import pl.jordanmruczynski.mctiktok.rest.requests.StatusType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TiktokCommand implements CommandExecutor {

    private final UserRepository userRepository;
    private final Map<UUID, Long> commandCooldown = new HashMap<>();

    public TiktokCommand(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        final Player player = (Player) sender;

        if (!player.hasPermission("mctiktok.command.tiktok")) {
            player.sendMessage("§cYou do not have required permission: §fmctiktok.command.tiktok");
            return true;
        }

        if (args.length == 0 || args.length > 2) {
            Arrays.asList(
                    "",
                    "§eMcTiktok §f2.1 §7by §8Jordan \"Jordii\" Mruczynski §9§o(jordanmruczynski#7622)",
                    "§7Download: https://github.com/jordanmruczynski/TikTok-Live-Connector",
                    "§6/mctiktok register <tiktokName> §8- §7register player as streamer",
                    "§6/mctiktok unregister §8- §7unregister yourself",
                    "§6/mctiktok start §8- §astart §7your stream",
                    "§6/mctiktok stop §8- §cstop §7your stream",
                    "§6/mctiktok info §8- §7info about you",
                    "§6/mctiktok chest <edit/give> (nick) §8- §7magic chest",
                    "§6/mctiktok list §8- §7streamers list",
                    ""
            ).forEach(x -> player.sendMessage(x));
            return true;
        }

        if (args[0].equalsIgnoreCase("register") && args.length != 2) {
            player.sendMessage("");
            player.sendMessage("§cMcTiktok error:");
            player.sendMessage("§7Incorrect command usage, please use §f/mctiktok register <yourTiktokName>");
            return true;
        }



        if (args[0].equalsIgnoreCase("start") || args[0].equalsIgnoreCase("stop")) {
            if (commandCooldown.containsKey(player.getUniqueId())) {
                if ((commandCooldown.get(player.getUniqueId()) + 30) >= System.currentTimeMillis() / 1000) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§cPlease wait before use this command §7(" + (commandCooldown.get(player.getUniqueId()) + 30 - System.currentTimeMillis() / 1000) + ")s"));
                    return true;
                } else {
                    commandCooldown.remove(player.getUniqueId());
                }
            } else {
                commandCooldown.put(player.getUniqueId(), System.currentTimeMillis() / 1000);
            }
        }

        final Streamer streamer = userRepository.getStreamer(player.getUniqueId());

        switch (args[0]) {
            case "register":
                if (streamer == null) {
                    userRepository.addStreamer(new Streamer(player.getUniqueId(), player.getName(), args[1], StreamStatus.OFF, 0, "None", "None"));
                    player.sendMessage("");
                    player.sendMessage("§aMcTiktok success:");
                    player.sendMessage("§7You have been registered as streamer!");
                } else {
                    player.sendMessage("");
                    player.sendMessage("§cMcTiktok error:");
                    player.sendMessage("§7You are already on streamer list, please unregister first yourself.");
                    player.sendMessage(streamer.toString());
                }
                break;
            case "unregister":
                if (streamer == null) {
                    player.sendMessage("");
                    player.sendMessage("§cMcTiktok error:");
                    player.sendMessage("§7You are not the streamer, please register first yourself.");
                } else {
                    userRepository.removeStreamer(player.getUniqueId());
                    if (streamer.getStreamStatus() == StreamStatus.ON) {
                        userRepository.getHandleStatusRequest().sendRequest(streamer.getTiktokName(), StatusType.STOP, callback -> {
                            System.out.println("UNREGISTER STREAMER WITH ENABLED LIVE, HANDLE STOP REQUEST: " + callback);
                        });
                    }
                    player.sendMessage("");
                    player.sendMessage("§aMcTiktok success:");
                    player.sendMessage("§7You have been unregistered.");
                }
                break;
            case "start":
                if (streamer == null) {
                    player.sendMessage("");
                    player.sendMessage("§cMcTiktok error:");
                    player.sendMessage("§7You are not the streamer, please register first yourself.");
                } else {
                    if (streamer.getStreamStatus() == StreamStatus.ON) {
                        player.sendMessage("");
                        player.sendMessage("§cMcTiktok error:");
                        player.sendMessage("§7You have already stream, please stop it first.");
                    } else {
                        streamer.setStreamStatus(StreamStatus.ON);
                        userRepository.replaceStreamer(player.getUniqueId(), streamer);
                        player.sendMessage("");
                        player.sendMessage("§3McTiktok information:");
                        player.sendMessage("§7Trying to connect with your stream session...");
                        userRepository.getHandleStatusRequest().sendRequest(streamer.getTiktokName(), StatusType.START, callback -> {
                            player.sendMessage(callback.toString());
                            if (callback.equalsIgnoreCase("ok")) {
                                player.sendMessage("");
                                player.sendMessage("§aMcTiktok success:");
                                player.sendMessage("§7You have started your stream!");
                            } else {
                                player.sendMessage(callback.toString());
                            }
                        });
                    }
                }
                break;
            case "stop":
                if (streamer == null) {
                    player.sendMessage("");
                    player.sendMessage("§cMcTiktok error:");
                    player.sendMessage("§7You are not the streamer, please register first yourself.");
                } else {
                    if (streamer.getStreamStatus() == StreamStatus.OFF) {
                        player.sendMessage("");
                        player.sendMessage("§cMcTiktok error:");
                        player.sendMessage("§7You do not have enabled stream, please start it first.");
                    } else {
                        streamer.setStreamStatus(StreamStatus.OFF);
                        userRepository.replaceStreamer(player.getUniqueId(), streamer);
                        userRepository.getHandleStatusRequest().sendRequest(streamer.getTiktokName(), StatusType.STOP, callback -> {
                            if (callback.equalsIgnoreCase("ok")) {
                                player.sendMessage("");
                                player.sendMessage("§aMcTiktok success:");
                                player.sendMessage("§7You have stopped your stream!");
                            } else {
                                player.sendMessage(callback.toString());
                            }
                        });
                    }
                }
                break;
            case "list":
                player.sendMessage("§cMcTiktok error:");
                player.sendMessage("§7Not implemented yet.");
                break;
            case "chest":
                player.sendMessage("§cMcTiktok error:");
                player.sendMessage("§7Not implemented yet.");
                break;
            case "info":
                if (streamer != null) {
                    player.sendMessage(ChatColor.BLUE + streamer.toString());
                }
                break;
            default:
                Arrays.asList(
                        "",
                        "§eMcTiktok §f2.1 §7by §8Jordan \"Jordii\" Mruczynski §9§o(jordanmruczynski#7622)",
                        "§7Download: https://github.com/jordanmruczynski/TikTok-Live-Connector",
                        "§6/mctiktok register <tiktokName> §8- §7register player as streamer",
                        "§6/mctiktok unregister §8- §7unregister yourself",
                        "§6/mctiktok start §8- §astart §7your stream",
                        "§6/mctiktok stop §8- §cstop §7your stream",
                        "§6/mctiktok info §8- §7info about you",
                        "§6/mctiktok chest <edit/give> (nick) §8- §7magic chest",
                        "§6/mctiktok list §8- §7streamers list",
                        ""
                ).forEach(x -> player.sendMessage(x));
                break;
        }

        return false;
    }
}
