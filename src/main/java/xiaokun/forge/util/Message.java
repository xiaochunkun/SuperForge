package xiaokun.forge.util;

import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xiaokun.forge.Forge;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author 小坤
 */
public class Message {
    @Getter
    private static File file = new File(Forge.getPluginFile(), "message.yml");

    @Getter
    private static YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);

    /**
     * 加载 message.yml
     */
    public static void loadConfig() {
        if (!file.exists()) {
            Forge.getPlugin().saveResource("message.yml", true);
            Message.sendMessage("§a保存message配置");
        }
        Message.sendMessage("§a读取 message.yml 成功");
        try {
            yml.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            Message.sendMessage("§a读取 message.yml 时发生错误");
        }
    }

    /**
     * 从配置文件获取提示
     *
     * @param name
     * @return
     */
    public static String getMessage(String name) {
        return yml.getString(name).replaceAll("&", "§");
    }

    /**
     * 向控制台发送内容
     *
     * @param msg
     */
    public static void sendMessage(String msg) {
        if (msg != null) {
            Forge.getPlugin().getLogger().info(msg.replaceAll("&", "§"));
        }
    }

    /**
     * 向玩家发送内容
     *
     * @param sender
     * @param msg
     */
    public static void playerMessage(CommandSender sender, String msg) {
        if (msg != null) {
            msg = Message.getMessage("Prefix").replaceAll("&", "§") + msg.replaceAll("&", "§");
            msg = PlaceholderAPI.setPlaceholders((Player) sender, msg);
            sender.sendMessage(msg);
        }
    }
    /**
     * 向玩家发送无前缀内容
     *
     * @param sender
     * @param msg
     */
    public static void playerMessages(CommandSender sender, String msg) {
        if (msg != null) {
            msg = msg.replaceAll("&", "§");
            msg = PlaceholderAPI.setPlaceholders((Player) sender, msg);
            sender.sendMessage(msg);
        }
    }

    /**
     * 向玩家发送内容
     *
     * @param player
     * @param msg
     */
    public static void playerMessage(Player player, String msg) {
        if (msg != null) {
            msg = Message.getMessage("Prefix").replaceAll("&", "§") + msg.replaceAll("&", "§");
            msg = PlaceholderAPI.setPlaceholders(player, msg);
            player.sendMessage(msg);
        }
    }

    /**
     * 获取文本组件
     *
     * @param message
     * @param command
     * @param stringList
     * @return
     */
    public static TextComponent getTextComponent(String message, String command, List<String> stringList) {
        TextComponent tcMessage = new TextComponent(message);
        if (stringList != null && stringList.size() > 0) {
            ComponentBuilder bc = new ComponentBuilder("§7" + stringList.get(0).replace("&", "§"));
            IntStream.range(1, stringList.size()).mapToObj(i -> "\n§7" + stringList.get(i).replace("&", "§")).forEach(bc::append);
            tcMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, bc.create()));
        }
        if (command != null) {
            tcMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        }
        return tcMessage;
    }

    /**
     * 发送文本组件
     *
     * @param sender
     * @param tc
     */
    public static void sendTextComponent(CommandSender sender, TextComponent tc) {
        if (sender instanceof Player) {
            ((Player) sender).spigot().sendMessage(tc);
        } else {
            sender.sendMessage(tc.getText());
        }
    }

    /**
     * 获取GUI标题
     * @param key
     * @return
     */
    public static String getGui(final String key){
        return yml.getString(key);
    }
}

