package bing.cqby.task;

import bing.cqby.domain.Equipment;
import bing.cqby.service.PlayerItemService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

/**
 * 角色装备加载异步任务服务
 *
 * @author: bing
 * @date: 2019-01-05
 */
public class PlayerItemLoadTaskService extends Service<List<Equipment>> {

    private PlayerItemService playerItemService = PlayerItemService.getInstance();

    private Long characterId;

    public PlayerItemLoadTaskService(Long characterId) {
        this.characterId = characterId;
    }

    @Override
    protected Task<List<Equipment>> createTask() {
        return new Task<List<Equipment>>() {
            @Override
            protected List<Equipment> call() throws Exception {
                return playerItemService.query(characterId);
            }
        };
    }

}
