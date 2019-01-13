package bing.cqby.common;

/**
 * 静态常量
 *
 * @author: bing
 * @date: 2018-12-30
 */
public interface Constants {

    /**
     * 整数最爱长度
     */
    int INT_MAX_LENGTH = 9;

    /**
     * 装备强化1级的点数
     */
    int STRENGTHEN_UNIT = 65537;

    /**
     * 0
     */
    int ZERO = 0;

    /**
     * 角色背包
     */
    interface Slot {

        /**
         * 最小背包
         */
        int MIN = 23;

        /**
         * 最大背包
         */
        int MAX = 86;

    }

    /**
     * FXML路径
     */
    interface Fxml {

        /**
         * 主界面
         */
        String MAIN = "fxml/main.fxml";

        /**
         * 配置界面
         */
        String CONFIG = "fxml/config.fxml";

    }

    /**
     * 数据库名称
     */
    interface DB {

        /**
         * 数据库驱动类
         */
        String DRIVER_CLASS = "com.mysql.jdbc.Driver";

        /**
         * 账号数据库
         */
        String LOGIN = "login";

        /**
         * 游戏数据库
         */
        String GAME = "game";

    }

    /**
     * 顶级
     */
    interface TopLevel {

        /**
         * 角色等级
         */
        int LEVEL = 120;

        /**
         * 内功等级
         */
        int LEVEL_NG = 200;

        /**
         * 转生等级
         */
        int LEVEL_ZS = 12;

        /**
         * 武魂等级
         */
        int LEVEL_WH = 20;

        /**
         * VIP等级
         */
        int VIP = 10;

    }

    /**
     * 能力数组下标
     */
    interface AbilityIndex {

        /**
         * 炼体
         */
        int LIAN_TI = 650;

        /**
         * 转生
         */
        int ZHUAN_SHENG = 658;

        /**
         * 武魂
         */
        int WU_HUN = 665;
    }

    /**
     * 最大数值限制
     */
    interface MaxNumber {

        /**
         * 一般常用
         */
        int COMMON = 1000000000;

    }

    /**
     * 装备位置
     */
    interface EquipmentSlot {

        /**
         * 头盔
         */
        int HELM = 0;

        /**
         * 项链
         */
        int NECKLACE = 1;

        /**
         * 左手镯
         */
        int BRACELET_LEFT = 2;

        /**
         * 右手镯
         */
        int BRACELET_RIGHT = 3;

        /**
         * 左戒指
         */
        int RING_LEFT = 4;

        /**
         * 右戒指
         */
        int RING_RIGHT = 5;

        /**
         * 腰带
         */
        int BELT = 6;

        /**
         * 靴子
         */
        int BOOTS = 7;

        /**
         * 武器
         */
        int WEAPON = 8;

        /**
         * 盔甲
         */
        int ARMOR = 9;

        /**
         * 勋章
         */
        int MEDAL = 10;

        /**
         * 护盾
         */
        int SHIELD = 11;

        /**
         * 宝石
         */
        int GEMSTONE = 12;

        /**
         * 血符
         */
        int BLOOD_SIGN = 13;

        /**
         * 魂珠
         */
        int SOUL_BEAD = 14;

        /**
         * 面甲
         */
        int VIZARD = 15;

        /**
         * 吊坠
         */
        int PENDANT = 16;

        /**
         * 护肩
         */
        int SHOULDER_PAD = 17;

        /**
         * 护膝
         */
        int KNEELET = 18;

        /**
         * 时装
         */
        int FASHION = 19;

        /**
         * 羽翼
         */
        int WING = 21;

    }

    /**
     * 职业
     */
    interface Profession {

        /**
         * 战圣
         */
        int WARRIOR = 1;

        /**
         * 法神
         */
        int ENCHANTER = 2;

        /**
         * 道士
         */
        int TAOIST_PRIEST = 3;

    }

    /**
     * 性别
     */
    interface Gender {

        /**
         * 男
         */
        long MALE = 1;

        /**
         * 女
         */
        long FEMALE = 2;

    }

}
