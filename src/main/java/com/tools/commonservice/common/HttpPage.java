package com.tools.commonservice.common;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Annotation @Accessors(chain = true) Usage:
 * page.setPage(10).setTotal(100).setPageSize(30);
 */
@Accessors(chain = true)
@Data
public class HttpPage<T> {
    private int total;
    private int page;
    private int pageSize;
    private List<T> data;
}
