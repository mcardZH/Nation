package cn.minezone.nation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * @author mcard
 */
public class PlayerNameAPI {
    public void setTag(Player p, String prefix, String suffix)
            throws Exception {
        prefix = ChatColor.translateAlternateColorCodes('&', prefix);
        suffix = ChatColor.translateAlternateColorCodes('&', suffix);
        if (prefix.length() > 16) {
            prefix = prefix.substring(0, 16);
        }
        if (suffix.length() > 16) {
            suffix = suffix.substring(0, 16);
        }
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team t = board.getTeam(p.getName());
        if (t == null) {
            t = board.registerNewTeam(p.getName());
            t.setPrefix(prefix);
            t.setSuffix(suffix);
            t.addPlayer(p);
        } else {
            t = board.getTeam(p.getName());
            t.setPrefix(prefix);
            t.setSuffix(suffix);
            t.addPlayer(p);
        }
        for (Player o : Bukkit.getOnlinePlayers()) {
            o.setScoreboard(board);
        }
    }

    public void setTag(Player p, String prefix)
            throws Exception {
        prefix = ChatColor.translateAlternateColorCodes('&', prefix);
        if (prefix.length() > 16) {
            prefix = prefix.substring(0, 16);
        }
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team t = board.getTeam(p.getName());
        if (t == null) {
            t = board.registerNewTeam(p.getName());
            t.setPrefix(prefix);
            t.addPlayer(p);
        } else {
            t = board.getTeam(p.getName());
            t.setPrefix(prefix);
            t.addPlayer(p);
        }
        for (Player o : Bukkit.getOnlinePlayers()) {
            o.setScoreboard(board);
        }
    }

    public void setTag(Player p, String suffix, boolean notUsed)
            throws Exception {
        suffix = ChatColor.translateAlternateColorCodes('&', suffix);
        if (suffix.length() > 16) {
            suffix = suffix.substring(0, 16);
        }
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team t = board.getTeam(p.getName());
        if (t == null) {
            t = board.registerNewTeam(p.getName());
            t.setSuffix(" " + suffix);
            t.addPlayer(p);
        } else {
            t = board.getTeam(p.getName());
            t.setSuffix(" " + suffix);
            t.addPlayer(p);
        }
        for (Player o : Bukkit.getOnlinePlayers()) {
            o.setScoreboard(board);
        }
    }

    public void unregisterTag(Player p)
            throws Exception {
        Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(p).unregister();
    }

    public void unregisterAll()
            throws Exception {
        for (Player o : Bukkit.getOnlinePlayers()) {
            unregisterTag(o);
        }
    }

    public void registerAll(String prefix, String suffix)
            throws Exception {
        for (Player o : Bukkit.getOnlinePlayers()) {
            setTag(o, prefix, " " + suffix);
        }
    }

    public void refresh() {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        for (Player o : Bukkit.getOnlinePlayers()) {
            o.setScoreboard(board);
        }
    }

    public String getPrefix(Player p)
            throws Exception {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team t = board.getTeam(p.getName());
        if ((t != null) && (board.getPlayerTeam(p).getPrefix() != null) && (!board.getPlayerTeam(p).getPrefix().isEmpty())) {
            return board.getPlayerTeam(p).getPrefix();
        }
        return "";
    }

    public String getSuffix(Player p)
            throws Exception {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team t = board.getTeam(p.getName());
        if ((t != null) && (board.getPlayerTeam(p).getSuffix() != null) && (!board.getPlayerTeam(p).getSuffix().isEmpty())) {
            return board.getPlayerTeam(p).getSuffix();
        }
        return "";
    }
}