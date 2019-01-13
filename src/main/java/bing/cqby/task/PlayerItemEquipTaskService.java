package bing.cqby.task;

import bing.cqby.domain.Equipment;
import bing.cqby.service.PlayerItemService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * 角色装备穿戴异步任务服务
 *
 * @author: bing
 * @date: 2019-01-05
 */
public class PlayerItemEquipTaskService extends Service<Void> {

    private PlayerItemService playerItemService = PlayerItemService.getInstance();

    private Long characterId;
    private Equipment equipment;

    public PlayerItemEquipTaskService(Long characterId, Equipment equipment) {
        this.characterId = characterId;
        this.equipment = equipment;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                playerItemService.equip(characterId, equipment);
                return null;
            }
        };
    }

}
