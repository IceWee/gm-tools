package bing.cqby.task;

import bing.cqby.service.PlayerItemService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * 角色装备一键满级异步任务服务
 *
 * @author: bing
 * @date: 2019-01-05
 */
public class PlayerItemTopTaskService extends Service<Void> {

    private PlayerItemService playerItemService = PlayerItemService.getInstance();

    private Long characterId;

    public PlayerItemTopTaskService(Long characterId) {
        this.characterId = characterId;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                playerItemService.topLevel(characterId);
                return null;
            }
        };
    }

}
