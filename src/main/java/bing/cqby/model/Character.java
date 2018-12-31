package bing.cqby.model;

import lombok.Data;

/**
 * 游戏角色实体
 *
 * @author: bing
 * @date: 2018-12-31
 */
@Data
public class Character {

    private Long characterId;

    private String characterName;

    private String characterType;

    private Integer level;

    private Integer ngLevel;

    private Integer zsLevel;

    private Integer whLevel;

    private Integer gold;

    private Integer boundGold;

    private Integer yb;

    private Integer boundYb;

    private Integer lianti;

    private Integer vip;

    private String others;

}
