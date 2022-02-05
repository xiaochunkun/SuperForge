package xiaokun.forge.command.subcommand;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xiaokun.forge.command.SubCommand;
import xiaokun.forge.config.LevelConfig;
import xiaokun.forge.config.PlayerData;
import xiaokun.forge.util.Message;

import java.util.regex.Pattern;

/**
 * @author 小坤
 */
public class LevelCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if ((args.length >= 2) && args[1].equalsIgnoreCase("add")) {
                if (sender.hasPermission("dz.add")) {
                    if (isInteger(args[2])) {
                        int level = Integer.valueOf(args[2]);
                        PlayerData.addLevel((Player) sender, level);
                        Message.playerMessage(sender, "§a你已经为自己增加" + args[2] + "级");
                    } else if (Bukkit.getPlayer(args[2]) != null && isInteger(args[3])) {
                        Player player = Bukkit.getPlayer(args[2]);
                        int level = Integer.valueOf(args[3]);
                        PlayerData.addLevel(player, level);
                        Message.playerMessage(sender, "§a你已经为" + args[2] + "增加" + args[3] + "级");
                    } else {
                        Message.playerMessage(sender, "§4请正确输入指令: §a/dz level add <玩家(可选)> <等级(数字)>");
                    }
                } else {
                    Message.playerMessage(sender, Message.getMessage("NoPermission"));
                }
            } else if ((args.length >= 2) && args[1].equalsIgnoreCase("set")) {
                if (sender.hasPermission("dz.set")) {
                    if (isInteger(args[2])) {
                        int level = Integer.valueOf(args[2]);
                        PlayerData.setLevel((Player) sender, level);
                        Message.playerMessage(sender, "§a你已经把自己设置为" + args[2] + "级");
                    } else if (Bukkit.getPlayer(args[2]) != null && isInteger(args[3])) {
                        Player player = Bukkit.getPlayer(args[2]);
                        int level = Integer.valueOf(args[3]);
                        PlayerData.setLevel(player, level);
                        Message.playerMessage(sender, "§a你已经把" + args[2] + "设置为" + args[3] + "级");
                    } else {
                        Message.playerMessage(sender, "§4请正确输入指令: §a/dz level add <玩家(可选)> <等级(数字)>");
                    }
                } else {
                    Message.playerMessage(sender, Message.getMessage("NoPermission"));
                }
            } else {
                Player player = (Player) sender;
                Message.playerMessages(sender, "§0-§8 --§7 ---§c ----§4 -----§b 当前等级 §4 -----§c ----§7 ---§8 --§0 -");
                Message.playerMessages(sender, "§e当前等级：§a" + PlayerData.getLevel(player));
                Message.playerMessages(sender, "§e当前经验：§a" + PlayerData.getExp(player));
                Message.playerMessages(sender, "§e距离升级：§a" + (LevelConfig.getExp(String.valueOf(PlayerData.getLevel(player))) - PlayerData.getExp(player)));
            }
        } else {
            Message.sendMessage(Message.getMessage("NotExecuted"));
        }
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}
