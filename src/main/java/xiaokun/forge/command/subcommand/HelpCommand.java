package xiaokun.forge.command.subcommand;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import xiaokun.forge.Forge;
import xiaokun.forge.command.SubCommand;
import xiaokun.forge.util.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认帮助列表
 * @author 小坤
 */
public class HelpCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("§0-§8 --§7 ---§c ----§4 -----§b " + Forge.getPluginName() + "§4 -----§c ----§7 ---§8 --§0 -");
        List<String> l = new ArrayList<String>();
        l.add("点击即可自动输入指令");
        TextComponent tc = Message.getTextComponent("§a/dz open - §4打开锻造列表", "/dz open", l);
        Message.sendTextComponent(sender, tc);

        tc = Message.getTextComponent("§a/dz level - §4查看自己经验", "/dz level", l);
        Message.sendTextComponent(sender, tc);

        if (sender.hasPermission("dz.item")) {
            tc = Message.getTextComponent("§a/dz item give <ConfigName> - §4给予玩家锻造RPG物品", "/dz item", l);
            Message.sendTextComponent(sender, tc);
        }

        if (sender.hasPermission("dz.map")) {
            tc = Message.getTextComponent("§a/dz map give <ConfigName> - §4给予玩家锻造RPG图纸", "/dz map", l);
            Message.sendTextComponent(sender, tc);
        }

        if (sender.hasPermission("dz.material")) {
            tc = Message.getTextComponent("§a/dz material give <ConfigName> - §4给予玩家锻造RPG材料", "/dz material", l);
            Message.sendTextComponent(sender, tc);
        }

        if (sender.hasPermission("dz.study")) {
            tc = Message.getTextComponent("§a/dz study <图纸> - §4直接学习命令", "/dz study", l);
            Message.sendTextComponent(sender, tc);
        }

        if (sender.hasPermission("dz.reload")) {
            tc = Message.getTextComponent("§a/dz reload - §4重载配置文件", "/dz reload", l);
            Message.sendTextComponent(sender, tc);
        }
        sender.sendMessage("§0-§8 --§7 ---§c ----§4 -----§b " + Forge.getPluginName() + "§4 -----§c ----§7 ---§8 --§0 -");
    }
}
