# TikTok-Live-Connector
Simple Node.js Bot RestfulAPI to get TikTok Live data and events (gifts, messages, likes, chat etc.) Bot can be managed via minecraft spigot plugin.

![Zrzut ekranu 2023-07-11 o 23 36 23](https://github.com/jordanmruczynski/TikTok-Live-Connector/assets/50798031/5b4a194b-a452-4af7-a1bb-8d0445755182)

**NOTE:** TikTok Live Connector / McTiktok is not an any official API.

## RestAPI (Params and description)

| Type | Param Required | Endpoint | Description |
| ---------- | -------- | -------- | ----------- |
| Chat messages | Yes | /messages/{name} | Get live chat messages. Example: `http://localhost:1234/messages/jordanmruczynski` |
| Gifts/donates | Yes | /gift/{name} | Get gifts. Example: `http://localhost:1234/gift/jordanmruczynski`  |
| Handle status | Yes, (stop and start) | /{status}/{name} | Connect and disconnect bot with stream. Examples: `http://localhost:1234/start/jordanmruczynski` and `http://localhost:1234/stop/jordanmruczynski` |
| All (likes, follows etc.) | Yes | /get/{name} | Get all data, including likes. You are able to easily filter data for likes only for example. Example: `http://localhost:1234/get/jordanmruczynski` |

## Minecraft Plugin (Java, Spigot 1.19.2)

![Zrzut ekranu 2023-07-11 o 22 22 38](https://github.com/jordanmruczynski/TikTok-Live-Connector/assets/50798031/afb70840-00b2-4c6d-8090-025307ca8584)

## Getting started

1. Download plugin from latest release: [McTiktok 2.1](https://github.com/jordanmruczynski/TikTok-Live-Connector/releases/tag/2.1)
2. Download plugin dependency for placeholders [GitHub PlaceholderAPI](https://github.com/PlaceholderAPI/PlaceholderAPI) or [SpigotMC PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)
3. Put both in /plugins folder on your server (/home/yourServer/plugins)
4. Restart your server (ps. always restart server, please never use /reload, because it has many negative aspects, for example, bad memory cleaning in maps)
5. If everything is correct you should get plugin folder with 3 files in JSON Format: settings, gifts, chestItems.
6. Configure each one as you need.
7. Use /mctiktok command on server, and play :)! Have fun!

## Default actions 

| ID | Description |
| -- | ----------- |
| 1 | Spawn zombie |
| 2 | Set cowweb under player |
| 3 | Give a 1x bread (food) for player |
| 4 | Spawn black skeletons |
| 5 | Spawn creeper |
| 6 | Give 1x enderpearl for player |
| 7 | Give 1x enchanted golden apple for player |
| 8 | Clear/reset player inventory |
| 9 | Give 1x totem of undying for player |
| 10 | Throw primed/fired tnt on player head |
| 11 | Freeze player on 7 seconds (can't jump/move) |
| 12 | Kill player |
| 13 | Gamemode creative for 10 seconds |
| 14 | Add one additional heart |
| 15 | Remove one heart |
| 16 | Spawn friendly iron golem |

## Configuration

## `gifts.json`

<details><summary> Example configuration </summary><p>

```json
{
  "_comment": "Set actions here from 1 to 16 or your own command executed by console. Parameter {player} is replaced by player name.",
  "5655": "say hi {player}",
  "5211": 1,
  "4412": 7
} 
``` 
**5655** = Rose ID Gift in my Country [TikTok Gifts IDs](https://streamdps.com/tiktok-widgets/gifts/?br=1)
so if someone send a rose, console will execute say command. <br>
**5211** is a Coffe (not sure) so if someone will send a Coffe, action ID 1 will be executed (spawn zombie with gifter username) <br>
**4412**... etc. <br>
You can set commands and actions ids as you want :)


</p></details>

## `settings.json`

<details><summary> Example configuration </summary><p>

```json
{
  "_comment": "Bot host address and port, NOT your minecraft server. You need host JS bot yourself or buy access from the author. ",
  "serverAddress": "localhost",
  "serverPort": 8080
}
``` 
**serverAddress** and **serverPort** are a host address/port where nodejs app (bot, TikTok package) is hosted. <br>
If you can't host it by yourself you can purchase access to my host on Discord: **jordanmruczynski#7622** <br>


</p></details>

## `chestItems.json`

<details><summary> Example configuration </summary><p>

```json
{
  "items": [
    {
      "material": "DIAMOND_SWORD",
      "amount": 1,
      "damage": 0,
      "enchantments": {
        "DAMAGE_ALL": 2,
        "DURABILITY": 1
      },
      "displayName": "Sword of Destruction"
    },
    {
      "material": "IRON_HELMET",
      "amount": 1,
      "damage": 0,
      "enchantments": {
        "DAMAGE_ALL": 2,
        "DURABILITY": 1
      },
      "displayName": "Sword of something idk what yet"
    }
  ]
}
``` 
Items for magic chest, you need configure it by Spigot Type [Materials](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html) and [Enchants](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/enchantments/Enchantment.html)<br>
For example SHARPNESS is DAMAGE_ALL <br>

</p></details>

Please remember about JSON format, so the last value is without a comma as on the example.


















