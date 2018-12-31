package bing.cqby.modules;

import bing.cqby.model.Character;
import bing.cqby.task.CharacterTaskService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * 主控制器
 *
 * @author: bing
 * @date: 2018-12-31
 */
@Slf4j
public class MainController implements Initializable {

    @FXML
    private TextField account;

    @FXML
    private ComboBox<Character> character;

    @FXML
    private TextField rechargeYb;

    @FXML
    private TextField characterId;

    @FXML
    private TextField characterName;

    @FXML
    private TextField characterType;

    @FXML
    private TextField level;

    @FXML
    private TextField ngLevel;

    @FXML
    private TextField zsLevel;

    @FXML
    private TextField whLevel;

    @FXML
    private TextField gold;

    @FXML
    private TextField boundGold;

    @FXML
    private TextField yb;

    @FXML
    private TextField boundYb;

    @FXML
    private TextField lianti;

    @FXML
    private ComboBox<Integer> vip;

    @FXML
    private Button loadBtn;

    @FXML
    private Button rechargeBtn;

    @FXML
    private Button updateBtn;

    private List<Character> characters = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initChoiceBoxes();
    }

    /**
     * 读取账号
     *
     * @param actionEvent
     */
    public void loadAccount(ActionEvent actionEvent) {
        String account = this.account.getText();
        if (StringUtils.isNotBlank(account)) {
            setControlsDisable(true);
            CharacterTaskService service = new CharacterTaskService();
            service.setAccount(account);
            service.setOnSucceeded(workerStateEvent -> {
                this.characters = (List<Character>) workerStateEvent.getSource().getValue();
                loadCharacter();
                setControlsDisable(false);
            });
            service.setOnFailed(workerStateEvent -> {
                Throwable e = workerStateEvent.getSource().getException();
                log.error(e.getMessage(), e);
                showDialogTip(Alert.AlertType.ERROR, "游戏角色信息读取失败");
                setControlsDisable(false);
            });
            service.start();
        } else {
            showDialogTip(Alert.AlertType.INFORMATION, "请填写要读取的账号");
        }
    }

    /**
     * 更新游戏角色信息
     */
    public void updateCharacterInfo() {
        showDialogTip(Alert.AlertType.INFORMATION, "更新成功");
    }

    /**
     * 初始化下拉列表
     */
    private void initChoiceBoxes() {
        List<Integer> vips = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        this.vip.getItems().addAll(vips);
    }

    /**
     * 加载游戏角色下拉列表
     */
    private void loadCharacter() {
        if (this.characters.isEmpty()) {
            showDialogTip(Alert.AlertType.WARNING, "未查询倒任何游戏角色信息");
            return;
        }
        this.character.getItems().addAll(this.characters);
        this.character.converterProperty().set(new StringConverter<Character>() {
            @Override
            public String toString(Character object) {
                return object.getCharacterName();
            }

            @Override
            public Character fromString(String string) {
                return null;
            }
        });
        this.character.getSelectionModel().selectFirst();
        refreshCharacterInfo();
    }

    /**
     * 刷新角色信息
     */
    public void refreshCharacterInfo() {
        Character character = this.character.getSelectionModel().getSelectedItem();
        this.characterId.setText(Objects.toString(character.getCharacterId()));
        this.characterName.setText(character.getCharacterName());
        this.characterType.setText(character.getCharacterType());
        this.level.setText(Objects.toString(character.getLevel()));
        this.ngLevel.setText(Objects.toString(character.getNgLevel()));
        this.gold.setText(Objects.toString(character.getGold()));
        this.boundGold.setText(Objects.toString(character.getBoundGold()));
        this.yb.setText(Objects.toString(character.getYb()));
        this.boundYb.setText(Objects.toString(character.getBoundYb()));
        this.vip.getSelectionModel().select(character.getVip());
    }

    /**
     * 设置控制器的可用性
     *
     * @param disable
     */
    private void setControlsDisable(boolean disable) {
        this.account.setDisable(disable);
        this.character.setDisable(disable);
        this.level.setDisable(disable);
        this.ngLevel.setDisable(disable);
        this.zsLevel.setDisable(disable);
        this.whLevel.setDisable(disable);
        this.gold.setDisable(disable);
        this.boundGold.setDisable(disable);
        this.yb.setDisable(disable);
        this.boundYb.setDisable(disable);
        this.vip.setDisable(disable);
        this.rechargeYb.setDisable(disable);
        this.loadBtn.setDisable(disable);
        this.rechargeBtn.setDisable(disable);
        this.updateBtn.setDisable(disable);
    }

    /**
     * 现实弹出框提示
     *
     * @param alertType
     * @param tip
     */
    private void showDialogTip(Alert.AlertType alertType, String tip) {
        Alert alert = new Alert(alertType);
        alert.setTitle("提示信息");
        alert.setContentText(tip);
        alert.show();
    }

}
