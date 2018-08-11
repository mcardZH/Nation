package cn.minezone.nation;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;

/**
 * @author mcard
 */
public class CommandHandler implements CommandExecutor, UpdateFinishHook {

    private Plugin plugin;
    private FileConfiguration config;
    private CommandSender sender;
    private YamlConfiguration lang;

    public CommandHandler(Plugin plugin) {
        this.plugin = plugin;
        config = plugin.getConfig();
        plugin.saveResource("language.yml", false);
        lang = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "language.yml"));
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 0 || "help".equalsIgnoreCase(args[0]) || !(commandSender instanceof Player)) {
            commandSender.sendMessage(lang.getStringList("help").toArray(new String[lang.getStringList("help").size()]));
            return true;
        }
        switch (args[0]) {
            case "kit": {
                if (NationAPI.isPlayerGetTodayReward((OfflinePlayer) commandSender)) {
                    commandSender.sendMessage(lang.getString("already-get"));
                    return true;
                }
                boolean isOP = commandSender.isOp();
                commandSender.setOp(true);
                for (String cmd : NationAPI.getPlayerRewardCommand((OfflinePlayer) commandSender)) {
                    Bukkit.dispatchCommand(commandSender, PlaceholderAPI.setPlaceholders((Player) commandSender, cmd));
                }
                commandSender.setOp(isOP);
                NationAPI.setPlayerDailyRewardType((OfflinePlayer) commandSender, System.currentTimeMillis());
                break;
            }
            case "nation": {
                if (NationAPI.isPlayerGetNationReward((OfflinePlayer) commandSender)) {
                    commandSender.sendMessage(lang.getString("already-get"));
                    return true;
                }
                boolean isOP = commandSender.isOp();
                commandSender.setOp(true);
                for (String cmd : NationAPI.getNationRewardCommand(NationAPI.getPlayerNation((OfflinePlayer) commandSender))) {
                    Bukkit.dispatchCommand(commandSender, PlaceholderAPI.setPlaceholders((Player) commandSender, cmd));
                }
                commandSender.setOp(isOP);
                NationAPI.setPlayerDailyRewardType((OfflinePlayer) commandSender, System.currentTimeMillis());
                break;
            }
            case "rank": {
                commandSender.sendMessage(lang.getString("rank").replace("{rank}", NationAPI.getPlayerRankInNation((OfflinePlayer) commandSender) + ""));
                break;
            }
            case "join": {
                if (!NationAPI.getPlayerDefaultNation().equals(NationAPI.getPlayerNation((OfflinePlayer) commandSender))) {
                    commandSender.sendMessage(lang.getString("cant-change"));
                    break;
                }
                if (args.length != 2) {
                    break;
                }
                if (!NationAPI.getNationList().contains(args[1])) {
                    commandSender.sendMessage(lang.getString("nation-no-exist"));
                    break;
                }
                NationAPI.setPlayerNation((OfflinePlayer) commandSender, args[1]);
                NationAPI.updateRankAsy(null);
                commandSender.sendMessage(lang.getString("join-success"));
                break;
            }
            case "list": {
                for (String string : NationAPI.getNationList()) {
                    String st = lang.getString("list");
                    st = st.replace("{nation}", string);
                    st = st.replace("{rank}", NationAPI.getNationRank(string) + "");
                    st = st.replace("{allpoint}", NationAPI.getNationPoint(string) + "");
                    commandSender.sendMessage(st);
                }
                break;
            }
            case "reload": {
                if (commandSender.isOp()) {
                    plugin.reloadConfig();
                    lang = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "language.yml"));
                    commandSender.sendMessage("§aReload Success");
                }
                break;
            }
            case "update": {
                if (commandSender.isOp()) {
                    NationAPI.updateRankAsy(this);
                    sender = commandSender;
                }
            }
            default: {
                commandSender.sendMessage(lang.getString("arg-error"));
            }
        }
        return true;
    }

    @Override
    public void finish() {
        sender.sendMessage("§aUpdate rank success!");
        sender = null;
    }
}
