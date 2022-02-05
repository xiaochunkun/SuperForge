package xiaokun.forge.command.subcommand;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xiaokun.forge.command.SubCommand;
import xiaokun.forge.config.Config;
import xiaokun.forge.config.ItemConfig;
import xiaokun.forge.config.PlayerData;
import xiaokun.forge.util.Message;

import java.util.List;

/**
 * @author 小坤
 */
public class StudyCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if ((args[0].equalsIgnoreCase("study")) && (args.length >= 1)) {
                if (sender.hasPermission("dz.study")) {
                    if (args.length == 2) {
                        Player player = (Player) sender;
                        final YamlConfiguration yml = PlayerData.getPlayerData(player);
                        final ConfigurationSection section = yml.getConfigurationSection("map");
                        final ItemStack item = ItemConfig.getMap(args[1]);
                        boolean exist = true;
                        for (String keys : section.getKeys(false)) {
                            if ((section.getItemStack(keys) != null) && (section.getItemStack(keys).equals(item))) {
                                Message.playerMessage(sender, Message.getMessage("NoStudyMap"));
                                exist = false;
                                break;
                            }
                        }
                        if (exist) {
                            PlayerData.addMap(player, args[1], yml);
                        }
                    } else if (args.length == 3) {
                        Player player = Bukkit.getPlayer(args[1]);
                        if (player != null) {
                            final YamlConfiguration yml = PlayerData.getPlayerData(player);
                            final ConfigurationSection section = yml.getConfigurationSection("map");
                            final ItemStack item = ItemConfig.getMap(args[2]);
                            boolean exist = true;
                            for (String keys : section.getKeys(false)) {
                                if ((section.getItemStack(keys) != null) && (section.getItemStack(keys).equals(item))) {
                                    Message.playerMessage(sender, Message.getMessage("NoStudyMap"));
                                    exist = false;
                                }
                            }
                            if (exist) {
                                PlayerData.addMap(player, args[2], yml);
                                Message.playerMessage(player,Message.getMessage("StudySuccess").replace("%item%",args[2]));
                            }
                        } else {
                            Message.playerMessage(sender, Message.getMessage("NoFound"));
                        }

                    } else {
                        List<String> list = ItemConfig.getList();
                        if (list.size() > 0) {
                            Message.playerMessage(sender, "§0-§8 --§7 ---§c ----§4 -----§b 图纸列表 §4 -----§c ----§7 ---§8 --§0 -");
                            for (int i = 0; i < list.size(); i++) {
                                String o = list.get(i);
                                final ItemMeta meta = ItemConfig.getMap(o).getItemMeta();
                                String name = null;
                                if (meta != null) {
                                    name = meta.getDisplayName();
                                } else {
                                    name = o;
                                }
                                TextComponent tc = Message.getTextComponent("§e" + String.valueOf(i + 1) + ". §a" + name, "/dz study " + o, meta.getLore());
                                Message.sendTextComponent(sender, tc);
                            }
                        } else {
                            Message.playerMessage(sender, "§4暂无图纸信息");
                        }
                        Message.playerMessage(sender, "§0-§8 --§7 ---§c ----§4 -----§b 点击学习 §4 -----§c ----§7 ---§8 --§0 -");
                        return;
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
