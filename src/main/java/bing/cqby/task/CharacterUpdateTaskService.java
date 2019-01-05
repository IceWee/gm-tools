package bing.cqby.task;

import bing.cqby.domain.Character;
import bing.cqby.service.CharacterService;
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

    private Character character;

    public CharacterUpdateTaskService(Character character) {
        this.character = character;
    }

    @Override
    protected Task<List<Character>> createTask() {
        return new Task<List<Character>>() {
            @Override
            protected List<Character> call() throws Exception {
                return characterService.update(character);
            }
        };
    }

}
