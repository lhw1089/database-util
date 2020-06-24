package com.jean.database.redis.view;


import com.jean.database.api.BaseTask;
import com.jean.database.api.TaskManger;
import com.jean.database.api.utils.DialogUtil;
import com.jean.database.api.utils.FxmlUtils;
import com.jean.database.api.utils.ImageUtils;
import com.jean.database.api.utils.StringUtils;
import com.jean.database.api.view.treeitem.BaseTreeItem;
import com.jean.database.redis.RedisConnectionConfiguration;
import com.jean.database.redis.RedisConstant;
import com.jean.database.redis.RedisDatabaseTabController;
import com.jean.database.redis.RedisObjectTabController;
import com.jean.database.redis.model.RedisKey;
import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.ScanCursor;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jinshubao
 */
public class RedisDatabaseItem extends BaseTreeItem<String> {

    private final int database;
    private final RedisConnectionConfiguration connectionConfiguration;
    private final ContextMenu contextMenu;
    private final RedisObjectTabController objectTabController;

    private RedisDatabaseTabController databaseTabController;

    public RedisDatabaseItem(int database,
                             RedisConnectionConfiguration connectionConfiguration,
                             RedisObjectTabController objectTabController) {
        super("db" + database);
        this.connectionConfiguration = connectionConfiguration;
        this.database = database;
        this.objectTabController = objectTabController;

        MenuItem refreshItem = new MenuItem("刷新", ImageUtils.createImageView("/image/redis/x16/refresh.png"));
        refreshItem.setOnAction(event -> {
        });

        MenuItem flushItem = new MenuItem("清空", ImageUtils.createImageView("/image/redis/x16/delete.png"));
        flushItem.setOnAction(event -> {
        });

        contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(refreshItem, flushItem);

    }


    @Override
    public void select() {
        TaskManger.execute(new RedisKeysTask());
    }

    @Override
    public ContextMenu getContextMenu() {
        return this.contextMenu;
    }


    private class RedisKeysTask extends BaseTask<List<RedisKey>> {

        @Override
        protected void scheduled() {
            super.scheduled();
        }

        @Override
        protected List<RedisKey> call() throws Exception {
            try (StatefulRedisConnection<byte[], byte[]> connection = connectionConfiguration.getConnection()) {
                RedisCommands<byte[], byte[]> commands = connection.sync();
                commands.select(database);
                Long size = commands.dbsize();
                ScanCursor scanCursor = ScanCursor.INITIAL;
                List<RedisKey> value = new ArrayList<>();
                ScanArgs limit = ScanArgs.Builder.limit(RedisConstant.KEY_SCAN_SIZE);
                do {
                    KeyScanCursor<byte[]> cursor = commands.scan(scanCursor, limit);
                    scanCursor = ScanCursor.of(cursor.getCursor());
                    scanCursor.setFinished(cursor.isFinished());
                    List<byte[]> keys = cursor.getKeys();
                    List<RedisKey> collect = keys.stream().map(key -> {
                        String type = commands.type(key);
                        Long ttl = commands.ttl(key);
                        return new RedisKey(connectionConfiguration, database, key, type, ttl, size);
                    }).collect(Collectors.toList());
                    value.addAll(collect);
                    updateProgress(value.size(), size);
                    if (isCancelled()) {
                        break;
                    }
                } while (!scanCursor.isFinished());
                value.sort(Comparator.comparing(o -> StringUtils.byteArrayToString(o.getKey())));
                return value;
            }
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            List<RedisKey> value = getValue();
            if (databaseTabController == null) {
                Callback<Class<?>, Object> factory = RedisDatabaseTabController.getFactory(RedisDatabaseItem.this.getValue(), objectTabController);
                try {
                    FxmlUtils.LoadFxmlResult loadFxmlResult = FxmlUtils.loadFxml("/fxml/redis-db-tab.fxml", factory);
                    databaseTabController = (RedisDatabaseTabController) loadFxmlResult.getController();
                    databaseTabController.updateKeyTableView(value);
                } catch (IOException e) {
                    DialogUtil.error(e);
                }
            } else {
                databaseTabController.updateKeyTableView(value);
            }
            databaseTabController.selected();
        }
    }
}
