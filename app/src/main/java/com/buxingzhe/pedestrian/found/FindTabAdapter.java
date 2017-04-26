package com.buxingzhe.pedestrian.found;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.buxingzhe.pedestrian.bean.RequestResultInfo;
import com.buxingzhe.pedestrian.common.GlobalParams;
import com.buxingzhe.pedestrian.found.bean.PageContent;
import com.buxingzhe.pedestrian.found.bean.RemarkPoint;
import com.buxingzhe.pedestrian.found.bean.Tag;
import com.buxingzhe.pedestrian.found.bean.WalkRecord;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by jackie on 2017/2/11.
 */

public class FindTabAdapter extends FragmentPagerAdapter {
    private List<WalkCategoryFragment> list_fragment;                         //fragment列表
    private List<Tag> tags;                              //tab名的列表
    private List<WalkRecord> walkRecords;

    FindTabAdapter(FragmentManager fm, List<WalkCategoryFragment> list_fragment, List<Tag> tags) {
        super(fm);
        this.list_fragment = list_fragment;
        this.tags = tags;
    }
    @Override
    public Fragment getItem(int position) {
        return list_fragment.get(position);
    }
    @Override
    public int getCount() {
        if (list_fragment != null)
        return list_fragment.size();
        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        loadData(position,1);
        return super.instantiateItem(container, position);
    }

    private void loadData(final int position, int pageNo) {
        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("userId", GlobalParams.USER_ID);
        paramsMap.put("tag", tags.get(position).getName());
        paramsMap.put("pageNo",String.valueOf(pageNo));
        paramsMap.put("pageSize ","20");

        Subscriber mSubscriber = new Subscriber<String>(){

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final String jsonStr) {
                // 由于服务端的返回数据格式不固定，因此这里采用手动解析
                RequestResultInfo resultInfo = JSON.parseObject(jsonStr, RequestResultInfo.class);
                if ("0".equals(resultInfo.getCode())) {
                    Object o = resultInfo.getContent();
                    if (o != null) {
                        PageContent content = JSON.parseObject(o.toString(),PageContent.class);
                        walkRecords = JSON.parseArray(content.getList().toString(), WalkRecord.class);
                        list_fragment.get(position).setData(walkRecords);
                    }
                }
            }
        };

        NetRequestManager.getInstance().queryWalkRecordByTag(paramsMap,mSubscriber);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        FragmentManager manager = ((WalkCategoryFragment) object).getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove((WalkCategoryFragment) object);
        trans.commitAllowingStateLoss();
        super.destroyItem(container, position, object);
    }
    //此方法用来显示tab上的名字
    @Override
    public CharSequence getPageTitle(int position) {
        if (tags != null){
            return tags.get(position).getName();
        }
        return "";
    }
}
