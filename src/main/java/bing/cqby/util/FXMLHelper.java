package bing.cqby.util;

import bing.cqby.common.Constants;

import java.net.URL;

/**
 * FXML工具
 *
 * @author: bing
 * @date: 2018-12-30
 */
public class FXMLHelper {

    private static FXMLHelper instance = new FXMLHelper();

    private FXMLHelper() {
        super();
    }

    public static FXMLHelper getInstance() {
        return instance;
    }

    /**
     * 获取主界面FXML
     *
     * @return
     */
    public URL getMainFXML() {
        return ClassLoader.getSystemResource(Constants.Fxml.MAIN);
    }

    /**
     * 获取配置界面FXML
     *
     * @return
     */
    public URL getConfigFXML() {
        return ClassLoader.getSystemResource(Constants.Fxml.CONFIG);
    }

}
