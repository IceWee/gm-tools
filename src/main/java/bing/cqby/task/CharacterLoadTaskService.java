package bing.cqby.task;

import bing.cqby.domain.Character;
import bing.cqby.service.CharacterService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

/**
 * 角色信息加载异步任务服务
 *
 * @author: bing
 * @date: 2019-01-01
 */
public final class CharacterLoadTaskService extends Service<List<Character>> {

    private CharacterService characterService = CharacterService.getInstance();

    private String account;

    public CharacterLoadTaskService(String account) {
        this.account = account;
    }

    @Override
    protected Task<List<Character>> createTask() {
        return new Task<List<Character>>() {
            @Override
            protected List<Character> call() throws Exception {
                return characterService.query(account);
            }
        };
    }

}
