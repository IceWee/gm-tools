package bing.cqby.controller;

import bing.cqby.common.Constants;
import bing.cqby.domain.Character;
import bing.cqby.domain.Equipment;
import bing.cqby.domain.Item;
import bing.cqby.domain.Page;
import bing.cqby.domain.Result;
import bing.cqby.domain.StrengthenLevel;
import bing.cqby.domain.ZhulingLevel;
import bing.cqby.task.CharacterLoadTaskService;
import bing.cqby.task.CharacterRechargeTaskService;
import bing.cqby.task.CharacterUpdateTaskService;
import bing.cqby.task.ItemSearchTaskService;
import bing.cqby.task.ItemSendTaskService;
import bing.cqby.task.PackageClearTaskService;
import bing.cqby.task.PlayerItemLoadTaskService;
import bing.cqby.task.PlayerItemTopTaskService;
import bing.cqby.task.PlayerItemUpdateTaskService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
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

    /* 选项卡1 begin */
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
    private TextField chengjiu;
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
    /* 选项卡1 end */

    /* 选项卡2 begin */
    @FXML
    private TextField searchItemName;
    @FXML
    private VBox itemListBox;
    @FXML
    private TableView<Item> itemList;
    @FXML
    private TableColumn<Item, Long> entry;
    @FXML
    private TableColumn<Item, String> name1;
    @FXML
    private TableColumn<Item, String> description;
    @FXML
    private Button searchBtn;
    @FXML
    private Pagination itemPage;
    @FXML
    private MenuItem sendMenuItem;
    private boolean firstTime = true;
    private Page<Item> page = new Page<>();
    /* 选项卡2 end */

    /* 选项卡3 begin */
    @FXML
    private TextField equipmentCharacter;
    @FXML
    private Button loadEquipmentBtn;
    @FXML
    private TableView<Equipment> characterEquipments;
    @FXML
    private TableColumn<Equipment, String> slotText;
    @FXML
    private TableColumn<Equipment, String> equipmentName;
    @FXML
    private TableColumn<Equipment, String> strengthenText;
    @FXML
    private TableColumn<Equipment, String> zhulingText;
    @FXML
    private TableColumn<Equipment, String> isBound;
    @FXML
    private TextField modifyEquipmentName;
    @FXML
    private CheckBox modifyEquipmentBound;
    @FXML
    private ComboBox<StrengthenLevel> modifyEquipmentStrength;
    @FXML
    private ComboBox<ZhulingLevel> modifyEquipmentZhuling;
    @FXML
    private Button modifyEquipmentBtn;
    @FXML
    private Button topLevelBtn;
    @FXML
    private Button clearPackageBtn;
    /* 选项卡3 end */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
        initVIPComboBox();
        configureTable();
        configurePager();
        configureEquipmentTable();
        initStrengthenBomboBox();
        initZhulingBomboBox();
    }

    /**
     * 读取账号
     */
    public void loadAccount() {
        String account = this.account.getText();
        if (StringUtils.isNotBlank(account)) {
            this.loadBtn.setDisable(true);
            CharacterLoadTaskService service = new CharacterLoadTaskService(account);
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
    public void updateCharacterInfo() {
        Character character = getSelectedCharacter();
        if (character != null) {
            Character updateCharacter = createUpdateCharacter();
            this.updateBtn.setDisable(true);
            CharacterUpdateTaskService service = new CharacterUpdateTaskService(updateCharacter);
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
     */
    public void rechargeYb() {
        Character character = getSelectedCharacter();
        if (character != null) {
            String yb = substring(this.rechargeYb.getText(), Constants.INT_MAX_LENGTH);
            if (StringUtils.isBlank(yb)) {
                showDialogTip(Alert.AlertType.WARNING, "请填写要充值的元宝数量");
                return;
            }
            this.rechargeBtn.setDisable(true);
            Integer recharge = NumberUtils.toInt(yb);
            CharacterRechargeTaskService service = new CharacterRechargeTaskService(character, recharge);
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
     * 刷新角色信息
     */
    public void refreshCharacterInfo() {
        Character character = getSelectedCharacter();
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
            this.chengjiu.setText(Objects.toString(character.getChengjiu()));
            this.sendMenuItem.setDisable(false);
            this.equipmentCharacter.setText(character.getCharacterName());
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
            this.chengjiu.setText(null);
            this.sendMenuItem.setDisable(true);
            this.equipmentCharacter.setText(null);
        }
    }

    /**
     * 物品查询
     */
    public void searchItems() {
        this.firstTime = false;
        this.searchBtn.setDisable(true);
        this.page.setPageNo(1);
        this.itemPage.setPageCount(1);
        this.itemPage.setCurrentPageIndex(0);
        String itemName = this.searchItemName.getText();
        ItemSearchTaskService service = new ItemSearchTaskService(itemName, this.page);
        service.setOnSucceeded(workerStateEvent -> {
            ObservableList<Item> items = FXCollections.observableArrayList(this.page.getData());
            this.itemList.setItems(items);
            this.itemPage.setPageCount(this.page.getTotalPages());
            this.searchBtn.setDisable(false);
        });
        service.setOnFailed(workerStateEvent -> {
            Throwable e = workerStateEvent.getSource().getException();
            errorHandle(e, "物品检索失败", this.searchBtn);
        });
        service.start();
    }

    /**
     * 发送物品
     */
    public void sendItem() {
        Item item = this.itemList.getSelectionModel().getSelectedItem();
        if (item != null) {
            Character character = getSelectedCharacter();
            Long characterId = character.getCharacterId();
            String characterName = character.getCharacterName();
            String itemName = item.getName1();
            ItemSendTaskService service = new ItemSendTaskService(characterId, item);
            service.setOnSucceeded(workerStateEvent -> {
                Result result = (Result) workerStateEvent.getSource().getValue();
                if (result.getSuccess()) {
                    showDialogTip(Alert.AlertType.INFORMATION, "[" + itemName + "]已成功发送到[" + characterName + "]的包裹中");
                } else {
                    showDialogTip(Alert.AlertType.ERROR, "物品发送到背包失败");
                }
            });
            service.setOnFailed(workerStateEvent -> {
                Throwable e = workerStateEvent.getSource().getException();
                errorHandle(e, "物品发送到背包失败", null);
            });
            service.start();
        }
    }

    /**
     * 加载角色装备
     */
    public void loadCharacterEquipment() {
        Character character = getSelectedCharacter();
        if (character != null) {
            this.loadEquipmentBtn.setDisable(true);
            PlayerItemLoadTaskService service = new PlayerItemLoadTaskService(character.getCharacterId());
            service.setOnSucceeded(workerStateEvent -> {
                List<Equipment> equipments = (List<Equipment>) workerStateEvent.getSource().getValue();
                this.characterEquipments.setItems(FXCollections.observableArrayList(equipments));
                this.loadEquipmentBtn.setDisable(false);
            });
            service.setOnFailed(workerStateEvent -> {
                Throwable e = workerStateEvent.getSource().getException();
                errorHandle(e, "角色装备加载失败", this.loadEquipmentBtn);
            });
            service.start();
        } else {
            showDialogTip(Alert.AlertType.WARNING, "请选择要加载装备的游戏角色");
        }
    }

    /**
     * 保存角色装备
     */
    public void saveCharacterEquipment() {
        Equipment equipment = getSelectedEquipment();
        if (equipment != null) {
            this.modifyEquipmentBtn.setDisable(true);
            Equipment updateEquipment = createUpdateEquipment();
            PlayerItemUpdateTaskService service = new PlayerItemUpdateTaskService(updateEquipment);
            service.setOnSucceeded(workerStateEvent -> {
                showDialogTip(Alert.AlertType.INFORMATION, "角色装备修改成功");
                loadCharacterEquipment();
                this.modifyEquipmentBtn.setDisable(false);
            });
            service.setOnFailed(workerStateEvent -> {
                Throwable e = workerStateEvent.getSource().getException();
                errorHandle(e, "角色装备修改失败", this.modifyEquipmentBtn);
            });
            service.start();
        } else {
            showDialogTip(Alert.AlertType.WARNING, "请选择要修改的装备");
        }
    }

    /**
     * 装备选中事件
     */
    public void selectEquipment() {
        Equipment equipment = getSelectedEquipment();
        if (equipment != null) {
            this.modifyEquipmentName.setText(equipment.getEquipmentName());
            ObservableList<StrengthenLevel> strengthenLevels = this.modifyEquipmentStrength.getItems();
            StrengthenLevel selectedStrengthenLevel = null;
            for (StrengthenLevel strengthenLevel : strengthenLevels) {
                if (Objects.equals(strengthenLevel.getValue(), equipment.getStrengthenLevel())) {
                    selectedStrengthenLevel = strengthenLevel;
                    break;
                }
            }
            this.modifyEquipmentStrength.getSelectionModel().select(selectedStrengthenLevel);
            this.modifyEquipmentZhuling.getSelectionModel().select(equipment.getZhulingLevel());
            if (equipment.getFlags() == 0) {
                this.modifyEquipmentBound.setSelected(false);
            } else {
                this.modifyEquipmentBound.setSelected(true);
            }
        }
    }

    /**
     * 一键装备强化和注灵满级
     */
    public void oneKeyTopLevel() {
        if (!this.characterEquipments.getItems().isEmpty()) {
            this.topLevelBtn.setDisable(true);
            Character character = getSelectedCharacter();
            PlayerItemTopTaskService service = new PlayerItemTopTaskService(character.getCharacterId());
            service.setOnSucceeded(workerStateEvent -> {
                showDialogTip(Alert.AlertType.INFORMATION, "一键满级操作成功");
                loadCharacterEquipment();
                this.topLevelBtn.setDisable(false);
            });
            service.setOnFailed(workerStateEvent -> {
                Throwable e = workerStateEvent.getSource().getException();
                errorHandle(e, "一键满级操作失败", this.topLevelBtn);
            });
            service.start();
        } else {
            showDialogTip(Alert.AlertType.WARNING, "请先加载装备列表再操作");
        }
    }

    /**
     * 清空背包
     */
    public void clearPackage() {
        this.clearPackageBtn.setDisable(true);
        Character character = getSelectedCharacter();
        PackageClearTaskService service = new PackageClearTaskService(character.getCharacterId());
        service.setOnSucceeded(workerStateEvent -> {
            showDialogTip(Alert.AlertType.INFORMATION, "背包已清空");
            this.clearPackageBtn.setDisable(false);
        });
        service.setOnFailed(workerStateEvent -> {
            Throwable e = workerStateEvent.getSource().getException();
            errorHandle(e, "背包清空失败", this.clearPackageBtn);
        });
        service.start();
    }

    /**
     * 设置表格
     */
    private void configureTable() {
        this.entry.setCellValueFactory(new PropertyValueFactory<>("entry"));
        this.name1.setCellValueFactory(new PropertyValueFactory<>("name1"));
        this.description.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    /**
     * 设置分页器
     */
    private void configurePager() {
        this.itemPage.setPageCount(1);
        this.itemPage.setCurrentPageIndex(0);
        this.itemPage.setPageFactory(pageIndex -> createItemListPage(pageIndex));
    }

    /**
     * 设置角色装备列表
     */
    private void configureEquipmentTable() {
        this.slotText.setCellValueFactory(new PropertyValueFactory<>("slotText"));
        this.equipmentName.setCellValueFactory(new PropertyValueFactory<>("equipmentName"));
        this.strengthenText.setCellValueFactory(new PropertyValueFactory<>("strengthenText"));
        this.zhulingText.setCellValueFactory(new PropertyValueFactory<>("zhulingText"));
        this.isBound.setCellValueFactory(new PropertyValueFactory<>("isBound"));
    }

    /**
     * 获取当前选中的游戏角色
     *
     * @return
     */
    private Character getSelectedCharacter() {
        return this.character.getSelectionModel().getSelectedItem();
    }

    /**
     * 获取当前选中的装备
     *
     * @return
     */
    private Equipment getSelectedEquipment() {
        return this.characterEquipments.getSelectionModel().getSelectedItem();
    }

    /**
     * 生成新页面
     *
     * @param pageIndex
     * @return
     */
    private VBox createItemListPage(int pageIndex) {
        this.page.setPageNo(pageIndex + 1);
        if (!firstTime) {
            String itemName = this.searchItemName.getText();
            ItemSearchTaskService service = new ItemSearchTaskService(itemName, this.page);
            service.setOnSucceeded(workerStateEvent -> {
                ObservableList<Item> items = FXCollections.observableArrayList(this.page.getData());
                this.itemList.setItems(items);
            });
            service.start();
        }
        return this.itemListBox;
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
     * 初始化VIP下拉列表
     */
    private void initVIPComboBox() {
        List<Integer> vips = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        this.vip.getItems().addAll(vips);
    }

    /**
     * 初始化强化等级下拉列表
     */
    private void initStrengthenBomboBox() {
        List<StrengthenLevel> strengthenLevels = new ArrayList<>();
        strengthenLevels.add(new StrengthenLevel("未强化", 0));
        StrengthenLevel strengthenLevel;
        for (int i = 1; i < 13; i++) {
            strengthenLevel = new StrengthenLevel(String.format("强化%d级", i), i * Constants.STRENGTHEN_UNIT);
            strengthenLevels.add(strengthenLevel);
        }
        this.modifyEquipmentStrength.setItems(FXCollections.observableArrayList(strengthenLevels));
        this.modifyEquipmentStrength.converterProperty().set(new StringConverter<StrengthenLevel>() {
            @Override
            public String toString(StrengthenLevel object) {
                return object.getLabel();
            }

            @Override
            public StrengthenLevel fromString(String string) {
                return null;
            }
        });
    }

    /**
     * 初始化注灵等级下拉列表
     */
    private void initZhulingBomboBox() {
        List<ZhulingLevel> zhulingLevels = new ArrayList<>();
        zhulingLevels.add(new ZhulingLevel("未强化", 0));
        for (int i = 1; i < 13; i++) {
            zhulingLevels.add(new ZhulingLevel(String.format("注灵%d级", i), i));
        }
        this.modifyEquipmentZhuling.setItems(FXCollections.observableArrayList(zhulingLevels));
        this.modifyEquipmentZhuling.converterProperty().set(new StringConverter<ZhulingLevel>() {
            @Override
            public String toString(ZhulingLevel object) {
                return object.getLabel();
            }

            @Override
            public ZhulingLevel fromString(String string) {
                return null;
            }
        });
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
        this.character.getSelectionModel().select(character);
    }

    /**
     * 根据表单参数构造更新角色对象
     *
     * @return
     */
    private Character createUpdateCharacter() {
        Character character = new Character();
        Character choice = getSelectedCharacter();
        character.setAccountId(choice.getAccountId());
        character.setCharacterId(choice.getCharacterId());
        character.setOthers(choice.getOthers());
        character.setLevel(NumberUtils.toInt(substring(this.level.getText(), Constants.INT_MAX_LENGTH)));
        character.setZsLevel(NumberUtils.toInt(substring(this.zsLevel.getText(), Constants.INT_MAX_LENGTH)));
        character.setNgLevel(NumberUtils.toInt(substring(this.ngLevel.getText(), Constants.INT_MAX_LENGTH)));
        character.setWhLevel(NumberUtils.toInt(substring(this.whLevel.getText(), Constants.INT_MAX_LENGTH)));
        character.setLianti(NumberUtils.toInt(substring(this.lianti.getText(), Constants.INT_MAX_LENGTH)));
        character.setChengjiu(NumberUtils.toInt(substring(this.chengjiu.getText(), Constants.INT_MAX_LENGTH)));
        character.setGold(NumberUtils.toInt(substring(this.gold.getText(), Constants.INT_MAX_LENGTH)));
        character.setBoundGold(NumberUtils.toInt(substring(this.boundGold.getText(), Constants.INT_MAX_LENGTH)));
        character.setYb(NumberUtils.toInt(substring(this.yb.getText(), Constants.INT_MAX_LENGTH)));
        character.setBoundYb(NumberUtils.toInt(substring(this.boundYb.getText(), Constants.INT_MAX_LENGTH)));
        character.setVip(this.vip.getSelectionModel().getSelectedItem());
        return character;
    }

    /**
     * 根据表单参数构造更新装备对象
     *
     * @return
     */
    private Equipment createUpdateEquipment() {
        Equipment equipment = new Equipment();
        Equipment choice = getSelectedEquipment();
        equipment.setGuid(choice.getGuid());
        equipment.setFlags(0L);
        if (this.modifyEquipmentBound.isSelected()) {
            equipment.setFlags(1L);
        }
        StrengthenLevel strengthenLevel = this.modifyEquipmentStrength.getValue();
        equipment.setStrengthenLevel(strengthenLevel.getValue());
        ZhulingLevel zhulingLevel = this.modifyEquipmentZhuling.getValue();
        equipment.setZhulingLevel(zhulingLevel.getValue());
        return equipment;
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
