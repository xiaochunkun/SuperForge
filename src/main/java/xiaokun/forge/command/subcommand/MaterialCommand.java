package xiaokun.forge.command.subcommand;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import xiaokun.forge.command.SubCommand;
import xiaokun.forge.config.ItemConfig;
import xiaokun.forge.util.Message;

import java.util.List;


/**
 * 材料列表以及指令拿材料
 * @author 小坤
 */
public class MaterialCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if ((args[0].equalsIgnoreCase("material")) && (args.length >= 1)) {
                if (sender.hasPermission("dz.material")) {
                    if (args.length == 1 || args.length == 2) {
                        List<String> list = ItemConfig.getList();
                        if (list.size() > 0) {
                            Message.playerMessage(sender, "§0-§8 --§7 ---§c ----§4 -----§b 材料列表 §4 -----§c ----§7 ---§8 --§0 -");
                            for (int i = 0; i < list.size(); i++) {
                                String o = list.get(i);
                                final ItemMeta meta = ItemConfig.getMap(o).getItemMeta();
                                String name = null;
                                if (meta != null) {
                                    name = meta.getDisplayName() + " §e的材料";
                                } else {
                                    name = o + " §e的材料";
                                }
                                TextComponent tc = Message.getTextComponent("§e" + String.valueOf(i + 1) + ". §a" + name, "/dz material give " + o, meta.getLore());
                                Message.sendTextComponent(sender, tc);
                            }
                        } else {
                            Message.playerMessage(sender, "§4暂无物品信息");
                        }
                        Message.playerMessage(sender, "§0-§8 --§7 ---§c ----§4 -----§b 点击领取 §4 -----§c ----§7 ---§8 --§0 -");
                        return;
                    }
                    if ((args[1].equalsIgnoreCase("give")) && (args.length > 2) && (sender instanceof Player)) {
                        Player player = (Player) sender;
                        if (!(Bukkit.getPlayer(args[2]) instanceof Player) && (ItemConfig.getList().contains(args[2]))) {
                            final YamlConfiguration yml = ItemConfig.getYml(args[2]);
                            ConfigurationSection section = yml.getConfigurationSection("Material");
                            for (String key : section.getKeys(false)) {
                                player.getInventory().addItem(ItemConfig.getMaterial(key, args[2]));
                            }
                            Message.playerMessage(player, "§a物品已经发送至背包，请查收");
                        }
                    }
                }else {
                    Message.playerMessage(sender, Message.getMessage("NoPermission"));
                }
            }
        } else {
            Message.sendMessage(Message.getMessage("NotExecuted"));
        }
    }
}
