package bing.cqby.task;

import bing.cqby.domain.Config;
import bing.cqby.util.DBHelper;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据库连接异步任务服务
 *
 * @author: bing
 * @date: 2018-12-31
 */
@Slf4j
public final class DatabaseConnectTaskService extends Service<Boolean> {

    private ObjectProperty<Config> configProperty = new SimpleObjectProperty<>();

    public DatabaseConnectTaskService() {
        super();
    }

    private final Config getConfig() {
        return configProperty.get();
    }

    public final void setConfig(Config config) {
        configProperty.setValue(config);
    }

    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {

            @Override
            protected Boolean call() throws Exception {
                DBHelper.getInstance().init(getConfig());
                log.info("数据库连接成功...");
                return true;
            }

        };
    }

}
