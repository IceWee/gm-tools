package bing.cqby.task;

import bing.cqby.domain.Item;
import bing.cqby.domain.Result;
import bing.cqby.service.PlayerItemService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

/**
 * 物品批量发送异步任务服务
 *
 * @author: bing
 * @date: 2019-01-05
 */
public class ItemBatchSendTaskService extends Service<Result> {

    private PlayerItemService playerItemService = PlayerItemService.getInstance();

    private Long characterId;
    private List<Item> items;

    public ItemBatchSendTaskService(Long characterId, List<Item> items) {
        this.characterId = characterId;
        this.items = items;
    }

    @Override
    protected Task<Result> createTask() {
        return new Task<Result>() {
            @Override
            protected Result call() throws Exception {
                return playerItemService.sendItems(characterId, items);
            }
        };
    }

}
