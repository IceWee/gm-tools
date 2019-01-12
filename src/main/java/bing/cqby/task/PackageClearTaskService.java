package bing.cqby.task;

import bing.cqby.service.PlayerItemService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * 角色背包清空异步任务服务
 *
 * @author: bing
 * @date: 2019-01-05
 */
public class PackageClearTaskService extends Service<Void> {

    private PlayerItemService playerItemService = PlayerItemService.getInstance();

    private Long characterId;

    public PackageClearTaskService(Long characterId) {
        this.characterId = characterId;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                playerItemService.clearPackage(characterId);
                return null;
            }
        };
    }

}
