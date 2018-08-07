package cn.minezone.nation;

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
    }


}
