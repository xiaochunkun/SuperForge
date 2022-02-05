package xiaokun.forge.listener;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import xiaokun.forge.Forge;
import xiaokun.forge.config.Config;
import xiaokun.forge.config.ItemConfig;
import xiaokun.forge.config.PlayerData;
import xiaokun.forge.event.ForgeCommandEvent;
import xiaokun.forge.event.ForgeItemEvent;
import xiaokun.forge.util.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 小坤
 */
public class ClickEvent implements Listener {
    private Inventory pInv;

    private JavaPlugin plugin = Forge.getPlugin();

    /**
     * 点击GUI的事件  GUI的操作大部分在这
     *
     * @param event
     */
    @EventHandler
    public void clickInventory(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        final Inventory inv = event.getInventory();
        final ItemStack eItem = event.getCurrentItem();
        final List<String> list = new ArrayList<String>();
        list.add(Message.getGui("GUI1"));
        list.add(Message.getGui("GUI2"));
        list.add(Message.getGui("GUI3"));
        list.add(Message.getGui("GUI4"));
        list.add(Message.getGui("GUI5"));

        if (list.contains(inv.getName())) {
            event.setCancelled(true);
        }

        if (Message.getGui("GUI2").equals(inv.getName()) && (event.getClick().equals(ClickType.LEFT)) && event.getRawSlot() < 54) {
            forgeAbleList(player, eItem);
        }

        if ((eItem != null)
                && (!eItem.getType().equals(Material.AIR))
                && (list.contains(inv.getName()))
                && (eItem.hasItemMeta())
                && (event.getClick().equals(ClickType.LEFT))
        ) {
            ItemMeta eMeta = eItem.getItemMeta();
            click(player, event, eMeta, inv);
        }
    }


    private void click(Player player, InventoryClickEvent event, ItemMeta eMeta, Inventory inv) {
        switch (eMeta.getDisplayName()) {
            case "查看已学习的图纸":
                pInv = CreateInventory.getInventory(1, player);
                player.closeInventory();
                player.openInventory(pInv);
                break;
            case "查看历史锻造的装备":
                pInv = CreateInventory.getInventory(3, player);
                player.closeInventory();
                player.openInventory(pInv);
                break;
            case "返回主菜单":
                pInv = CreateInventory.getInventory(0, player);
                player.closeInventory();
                player.openInventory(pInv);
                break;
            case "上一页":
                if (event.getInventory().getHolder() instanceof ForgeListInventory) {
                    ForgeListInventory forgeListInventory = (ForgeListInventory) event.getInventory().getHolder();
                    forgeListInventory.prevPage();
                    player.closeInventory();
                    ItemStack itemStack = forgeListInventory.getInventory().getItem(49);
                    itemStack.setAmount(forgeListInventory.getPage());
                    player.openInventory(forgeListInventory.getInventory());
                }

                if (event.getInventory().getHolder() instanceof ForgeHistoryInventory) {
                    ForgeHistoryInventory forgeListInventory = (ForgeHistoryInventory) event.getInventory().getHolder();
                    forgeListInventory.prevPage();
                    player.closeInventory();
                    ItemStack itemStack = forgeListInventory.getInventory().getItem(49);
                    itemStack.setAmount(forgeListInventory.getPage());
                    player.openInventory(forgeListInventory.getInventory());
                }
                break;
            case "下一页":
                if (event.getInventory().getHolder() instanceof ForgeListInventory) {
                    ForgeListInventory forgeListInventory = (ForgeListInventory) event.getInventory().getHolder();
                    forgeListInventory.nextPage();
                    player.closeInventory();
                    ItemStack itemStack = forgeListInventory.getInventory().getItem(49);
                    itemStack.setAmount(forgeListInventory.getPage());
                    player.openInventory(forgeListInventory.getInventory());
                }

                if (event.getInventory().getHolder() instanceof ForgeHistoryInventory) {
                    ForgeHistoryInventory forgeListInventory = (ForgeHistoryInventory) event.getInventory().getHolder();
                    forgeListInventory.nextPage();
                    player.closeInventory();
                    ItemStack itemStack = forgeListInventory.getInventory().getItem(49);
                    itemStack.setAmount(forgeListInventory.getPage());
                    player.openInventory(forgeListInventory.getInventory());
                }
                break;
            case "开始锻造":
                startForging(player, inv);
                break;
        }
    }

    /**
     * 可锻造列表
     *
     * @param player
     * @param eItem
     */
    private void forgeAbleList(Player player, ItemStack eItem) {
        pInv = CreateInventory.getInventory(2, player);
        assert pInv != null;
        pInv.setItem(19, eItem);
        String key = ItemConfig.getKey(eItem);
        if (key != null) {
            final ConfigurationSection section = ItemConfig.getYml(key).getConfigurationSection("Material");
            int nums = 0;
            for (String keys : section.getKeys(false)) {
                int finalNums = (((nums / 5) + 1) * 9) + 3 + (nums % 5);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        pInv.setItem(finalNums, ItemConfig.getMaterial(keys, key));
                    }
                }.runTask(plugin);
                nums++;
            }
            player.closeInventory();
            player.openInventory(pInv);
        }
    }

    /**
     * 开始锻造
     *
     * @param player
     * @param inv
     */
    private void startForging(Player player, Inventory inv) {
        ItemStack map = inv.getItem(19);
        String key = ItemConfig.getKey(map);
        final int level = ItemConfig.getLevel(key);
        final int playerLevel = PlayerData.getLevel(player);
        if (playerLevel >= level) {
            if (key != null) {
                YamlConfiguration yml = ItemConfig.getYml(key);
                ConfigurationSection section = yml.getConfigurationSection("Material");
                Map<ItemStack, Integer> material = new HashMap<>();
                boolean confirm = false;
                for (String keys : section.getKeys(false)) {
                    final ItemStack item = ItemConfig.getMaterial(keys, key);
                    final int num = item.getAmount();
                    ItemStack itemClone = item.clone();
                    itemClone.setAmount(1);
                    if (material.containsKey(itemClone)) {
                        material.put(itemClone, material.get(itemClone) + num);
                    } else {
                        material.put(itemClone, num);
                    }
                }

                for (ItemStack itemStack : material.keySet()) {
                    Inventory playerInventory = Bukkit.createInventory(null, 36);
                    for (int i = 0; i < 36; i++) {
                        playerInventory.setItem(i, player.getInventory().getItem(i));
                    }
                    final int hasNum = ItemUtil.getInvItemNum(playerInventory, itemStack);
                    final int needNum = material.get(itemStack);
                    if (hasNum < needNum) {
                        confirm = false;
                        break;
                    } else {
                        confirm = true;
                    }
                }
                if (confirm) {
                    for (ItemStack itemStack : material.keySet()) {
                        final int needNum = material.get(itemStack);
                        ItemUtil.removeItem(player.getInventory(), itemStack, needNum);
                    }
                    forgingProcess(player, key, map, material);
                } else {
                    Message.playerMessage(player, Message.getMessage("NoMaterial"));
                }

            } else {
                Message.playerMessage(player, Message.getMessage("InsufficientLevel"));
            }
        }
    }

    /**
     * 锻造过程及结果
     *
     * @param player
     * @param key
     */
    public void forgingProcess(final Player player, final String key, final ItemStack map, final Map<ItemStack, Integer> material) {
        ItemStack item2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
        ItemMeta meta2 = item2.getItemMeta();
        meta2.setDisplayName(" ");
        item2.setItemMeta(meta2);
        Inventory inv2 = Bukkit.createInventory(player, 27, Message.getGui("GUI5"));
        player.closeInventory();
        player.openInventory(inv2);
        AtomicInteger count = new AtomicInteger(0);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                inv2.setItem(count.get(), item2);
                count.getAndIncrement();
                if (count.get() >= 27) {
                    player.closeInventory();
                    if (ItemConfig.getType(key).equalsIgnoreCase("item")) {
                        final ItemStack item = ItemConfig.getForgeItem(key, player.getName(), ItemUtil.getStrength(player));
                        final ForgeItemEvent event = new ForgeItemEvent(player, item, map);
                        Bukkit.getPluginManager().callEvent(event);
                        if (!event.isCancelled()) {
                            forgedItem(event.getItemStack(), player);
                        }
                    }
                    if (ItemConfig.getType(key).equalsIgnoreCase("command")) {
                        final List<String> commandList = PlaceholderAPI.setPlaceholders(player, ItemConfig.getCommand(key));
                        final ForgeCommandEvent event = new ForgeCommandEvent(player, commandList, map);
                        Bukkit.getPluginManager().callEvent(event);
                        if (!event.isCancelled()) {
                            forgeCommand(event.getCommandList(), player, key);
                        }
                    }
                    PlayerData.addExp(player, ItemConfig.getExp(key));
                    cancel();
                }
            }
        }, 0, Config.getTime());
    }

    /**
     * 使用命令
     *
     * @param commandList
     * @param player
     */
    private void forgeCommand(List<String> commandList, Player player, String key) {
        Bukkit.getScheduler().runTask(Forge.getPlugin(), () -> {
            for (String command : commandList) {
                command = command.replaceAll("%player%", player.getName());
                if (command.contains("[command]")) {
                    command = command.replace("[command]", "");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                } else if (command.contains("[op]")) {
                    command = command.replace("[op]", "");
                    if (player.isOp()) {
                        player.performCommand(command);
                    } else {
                        player.setOp(true);
                        try {
                            player.performCommand(command);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        player.setOp(false);
                    }
                } else if (command.contains("[permission]")) {
                    command = command.replace("[permission]", "");
                    if (player.isOp()) {
                        player.performCommand(command);
                    } else {
                        Set<Permission> permissions = Bukkit.getPluginManager().getPermissions();
                        List<PermissionAttachment> list = new ArrayList<>();
                        for (Permission permission : permissions) {
                            String name = permission.getName();
                            if (!(player.hasPermission(permission)))
                                list.add(player.addAttachment(Forge.getPlugin(), name, true, 5));
                        }
                        try {
                            player.performCommand(command);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        for (PermissionAttachment permissionAttachment : list) {
                            permissionAttachment.remove();
                        }
                    }
                } else {
                    player.performCommand(command);
                }
            }
        });
        Message.playerMessage(player, Message.getMessage("CommandSuccessful"));
    }

    /**
     * 锻造物品
     *
     * @param itemStack
     * @param player
     */
    private void forgedItem(ItemStack itemStack, Player player) {
        player.getInventory().addItem(itemStack);
        Message.playerMessage(player, Message.getMessage("ItemSuccessful"));
        String notice = Message.getMessage("Announce");
        if (notice.length() != 0 && notice != null) {
            TextComponent tc = Message.getTextComponent(notice.replaceAll("%item%", itemStack.getItemMeta().getDisplayName()).replaceAll("%player%", player.getName()), null, itemStack.getItemMeta().getLore());
            Bukkit.spigot().broadcast(tc);
        }

        if (Config.isHistoryForge()){
            File file = PlayerData.getPlayerFile(player);
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
            int num = PlayerData.getItemNum(player);
            yml.set("item." + num, itemStack);
            yml.set("item.num", ++num);
            try {
                yml.save(file);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
