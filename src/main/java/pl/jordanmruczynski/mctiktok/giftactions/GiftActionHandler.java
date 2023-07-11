package pl.jordanmruczynski.mctiktok.giftactions;

import org.apache.commons.codec.binary.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import pl.jordanmruczynski.mctiktok.rest.objects.Gift;

public class GiftActionHandler {
    private final GiftAction giftAction = new GiftAction();

    public void handleGift(String giftNumber, Player player, Gift gift) {
        player.sendTitle("", gift.uniqueId + " §6sent you §e" + gift.data.giftName);
        player.sendMessage(gift.uniqueId + " §6sent you §e" + gift.data.giftName);
        System.out.println("akcja1: " + giftNumber);
        if (isNumeric(giftNumber) || giftNumber.equals("-1")) {
            System.out.println("akcja 2");
            int giftNumberInt = Integer.parseInt(giftNumber);
            System.out.println("liczba: " + giftNumberInt);
            switch (giftNumberInt) {
                case 1:
                    System.out.println("akcja3");
                    giftAction.spawnZombie(player, gift.uniqueId);
                    break;
                case 2:
                    giftAction.setCowweb(player);
                    break;
                case 3:
                    giftAction.giveChlebeek(player);
                    break;
                case 4:
                    giftAction.spawnBlackSkeletons(player, gift.uniqueId);
                    break;
                case 5:
                    giftAction.spawnCreeper(player, gift.uniqueId);
                    break;
                case 6:
                    giftAction.giveEnderpearl(player);
                    break;
                case 7:
                    giftAction.giveKox(player);
                    break;
                case 8:
                    giftAction.clearEq(player);
                    break;
                case 9:
                    giftAction.giveTotem(player);
                    break;
                case 10:
                    giftAction.throwTnt(player);
                    break;
                case 11:
                    giftAction.setFreeze(player);
                    break;
                case 12:
                    giftAction.kill(player);
                    break;
                case 13:
                    giftAction.gamemode(player);
                    break;
                case 14:
                    giftAction.addOneHeart(player);
                    break;
                case 15:
                    giftAction.removeOneHeart(player);
                    break;
                case 16:
                    giftAction.spawnFriendlyIronGolem(player, gift.uniqueId);
                    break;
                default:
                    break;
            }
        } else {
            System.out.println("komenda: " + giftNumber);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), giftNumber.replace("{player}", player.getName()));
        }
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
