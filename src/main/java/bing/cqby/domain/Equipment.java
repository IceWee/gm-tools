package bing.cqby.domain;

import bing.cqby.common.Constants;
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
     * 获取装备位置名称
     *
     * @return
     */
    public String getSlotText() {
        switch (slot) {
            case 0:
                slotText = "头盔";
                break;
            case 1:
                slotText = "项链";
                break;
            case 2:
                slotText = "手镯（左）";
                break;
            case 3:
                slotText = "手镯（右）";
                break;
            case 4:
                slotText = "戒指（左）";
                break;
            case 5:
                slotText = "戒指（右）";
                break;
            case 6:
                slotText = "腰带";
                break;
            case 7:
                slotText = "靴子";
                break;
            case 8:
                slotText = "武器";
                break;
            case 9:
                slotText = "衣服";
                break;
            case 10:
                slotText = "勋章";
                break;
            case 11:
                slotText = "护盾";
                break;
            case 12:
                slotText = "宝石";
                break;
            case 13:
                slotText = "血符";
                break;
            case 14:
                slotText = "魂珠";
                break;
            case 15:
                slotText = "面甲";
                break;
            case 16:
                slotText = "吊坠";
                break;
            case 17:
                slotText = "护肩";
                break;
            case 18:
                slotText = "护膝";
                break;
            case 21:
                slotText = "羽翼";
                break;
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

}
