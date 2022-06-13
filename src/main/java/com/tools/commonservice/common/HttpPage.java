package com.tools.commonservice.common;

import com.fasterxml.jackson.databind.ser.PropertyFilter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Object HttpPage is a template used to return paginated data.
 *
 * Annotation @Accessors(chain = true) Usage:
 * page.setCurrent(10).setTotal(100).setPageSize(30);
 */
@Accessors(chain = true)
@Data
public class HttpPage<T> {
    private int total = 0;
    private int current;
    private int pageSize;
    private List<T> data;
}
