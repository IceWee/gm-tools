package bing.cqby.service;

import bing.cqby.common.Constants;
import bing.cqby.common.EquipmentType;
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
     * @throws Exception
     */
    public Integer getOneEmptySlot(Long characterId) throws Exception {
        Integer slot = null;
        List<Integer> allSlots = getEmptySlots(characterId);
        if (!allSlots.isEmpty()) {
            slot = allSlots.get(0);
        }
        return slot;
    }

    /**
     * 获取角色全部未使用背包空间
     *
     * @param characterId
     * @return
     * @throws Exception
     */
    public List<Integer> getEmptySlots(Long characterId) throws Exception {
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
        return allSlots;
    }

    /**
     * 发送物品到游戏人物背包
     *
     * @param characterId
     * @param item
     * @return
     * @throws Exception
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
     * 批量发送物品到角色背包
     *
     * @param characterId
     * @param items
     * @return
     * @throws Exception
     */
    public Result sendItems(Long characterId, List<Item> items) throws Exception {
        Result result = new Result();
        int itemCount = items.size();
        List<Integer> emptySlots = getEmptySlots(characterId);
        if (itemCount > emptySlots.size()) {
            result.setSuccess(false);
            result.setMessage("物品发送失败，背包空间不足，剩余空间为：" + emptySlots.size());
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append("INSERT [game].playeritems(ownerguid, entry, slot, count, randomsuffix, enchantments, strengthen_level, lucy, btaoattack, bhysicsattack, bspellattack, bhysicsguard, bspellguard, text)");
            builder.append(" VALUES(?, ?, ?, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, '')");
            List<Object[]> args = new ArrayList<>();
            Item item;
            for (int i = 0; i < items.size(); i++) {
                item = items.get(i);
                Integer slot = emptySlots.get(i);
                Object[] params = new Object[3];
                params[0] = characterId;
                params[1] = item.getEntry();
                params[2] = slot;
                args.add(params);
            }
            String sql = SQLUtils.replaceDBNames(builder.toString());
            DBHelper.getInstance().executeBatch(sql, args);
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
        builder.append(" [game].playeritems.btaoattack zhulingLevel, [game].playeritems.flags, [game].items.class clazz, [game].items.subclass subClass,");
        builder.append(" [game].items.allowableclass allowableClass, [game].items.allowablerace allowableRace, [game].items.inventorytype inventoryType");
        builder.append(" FROM [game].playeritems LEFT OUTER JOIN [game].items ON [game].playeritems.entry = [game].items.entry");
        builder.append(" WHERE [game].playeritems.ownerguid = ?");
        builder.append(" ORDER BY [game].playeritems.slot");
        String sql = SQLUtils.replaceDBNames(builder.toString());
        List<Equipment> equipments = DBHelper.getInstance().query(Equipment.class, sql, new Long[]{characterId});
        return equipments;
    }

    /**
     * 更新角色装备信息
     *
     * @param equipment
     * @throws Exception
     */
    public void update(Equipment equipment) throws Exception {
        Object[] args = new Object[]{equipment.getStrengthenLevel(), equipment.getZhulingLevel(), equipment.getFlags(), equipment.getGuid()};
        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE [game].playeritems SET");
        builder.append(" strengthen_level = ?,");
        builder.append(" btaoattack = ?,");
        builder.append(" flags = ?");
        builder.append(" WHERE guid = ?");
        String sql = SQLUtils.replaceDBNames(builder.toString());
        DBHelper.getInstance().execute(sql, args);
    }

    /**
     * 一键顶级（强化和注灵）
     *
     * @param characterId
     * @throws Exception
     */
    public void topLevel(Long characterId) throws Exception {
        Long[] args = new Long[]{characterId};
        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE [game].playeritems SET strengthen_level = ").append(Constants.STRENGTHEN_UNIT * 12).append(",");
        builder.append(" btaoattack = 12");
        builder.append(" WHERE [game].playeritems.ownerguid = ?");
        builder.append(" AND [game].playeritems.slot <= 21 AND [game].playeritems.slot NOT IN(13, 19)"); // 13为血符，19为时装，不能强化不能注灵需排除
        String sql = SQLUtils.replaceDBNames(builder.toString());
        DBHelper.getInstance().execute(sql, args);
    }

    /**
     * 清空背包
     *
     * @param characterId
     * @throws Exception
     */
    public void clearPackage(Long characterId) throws Exception {
        Long[] args = new Long[]{characterId};
        StringBuilder builder = new StringBuilder();
        builder.append("DELETE FROM [game].playeritems");
        builder.append(" WHERE [game].playeritems.slot > 21");
        builder.append(" AND [game].playeritems.ownerguid = ?");
        String sql = SQLUtils.replaceDBNames(builder.toString());
        DBHelper.getInstance().execute(sql, args);
    }

    /**
     * 穿戴装备
     * 手镯和戒指比较特殊，优先穿戴到未戴的位置上，如果都有则戴在左侧
     *
     * @param characterId
     * @param equipment
     */
    public void equip(Long characterId, Equipment equipment) throws Exception {
        Integer slot = equipment.getSlotByType();
        Long guid = getEquipmentGuid(characterId, slot);
        EquipmentType type = equipment.getType();
        if (EquipmentType.BRACELET == type) { // 手镯
            slot = Constants.EquipmentSlot.BRACELET_LEFT; // 检查左手镯是否佩戴
            guid = getEquipmentGuid(characterId, slot);
            if (guid != null) {
                slot = Constants.EquipmentSlot.BRACELET_RIGHT; // 检查右手镯是否佩戴
                guid = getEquipmentGuid(characterId, slot);
            }
        } else if (EquipmentType.RING == type) { // 戒指
            slot = Constants.EquipmentSlot.RING_LEFT; // 检查左戒指是否佩戴
            guid = getEquipmentGuid(characterId, slot);
            if (guid != null) {
                slot = Constants.EquipmentSlot.RING_RIGHT; // 检查右戒指是否佩戴
                guid = getEquipmentGuid(characterId, slot);
            }
        }
        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE [game].playeritems SET slot = ? WHERE [game].playeritems.guid = ?");
        String sql = SQLUtils.replaceDBNames(builder.toString());
        List<Object[]> args = new ArrayList<>();
        Object[] params = new Object[2];
        params[0] = slot;
        params[1] = equipment.getGuid();
        args.add(params);
        if (guid != null) { // 已戴装备则和当前装备的slot互换
            params = new Object[2];
            params[0] = equipment.getSlot();
            params[1] = guid;
            args.add(params);
        }
        DBHelper.getInstance().executeBatch(sql, args);
    }

    /**
     * 获取制定位置的装备ID
     *
     * @param characterId
     * @param slot
     * @return 如果装备位置未戴任何装备则返回NULL
     * @throws Exception
     */
    private Long getEquipmentGuid(Long characterId, Integer slot) throws Exception {
        Long guid = null;
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT [game].playeritems.guid");
        builder.append(" FROM [game].playeritems");
        builder.append(" WHERE [game].playeritems.ownerguid = ?");
        builder.append(" AND slot = ?");
        String sql = SQLUtils.replaceDBNames(builder.toString());
        List<Map<String, Object>> list = DBHelper.getInstance().query(sql, new Object[]{characterId, slot});
        if (!list.isEmpty()) {
            Map<String, Object> map = list.get(0);
            guid = NumberUtils.toLong(Objects.toString(map.get("guid")));
        }
        return guid;
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
