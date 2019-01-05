package bing.cqby.util;

import bing.cqby.common.Constants;
import bing.cqby.domain.Config;
import bing.cqby.domain.Page;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据库工具
 *
 * @author: bing
 * @date: 2018-12-30
 */
@Slf4j
public final class DBHelper {

    private static DBHelper instance = new DBHelper();

    static {
        try {
            Class.forName(Constants.DB.DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            log.error("数据库驱动加载失败", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private Connection connection;
    private String loginDB;
    private String gameDB;

    private DBHelper() {
        super();
    }

    public static DBHelper getInstance() {
        return instance;
    }

    /**
     * 初始化数据库连接
     *
     * @param config
     */
    public void init(Config config) {
        String ip = config.getDbIP();
        int port = config.getDbPort();
        String loginDB = config.getLoginDB();
        this.loginDB = loginDB;
        this.gameDB = config.getGameDB();
        String url = createJdbcUrl(ip, port, loginDB);
        String user = config.getDbUser();
        String passwd = config.getDbPasswd();
        log.info("DB Info:");
        log.info("---------------------------------------------------------------------------------------");
        log.info("url: {}", url);
        log.info("user: {}", user);
        log.info("passwd: {}", passwd);
        log.info("---------------------------------------------------------------------------------------");
        try {
            this.connection = DriverManager.getConnection(url, user, passwd);
        } catch (SQLException e) {
            log.error("数据库连接失败", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * 计数
     *
     * @param sql
     * @return
     * @throws Exception
     */
    public int count(String sql, Object... args) throws Exception {
        int count = 0;
        String countSQL = createCountSQL(sql);
        log.info("sql: \r\n{}", countSQL);
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(countSQL);
            for (int i = 0; i < args.length; i++) {
                statement.setObject(i + 1, args[i]);
            }
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new Exception(e.getMessage(), e);
        } finally {
            release(statement, resultSet);
        }
        return count;
    }

    /**
     * 分页查询
     *
     * @param clazz
     * @param page
     * @param sql
     * @param args
     * @param <T>
     * @throws Exception
     */
    public <T> void query(Class<T> clazz, Page<T> page, String sql, Object... args) throws Exception {
        String pageSQL = createPageSQL(sql, page);
        int totalRows = count(sql, args);
        page.setTotalCount(totalRows);
        List<T> rows = query(clazz, pageSQL, args);
        page.setData(rows);
    }

    /**
     * 通用查询接口
     *
     * @param clazz
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    public <T> List<T> query(Class<T> clazz, String sql, Object... args) throws Exception {
        log.info("SQL: \r\n{}", sql);
        try {
            List<Map<String, Object>> rows = query(sql, args);
            return mapsToBeans(clazz, rows);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception(e.getMessage(), e);
        }
    }

    /**
     * 通用查询
     *
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> query(String sql, Object... args) throws Exception {
        log.info("SQL: \r\n{}", sql);
        List<Map<String, Object>> rows = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ResultSetMetaData resultSetMetaData;
        try {
            statement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                statement.setObject(i + 1, args[i]);
            }
            resultSet = statement.executeQuery();
            resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            Map<String, Object> row;
            String columnAlias;
            Object columnValue;
            while (resultSet.next()) {
                row = new HashMap<>();
                for (int i = 0; i < columnCount; i++) {
                    columnAlias = resultSetMetaData.getColumnLabel(i + 1);
                    columnValue = resultSet.getObject(i + 1);
                    row.put(columnAlias, columnValue);
                }
                rows.add(row);
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new Exception(e.getMessage(), e);
        } finally {
            release(statement, resultSet);
        }
        return rows;
    }

    /**
     * 执行SQL
     *
     * @param sql
     * @param args
     * @throws Exception
     */
    public void execute(String sql, Object... args) throws Exception {
        log.info("SQL: \r\n{}", sql);
        Connection connection = getConnection();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                statement.setObject(i + 1, args[i]);
            }
            int count = statement.executeUpdate();
            log.info("count: {}", count);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new Exception(e.getMessage(), e);
        } finally {
            release(statement, null);
        }
    }

    public String getLoginDB() {
        return loginDB;
    }

    public String getGameDB() {
        return gameDB;
    }

    /**
     * 根据IP和端口拼接数据库URL
     *
     * @param ip
     * @param port
     * @param db
     * @return
     */
    private String createJdbcUrl(String ip, int port, String db) {
        StringBuilder builder = new StringBuilder("jdbc:mysql://");
        builder.append(ip);
        builder.append(":");
        builder.append(port);
        builder.append("/");
        builder.append(db);
        builder.append("?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
        return builder.toString();
    }

    /**
     * Map集合转换为Bean集合
     *
     * @param clazz
     * @param maps
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchFieldException
     */
    private <T> List<T> mapsToBeans(Class<T> clazz, List<Map<String, Object>> maps) throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        List<T> list = new ArrayList<>();
        Set<Map.Entry<String, Object>> entrySet;
        T bean;
        String propertyName;
        Object value;
        for (Map<String, Object> map : maps) {
            entrySet = map.entrySet();
            bean = clazz.newInstance();
            for (Map.Entry<String, Object> entry : entrySet) {
                propertyName = entry.getKey();
                value = entry.getValue();
                ReflectionUtils.setPropertyValue(clazz, bean, propertyName, value);
            }
            list.add(bean);
        }
        return list;
    }

    /**
     * 生成统计行数SQL
     *
     * @param sql
     * @return
     */
    private String createCountSQL(String sql) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(1) counts FROM (").append(sql).append(") T");
        return builder.toString();
    }

    /**
     * 生成分页SQL
     *
     * @param sql
     * @param page
     * @return
     */
    private String createPageSQL(String sql, Page page) {
        StringBuilder builder = new StringBuilder(sql);
        int rowIndex = page.getDataIndex();
        int pageSize = page.getPageSize();
        builder.append(" LIMIT ").append(rowIndex).append(", ").append(pageSize);
        return builder.toString();
    }

    /**
     * 资源释放
     *
     * @param statement
     * @param resultSet
     */
    private void release(Statement statement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
