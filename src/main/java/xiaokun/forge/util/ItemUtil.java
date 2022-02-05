package xiaokun.forge.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xiaokun.forge.Forge;
import xiaokun.forge.config.Config;
import xiaokun.forge.config.LevelConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author 小坤
 */
public class ItemUtil {

    /**
     * 快速生成物品
     *
     * @param itemName
     * @param id
     * @param itemLore
     * @param unbreakable
     * @return
     */
    public static ItemStack getItemStack(String itemName, String id, List<String> itemLore, Boolean unbreakable) {
        int itemMaterial = 0;
        int itemDurability = 0;
        if (id != null) {
            if (id.contains(":") && id.split(":")[0].replaceAll("[^0-9]", "").length() > 0) {
                String[] idSplit = id.split(":");
                if (idSplit[0].replaceAll("[^0-9]", "").length() > 0 && idSplit[1].replaceAll("[^0-9]", "").length() > 0) {
                    itemMaterial = Integer.valueOf(idSplit[0].replaceAll("[^0-9]", ""));
                    itemDurability = Integer.valueOf(idSplit[1].replaceAll("[^0-9]", ""));
                }
            } else if (id.replaceAll("[^0-9]", "").length() > 0) {
                itemMaterial = Integer.valueOf(id.replaceAll("[^0-9]", ""));
            }
        }
        ItemStack item = new ItemStack(itemMaterial, 1, (short) itemDurability);
        if (item.getType().name().equals(Material.AIR.name())) {
            return null;
        }
        ItemMeta meta = item.getItemMeta();
        if (itemName != null) {
            meta.setDisplayName(itemName.replace("&", "§"));
        }

        if (unbreakable != null) {
            if (Forge.getVersionSplit()[1] >= 11) {
                meta.setUnbreakable(unbreakable);
            } else {
                meta.spigot().setUnbreakable(unbreakable);
            }
        }

        if (itemLore != null) {
            for (int i = 0; i < itemLore.size(); i++) {
                itemLore.set(i, itemLore.get(i).replace("&", "§"));
            }
            meta.setLore(itemLore);
        }
        item.setItemMeta(meta);
        return item;
    }

    /**
     * 快速处理lore
     *
     * @param lore
     * @param name
     * @return
     */
    public static List<String> getLore(List<String> lore, String name, int strength) {
        List<String> list = new ArrayList<>();
        String level = LevelConfig.getQuality();
        String attribute = LevelConfig.getAttribute(level);
        String Strength = Config.getStrengthColor(strength);
        for (String key : lore) {
            if (key.contains("%dz_author%")) {
                key = key.replaceAll("%dz_author%", name);
            }

            if (key.contains("%Author%")) {
                key = key.replaceAll("%Author%", name);
            }

            if (key.contains("%author%")) {
                key = key.replaceAll("%author%", name);
            }

            if (key.contains("%Quality%")) {
                key = key.replaceAll("%Quality%", level);
            }

            if (key.contains("%Strength%")) {
                key = key.replaceAll("%Strength%", Strength);
            }

            if (key.contains("<f:") && key.contains(">")) {
                List<String> lists = getStringList("<f:", ">", key);
                if (lists.size() >= 2) {
                    String formula = attribute.replaceAll("%强度%", String.valueOf(strength)).replaceAll("%预设值%", lists.get(1));
                    double math;
                    if (isNumeric(formula)) {
                        math = Double.parseDouble(formula);
                    } else {
                        math = MathUtil.arithmetic(formula);
                    }
                    key = key.replaceAll("<f:" + lists.get(1) + ">", String.valueOf((int) (math)));
                }
                String formula = attribute.replaceAll("%强度%", String.valueOf(strength)).replaceAll("%预设值%", lists.get(0));
                double math;
                if (isNumeric(formula)) {
                    math = Double.parseDouble(formula);
                } else {
                    math = MathUtil.arithmetic(formula);
                }
                key = key.replaceAll("<f:" + lists.get(0) + ">", String.valueOf((int) (math)));
            }

            list.add(key);
        }
        return list;
    }

    /**
     * 判断字符串是否为正整数
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取物品lore
     */
    public static List<String> getItemLore(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().equals(Material.AIR) || !(itemStack.hasItemMeta()) || !(itemStack.getItemMeta().hasLore()))
            return null;
        return itemStack.getItemMeta().getLore();
    }

    /**
     * 提取lore的数字
     *
     * @param prefix
     * @param suffix
     * @param string
     * @return
     */
    public static List<String> getStringList(String prefix, String suffix, String string) {
        List<String> stringList = new ArrayList<String>();
        if (string.contains(prefix)) {
            String[] args = string.split(prefix);
            if (args.length > 1 && args[1].contains(suffix)) {
                for (int i = 1; i < args.length && args[i].contains(suffix); i++) {
                    stringList.add(args[i].split(suffix)[0]);
                }
            }
        }
        return stringList;
    }

    /**
     * 获取背包指定物品的数量
     *
     * @param inv
     * @param itemStack
     * @return
     */
    public static int getInvItemNum(final Inventory inv, final ItemStack itemStack) {
        int num = 0;
        for (final ItemStack item : inv.getContents()) {
            if (item != null && item.isSimilar(itemStack)) {
                num += item.getAmount();
            }
        }
        return num;
    }

    /**
     * 移除背包某个物品给定的数量
     *
     * @param inv
     * @param itemStack
     * @param num
     */
    public static void removeItem(final Inventory inv, final ItemStack itemStack, final int num) {
        final ItemStack itemClone = itemStack.clone();
        itemClone.setAmount(1);
        for (int i = 0; i < num; ++i) {
            inv.removeItem(new ItemStack[]{itemClone});
        }
    }

    /**
     * 获取一个强度等级
     * */
    public static int getStrength(Player player) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int strength = random.nextInt(10) + 1;
        if (strength >= 10) return 10;
        ItemStack item = player.getEquipment().getItemInMainHand();
        List<String> lore = ItemUtil.getItemLore(item);
        int add = 0;
        boolean confirm = false;
        if (lore != null) {
            for (String s : Config.getItemMap().keySet()) {
                if (lore.contains(s)) {
                    confirm = true;
                    add = Config.getItemMap().get(s);
                }
            }
        }
        if (!confirm) return strength;
        int num = item.getAmount();
        if (add == 0) return strength;
        if (Config.isContinued()){
            for (int i = 0; i < num; i++) {
                strength = strength + add;
                item.setAmount(item.getAmount() - 1);
                if (strength >= 10) break;
            }
        }else {
            strength = strength + add;
            item.setAmount(item.getAmount() - 1);
        }
        if (strength >= 10) strength = 10;
        return strength;
    }
}
