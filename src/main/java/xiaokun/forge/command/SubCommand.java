package xiaokun.forge.command;

import org.bukkit.command.CommandSender;

/**
 * @author 小坤
 */
public interface SubCommand {

    /**
     * 命令操作转换
     * @param sender
     * @param args
     */
    void execute(CommandSender sender, String[] args);
}
