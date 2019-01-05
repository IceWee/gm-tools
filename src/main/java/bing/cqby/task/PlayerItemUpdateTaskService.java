package bing.cqby.task;

import bing.cqby.domain.Equipment;
import bing.cqby.service.PlayerItemService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * 角色装备修改异步任务服务
 *
 * @author: bing
 * @date: 2019-01-05
 */
public class PlayerItemUpdateTaskService extends Service<Void> {

    private PlayerItemService playerItemService = PlayerItemService.getInstance();

    private Equipment equipment;

    public PlayerItemUpdateTaskService(Equipment equipment) {
        this.equipment = equipment;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                playerItemService.update(equipment);
                return null;
            }
        };
    }

}
