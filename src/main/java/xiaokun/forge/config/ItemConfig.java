package xiaokun.forge.config;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xiaokun.forge.Forge;
import xiaokun.forge.util.ItemUtil;
import xiaokun.forge.util.Message;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author 小坤
 */
public class ItemConfig {
    @Getter
    private static Map<String, ItemStack> map = new HashMap<>();

    @Getter
    private static List<String> list;

    /**
     * 加载配置文件  如果没有则生成
     */
    public static void loadConfig() {
        File file = new File("plugins" + File.separator + Forge.getPluginName() + File.separator + "Item");
        if (!file.exists()) {
            file.mkdirs();
            createItem("Default");
            createCommands("DefaultCommand");
            createMythicItem("DefaultMythic");
        }
        String[] fs = file.list();
        list = new ArrayList<>();
        int num = 0;
        if (fs.length != 0) {
            for (String f : fs) {
                if (f.endsWith("yml")) {
                    num++;
                    String name = f.replace(".yml", "");
                    list.add(name);
                    map.put(name, getMap(name));
                }
            }
        }

        if (num == 0) {
            createItem("Default");
            createCommands("DefaultCommand");
            createMythicItem("DefaultMythic");
            loadConfig();
        }
        Message.sendMessage("找到了" + num + "个配置文件！");
    }

    /**
     * 读取配置文件
     *
     * @param name 配置文件名（无后缀）
     * @return
     */
    public static File getFile(String name) {
        return new File(Forge.getPluginFile(), "Item/" + name + ".yml");
    }

    /**
     * 获取配置文件
     *
     * @param name 配置文件名（无后缀）
     * @return
     */
    public static YamlConfiguration getYml(String name) {
        return YamlConfiguration.loadConfiguration(getFile(name));
    }

    /**
     * 获取锻造的装备
     *
     * @param name  配置文件名（无后缀）
     * @param title 玩家名字
     * @return
     */
    public static ItemStack getForgeItem(String name, String title, int strength) {
        YamlConfiguration yml = getYml(name);
        String type = yml.getString("Item.ItemType", "item");
        String names = yml.getString("Item.Name");
        ItemStack item = null;
        if (type.equalsIgnoreCase("mythic") && Forge.isMm()) {
            Optional<MythicItem> mythicItemOptional = MythicMobs.inst().getItemManager().getItem(names);
            if (mythicItemOptional.isPresent()) {
                MythicItem mythicItem = mythicItemOptional.get();
                item = BukkitAdapter.adapt(mythicItem.generateItemStack(1, null, null));
                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.getLore();
                if (strength > 0) {
                    lore = ItemUtil.getLore(lore, title, strength);
                } else {
                    lore = ItemUtil.getLore(lore, title, 0);
                }
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
        } else {
            String id = yml.getString("Item.Id");
            List<String> lore = yml.getStringList("Item.Lore");
            if (strength > 0) {
                lore = ItemUtil.getLore(lore, title, strength);
            } else {
                lore = ItemUtil.getLore(lore, title, 0);
            }
            Boolean unbreakable = yml.getBoolean("Item.Unbreakable");
            item = ItemUtil.getItemStack(names, id, lore, unbreakable);
        }
        return item;
    }

    /**
     * 获取配置文件的装备
     *
     * @param name 配置文件名（无后缀）
     * @return
     */
    public static ItemStack getItem(String name) {
        YamlConfiguration yml = getYml(name);
        String type = yml.getString("Item.ItemType", "item");
        String names = yml.getString("Item.Name");
        ItemStack item = null;
        if (type.equalsIgnoreCase("mythic") && Forge.isMm()) {
            Optional<MythicItem> mythicItemOptional = MythicMobs.inst().getItemManager().getItem(names);
            if (mythicItemOptional.isPresent()) {
                MythicItem mythicItem = mythicItemOptional.get();
                item = BukkitAdapter.adapt(mythicItem.generateItemStack(1, null, null));
            }
        } else {
            String id = yml.getString("Item.Id");
            List<String> lore = yml.getStringList("Item.Lore");
            Boolean unbreakable = yml.getBoolean("Item.Unbreakable");
            item = ItemUtil.getItemStack(names, id, lore, unbreakable);
        }
        return item;
    }

    /**
     * 获取配置文件命令
     *
     * @param name
     * @return
     */
    public static List<String> getCommand(String name) {
        return getYml(name).getStringList("Command.commands");
    }

    /**
     * 获取图纸
     *
     * @param name 配置文件名（无后缀）
     * @return
     */
    public static ItemStack getMap(String name) {
        YamlConfiguration yml = getYml(name);
        String type = yml.getString("Map.ItemType", "item");
        String names = yml.getString("Map.Name");
        ItemStack item = null;
        if (type.equalsIgnoreCase("mythic") && Forge.isMm()) {
            Optional<MythicItem> mythicItemOptional = MythicMobs.inst().getItemManager().getItem(names);
            if (mythicItemOptional.isPresent()) {
                MythicItem mythicItem = mythicItemOptional.get();
                item = BukkitAdapter.adapt(mythicItem.generateItemStack(1, null, null));
            }
        }else {
            String id = yml.getString("Map.Id");
            List<String> lore = yml.getStringList("Map.Lore");
            Boolean unbreakable = yml.getBoolean("Map.Unbreakable");
            item = ItemUtil.getItemStack(names, id, lore, unbreakable);
        }
        return item;
    }

    /**
     * 获取图纸所需要的等级
     *
     * @param name 配置文件名（无后缀）
     * @return
     */
    public static int getLevel(String name) {
        return getYml(name).getInt("Map.Level");
    }

    /**
     * 获取锻造成功后增加的经验
     *
     * @param name 配置文件名（无后缀）
     * @return
     */
    public static int getExp(String name) {
        return getYml(name).getInt("Map.Exp");
    }

    /**
     * 获取图纸中的物品类型
     *
     * @param name
     * @return
     */
    public static String getType(String name) {
        return getYml(name).getString("Map.Type");
    }

    /**
     * 获取锻造所需要的材料
     *
     * @param num  第几个材料
     * @param name 配置文件名（无后缀）
     * @return
     */
    public static ItemStack getMaterial(String num, String name) {
        YamlConfiguration yml = getYml(name);
        String type = yml.getString("Material." + num + ".ItemType", "item");
        String names = yml.getString("Material." + num + ".Name");
        int count = yml.getInt("Material." + num + ".Count");
        ItemStack item = null;
        if (type.equalsIgnoreCase("mythic") && Forge.isMm()) {
            Optional<MythicItem> mythicItemOptional = MythicMobs.inst().getItemManager().getItem(names);
            if (mythicItemOptional.isPresent()) {
                MythicItem mythicItem = mythicItemOptional.get();
                item = BukkitAdapter.adapt(mythicItem.generateItemStack(1, null, null));
            }
        }else {
            String id = yml.getString("Material." + num + ".Id");
            List<String> lore = yml.getStringList("Material." + num + ".Lore");
            Boolean unbreakable = yml.getBoolean("Material." + num + ".Unbreakable");
            item = ItemUtil.getItemStack(names, id, lore, unbreakable);
        }
        item.setAmount(count);
        return item;
    }

    /**
     * 生成默认指令配置文件
     *
     * @param name
     */
    public static void createCommands(String name) {
        File file = getFile(name);
        YamlConfiguration yml = getYml(name);
        List<String> lore = new ArrayList<>();

        yml.set("Map.Name", "&b&l创造切换锻造图纸");
        yml.set("Map.Id", "403");
        lore.add("&a创造物品锻造");
        lore.add("&c使用该物品可以切换到创造模式");
        yml.set("Map.Lore", lore);
        yml.set("Map.Unbreakable", false);
        yml.set("Map.Level", 1);
        yml.set("Map.Exp", 1);
        yml.set("Map.Type", "command");
        yml.set("Map.ItemType","item");

        lore = new ArrayList<>();
        lore.add("gamemode 3");
        lore.add("[op]gamemode 2");
        lore.add("[permission]gamemode 1");
        lore.add("[command]gamemode 0");
        yml.set("Command.commands", lore);
        yml.set("Material.0.Name", "钻石");
        yml.set("Material.0.Id", "264");
        yml.set("Material.0.Count", 64);
        yml.set("Material.0.Unbreakable", false);
        try {
            yml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成默认物品配置文件
     *
     * @param name 配置文件名（无后缀）
     */
    public static void createItem(String name) {
        File file = getFile(name);
        YamlConfiguration yml = getYml(name);
        List<String> lore = new ArrayList<>();

        yml.set("Map.Name", "&b&l初造之剑图纸");
        yml.set("Map.Id", "403");
        lore.add("&a锻造师之剑");
        lore.add("&c该图纸需要锻造等级:&b&l1");
        lore.add("&c&l需要锻造材料:");
        lore.add("&5煤炭X1组");
        lore.add("&b一阶锻造材料X10");
        lore.add("&a下级装备核心X1");
        yml.set("Map.Lore", lore);
        yml.set("Map.Unbreakable", false);
        yml.set("Map.Level", 1);
        yml.set("Map.Exp", 1);
        yml.set("Map.Type", "item");
        yml.set("Map.ItemType","item");


        lore = new ArrayList<>();
        yml.set("Item.Name", "&b&l初造之剑");
        yml.set("Item.Id", "276");
        lore.add("&a锻造武器    优秀");
        lore.add("&e&l——————————————————");
        lore.add("&e锻造信息：");
        lore.add("&e锻造者：&7[&e%Author%&7]");
        lore.add("&e锻造品质：&7%Quality%");
        lore.add("&e锻造强度：&7%Strength%");
        lore.add("&e&l——————————————————");
        lore.add("&7固定属性：");
        lore.add("&7暴击几率：10%");
        lore.add("&7吸血几率：20%");
        lore.add("&e&l——————————————————");
        lore.add("&a锻造属性：");
        lore.add("&a攻击力：<f:50> - <f:100>");
        lore.add("&a吸血倍率：<f:50>%");
        lore.add("&a暴击倍率：<f:5>%");
        lore.add("&e&l——————————————————");
        lore.add("&e初学锻造师的一门作品");
        lore.add("&e&l——————————————————");
        yml.set("Item.Lore", lore);
        yml.set("Item.Unbreakable", true);
        yml.set("Item.ItemType","item");
        lore = new ArrayList<>();

        yml.set("Material.0.Id", "263");
        yml.set("Material.0.Count", 64);
        yml.set("Material.0.Unbreakable", false);
        yml.set("Material.0.ItemType","item");

        yml.set("Material.1.Name", "&6一阶锻造材料");
        yml.set("Material.1.Id", "263:1");
        yml.set("Material.1.Count", 10);
        lore.add("&a最初级的锻造材料之一");
        yml.set("Material.1.Lore", lore);
        yml.set("Material.1.Lore", lore);
        yml.set("Material.1.Unbreakable", false);
        yml.set("Material.1.ItemType","item");

        yml.set("Material.2.Name", "&6下级装备核心");
        yml.set("Material.2.Id", "264");
        yml.set("Material.2.Count", 1);
        lore = new ArrayList<>();
        lore.add("&a最初级的锻造材料之一");
        yml.set("Material.2.Lore", lore);
        yml.set("Material.2.Unbreakable", false);
        yml.set("Material.2.ItemType","item");


        try {
            yml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 生成默认物品MM配置文件
     *
     * @param name 配置文件名（无后缀）
     */
    public static void createMythicItem(String name) {
        File file = getFile(name);
        YamlConfiguration yml = getYml(name);

        yml.set("Map.Name", "新手之剑图纸");
        yml.set("Map.Level", 1);
        yml.set("Map.Exp", 1);
        yml.set("Map.Type", "item");
        yml.set("Map.ItemType","mythic");

        yml.set("Item.Name", "新手之剑");
        yml.set("Item.ItemType","mythic");

        yml.set("Material.0.Name", "煤炭");
        yml.set("Material.0.Count", 64);
        yml.set("Material.0.ItemType","mythic");

        yml.set("Material.1.Name", "一阶锻造材料");
        yml.set("Material.1.Count", 10);
        yml.set("Material.1.ItemType","mythic");

        yml.set("Material.2.Name", "下级装备核心");
        yml.set("Material.2.Count", 1);
        yml.set("Material.2.ItemType","mythic");


        try {
            yml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取物品在集合中对应的key
     *
     * @param item 可获取的物品
     * @return
     */
    public static String getKey(ItemStack item) {
        if (item != null && !item.getType().equals(Material.AIR)) {
            for (Map.Entry<String, ItemStack> m : map.entrySet()) {
                if (m.getValue() != null && m.getValue().equals(item)) {
                    return m.getKey();
                }
            }
        }
        return null;
    }
}
