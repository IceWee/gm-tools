package bing.cqby.domain;

import lombok.Data;

/**
 * 结果
 *
 * @author: bing
 * @date: 2019-01-05
 */
@Data
public class Result {

    /**
     * 成功与否
     */
    private Boolean success;

    /**
     * 结果信息
     */
    private String message;

}
