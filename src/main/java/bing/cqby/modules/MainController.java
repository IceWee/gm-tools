package bing.cqby.modules;

import bing.cqby.common.Constants;
import bing.cqby.model.Character;
import bing.cqby.task.CharacterLoadTaskService;
import bing.cqby.task.CharacterRechargeTaskService;
import bing.cqby.task.CharacterUpdateTaskService;
import javafx.collections.ObservableList;
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
import org.apache.commons.lang3.math.NumberUtils;

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

    private Long selectedCharacterId = null;

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
            this.loadBtn.setDisable(true);
            CharacterLoadTaskService service = new CharacterLoadTaskService();
            service.setAccount(account);
            service.setOnSucceeded(workerStateEvent -> {
                this.characters = (List<Character>) workerStateEvent.getSource().getValue();
                this.selectedCharacterId = null;
                loadCharacter();
                this.loadBtn.setDisable(false);
            });
            service.setOnFailed(workerStateEvent -> {
                Throwable e = workerStateEvent.getSource().getException();
                errorHandle(e, "游戏角色信息读取失败", this.loadBtn);
            });
            service.start();
        } else {
            loadCharacter();
        }
    }

    /**
     * 更新游戏角色信息
     */
    public void updateCharacterInfo(ActionEvent actionEvent) {
        Character character = this.character.getSelectionModel().getSelectedItem();
        if (character != null) {
            Character updateCharacter = createUpdateCharacter();
            this.updateBtn.setDisable(true);
            CharacterUpdateTaskService service = new CharacterUpdateTaskService();
            service.setCharacter(updateCharacter);
            service.setOnSucceeded(workerStateEvent -> {
                this.selectedCharacterId = character.getCharacterId();
                this.characters = (List<Character>) workerStateEvent.getSource().getValue();
                loadCharacter();
                showDialogTip(Alert.AlertType.INFORMATION, "游戏角色信息修改成功");
                this.updateBtn.setDisable(false);
            });
            service.setOnFailed(workerStateEvent -> {
                Throwable e = workerStateEvent.getSource().getException();
                errorHandle(e, "游戏角色信息修改失败", this.updateBtn);
            });
            service.start();
        } else {
            showDialogTip(Alert.AlertType.WARNING, "请选择要修改的游戏角色");
        }
    }

    /**
     * 元宝充值
     *
     * @param actionEvent
     */
    public void rechargeYb(ActionEvent actionEvent) {
        Character character = this.character.getSelectionModel().getSelectedItem();
        if (character != null) {
            String yb = substring(this.rechargeYb.getText(), Constants.INT_MAX_LENGTH);
            if (StringUtils.isBlank(yb)) {
                showDialogTip(Alert.AlertType.WARNING, "请填写要充值的元宝数量");
                return;
            }
            this.rechargeBtn.setDisable(true);
            Integer recharge = NumberUtils.toInt(yb);
            CharacterRechargeTaskService service = new CharacterRechargeTaskService();
            service.setCharacter(character);
            service.setRecharge(recharge);
            service.setOnSucceeded(workerStateEvent -> {
                showDialogTip(Alert.AlertType.INFORMATION, "游戏角色元宝充值成功");
                this.rechargeYb.setText(null);
                this.rechargeBtn.setDisable(false);
            });
            service.setOnFailed(workerStateEvent -> {
                Throwable e = workerStateEvent.getSource().getException();
                errorHandle(e, "游戏角色元宝充值失败", this.rechargeBtn);
            });
            service.start();
        } else {
            showDialogTip(Alert.AlertType.WARNING, "请选择要充值的游戏角色");
        }
    }

    /**
     * 操作失败处理
     *
     * @param e
     * @param tip
     * @param buttons
     */
    private void errorHandle(Throwable e, String tip, Button... buttons) {
        log.error(e.getMessage(), e);
        showDialogTip(Alert.AlertType.ERROR, tip);
        for (Button button : buttons) {
            button.setDisable(false);
        }
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
        this.character.getItems().clear();
        if (this.characters.isEmpty()) {
            showDialogTip(Alert.AlertType.WARNING, "未查询倒任何游戏角色信息");
            refreshCharacterInfo();
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
        if (this.selectedCharacterId == null) {
            this.character.getSelectionModel().selectFirst();
        } else {
            selectCharacter(selectedCharacterId);
        }
        refreshCharacterInfo();
    }

    /**
     * 刷新角色信息
     */
    public void refreshCharacterInfo() {
        Character character = this.character.getSelectionModel().getSelectedItem();
        if (character != null) {
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
            this.zsLevel.setText(Objects.toString(character.getZsLevel()));
            this.whLevel.setText(Objects.toString(character.getWhLevel()));
            this.lianti.setText(Objects.toString(character.getLianti()));
        } else {
            this.characterId.setText(null);
            this.characterName.setText(null);
            this.characterType.setText(null);
            this.level.setText(null);
            this.ngLevel.setText(null);
            this.gold.setText(null);
            this.boundGold.setText(null);
            this.yb.setText(null);
            this.boundYb.setText(null);
            this.vip.getSelectionModel().selectFirst();
            this.zsLevel.setText(null);
            this.whLevel.setText(null);
            this.lianti.setText(null);
        }
    }

    /**
     * 选中角色
     *
     * @param characterId
     */
    private void selectCharacter(Long characterId) {
        ObservableList<Character> items = this.character.getItems();
        Character character = null;
        for (Character item : items) {
            if (Objects.equals(item.getCharacterId(), characterId)) {
                character = item;
                break;
            }
        }
//        this.character.setValue(character);
        this.character.getSelectionModel().select(character);
    }

    /**
     * 根据表单参数构造更新角色对象
     *
     * @return
     */
    private Character createUpdateCharacter() {
        Character character = new Character();
        Character choice = this.character.getSelectionModel().getSelectedItem();
        character.setAccountId(choice.getAccountId());
        character.setCharacterId(choice.getCharacterId());
        character.setOthers(choice.getOthers());
        character.setLevel(NumberUtils.toInt(substring(this.level.getText(), Constants.INT_MAX_LENGTH)));
        character.setZsLevel(NumberUtils.toInt(substring(this.zsLevel.getText(), Constants.INT_MAX_LENGTH)));
        character.setNgLevel(NumberUtils.toInt(substring(this.ngLevel.getText(), Constants.INT_MAX_LENGTH)));
        character.setWhLevel(NumberUtils.toInt(substring(this.whLevel.getText(), Constants.INT_MAX_LENGTH)));
        character.setLianti(NumberUtils.toInt(substring(this.lianti.getText(), Constants.INT_MAX_LENGTH)));
        character.setGold(NumberUtils.toInt(substring(this.gold.getText(), Constants.INT_MAX_LENGTH)));
        character.setBoundGold(NumberUtils.toInt(substring(this.boundGold.getText(), Constants.INT_MAX_LENGTH)));
        character.setYb(NumberUtils.toInt(substring(this.yb.getText(), Constants.INT_MAX_LENGTH)));
        character.setBoundYb(NumberUtils.toInt(substring(this.boundYb.getText(), Constants.INT_MAX_LENGTH)));
        character.setVip(this.vip.getSelectionModel().getSelectedItem());
        return character;
    }

    private String substring(String string, int maxLength) {
        return StringUtils.substring(string, 0, maxLength);
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
