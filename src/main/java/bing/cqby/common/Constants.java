package bing.cqby.common;

/**
 * 静态常量
 *
 * @author: bing
 * @date: 2018-12-30
 */
public interface Constants {

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

}
