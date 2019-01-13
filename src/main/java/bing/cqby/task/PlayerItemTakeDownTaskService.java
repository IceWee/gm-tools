package bing.cqby.task;

import bing.cqby.domain.Equipment;
import bing.cqby.domain.Result;
import bing.cqby.service.PlayerItemService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * 角色装备取下异步任务服务
 *
 * @author: bing
 * @date: 2019-01-05
 */
public class PlayerItemTakeDownTaskService extends Service<Result> {

    private PlayerItemService playerItemService = PlayerItemService.getInstance();

    private Long characterId;
    private Equipment equipment;

    public PlayerItemTakeDownTaskService(Long characterId, Equipment equipment) {
        this.characterId = characterId;
        this.equipment = equipment;
    }

    @Override
    protected Task<Result> createTask() {
        return new Task<Result>() {
            @Override
            protected Result call() throws Exception {
                return playerItemService.takeDown(characterId, equipment);
            }
        };
    }

}
