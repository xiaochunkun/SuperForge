package xiaokun.forge.command.subcommand;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xiaokun.forge.command.SubCommand;
import xiaokun.forge.config.ItemConfig;
import xiaokun.forge.util.Message;

import java.util.List;

/**
 * 图纸列表以及指令给图纸
 * @author 小坤
 */
public class MapCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if ((args[0].equalsIgnoreCase("map")) && (args.length >= 1)) {
                if (sender.hasPermission("dz.map")) {
                    if (args.length == 1 || args.length == 2) {
                        List<String> list = ItemConfig.getList();
                        if (list.size() > 0) {
                            Message.playerMessage(sender, "§0-§8 --§7 ---§c ----§4 -----§b 图纸列表 §4 -----§c ----§7 ---§8 --§0 -");
                            for (int i = 0; i < list.size(); i++) {
                                String o = list.get(i);
                                if (ItemConfig.getMap(o) != null && !ItemConfig.getMap(o).getType().equals(Material.AIR) && ItemConfig.getMap(o).hasItemMeta()) {
                                    final ItemMeta meta = ItemConfig.getMap(o).getItemMeta();
                                    String name = null;
                                    if (meta != null) {
                                        name = meta.getDisplayName();
                                    } else {
                                        name = o;
                                    }
                                    TextComponent tc = Message.getTextComponent("§e" + String.valueOf(i + 1) + ". §a" + name, "/dz map give " + o, meta.getLore());
                                    Message.sendTextComponent(sender, tc);
                                }
                            }
                        } else {
                            Message.playerMessage(sender, "§4暂无图纸信息");
                        }
                        Message.playerMessage(sender, "§0-§8 --§7 ---§c ----§4 -----§b 点击领取 §4 -----§c ----§7 ---§8 --§0 -");
                        return;
                    } if ((args[1].equalsIgnoreCase("give")) && (args.length > 2) && (sender instanceof Player)) {
                        Player player = (Player) sender;
                        if (!(Bukkit.getPlayer(args[2]) instanceof Player) && (ItemConfig.getList().contains(args[2]))) {
                            final ItemStack item = ItemConfig.getMap(args[2]);
                            if (args.length == 3) {
                                player.getInventory().addItem(item);
                            } else {
                                if (Integer.valueOf(args[3]) instanceof Integer) {
                                    item.setAmount(Integer.valueOf(args[3]));
                                }
                                player.getInventory().addItem(item);
                            }
                            Message.playerMessage(player,"§a物品已经发送至背包，请查收");
                        }
                    }
                } else {
                    Message.playerMessage(sender, Message.getMessage("NoPermission"));
                }
            }
        } else {
            Message.sendMessage(Message.getMessage("NotExecuted"));
        }
    }
}
