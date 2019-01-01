package bing.cqby.task;

import bing.cqby.model.Item;
import bing.cqby.model.Page;
import bing.cqby.service.ItemService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

    private StringProperty itemProperty = new SimpleStringProperty();
    private ObjectProperty<Page<Item>> pageProperty = new SimpleObjectProperty<>();

    private final String getItemName() {
        return itemProperty.get();
    }

    public final void setItemName(String itemName) {
        itemProperty.set(itemName);
    }

    private final Page<Item> getPage() {
        return pageProperty.get();
    }

    public final void setPage(Page<Item> page) {
        pageProperty.set(page);
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                itemService.query(getPage(), getItemName());
                return null;
            }
        };
    }

}
