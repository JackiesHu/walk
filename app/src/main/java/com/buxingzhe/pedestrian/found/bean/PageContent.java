package com.buxingzhe.pedestrian.found.bean;

import com.buxingzhe.pedestrian.bean.user.UserBaseInfo;

import java.util.List;

/**
 * Created by hasee on 2017/4/24.
 */

public class PageContent<T> {

    private int pageNo;//1
    private int pageSize;//": 20,
    private int count;//2,
    private List<T> list;//

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
