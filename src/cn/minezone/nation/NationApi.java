package cn.minezone.nation;

import com.github.shawhoi.pointcommand.Main;
import com.github.shawhoi.pointcommand.PointCommandAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author mcard
 */
public class NationApi {

    private static Plugin plugin;
    private static long lastUpdateTime = System.currentTimeMillis();
    private static Map<String, List<NationPlayer>> rank = new HashMap<>();
    private static YamlConfiguration main;
    private static final String list = "player-list";
    private static PointCommandAPI api;

    private static void update() {
        lastUpdateTime = System.currentTimeMillis();

        for (String s : getNationList()) {
            //读入“国家名.yml”
            File f = new File(plugin.getDataFolder(), s + ".yml");
            if (!f.exists()) {
                try {
                    //不存在就创建
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
            YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
            List<NationPlayer> rank = new ArrayList<>();
            for (String player : config.getStringList(list)) {
                rank.add(new NationPlayer(Bukkit.getOfflinePlayer(player), s, api.getPlayerPoint(player)));
            }
            //对数组进行排序
            Arrays.sort(rank.toArray(new NationPlayer[rank.size()]));
            //加入到Map中
            NationApi.rank.put(s, rank);
        }
    }

    private static void initialize() {
        if (plugin == null) {
            plugin = Bukkit.getPluginManager().getPlugin("Nation");
        }
        if (main == null) {
            main = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
        }
        if (api == null) {
            api = new PointCommandAPI(Main.class.cast(Bukkit.getPluginManager().getPlugin("PointCommand")));
        }
    }

    /**
     * 获取排名列表（非实时）
     *
     * @return 最后一次更新时的排名
     */
    public static Map<String, List<NationPlayer>> getRank() {
        initialize();
        return rank;
    }

    /**
     * 获取最后一次更新数据表的时间
     *
     * @return 返回最后一次更新时System.currentTimeMillis()取回的时间
     */
    public static long getLastUpdateTime() {
        initialize();
        return lastUpdateTime;
    }

    /**
     * 更新数据表（同步方法，消耗大量资源）
     */
    public static void updateRank() {
        initialize();
        update();
    }

    /**
     * 异步更新数据表
     *
     * @param hook 更新完成时调用里面的finish方法（更新完成的下一tick同步调用）
     */
    public static void updateRankAsy(UpdateFinishHook hook) {
        initialize();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            update();
            Bukkit.getScheduler().runTask(plugin, () -> {
                if (hook != null) {
                    hook.finish();
                }
            });
        });
    }


    /**
     * 获取玩家所在国家
     *
     * @param player 玩家
     * @return 国家名
     */
    public static String getPlayerNation(OfflinePlayer player) {
        initialize();
        for (String s : getNationList()) {
            File f = new File(plugin.getDataFolder(), s + ".yml");
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (YamlConfiguration.loadConfiguration(f).getStringList(list).contains(player.getName())) {
                return s;
            }
        }
        setPlayerNation(player, getPlayerDefaultNation());
        return getPlayerDefaultNation();
    }

    /**
     * 默认玩家国家
     */
    public static String getPlayerDefaultNation() {
        initialize();
        return main.getStringList("nations").get(0);
    }

    /**
     * 获取玩家阵营排名
     *
     * @param player 玩家
     * @return 在阵营中的排名
     */
    @Deprecated
    public static int getPlayerRankInNation(OfflinePlayer player) {
        initialize();
        for (int i = 0; i < getRank().get(getPlayerNation(player)).size(); i++) {
            //判断名字是否相等
            if (getRank().get(getPlayerNation(player)).get(i).getPlayer().getName().equals(player.getName())) {
                return i + 1;
            }
        }
        return -1;
    }

    /**
     * 获取玩家在世界中的排名
     *
     * @param player 玩家
     * @return 世界排名
     */
    @Deprecated
    public static int getPlayerRankInWorld(OfflinePlayer player) {
        initialize();
        int bigger = 0;
        int playerPoint = api.getPlayerPoint(player.getName());
        for (String key : getRank().keySet()) {
            //遍历所有阵营中的所有人
            for (NationPlayer np : getRank().get(key)) {
                if (np.getPoint() > playerPoint) {
                    bigger++;
                }
            }
        }
        return bigger + 1;
    }

    /**
     * 设置玩家国家
     *
     * @param player 玩家
     * @param name   国家名
     */
    public static void setPlayerNation(OfflinePlayer player, String name) {
        initialize();
        //remove
        File f = new File(plugin.getDataFolder(), getPlayerNation(player));
        if (f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
        List<String> pl = config.getStringList(list);
        pl.remove(player.getName());
        config.set(list, pl);
        try {
            config.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //add
        f = new File(plugin.getDataFolder(), name + ".yml");
        if (f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        config = YamlConfiguration.loadConfiguration(f);
        pl = config.getStringList(list);
        pl.add(player.getName());
        config.set(list, pl);
        try {
            config.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过国家排名获取玩家
     *
     * @param name 国家名
     * @param rank 排名
     * @return 所对应的玩家
     */
    @Deprecated
    public static OfflinePlayer getPlayerByRankInNation(String name, int rank) {
        initialize();
        return getRank().get(name).get(rank - 1).getPlayer();
    }

    /**
     * 通过世界排名获取玩家
     *
     * @param rank 排名
     * @return 对应的玩家
     */
    @Deprecated
    public static OfflinePlayer getPlayerByRankInWorld(int rank) {
        initialize();
        String player;
        List<NationPlayer> full = new ArrayList<>();
        for (String s : getRank().keySet()) {
            full.addAll(getRank().get(s));
        }
        return full.get(rank - 1).getPlayer();
    }

    /**
     * 获取国家在所有国家中的排名
     *
     * @param name 国家名称
     * @return 国家排名
     */
    public static int getNationRank(String name) {
        int bigger = 0;
        int np = getNationPoint(name);
        for (String s : getNationList()) {
            if (getNationPoint(s) > np) {
                bigger++;
            }
        }
        return bigger + 1;
    }

    /**
     * 获取国家总点数
     *
     * @param name 国家名称
     * @return 国家所有成员点数和
     */
    public static int getNationPoint(String name) {
        int all = 0;
        for (NationPlayer np : getRank().get(name)) {
            all += np.getPoint();
        }
        return all;
    }

    /**
     * 获取全部国家列表
     *
     * @return 国家名集合
     */
    public static List<String> getNationList() {
        return main.getStringList("nation");
    }

    /**
     * 获取国家内玩家列表
     *
     * @param name 国家名
     * @return
     */
    public static List<OfflinePlayer> getNationPlayerList(String name) {
        File f = new File(plugin.getDataFolder(), name + ".yml");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }
        List<OfflinePlayer> players = new ArrayList<>();
        for (String s : YamlConfiguration.loadConfiguration(f).getStringList(list)) {
            players.add(Bukkit.getOfflinePlayer(s));
        }
        return players;
    }


}
