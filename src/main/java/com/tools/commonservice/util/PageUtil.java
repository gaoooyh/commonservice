package com.tools.commonservice.util;

import com.tools.commonservice.common.HttpPage;

import java.util.List;

public class PageUtil {


    /**
     * 分页功能
     * @param pageNum 当前页数
     * @param pageSize 每页条数
     * @param sourceList 源列表
     * @param <T>  泛型对象
     * @return 分页对象
     */
    public static <T> HttpPage<T> page(Integer pageNum, Integer pageSize, List<T> sourceList){

        HttpPage<T> page = new HttpPage<>();
        page.setTotal(0);

        // 当列表不为空的时候，才进行数据逻辑处理
        if (sourceList != null && !sourceList.isEmpty()) {
            int sourceSize = sourceList.size();

            pageNum = (pageNum == null || pageNum <= 0) ? 1 : pageNum;
            pageSize = (pageSize == null || pageSize <= 0) ? 10 : pageSize;

            /*
            步骤分解，便于理解
            int startPosition = Math.min((pageNum - 1) * pageSize, sourceSize);
            int endPosition = Math.min(pageNum * pageSize, sourceSize);
            pageList = sourceList.subList(startPosition, endPosition);

            分页的起始和截止位置，和源列表的size大小进行对比，分别取最小值
             */

            page.setData(sourceList.subList(Math.min((pageNum - 1) * pageSize, sourceSize), Math.min(pageNum * pageSize, sourceSize)));
            page.setTotal(sourceSize);
            page.setCurrent(pageNum);
            page.setPageSize(pageSize);
        }
        return page;

    }
}
