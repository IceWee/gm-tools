package bing.cqby.task;

import bing.cqby.domain.Character;
import bing.cqby.service.CharacterService;
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

    private Character character;
    private Integer recharge;

    public CharacterRechargeTaskService(Character character, Integer recharge) {
        this.character = character;
        this.recharge = recharge;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                characterService.recharge(character, recharge);
                return null;
            }
        };
    }

}
