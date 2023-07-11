package pl.jordanmruczynski.mctiktok.giftactions;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import pl.jordanmruczynski.mctiktok.McTiktok;
import pl.jordanmruczynski.mctiktok.util.ItemBuilder;

public class GiftAction implements GiftActionService {

    @Override
    public void spawnChicken(Player player, String name) {
        final Chicken chicken = (Chicken) player.getWorld().spawnEntity(player.getLocation(), EntityType.CHICKEN);
        chicken.setCustomName(name);
        chicken.setAI(false);
    }

    @Override
    public void spawnBlackSkeletons(Player player, String name) {
        final WitherSkeleton witherSkeleton = (WitherSkeleton) player.getWorld().spawnEntity(player.getLocation(), EntityType.WITHER_SKELETON);
        witherSkeleton.setCustomName(name);
    }

    @Override
    public void spawnCreeper(Player player, String name) {
        final Creeper creeper = (Creeper) player.getWorld().spawnEntity(player.getLocation(), EntityType.CREEPER);
        creeper.setCustomName(name);
    }

    @Override
    public void giveChlebeek(Player player) { //easter egg
        player.getInventory().addItem(new ItemBuilder(Material.BREAD).setDisplayName(ChatColor.GOLD + "Bread").build());
    }

    @Override
    public void giveEnderpearl(Player player) {
        player.getInventory().addItem(new ItemBuilder(Material.ENDER_PEARL).setDisplayName(ChatColor.AQUA + "Enderpearl").build());
    }

    @Override
    public void giveKox(Player player) {
        player.getInventory().addItem(new ItemBuilder(Material.GOLDEN_APPLE, 1).setDisplayName(ChatColor.YELLOW + "Kox").build());
    }

    @Override
    public void clearEq(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
    }

    @Override
    public void giveTotem(Player player) {
        player.getInventory().addItem(new ItemBuilder(Material.TOTEM_OF_UNDYING).setDisplayName(ChatColor.RED + "Totem").build());
    }

    @Override
    public void throwTnt(Player player) {
        final TNTPrimed tnt = (TNTPrimed) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.PRIMED_TNT);
    }

    @Override
    public void setCowweb(Player player) {
        player.getLocation().getBlock().setType(Material.COBWEB);
    }

    @Override
    public void setFreeze(Player player) {
        GiftActionListeners.freezedPlayers.put(player.getUniqueId(), System.currentTimeMillis() / 1000);
    }

    @Override
    public void kill(Player player) {
        player.setHealth(0.0D);
    }

    @Override
    public void gamemode(Player player) {
        player.setGameMode(GameMode.CREATIVE);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player != null) {
                    player.setGameMode(GameMode.SURVIVAL);
                }
            }
        }.runTaskLater(McTiktok.getMcTiktok(), 20L * 10L);
    }

    @Override
    public void addOneHeart(Player player) {
        final double hpNow = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        if (hpNow+2 >= 40) {
            return;
        }
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()+2);
    }

    @Override
    public void removeOneHeart(Player player) {
        final double hpNow = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        if (hpNow-2 <= 0) {
            return;
        }
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()-2);
    }

    @Override
    public void spawnZombie(Player player, String name) {
        final Zombie zombie = (Zombie) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
        zombie.setCustomName(name);
    }

    @Override
    public void spawnFriendlyIronGolem(Player player, String name) {
        final IronGolem ironGolem = (IronGolem) player.getWorld().spawnEntity(player.getLocation(), EntityType.IRON_GOLEM);
        ironGolem.setPlayerCreated(true);
        ironGolem.setCustomName(name);
    }


}
