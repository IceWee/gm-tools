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

}
