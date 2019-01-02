package bing.cqby.service;

import bing.cqby.common.Constants;
import bing.cqby.domain.Character;
import bing.cqby.util.DBHelper;
import bing.cqby.util.DateUtils;
import bing.cqby.util.SQLUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 角色服务
 *
 * @author: bing
 * @date: 2018-12-31
 */
@Slf4j
public final class CharacterService {

    private static String QUERY;
    private static String QUERY_BY_ACCOUNT;
    private static String QUERY_BY_ACCOUNT_ID;

    private static CharacterService instance = new CharacterService();

    static {
        initQuery();
        initQueryByAccount();
        initQueryByAccountId();
    }

    private CharacterService() {
        super();
    }

    public static CharacterService getInstance() {
        return instance;
    }

    /**
     * 初始化查询角色SQL语句
     */
    private static void initQuery() {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT [game].characters.acct accountId, [game].characters.guid characterId, [game].characters.name characterName, [game].playervip.vip_type vip,");
        builder.append(" (CASE [game].characters.class WHEN 1 THEN '战圣' WHEN 2 THEN '法神' WHEN 3 THEN '道尊' ELSE '未知' END) characterType,");
        builder.append(" [game].characters.level, [game].characters.honorRolloverTime ngLevel, [game].characters.gold, [game].characters.binggold boundGold,");
        builder.append(" [game].characters.goldmoney yb, [game].characters.binggoldmoney boundYb, [game].characters.actions2 others");
        builder.append(" FROM [game].characters LEFT OUTER JOIN [game].playervip ON [game].characters.guid = [game].playervip.guid");
        QUERY = builder.toString();
    }

    /**
     * 初始化根据账号查询角色SQL语句
     */
    private static void initQueryByAccount() {
        StringBuilder builder = new StringBuilder();
        builder.append(QUERY);
        builder.append(" WHERE [game].characters.acct = (SELECT [login].accounts.acct FROM [login].accounts WHERE [login].accounts.login = ?)");
        QUERY_BY_ACCOUNT = builder.toString();
    }

    /**
     * 初始化根据账号ID查询角色SQL语句
     */
    private static void initQueryByAccountId() {
        StringBuilder builder = new StringBuilder();
        builder.append(QUERY);
        builder.append(" WHERE [game].characters.acct = ?");
        QUERY_BY_ACCOUNT_ID = builder.toString();
    }

    /**
     * 根据账号查询角色列表
     *
     * @param account
     * @return
     * @throws Exception
     */
    public List<Character> query(String account) throws Exception {
        String sql = SQLUtils.replaceDBNames(QUERY_BY_ACCOUNT);
        List<Character> characters = DBHelper.getInstance().query(Character.class, sql, new String[]{account});
        return characters;
    }

    /**
     * 根据账号ID查询角色列表
     *
     * @param accountId
     * @return
     * @throws Exception
     */
    public List<Character> query(Long accountId) throws Exception {
        String sql = SQLUtils.replaceDBNames(QUERY_BY_ACCOUNT_ID);
        List<Character> characters = DBHelper.getInstance().query(Character.class, sql, new Long[]{accountId});
        return characters;
    }

    /**
     * 更新角色信息
     *
     * @param character
     */
    public List<Character> update(Character character) throws Exception {
        String gameDB = DBHelper.getInstance().getGameDB();
        Long guid = character.getCharacterId();
        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE ").append(gameDB).append(".characters ");
        builder.append("SET level = ").append(character.getLevel()).append(", ");
        builder.append("gold = ").append(character.getGold()).append(", ");
        builder.append("binggold = ").append(character.getBoundGold()).append(", ");
        builder.append("goldmoney = ").append(character.getYb()).append(", ");
        builder.append("binggoldmoney = ").append(character.getBoundYb()).append(", ");
        builder.append("honorRolloverTime = ").append(character.getNgLevel()).append(", ");
        String actions2 = character.getOthers();
        String[] array = StringUtils.split(actions2, ",");
        // 转生
        array[Constants.AbilityIndex.ZHUAN_SHENG] = Objects.toString(character.getZsLevel());
        // 武魂
        array[Constants.AbilityIndex.WU_HUN] = Objects.toString(character.getWhLevel());
        // 炼体值
        array[Constants.AbilityIndex.LIAN_TI] = Objects.toString(character.getLianti());
        String newActions2 = StringUtils.join(array, ",");
        newActions2 = StringUtils.appendIfMissing(newActions2, ",");
        builder.append("actions2 = '").append(newActions2).append("' ");
        builder.append("where guid = ").append(guid);
        DBHelper.getInstance().execute(builder.toString());
        // 清除VIP
        StringBuilder clear = new StringBuilder();
        clear.append("DELETE FROM ").append(gameDB).append(".playervip WHERE guid = ").append(guid);
        DBHelper.getInstance().execute(clear.toString());
        // 设置VIP
        StringBuilder setVip = new StringBuilder();
        setVip.append("INSERT INTO ").append(gameDB).append(".playervip(guid, vip_type, vip_draw_flag) VALUES(");
        setVip.append(guid).append(", ").append(character.getVip()).append(", 5)");
        DBHelper.getInstance().execute(setVip.toString());
        List<Character> characters = query(character.getAccountId());
        return characters;
    }

    /**
     * 元宝充值
     *
     * @param character
     * @param recharge
     * @throws Exception
     */
    public void recharge(Character character, Integer recharge) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO game.paylog(acct, guid, paynum, ip, paygold, paymoney, procTime, flag) VALUES(");
        builder.append(character.getAccountId()).append(", ");
        builder.append(character.getCharacterId()).append(", ");
        String serialNo = DateUtils.createSerialNo();
        builder.append("'").append(serialNo).append("', ");
        builder.append("'127.0.0.1'").append(", ");
        builder.append(recharge).append(", ");
        Integer payMoney = recharge / 10;
        builder.append(payMoney).append(", ");
        String timestamp = DateUtils.createTimestamp();
        builder.append("'").append(timestamp).append("', 0)");
        DBHelper.getInstance().execute(builder.toString());
    }

}
