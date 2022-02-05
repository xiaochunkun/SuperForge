package xiaokun.forge.command.subcommand;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xiaokun.forge.Forge;
import xiaokun.forge.command.SubCommand;
import xiaokun.forge.config.ItemConfig;
import xiaokun.forge.config.PlayerData;
import xiaokun.forge.util.ItemUtil;
import xiaokun.forge.util.Message;

/**
 * @author 小坤
 */
public class ConsoleCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 5) {
                Player player = Bukkit.getPlayer(args[3]);
                if (player != null) {
                    if (ItemConfig.getList().contains(args[4])) {
                        if (args[1].equalsIgnoreCase("item") && args[2].equalsIgnoreCase("give")) {
                            final ItemStack item = ItemConfig.getForgeItem(args[4], args[3], ItemUtil.getStrength(player));
                            player.getInventory().addItem(item);
                            Message.playerMessage(player, "§a物品已经发送至" + player.getName() + "背包，请查收");
                        } else if (args[1].equalsIgnoreCase("map") && args[2].equalsIgnoreCase("give")) {
                            final ItemStack item = ItemConfig.getMap(args[4]);
                            player.getInventory().addItem(item);
                            Message.playerMessage(player, "§a图纸已经发送至" + player.getName() + "背包，请查收");
                        } else if (args[1].equalsIgnoreCase("material") && args[2].equalsIgnoreCase("give")) {
                            final YamlConfiguration yml = ItemConfig.getYml(args[4]);
                            ConfigurationSection section = yml.getConfigurationSection("Material");
                            for (String key : section.getKeys(false)) {
                                player.getInventory().addItem(ItemConfig.getMaterial(key, args[4]));
                            }
                            Message.playerMessage(player, "§a材料已经发送至" + player.getName() + "背包，请查收");
                        } else if (args[1].equalsIgnoreCase("study") && args[2].equalsIgnoreCase("give")) {
                            final YamlConfiguration yml = PlayerData.getPlayerData(player);
                            final ConfigurationSection section = yml.getConfigurationSection("map");
                            final ItemStack item = ItemConfig.getMap(args[4]);
                            boolean exist = true;
                            for (String keys : section.getKeys(false)) {
                                if ((section.getItemStack(keys) != null) && (section.getItemStack(keys).equals(item))) {
                                    Message.sendMessage(Message.getMessage("NoStudyMap"));
                                    exist = false;
                                }
                            }
                            if (exist) {
                                PlayerData.addMap(player, args[4], yml);
                                Message.playerMessage(player, Message.getMessage("StudySuccess").replace("%item%", args[4]));
                            }
                        }
                    }else if (args[1].equalsIgnoreCase("level")) {
                        if (args[2].equalsIgnoreCase("add")) {
                            if (LevelCommand.isInteger(args[4])) {
                                int level = Integer.valueOf(args[4]);
                                PlayerData.addLevel(player, level);
                                Message.playerMessage(player, "§a已经为" + player.getName() + "增加" + args[4] + "级");
                            }
                        } else if (args[2].equalsIgnoreCase("set")) {
                            if (LevelCommand.isInteger(args[4])) {
                                int level = Integer.valueOf(args[4]);
                                PlayerData.setLevel(player, level);
                                Message.playerMessage(player, "§a已经将" + player.getName() + "设置为" + args[4] + "级");
                            }
                        }
                    }else {
                        Message.sendMessage(Message.getMessage("NoContains"));
                    }
                } else {
                    Message.sendMessage(Message.getMessage("NoPlayer"));
                }
            } else {
                Message.sendMessage("§0-§8 --§7 ---§c ----§4 -----§b " + Forge.getPluginName() + "§4 -----§c ----§7 ---§8 --§0 -");
                Message.sendMessage("§adz console item give <player> <name> - §4给予玩家随机锻造物品");
                Message.sendMessage("§adz console map give <player> <name> - §4给予玩家图纸");
                Message.sendMessage("§adz console material give <player> <name> - §4给予玩家锻造材料");
                Message.sendMessage("§adz console study give <player> <name> - §4让玩家学习对应图纸");
                Message.sendMessage("§adz console level set <player> <num> - §4设置玩家等级");
                Message.sendMessage("§adz console level add <player> <num> - §4增加玩家等级");
                Message.sendMessage("§0-§8 --§7 ---§c ----§4 -----§b " + Forge.getPluginName() + "§4 -----§c ----§7 ---§8 --§0 -");
            }
        } else {
            Message.playerMessage(sender, Message.getMessage("NotConsole"));
        }
    }
}
