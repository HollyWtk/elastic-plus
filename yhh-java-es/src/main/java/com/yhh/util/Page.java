package com.yhh.util;

import java.util.List;

import lombok.Data;

/**  
 * <p>Description: </p>  
 * @author yhh  
 * @date 2020年10月26日  
 */
@Data
public class Page<T> {

    private long total;
    
    private int page;
    
    private int pageSize;
    
    private List<T> data;

    /**
     * @param total
     * @param page
     * @param pageSize
     * @param data
     */
    public Page(long total, int page, int pageSize, List<T> data) {
        super();
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
        this.data = data;
    }
    
    /**
     * @param total
     * @param page
     * @param pageSize
     * @param data
     */
    public Page(int page, int pageSize, List<T> data) {
        super();
        this.page = page;
        this.pageSize = pageSize;
        this.data = data;
    }
    
}
