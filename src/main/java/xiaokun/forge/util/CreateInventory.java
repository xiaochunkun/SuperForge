package xiaokun.forge.util;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import xiaokun.forge.Forge;
import xiaokun.forge.config.PlayerData;

/**
 * @author 小坤
 */
public class CreateInventory {

    private static Inventory inv;
    private static ItemStack item;
    private static ItemMeta meta;

    private static JavaPlugin plugin = Forge.getPlugin();

    @Getter
    public static int mapNum;
    @Getter
    public static int itemNum;

    /**
     * GUI 的所在位置
     *
     * @param type
     * @param player
     * @return
     */
    public static Inventory getInventory(int type, Player player) {
        if (type == 0) {
            inv = Bukkit.createInventory(null, 27, Message.getGui("GUI1"));
            item = new ItemStack(Material.ENCHANTED_BOOK);
            meta = item.getItemMeta();
            meta.setDisplayName("查看已学习的图纸");
            item.setItemMeta(meta);
            inv.setItem(11, item);

            meta.setDisplayName("查看历史锻造的装备");
            item.setItemMeta(meta);
            inv.setItem(15, item);

            init();
            return inv;
        }
        if (type == 1) {
            inv = Bukkit.createInventory(null, 54, Message.getGui("GUI2"));
            int num = PlayerData.getMapNum(player);
            for (int i = 0; i < num; i++) {
                if (i < (mapNum * 45)) {
                    if (PlayerData.getMap(player, ((mapNum - 1) * 45) + i) != null) {
                        ItemStack item = PlayerData.getMap(player, ((mapNum - 1) * 45) + i);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                inv.addItem(item);
                            }
                        }.runTask(plugin);
                    }
                } else {
                    break;
                }
            }
            setInventory(inv, "map");
            return inv;
        }
        if (type == 2) {
            inv = Bukkit.createInventory(null, 54, Message.getGui("GUI3"));
            ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(" ");
            item.setItemMeta(meta);
            inv.setItem(0, item);
            inv.setItem(1, item);
            inv.setItem(2, item);
            inv.setItem(3, item);
            inv.setItem(4, item);
            inv.setItem(5, item);
            inv.setItem(6, item);
            inv.setItem(7, item);
            inv.setItem(8, item);
            inv.setItem(9, item);
            inv.setItem(11, item);
            inv.setItem(17, item);
            inv.setItem(18, item);
            inv.setItem(20, item);
            inv.setItem(26, item);
            inv.setItem(27, item);
            inv.setItem(29, item);
            inv.setItem(35, item);
            inv.setItem(36, item);
            inv.setItem(38, item);
            inv.setItem(44, item);
            inv.setItem(45, item);
            inv.setItem(46, item);
            inv.setItem(47, item);
            inv.setItem(48, item);
            inv.setItem(50, item);
            inv.setItem(51, item);
            inv.setItem(52, item);
            inv.setItem(53, item);

            item = new ItemStack(Material.WORKBENCH);
            meta = item.getItemMeta();
            meta.setDisplayName("开始锻造");
            item.setItemMeta(meta);
            inv.setItem(49, item);
            return inv;
        }
        if (type == 3) {
            inv = Bukkit.createInventory(null, 54, Message.getGui("GUI4"));
            int num = PlayerData.getItemNum(player);
            setInventory(inv, "item");
            for (int i = 0; i < num; i++) {
                if (i < (itemNum * 45)) {
                    int finalI = i;
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        ItemStack item = PlayerData.getItem(player, ((itemNum - 1) * 45) + finalI);
                        if (item != null) {
                            inv.addItem(item);
                        }
                    }, 10L);
                } else {
                    break;
                }
            }
            return inv;
        }
        return null;
    }

    /**
     * 设置Gui的内容
     *
     * @param inv
     * @param type
     */
    static void setInventory(Inventory inv, String type) {
        item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
        meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        inv.setItem(46, item);
        inv.setItem(47, item);
        inv.setItem(48, item);
        inv.setItem(50, item);
        inv.setItem(51, item);
        inv.setItem(52, item);

        item = new ItemStack(Material.ARROW);
        meta = item.getItemMeta();
        meta.setDisplayName("上一页");
        item.setItemMeta(meta);
        inv.setItem(45, item);

        meta.setDisplayName("下一页");
        item.setItemMeta(meta);
        inv.setItem(53, item);

        item = new ItemStack(Material.SKULL_ITEM);
        meta = item.getItemMeta();
        meta.setDisplayName("返回主菜单");
        if (type.equalsIgnoreCase("map")) {
            item.setAmount(mapNum);
        } else {
            item.setAmount(itemNum);
        }
        item.setItemMeta(meta);
        inv.setItem(49, item);
    }

    /**
     * GUI翻页初始化
     */
    static void init() {
        itemNum = 1;
        mapNum = 1;
    }

    /**
     * 设置物品的页数
     *
     * @param itemNums
     */
    public static void setItemNum(int itemNums) {
        if (itemNums > 0) {
            itemNum = itemNums;
        }
    }

    /**
     * 设置图纸的页数
     *
     * @param mapNums
     */
    public static void setMapNum(int mapNums) {
        if (mapNums > 0) {
            mapNum = mapNums;
        }
    }
}
