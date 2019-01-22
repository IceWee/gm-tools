package bing.cqby.domain;

import bing.cqby.common.Constants;
import bing.cqby.common.EquipmentType;
import lombok.Data;

/**
 * 装备
 *
 * @author: bing
 * @date: 2019-01-05
 */
@Data
public class Equipment {

    /**
     * 唯一标示
     */
    private Long guid;

    /**
     * 位置
     */
    private Integer slot;

    /**
     * 位置文本
     */
    private String slotText;

    /**
     * 名称
     */
    private String equipmentName;

    /**
     * 强化等级：65537、131074...786444
     */
    private Integer strengthenLevel;

    /**
     * 强化等级
     */
    private volatile String strengthenText;

    /**
     * 注灵等级
     */
    private Integer zhulingLevel;

    /**
     * 注灵等级
     */
    private String zhulingText;

    /**
     * 绑定标识：0-未绑定；1-绑定
     */
    private Long flags;

    /**
     * 是否绑定
     */
    private volatile String isBound;

    /**
     * 一级分类
     */
    private Integer clazz;

    /**
     * 二级分类
     */
    private Integer subClass;

    /**
     * 穿戴职业
     */
    private Integer allowableClass;

    /**
     * 穿戴性别
     */
    private Integer allowableRace;

    /**
     * 内四件套标识符
     */
    private Integer inventoryType;

    /**
     * 装备类型
     */
    private volatile EquipmentType type;

    /**
     * 获取装备位置名称
     *
     * @return
     */
    public String getSlotText() {
        switch (slot) {
            case Constants.EquipmentSlot.HELM:
                slotText = "头盔";
                break;
            case Constants.EquipmentSlot.NECKLACE:
                slotText = "项链";
                break;
            case Constants.EquipmentSlot.BRACELET_LEFT:
                slotText = "手镯（左）";
                break;
            case Constants.EquipmentSlot.BRACELET_RIGHT:
                slotText = "手镯（右）";
                break;
            case Constants.EquipmentSlot.RING_LEFT:
                slotText = "戒指（左）";
                break;
            case Constants.EquipmentSlot.RING_RIGHT:
                slotText = "戒指（右）";
                break;
            case Constants.EquipmentSlot.BELT:
                slotText = "腰带";
                break;
            case Constants.EquipmentSlot.BOOTS:
                slotText = "靴子";
                break;
            case Constants.EquipmentSlot.WEAPON:
                slotText = "武器";
                break;
            case Constants.EquipmentSlot.ARMOR:
                slotText = "盔甲";
                break;
            case Constants.EquipmentSlot.MEDAL:
                slotText = "勋章";
                break;
            case Constants.EquipmentSlot.SHIELD:
                slotText = "护盾";
                break;
            case Constants.EquipmentSlot.GEMSTONE:
                slotText = "宝石";
                break;
            case Constants.EquipmentSlot.BLOOD_SIGN:
                slotText = "血符";
                break;
            case Constants.EquipmentSlot.SOUL_BEAD:
                slotText = "魂珠";
                break;
            case Constants.EquipmentSlot.VIZARD:
                slotText = "面甲";
                break;
            case Constants.EquipmentSlot.PENDANT:
                slotText = "吊坠";
                break;
            case Constants.EquipmentSlot.SHOULDER_PAD:
                slotText = "护肩";
                break;
            case Constants.EquipmentSlot.KNEELET:
                slotText = "护膝";
                break;
            case Constants.EquipmentSlot.WING:
                slotText = "羽翼";
                break;
            case Constants.EquipmentSlot.FASHION:
                slotText = "时装";
                break;
            default:
                slotText = "背包";
        }
        return slotText;
    }

    /**
     * 获取强化等级文字
     *
     * @return
     */
    public String getStrengthenText() {
        if (strengthenLevel == 0) {
            strengthenText = "未强化";
        } else {
            int level = strengthenLevel / Constants.STRENGTHEN_UNIT;
            if (level > 12) {
                level = 12;
            }
            strengthenText = "强化" + level + "级";
        }
        return strengthenText;
    }

    /**
     * 获取注灵等级文字
     *
     * @return
     */
    public String getZhulingText() {
        if (zhulingLevel == 0) {
            zhulingText = "未注灵";
        } else {
            if (zhulingLevel > 12) {
                zhulingLevel = 12;
            }
            zhulingText = "注灵" + zhulingLevel + "级";
        }
        return zhulingText;
    }

    /**
     * 获取绑定文字
     *
     * @return
     */
    public String getIsBound() {
        if (flags == 0L) {
            isBound = "未绑定";
        } else {
            isBound = "绑定";
        }
        return isBound;
    }

    /**
     * 根据当前装备类型获取位置
     *
     * @return 如果返回null说明当前物品是不能装备到身上的
     */
    public Integer getSlotByType() {
        Integer slot = null;
        switch (getType()) {
            case WEAPON: // 武器
                slot = Constants.EquipmentSlot.WEAPON;
                break;
            case HELM: // 头盔
                slot = Constants.EquipmentSlot.HELM;
                break;
            case ARMOR: // 盔甲
                slot = Constants.EquipmentSlot.ARMOR;
                break;
            case BELT: // 腰带
                slot = Constants.EquipmentSlot.BELT;
                break;
            case BOOTS: // 靴子
                slot = Constants.EquipmentSlot.BOOTS;
                break;
            case NECKLACE: // 项链
                slot = Constants.EquipmentSlot.NECKLACE;
                break;
            case BRACELET: // 手镯
                slot = Constants.EquipmentSlot.BRACELET_LEFT;
                break;
            case RING: // 戒指
                slot = Constants.EquipmentSlot.RING_LEFT;
                break;
            case GEMSTONE: // 宝石
                slot = Constants.EquipmentSlot.GEMSTONE;
                break;
            case MEDAL: // 勋章
                slot = Constants.EquipmentSlot.MEDAL;
                break;
            case SOUL_BEAD: // 魂珠
                slot = Constants.EquipmentSlot.SOUL_BEAD;
                break;
            case SHIELD: // 护盾
                slot = Constants.EquipmentSlot.SHIELD;
                break;
            case BLOOD_SIGN: // 血符
                slot = Constants.EquipmentSlot.BLOOD_SIGN;
                break;
            case WING: // 羽翼
                slot = Constants.EquipmentSlot.WING;
                break;
            case PENDANT: // 吊坠
                slot = Constants.EquipmentSlot.PENDANT;
                break;
            case VIZARD: // 面甲
                slot = Constants.EquipmentSlot.VIZARD;
                break;
            case SHOULDER_PAD: // 护肩
                slot = Constants.EquipmentSlot.SHOULDER_PAD;
                break;
            case KNEELET: // 护膝
                slot = Constants.EquipmentSlot.KNEELET;
                break;
            case FASHION: // 时装
                slot = Constants.EquipmentSlot.FASHION;
                break;
            case OTHER: // 其他
                // 其他物品不能装备
        }
        return slot;
    }

    /**
     * 获取装备类型
     *
     * @return
     */
    public EquipmentType getType() {
        switch (clazz) {
            case 2: // 武器
                type = EquipmentType.WEAPON;
                break;
            case 3:
                switch (subClass) {
                    case 2:
                        switch (inventoryType) {
                            case 2: // 项链
                                type = EquipmentType.NECKLACE;
                                break;
                            case 14: // 吊坠
                                type = EquipmentType.PENDANT;
                        }
                        break;
                    case 3:
                        type = EquipmentType.GEMSTONE; // 宝石
                        break;
                    case 5:
                        type = EquipmentType.MEDAL; // 勋章
                        break;
                    case 7:
                        type = EquipmentType.BLOOD_SIGN; // 血符
                }
                break;
            case 4:
                switch (subClass) {
                    case 1:
                        switch (inventoryType) {
                            case 1: // 头盔
                                type = EquipmentType.HELM;
                                break;
                            case 10: // 面甲
                                type = EquipmentType.VIZARD;
                        }
                        break;
                    case 4:
                        switch (inventoryType) {
                            case 4: // 盔甲
                                type = EquipmentType.ARMOR;
                                break;
                            case 22: // 时装
                                type = EquipmentType.FASHION;
                        }
                        break;
                    case 6:
                        switch (inventoryType) {
                            case 6: // 腰带
                                type = EquipmentType.BELT;
                                break;
                            case 8: // 靴子
                                type = EquipmentType.BOOTS;
                                break;
                            case 15: // 护肩
                                type = EquipmentType.SHOULDER_PAD;
                                break;
                            case 19: // 护膝
                                type = EquipmentType.KNEELET;
                        }
                        break;
                    case 9: // 护盾
                        type = EquipmentType.SHIELD;
                        break;
                    case 11: // 戒指
                        type = EquipmentType.RING;
                        break;
                    case 12: // 手镯
                        type = EquipmentType.BRACELET;
                        break;
                    case 16: // 魂珠
                        type = EquipmentType.SOUL_BEAD;
                        break;
                    case 29: // 羽翼
                        type = EquipmentType.WING;
                }
                break;
            default: // 其他
                type = EquipmentType.OTHER;

        }
        return type;
    }

}
