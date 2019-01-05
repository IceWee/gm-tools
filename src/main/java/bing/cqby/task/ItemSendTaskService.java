package bing.cqby.task;

import bing.cqby.domain.Item;
import bing.cqby.domain.Result;
import bing.cqby.service.PlayerItemService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * 物品发送异步任务服务
 *
 * @author: bing
 * @date: 2019-01-05
 */
public class ItemSendTaskService extends Service<Result> {

    private PlayerItemService playerItemService = PlayerItemService.getInstance();

    private Long characterId;
    private Item item;

    public ItemSendTaskService(Long characterId, Item item) {
        this.characterId = characterId;
        this.item = item;
    }

    @Override
    protected Task<Result> createTask() {
        return new Task<Result>() {
            @Override
            protected Result call() throws Exception {
                return playerItemService.sendItem(characterId, item);
            }
        };
    }

}
