package bing.cqby.domain;

import bing.cqby.common.Constants;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 游戏角色实体
 *
 * @author: bing
 * @date: 2018-12-31
 */
@Data
public class Character {

    private Long accountId;

    private Long characterId;

    private String characterName;

    /**
     * 角色类型：战圣、法神、道尊
     */
    private String characterType;

    /**
     * 角色等级
     */
    private Integer level;

    /**
     * 内功等级
     */
    private Integer ngLevel;

    /**
     * 转生等级
     */
    private Integer zsLevel;

    /**
     * 武魂等级
     */
    private Integer whLevel;

    /**
     * 金币
     */
    private Integer gold;

    /**
     * 绑定金币
     */
    private Integer boundGold;

    /**
     * 元宝
     */
    private Integer yb;

    /**
     * 绑定元宝
     */
    private Integer boundYb;

    /**
     * 炼体值
     */
    private Integer lianti;

    /**
     * 成就积分
     */
    private Integer chengjiu;

    /**
     * VIP等级
     */
    private Integer vip;

    /**
     * 其他属性集合
     */
    private String others;

    /**
     * 性别
     */
    private Long gender;

    /**
     * 职业
     */
    private Integer profession;

    public Integer getVip() {
        if (vip == null) {
            vip = 1;
        }
        if (vip > Constants.TopLevel.VIP) {
            vip = Constants.TopLevel.VIP;
        }
        return vip;
    }

    public Integer getGold() {
        gold = getCommonMaxNumber(gold);
        return gold;
    }

    public Integer getBoundGold() {
        boundGold = getCommonMaxNumber(boundGold);
        return boundGold;
    }

    public Integer getYb() {
        yb = getCommonMaxNumber(yb);
        return yb;
    }

    public Integer getBoundYb() {
        boundYb = getCommonMaxNumber(boundYb);
        return boundYb;
    }

    public Integer getChengjiu() {
        chengjiu = getCommonMaxNumber(chengjiu);
        return chengjiu;
    }

    /**
     * 从action2字段中解析出炼体值
     *
     * @return
     */
    private void parseLianti() {
        if (StringUtils.isNotBlank(others)) {
            String[] array = StringUtils.split(others, ",");
            lianti = NumberUtils.toInt(array[Constants.AbilityIndex.LIAN_TI]);
        } else {
            lianti = 0;
        }
    }

    public Integer getLianti() {
        if (lianti == null) {
            parseLianti();
        }
        if (lianti > Constants.MaxNumber.COMMON) {
            lianti = Constants.MaxNumber.COMMON;
        }
        return lianti;
    }

    /**
     * 从action2字段中解析出转生等级
     */
    private void parseZhuansheng() {
        if (StringUtils.isNotBlank(others)) {
            String[] array = StringUtils.split(others, ",");
            zsLevel = NumberUtils.toInt(array[Constants.AbilityIndex.ZHUAN_SHENG]);
        } else {
            zsLevel = 0;
        }
    }

    public Integer getZsLevel() {
        if (zsLevel == null) {
            parseZhuansheng();
        }
        if (zsLevel > Constants.TopLevel.LEVEL_ZS) {
            zsLevel = Constants.TopLevel.LEVEL_ZS;
        }
        return zsLevel;
    }

    /**
     * 从action2字段中解析出武魂等级
     */
    private void parseWuhun() {
        if (StringUtils.isNotBlank(others)) {
            String[] array = StringUtils.split(others, ",");
            whLevel = NumberUtils.toInt(array[Constants.AbilityIndex.WU_HUN]);
        } else {
            whLevel = 0;
        }
    }

    public Integer getWhLevel() {
        if (whLevel == null) {
            parseWuhun();
        }
        if (whLevel > Constants.TopLevel.LEVEL_WH) {
            whLevel = Constants.TopLevel.LEVEL_WH;
        }
        return whLevel;
    }

    public Integer getLevel() {
        if (level == null) {
            level = 0;
        }
        if (level > Constants.TopLevel.LEVEL) {
            return Constants.TopLevel.LEVEL;
        } else {
            return level;
        }
    }

    public Integer getNgLevel() {
        if (ngLevel == null) {
            ngLevel = 0;
        }
        if (ngLevel > Constants.TopLevel.LEVEL_NG) {
            return Constants.TopLevel.LEVEL_NG;
        } else {
            return ngLevel;
        }
    }

    /**
     * 获取普通最大显示数值
     *
     * @param number
     * @return
     */
    private Integer getCommonMaxNumber(Integer number) {
        if (number == null) {
            number = 0;
        }
        if (number > Constants.MaxNumber.COMMON) {
            return Constants.MaxNumber.COMMON;
        } else {
            return number;
        }
    }

    /**
     * 获取角色类型显示文字
     *
     * @return
     */
    public String getCharacterType() {
        switch (profession) {
            case Constants.Profession.WARRIOR:
                characterType = "战圣";
                break;
            case Constants.Profession.ENCHANTER:
                characterType = "法神";
                break;
            case Constants.Profession.TAOIST_PRIEST:
                characterType = "道尊";
        }
        return characterType;
    }
}
