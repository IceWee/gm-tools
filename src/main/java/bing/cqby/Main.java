package bing.cqby;

import bing.cqby.common.FXMLHelper;
import bing.cqby.config.ConfigController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;

/**
 * 传奇霸业GM工具
 *
 * @author bing
 */
@Slf4j
public class Main extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        URL location = FXMLHelper.getInstance().getMainFXML();
        Parent root = FXMLLoader.load(location);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setFullScreen(false);
        stage.show();
        // 弹出数据库配置窗口
        openConfigWindow();
    }

    /**
     * 打开数据库配置窗口
     */
    private void openConfigWindow() {
        try {
            URL location = FXMLHelper.getInstance().getConfigFXML();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            ConfigController configController = fxmlLoader.getController();
            configController.setSelf(stage);
            // 模态窗口
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest((WindowEvent event) -> {
                System.exit(0);
            });
        } catch (Exception e) {
            log.error("初始化配置窗口失败", e);
        }
    }

}
