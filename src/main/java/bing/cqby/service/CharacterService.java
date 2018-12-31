package bing.cqby.service;

import bing.cqby.common.DBHelper;
import bing.cqby.model.Character;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 角色服务
 *
 * @author: bing
 * @date: 2018-12-31
 */
@Slf4j
public final class CharacterService {

    private static String QUERY_BY_ACCOUNT;

    private static CharacterService instance = new CharacterService();

    static {
        initQueryByAccount();
    }

    private CharacterService() {
        super();
    }

    public static CharacterService getInstance() {
        return instance;
    }

    /**
     * 初始化根据账号查询角色SQL语句
     */
    private static void initQueryByAccount() {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT [game].characters.guid characterId, [game].characters.name characterName, [game].playervip.vip_type vip,");
        builder.append(" (CASE [game].characters.class WHEN 1 THEN '战圣' WHEN 2 THEN '法神' WHEN 3 THEN '道尊' ELSE '未知' END) characterType,");
        builder.append(" [game].characters.level, [game].characters.honorRolloverTime ngLevel, [game].characters.gold, [game].characters.binggold boundGold,");
        builder.append(" [game].characters.goldmoney yb, [game].characters.binggoldmoney boundYb, [game].characters.actions2 others");
        builder.append(" FROM [game].characters LEFT OUTER JOIN [game].playervip ON [game].characters.guid = [game].playervip.guid");
        builder.append(" WHERE [game].characters.acct = (SELECT [login].accounts.acct FROM [login].accounts WHERE [login].accounts.login = ?)");
        QUERY_BY_ACCOUNT = builder.toString();
    }

    /**
     * 根据账号查询角色列表
     *
     * @param account
     * @return
     * @throws Exception
     */
    public List<Character> query(String account) throws Exception {
        String loginDB = DBHelper.getInstance().getLoginDB();
        String gameDB = DBHelper.getInstance().getGameDB();
        log.info("loginDB: {}, gameDB: {}, account: {}", loginDB, gameDB, account);
        String sql = StringUtils.replace(QUERY_BY_ACCOUNT, "[login]", loginDB);
        sql = StringUtils.replace(sql, "[game]", gameDB);
        log.info("sql: \r\n{}", sql);
        List<Character> characters = DBHelper.getInstance().query(Character.class, sql, new String[]{account});
        return characters;
    }

}
