package pl.jordanmruczynski.mctiktok.model;

import java.util.UUID;

public class Streamer {

    private UUID playerUuid;
    private String playerName;
    private String tiktokName;
    private StreamStatus streamStatus;
    private int watchersCount;
    private String lastGifter;
    private String lastGift;

    public Streamer(UUID playerUuid, String playerName, String tiktokName, StreamStatus streamStatus, int watchersCount, String lastGifter, String lastGift) {
        this.playerUuid = playerUuid;
        this.playerName = playerName;
        this.tiktokName = tiktokName;
        this.streamStatus = streamStatus;
        this.watchersCount = watchersCount;
        this.lastGifter = lastGifter;
        this.lastGift = lastGift;
    }

    public String getLastGift() {
        return lastGift;
    }

    public void setLastGift(String lastGift) {
        this.lastGift = lastGift;
    }

    public String getLastGifter() {
        return lastGifter;
    }

    public void setLastGifter(String lastGifter) {
        this.lastGifter = lastGifter;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public void setPlayerUuid(UUID playerUuid) {
        this.playerUuid = playerUuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getTiktokName() {
        return tiktokName;
    }

    public void setTiktokName(String tiktokName) {
        this.tiktokName = tiktokName;
    }

    public StreamStatus getStreamStatus() {
        return streamStatus;
    }

    public void setStreamStatus(StreamStatus streamStatus) {
        this.streamStatus = streamStatus;
    }

    public int getWatchersCount() {
        return watchersCount;
    }

    public void setWatchersCount(int watchersCount) {
        this.watchersCount = watchersCount;
    }

    @Override
    public String toString() {
        return "Streamer{" +
                "playerUuid=" + playerUuid +
                ", playerName='" + playerName + '\'' +
                ", tiktokName='" + tiktokName + '\'' +
                ", streamStatus=" + streamStatus +
                ", watchersCount=" + watchersCount +
                '}';
    }
}
