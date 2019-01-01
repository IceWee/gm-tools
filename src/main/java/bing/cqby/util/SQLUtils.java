package bing.cqby.util;

import bing.cqby.common.DBHelper;
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
     * @param sqlTemplate
     * @return
     */
    public static String replacePlaceholders(String sqlTemplate) {
        String loginDB = DBHelper.getInstance().getLoginDB();
        String gameDB = DBHelper.getInstance().getGameDB();
        log.info("loginDB: {}, gameDB: {}", loginDB, gameDB);
        String sql = StringUtils.replace(sqlTemplate, "[login]", loginDB);
        sql = StringUtils.replace(sql, "[game]", gameDB);
        return sql;
    }

}
