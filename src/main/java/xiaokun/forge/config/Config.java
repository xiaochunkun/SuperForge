package xiaokun.forge.config;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import xiaokun.forge.Forge;
import xiaokun.forge.util.Message;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 小坤
 */
public class Config {

    @Getter
    private static File file = new File(Forge.getPluginFile(), "config.yml");

    @Getter
    private static YamlConfiguration yml = new YamlConfiguration();

    @Getter
    private static Map<String, Integer> itemMap;

    @Getter
    private static boolean continued;

    @Getter
    private static boolean historyForge;

    /**
     * 加载 config.yml
     */
    public static void loadConfig() {
        if (!file.exists()) {
            Forge.getPlugin().saveDefaultConfig();
            Message.sendMessage("§a保存默认配置");
        }
        try {
            yml.load(file);
            Message.sendMessage("§a读取 config.yml 成功");
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            Message.sendMessage("§a读取 config.yml 时发生错误");
        }
        continued = yml.getBoolean("Continued", false);
        historyForge = yml.getBoolean("Options.HistoryForge", true);
        ConfigurationSection section = yml.getConfigurationSection("Item");
        if (section == null) return;
        itemMap = new HashMap<>();
        for (String key : section.getKeys(false)) {
            itemMap.put(section.getString(key + ".Lore").replaceAll("&", "§"), section.getInt(key + ".Add"));
        }
    }

    /**
     * 生成强度的颜色字符串
     *
     * @param level
     * @return
     */
    public static String getStrengthColor(int level) {
        String color = yml.getString("Options.Strength-color").substring(level - 1, level);
        String string = "";
        for (int i = 0; i < 10; i++) {
            if (i < level) {
                String colors = "§" + color + "|";
                string += colors;
            } else {
                string += "§8|";
            }
        }
        return string;
    }

    /**
     * GUI运行的时间
     *
     * @return
     */
    public static int getTime() {
        int time = yml.getInt("Options.Time") / 27;
        return time;
    }

}
