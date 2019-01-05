package bing.cqby.service;

import bing.cqby.common.Constants;
import bing.cqby.domain.Equipment;
import bing.cqby.domain.Item;
import bing.cqby.domain.Result;
import bing.cqby.util.DBHelper;
import bing.cqby.util.SQLUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 游戏人物与物品关系服务
 *
 * @author: bing
 * @date: 2019-01-05
 */
@Slf4j
public class PlayerItemService {

    private static PlayerItemService instance = new PlayerItemService();

    private PlayerItemService() {
        super();
    }

    public static PlayerItemService getInstance() {
        return instance;
    }

    /**
     * 获取角色一个背包空间
     *
     * @param characterId
     * @return 返回null时说明空间已满
     */
    public Integer getOneEmptySlot(Long characterId) throws Exception {
        Integer slot = null;
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT slot FROM [game].playeritems");
        builder.append(" WHERE ownerguid = ? AND slot > 22"); // 空闲背包空间从23开始
        builder.append(" ORDER BY slot");
        String sql = SQLUtils.replaceDBNames(builder.toString());
        List<Map<String, Object>> list = DBHelper.getInstance().query(sql, new Long[]{characterId});
        List<Integer> usedSlots = list.stream().map(stringObjectMap -> {
            Object value = stringObjectMap.get("slot");
            return NumberUtils.toInt(Objects.toString(value));
        }).collect(Collectors.toList());
        List<Integer> allSlots = getSlots();
        allSlots.removeAll(usedSlots);
        if (!allSlots.isEmpty()) {
            slot = allSlots.get(0);
        }
        return slot;
    }

    /**
     * 发送物品到游戏人物背包
     *
     * @param characterId
     * @param item
     * @return
     */
    public Result sendItem(Long characterId, Item item) throws Exception {
        Result result = new Result();
        Integer emptySlot = getOneEmptySlot(characterId);
        if (emptySlot == null) {
            result.setSuccess(false);
            result.setMessage("物品发送失败，背包空间已满");
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append("INSERT [game].playeritems(ownerguid, entry, slot, count, randomsuffix, enchantments, strengthen_level, lucy, btaoattack, bhysicsattack, bspellattack, bhysicsguard, bspellguard, text)");
            builder.append(" VALUES(");
            builder.append(characterId).append(", ");
            builder.append(item.getEntry()).append(", ");
            builder.append(emptySlot).append(", ");
            builder.append("1, 0, 0, 0, 0, 0, 0, 0, 0, 0, '')");
            String sql = SQLUtils.replaceDBNames(builder.toString());
            DBHelper.getInstance().execute(sql);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 查询角色装备列表
     *
     * @param characterId
     * @return
     * @throws Exception
     */
    public List<Equipment> query(Long characterId) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT [game].playeritems.guid, [game].playeritems.slot, [game].items.name1 equipmentName, [game].playeritems.strengthen_level strengthenLevel,");
        builder.append(" [game].playeritems.btaoattack zhulingLevel, [game].playeritems.flags");
        builder.append(" FROM [game].playeritems LEFT OUTER JOIN [game].items ON [game].playeritems.entry = [game].items.entry");
        builder.append(" WHERE [game].playeritems.ownerguid = ?");
        builder.append(" AND [game].playeritems.slot <= 21 AND [game].playeritems.slot NOT IN(13, 19)"); // 13为血符，不能强化不能注灵需排除
        builder.append(" ORDER BY [game].playeritems.slot");
        String sql = SQLUtils.replaceDBNames(builder.toString());
        List<Equipment> equipments = DBHelper.getInstance().query(Equipment.class, sql, new Long[]{characterId});
        return equipments;
    }

    /**
     * 获取角色背包全部序号
     *
     * @return
     */
    private List<Integer> getSlots() {
        List<Integer> slots = new ArrayList<>();
        for (int i = Constants.Slot.MIN; i <= Constants.Slot.MAX; i++) {
            slots.add(i);
        }
        return slots;
    }

}
