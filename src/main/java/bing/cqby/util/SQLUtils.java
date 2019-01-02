package bing.cqby.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * SQL工具
 *
 * @author: bing
 * @date: 2019-01-01
 */
@Slf4j
public final class SQLUtils {

    private SQLUtils() {
        super();
    }

    /**
     * 替换SQL模板中的占位符
     *
     * @param sql
     * @return
     */
    public static String replaceDBNames(String sql) {
        String loginDB = DBHelper.getInstance().getLoginDB();
        String gameDB = DBHelper.getInstance().getGameDB();
        log.info("loginDB: {}, gameDB: {}", loginDB, gameDB);
        sql = StringUtils.replace(sql, "[login]", loginDB);
        sql = StringUtils.replace(sql, "[game]", gameDB);
        return sql;
    }

}
