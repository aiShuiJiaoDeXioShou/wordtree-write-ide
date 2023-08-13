package lh.wordtree.model.plugin;

import cn.hutool.core.thread.ThreadUtil;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import lh.wordtree.plugin.WTPlugin;
import lh.wordtree.service.plugin.WTPluginService;

public class PluginController {

    private PluginView view;

    public PluginController(PluginView view) {
        this.view = view;
        this.install();
        this.market();
    }


    // 获取已经安装的插件
    private void install() {
        ThreadUtil.execAsync(() -> {
            var pluginMarketList = new ListView<PluginView.MarketPlugin>();
            Platform.runLater(() -> view.install.setContent(pluginMarketList));
            var wtPlugins = WTPluginService.pluginService.getPlugins();
            for (WTPlugin wtPlugin : wtPlugins) {
                var marketPlugin = new PluginView.MarketPlugin(wtPlugin, true);
                Platform.runLater(() -> pluginMarketList.getItems().add(marketPlugin));
            }
            showInfo(pluginMarketList);
        });
    }

    // 获取插件市场的插件
    private void market() {
        ThreadUtil.execAsync(() -> {
            var marketPlugin = new ListView<PluginView.MarketPlugin>();
            marketPlugin.prefWidth(view.tabW);
            Platform.runLater(() -> view.pluginMarket.setContent(marketPlugin));
            showInfo(marketPlugin);
        });
    }

    private void showInfo(ListView<PluginView.MarketPlugin> mp) {
        mp.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    view.setCenter(new PluginView.PluginInfo(newValue.getWtPlugin()));
                });
    }


}
