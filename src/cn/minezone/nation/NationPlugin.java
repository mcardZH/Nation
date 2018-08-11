package cn.minezone.nation;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * @author mcard
 */
public class NationPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        //启用授权管理器
        String url = "https://mcardzh.000webhostapp.com/verify.php";
        AdvancedLicense al = new AdvancedLicense(getConfig().getString("cdk"), url, this);
        boolean success = al.register();
        //启用插件使用记录器
        Metrics metrics = new Metrics(this);
        // 增加自定义图表
        metrics.addCustomChart(new Metrics.SimplePie("license", () -> {
            if (success) {
                return "成功授权";
            } else {
                return "授权失败";
            }
        }));
        //转移监听器
        Bukkit.getPluginCommand("nation").setExecutor(new CommandHandler(this));
        //启用Runnable                                                                     一小时一次
        new RankUpdateRunnable().runTaskTimerAsynchronously(this, 0, 20 * 60 * 60);
        new RewardUpdateRunnable(this).runTaskTimerAsynchronously(this, 0, 20 * 60 * 30);
        //new UpdatePlayerTag().runTaskTimer(this, 0, 20);
        //update
        NationAPI.updateRank();
        //register listener
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        //hook
        new PlaceholderHook(this).hook();
    }


}
