package xiaokun.forge.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xiaokun.forge.Forge;
import xiaokun.forge.event.ForgeStudyEvent;
import xiaokun.forge.util.Message;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 小坤
 */
public class PlayerData {

    /**
     * 生成玩家的默认配置文件
     *
     * @param player
     */
    public static void createDefaultPlayerData(final Player player) {
        Message.sendMessage("创建新玩家 §e[§4" + player.getName() + "§e] §a配置");
        File FILE = new File("plugins" + File.separator + Forge.getPluginName() + File.separator + "PlayerData" + File.separator + player.getName() + ".yml");
        YamlConfiguration playerData = new YamlConfiguration();
        playerData.set("forge.level", 1);
        playerData.set("forge.exp", 0);
        playerData.set("map.num", 0);
        playerData.set("map.0", "");
        playerData.set("item.num", 0);
        playerData.set("item.0", "");
        try {
            playerData.save(FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取玩家配置文件的File对象
     *
     * @param player
     * @return
     */
    public static File getPlayerFile(final Player player) {
        File file = new File("plugins" + File.separator + Forge.getPluginName() + File.separator + "PlayerData" + File.separator + player.getName() + ".yml");
        if (!file.exists()) {
            createDefaultPlayerData(player);
        }
        return file;
    }

    /**
     * 获取玩家的锻造经验
     *
     * @param player
     * @return
     */
    public static int getExp(final Player player) {
        return getPlayerData(player).getInt("forge.exp");
    }

    /**
     * 获取玩家拥有的图纸数量
     *
     * @param player
     * @return
     */
    public static int getMapNum(final Player player) {
        return getPlayerData(player).getInt("map.num");
    }

    /**
     * 获取玩家历史锻造的物品数量
     *
     * @param player
     * @return
     */
    public static int getItemNum(final Player player) {
        return getPlayerData(player).getInt("item.num");
    }

    /**
     * 获取玩家历史锻造的全部物品
     *
     * @param player
     * @return
     */
    public static List<ItemStack> getItem(final Player player) {
        ConfigurationSection section = getPlayerData(player).getConfigurationSection("item");
        List<ItemStack> list = new ArrayList<>();
        for (String key : section.getKeys(false)) {
            if (!key.equalsIgnoreCase("num")) {
                list.add(section.getItemStack(key));
            }
        }
        return list;
    }

    /**
     * 获取玩家拥有的全部图纸
     *
     * @param player
     * @return
     */
    public static List<ItemStack> getMap(final Player player) {
        ConfigurationSection section = getPlayerData(player).getConfigurationSection("map");
        List<ItemStack> list = new ArrayList<>();
        for (String key : section.getKeys(false)) {
            if (!key.equalsIgnoreCase("num")) {
                list.add(section.getItemStack(key));
            }
        }
        return list;
    }

    /**
     * 获取玩家的锻造等级
     *
     * @param player
     * @return
     */
    public static int getLevel(final Player player) {
        return getPlayerData(player).getInt("forge.level");
    }

    /**
     * 增加经验
     *
     * @param player
     * @param num
     */
    public static void addExp(final Player player, int num) {
        YamlConfiguration yml = getPlayerData(player);
        int level = getLevel(player);
        int newLevel = level;
        int exp = getExp(player);
        final List<String> levelList = new ArrayList<>(LevelConfig.getYml().getConfigurationSection("Level").getKeys(false));
        for (String levels : levelList) {
            if (levels.equalsIgnoreCase(String.valueOf(level))) {
                int needExp = LevelConfig.getExp(String.valueOf(level));
                if (exp + num < needExp) {
                    exp += num;
                    break;
                } else {
                    exp += num;
                    exp -= needExp;
                    newLevel = level + 1;
                    break;
                }
            }
        }

        yml.set("forge.exp", exp);
        yml.set("forge.level", newLevel);
        try {
            yml.save(getPlayerFile(player));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getLevelName(final Player player) {
        return LevelConfig.getName(String.valueOf(PlayerData.getLevel(player)));
    }

    /**
     * 给玩家添加等级
     *
     * @param player
     * @param level
     */
    public static void addLevel(final Player player, final int level) {
        YamlConfiguration yml = getPlayerData(player);
        int newLevel = getLevel(player) + level;
        yml.set("forge.level", newLevel);
        try {
            yml.save(getPlayerFile(player));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 为玩家设置等级
     *
     * @param player
     * @param level
     */
    public static void setLevel(final Player player, final int level) {
        YamlConfiguration yml = getPlayerData(player);
        yml.set("forge.level", level);
        try {
            yml.save(getPlayerFile(player));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取玩家配置文件的YML对象
     *
     * @param player
     * @return
     */
    public static YamlConfiguration getPlayerData(final Player player) {
        final File f = getPlayerFile(player);
        return YamlConfiguration.loadConfiguration(f);
    }

    /**
     * 玩家获得图纸
     *
     * @param player
     * @param arg
     */
    public static boolean addMap(Player player, String arg, YamlConfiguration yml) {
        int mapNum = PlayerData.getMapNum(player);
        ItemStack items = ItemConfig.getMap(arg);
        final ForgeStudyEvent event = new ForgeStudyEvent(player, items);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            String msg = Message.getMessage("StudyMap").replaceAll("%item%", items.getItemMeta().getDisplayName());
            Message.playerMessage(player, msg);
            yml.set("map." + mapNum, items);
            yml.set("map.num", ++mapNum);
            try {
                yml.save(PlayerData.getPlayerFile(player));
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
