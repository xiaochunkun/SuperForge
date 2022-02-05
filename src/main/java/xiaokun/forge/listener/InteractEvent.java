package xiaokun.forge.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xiaokun.forge.config.ItemConfig;
import xiaokun.forge.config.PlayerData;
import xiaokun.forge.event.ForgeStudyEvent;
import xiaokun.forge.util.Message;

import java.io.IOException;


/**
 * @author 小坤
 */
public class InteractEvent implements Listener {

    /**
     * 右键图纸
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        final YamlConfiguration yml = PlayerData.getPlayerData(player);
        final ConfigurationSection section = yml.getConfigurationSection("map");
        boolean exist = true;
        if ((item != null) && (!item.getType().equals(Material.AIR))) {
            ItemStack items = item.clone();
            items.setAmount(1);
            if (ItemConfig.getKey(items) != null) {
                final String key = ItemConfig.getKey(items);
                final int level = ItemConfig.getLevel(key);
                final int playerLevel = PlayerData.getLevel(player);
                if (playerLevel >= level) {
                    for (String keys : section.getKeys(false)) {
                        if ((section.getItemStack(keys) != null) && (section.getItemStack(keys).equals(items))) {
                            Message.playerMessage(player, Message.getMessage("NoStudyMap"));
                            exist = false;
                            break;
                        }
                    }
                    if (exist) {
                        if (PlayerData.getLevel(player) >= ItemConfig.getLevel(ItemConfig.getKey(items))) {
                            if (PlayerData.addMap(player, ItemConfig.getKey(items), yml)) {
                                item.setAmount(item.getAmount() - 1);
                            }
                        } else {
                            Message.playerMessage(player, Message.getMessage("NoForgeGrade"));
                        }
                    }
                } else {
                    Message.playerMessage(player, Message.getMessage("InsufficientLevel"));
                }
            }
        }
    }
}
