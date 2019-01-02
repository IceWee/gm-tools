package bing.cqby.controller;

import bing.cqby.common.Constants;
import bing.cqby.domain.Config;
import bing.cqby.task.DatabaseConnectTaskService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 配置控制器
 *
 * @author: bing
 * @date: 2018-12-30
 */
@Slf4j
public class ConfigController implements Initializable {

    @FXML
    private TextField dbIP;

    @FXML
    private TextField dbPort;

    @FXML
    private TextField dbUser;

    @FXML
    private TextField dbPasswd;

    @FXML
    private ComboBox<String> loginDB;

    @FXML
    private ComboBox<String> gameDB;

    @FXML
    private Text errorTip;

    @FXML
    private Button connectBtn;

    private Stage self;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initComboBoxes();
    }

    /**
     * 连接数据库
     */
    public void connectDB() {
        if (validate()) {
            this.connectBtn.setDisable(true);
            Config config = createConfig();
            DatabaseConnectTaskService service = new DatabaseConnectTaskService();
            service.setConfig(config);
            service.setOnSucceeded(workerStateEvent -> {
                closeSelf();
                this.connectBtn.setDisable(false);
            });
            service.setOnFailed(workerStateEvent -> {
                showErrorTip("数据库连接失败");
                this.connectBtn.setDisable(false);
            });
            service.start();
        }
    }

    /**
     * 初始化下拉列表框
     */
    private void initComboBoxes() {
        loginDB.getItems().add(Constants.DB.LOGIN);
        loginDB.getSelectionModel().selectFirst();
        gameDB.getItems().add(Constants.DB.GAME);
        gameDB.getSelectionModel().selectFirst();
    }

    /**
     * 校验输入
     *
     * @return
     */
    private boolean validate() {
        boolean valid = true;
        String dbIP = this.dbIP.getText();
        if (StringUtils.isBlank(dbIP)) {
            showErrorTip("请填写数据库IP地址");
            return false;
        }
        int dbPort = NumberUtils.toInt(this.dbPort.getText());
        if (dbPort == 0) {
            showErrorTip("请填写数据库端口");
            return false;
        }
        String dbUser = this.dbUser.getText();
        if (StringUtils.isBlank(dbUser)) {
            showErrorTip("请填写数据库用户名");
            return false;
        }
        String dbPasswd = this.dbPasswd.getText();
        if (StringUtils.isBlank(dbPasswd)) {
            showErrorTip("请填写数据库密码");
            return false;
        }
        String loginDB = this.loginDB.getSelectionModel().getSelectedItem();
        if (StringUtils.isBlank(loginDB)) {
            showErrorTip("请选择或填写账号数据库");
            return false;
        }
        String gameDB = this.gameDB.getSelectionModel().getSelectedItem();
        if (StringUtils.isBlank(gameDB)) {
            showErrorTip("请选择或填写游戏数据库");
            return false;
        }
        clearErrorTip();
        return valid;
    }

    /**
     * 创建配置对象
     *
     * @return
     */
    private Config createConfig() {
        Config config = new Config();
        String dbIP = this.dbIP.getText();
        int dbPort = NumberUtils.toInt(this.dbPort.getText());
        String dbUser = this.dbUser.getText();
        String dbPasswd = this.dbPasswd.getText();
        String loginDB = this.loginDB.getSelectionModel().getSelectedItem();
        String gameDB = this.gameDB.getSelectionModel().getSelectedItem();
        config.dbIP(dbIP).dbPort(dbPort).dbUser(dbUser).dbPasswd(dbPasswd).loginDB(loginDB).gameDB(gameDB);
        return config;
    }

    /**
     * 显示错误提示
     *
     * @param tip
     */
    private void showErrorTip(String tip) {
        this.errorTip.setText(tip);
        this.errorTip.setVisible(true);
    }

    /**
     * 隐藏错误提示
     */
    private void clearErrorTip() {
        this.errorTip.setText(StringUtils.EMPTY);
        this.errorTip.setVisible(false);
    }

    /**
     * 关闭当前窗口
     */
    private void closeSelf() {
        this.self.close();
    }

    /**
     * 保存当前窗口引用
     *
     * @param self
     */
    public void setSelf(Stage self) {
        this.self = self;
    }

}
