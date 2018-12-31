package bing.cqby.task;

import bing.cqby.model.Character;
import bing.cqby.service.CharacterService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

/**
 * 角色信息异步任务服务
 *
 * @author: bing
 * @date: 2019-01-01
 */
public final class CharacterTaskService extends Service<List<Character>> {

    private CharacterService characterService = CharacterService.getInstance();

    private StringProperty accountProperty = new SimpleStringProperty();

    public CharacterTaskService() {
        super();
    }

    public final String getAccount() {
        return accountProperty.get();
    }

    public final void setAccount(String account) {
        accountProperty.set(account);
    }

    @Override
    protected Task<List<Character>> createTask() {
        return new Task<List<Character>>() {
            @Override
            protected List<Character> call() throws Exception {
                return characterService.query(getAccount());
            }
        };
    }

}
