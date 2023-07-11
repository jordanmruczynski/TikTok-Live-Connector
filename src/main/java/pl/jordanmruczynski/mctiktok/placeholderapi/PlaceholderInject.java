package pl.jordanmruczynski.mctiktok.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import pl.jordanmruczynski.mctiktok.UserRepository;
import pl.jordanmruczynski.mctiktok.model.StreamStatus;
import pl.jordanmruczynski.mctiktok.model.Streamer;

public class PlaceholderInject extends PlaceholderExpansion {

    private final UserRepository userRepository;

    public PlaceholderInject(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String getIdentifier() {
        return "mctiktok";
    }

    @Override
    public String getAuthor() {
        return "Jordii";
    }

    @Override
    public String getVersion() {
        return "2.1";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }

        final Streamer streamer = userRepository.getStreamer(player.getUniqueId());

        switch (identifier) {
            case "streamStatus":
                return streamer == null ? StreamStatus.OFF.name() : streamer.getStreamStatus().name();
            case "streamerName":
                return streamer == null ? "None" : streamer.getTiktokName();
            case "watchers":
                return streamer == null ? "0" : String.valueOf(streamer.getWatchersCount());
            case "lastGifter":
                return streamer == null ? "None" : streamer.getLastGifter();
            case "lastGift":
                return streamer == null ? "None" : streamer.getLastGift();
            case "streamers":
                return String.valueOf(userRepository.getStreamersAmount());
            default:
                break;
        }

        return null;
    }
}

