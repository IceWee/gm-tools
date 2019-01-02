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
    private int totalRows;

    /**
     * 总页数
     */
    private volatile int totalPages;

    /**
     * 数据记录下标
     */
    private volatile int rowIndex;

    /**
     * 数据
     */
    private List<T> rows;

    public int getRowIndex() {
        rowIndex = (pageNo - 1) * pageSize;
        return rowIndex;
    }

    public int getTotalPages() {
        if (totalRows % pageSize == 0) {
            totalPages = totalRows / pageSize;
        } else {
            totalPages = (totalRows / pageSize) + 1;
        }
        return totalPages;
    }

}
