package pl.jordanmruczynski.mctiktok.giftactions;

import org.bukkit.entity.Player;

public interface GiftActionService {

    void spawnZombie(Player player, String name);

    void spawnFriendlyIronGolem(Player player, String name);

    void spawnChicken(Player player, String name);

    void spawnBlackSkeletons(Player player, String name);

    void spawnCreeper(Player player, String name);

    void giveChlebeek(Player player);

    void giveEnderpearl(Player player);

    void giveKox(Player player);

    void clearEq(Player player);

    void giveTotem(Player player);

    void throwTnt(Player player);

    void setCowweb(Player player);

    void setFreeze(Player player);

    void kill(Player player);

    void gamemode(Player player);

    void addOneHeart(Player player);

    void removeOneHeart(Player player);

}
