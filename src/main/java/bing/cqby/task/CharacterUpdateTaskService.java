package bing.cqby.task;

import bing.cqby.model.Character;
import bing.cqby.service.CharacterService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

/**
 * 角色信息更新异步任务服务
 *
 * @author: bing
 * @date: 2019-01-01
 */
public final class CharacterUpdateTaskService extends Service<List<Character>> {

    private CharacterService characterService = CharacterService.getInstance();

    private ObjectProperty<Character> characterProperty = new SimpleObjectProperty();

    public CharacterUpdateTaskService() {
        super();
    }

    private final Character getCharacter() {
        return characterProperty.get();
    }

    public final void setCharacter(Character character) {
        characterProperty.set(character);
    }

    @Override
    protected Task<List<Character>> createTask() {
        return new Task<List<Character>>() {
            @Override
            protected List<Character> call() throws Exception {
                return characterService.update(getCharacter());
            }
        };
    }

}
