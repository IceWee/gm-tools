package bing.cqby.domain;

import lombok.Data;

import java.util.List;

/**
 * 分页对象
 *
 * @author: bing
 * @date: 2019-01-01
 */
@Data
public class Page<T> {

    /**
     * 页号
     */
    private int pageNo = 1;

    /**
     * 每页显示记录数
     */
    private int pageSize = 15;

    /**
     * 总记录数
     */
    private int totalCount;

    /**
     * 总页数
     */
    private volatile int totalPages;

    /**
     * 数据记录下标
     */
    private volatile int dataIndex;

    /**
     * 数据
     */
    private List<T> data;

    public int getDataIndex() {
        dataIndex = (pageNo - 1) * pageSize;
        return dataIndex;
    }

    public int getTotalPages() {
        if (totalCount % pageSize == 0) {
            totalPages = totalCount / pageSize;
        } else {
            totalPages = (totalCount / pageSize) + 1;
        }
        return totalPages;
    }

}
