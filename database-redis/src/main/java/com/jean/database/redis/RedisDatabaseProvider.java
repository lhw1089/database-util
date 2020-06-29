package com.jean.database.redis;

import com.jean.database.api.IDatabaseProvider;
import com.jean.database.api.ViewManger;
import com.jean.database.api.utils.DialogUtil;
import com.jean.database.api.utils.FxmlUtils;
import com.jean.database.api.utils.ImageUtils;
import com.jean.database.redis.view.RedisServerItem;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.util.Callback;

import java.util.Locale;

public class RedisDatabaseProvider implements IDatabaseProvider {

    private static final String NAME = "Redis";

    private final RedisConnectionConfiguration defaultCollectConfiguration;

    public RedisDatabaseProvider() {
        String host = "127.0.0.1";
        this.defaultCollectConfiguration
                = new RedisConnectionConfiguration("Redis[" + host + "]", host, 6379, null);
    }

    @Override
    public void init() {
        MenuItem menuItem = new MenuItem(getName(), ImageUtils.createImageView("/redis/redis.png"));
        menuItem.setOnAction(event -> {
            RedisConnectionConfiguration configuration = getConnectionConfiguration();
            if (configuration != null) {
                ViewManger.getViewContext().addDatabaseItem(new RedisServerItem(configuration));
            }
        });
        ViewManger.getViewContext().addConnectionMenus(menuItem);
    }

    @Override
    public String getIdentifier() {
        return NAME;
    }

    @Override
    public String getName() {
        return NAME;
    }

    public void close() {

    }

    private RedisConnectionConfiguration getConnectionConfiguration() {
        try {
            FxmlUtils.LoadFxmlResult fxmlResult = FxmlUtils.loadFxml("/fxml/redis-conn-cfg.fxml", null, Locale.SIMPLIFIED_CHINESE);
            RedisConnectionConfigurationController cfgController = (RedisConnectionConfigurationController) fxmlResult.getController();
            cfgController.setValue(defaultCollectConfiguration);
            Callback<ButtonType, RedisConnectionConfiguration> callback = buttonType -> {
                if (buttonType == ButtonType.OK) {
                    return cfgController.getValue();
                }
                return null;
            };
            return DialogUtil.customizeDialog("New Redis connection", fxmlResult.getParent(), callback).orElse(null);
        } catch (Exception e) {
            DialogUtil.error(e);
        }
        return null;
    }

    @Override
    public int getOrder() {
        return 30000;
    }
}
