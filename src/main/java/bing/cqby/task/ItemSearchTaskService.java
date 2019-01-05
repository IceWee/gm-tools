package bing.cqby.task;

import bing.cqby.domain.Item;
import bing.cqby.domain.Page;
import bing.cqby.service.ItemService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * 物品查询异步任务服务
 *
 * @author: bing
 * @date: 2019-01-01
 */
public final class ItemSearchTaskService extends Service<Void> {

    private ItemService itemService = ItemService.getInstance();

    private String itemName;
    private Page<Item> page;

    public ItemSearchTaskService(String itemName, Page<Item> page) {
        this.itemName = itemName;
        this.page = page;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                itemService.query(page, itemName);
                return null;
            }
        };
    }

}
