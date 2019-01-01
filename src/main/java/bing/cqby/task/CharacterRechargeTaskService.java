package bing.cqby.task;

import bing.cqby.model.Character;
import bing.cqby.service.CharacterService;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * 角色元宝充值异步任务服务
 *
 * @author: bing
 * @date: 2019-01-01
 */
public final class CharacterRechargeTaskService extends Service<Void> {

    private CharacterService characterService = CharacterService.getInstance();

    private ObjectProperty<Character> characterProperty = new SimpleObjectProperty<>();
    private IntegerProperty rechargeProperty = new SimpleIntegerProperty();

    public CharacterRechargeTaskService() {
        super();
    }

    private final Integer getRecharge() {
        return rechargeProperty.get();
    }

    public final void setRecharge(Integer recharge) {
        rechargeProperty.set(recharge);
    }

    private final Character getCharacter() {
        return characterProperty.get();
    }

    public final void setCharacter(Character character) {
        characterProperty.set(character);
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                characterService.recharge(getCharacter(), getRecharge());
                return null;
            }
        };
    }

}
