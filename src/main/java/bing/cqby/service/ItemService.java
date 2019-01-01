package bing.cqby.service;

import bing.cqby.common.DBHelper;
import bing.cqby.model.Item;
import bing.cqby.model.Page;
import bing.cqby.util.SQLUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 物品服务
 *
 * @author: bing
 * @date: 2019-01-01
 */
@Slf4j
public final class ItemService {

    private static ItemService instance = new ItemService();

    private ItemService() {
        super();
    }

    public static ItemService getInstance() {
        return instance;
    }

    /**
     * 物品查询
     *
     * @param page
     * @param itemName
     * @throws Exception
     */
    public void query(Page<Item> page, String itemName) throws Exception {
        String[] args = new String[]{};
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT entry, name1, description FROM [game].items");
        if (StringUtils.isNotBlank(itemName)) {
            builder.append(" WHERE name1 LIKE ?");
            args = new String[]{"%" + itemName + "%"};
        }
        builder.append(" ORDER BY entry");
        String sql = builder.toString();
        sql = SQLUtils.replacePlaceholders(sql);
        DBHelper.getInstance().query(Item.class, page, sql, args);
    }

}
