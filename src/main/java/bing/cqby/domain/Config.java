package bing.cqby.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DB配置
 *
 * @author: bing
 * @date: 2018-12-30
 */
@Getter
@NoArgsConstructor
@ToString
public class Config {

    private String dbIP;
    private int dbPort;
    private String dbUser;
    private String dbPasswd;
    private String loginDB;
    private String gameDB;

    public Config dbIP(String dbIP) {
        this.dbIP = dbIP;
        return this;
    }

    public Config dbPort(int dbPort) {
        this.dbPort = dbPort;
        return this;
    }

    public Config dbUser(String dbUser) {
        this.dbUser = dbUser;
        return this;
    }

    public Config dbPasswd(String dbPasswd) {
        this.dbPasswd = dbPasswd;
        return this;
    }

    public Config loginDB(String loginDB) {
        this.loginDB = loginDB;
        return this;
    }

    public Config gameDB(String gameDB) {
        this.gameDB = gameDB;
        return this;
    }

}
