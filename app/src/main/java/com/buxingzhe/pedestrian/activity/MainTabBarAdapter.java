package com.buxingzhe.pedestrian.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.buxingzhe.pedestrian.PDConfig;
import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.utils.SystemUtils;
import com.buxingzhe.pedestrian.widget.TabBarBaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/11.
 */
public class MainTabBarAdapter extends TabBarBaseAdapter {
    private LayoutInflater inflater;
    private List<String> mTitleList = new ArrayList<String>();
    private List<Integer> mIconNormalList = new ArrayList<Integer>();
    private List<Integer> mIconSelectList = new ArrayList<Integer>();
    private List<Holder> mHolderList = new ArrayList<Holder>();
    private int textNormalColor = 0;
    private int textSelectColor = 0;


    public MainTabBarAdapter() {
        inflater = (LayoutInflater) PDConfig.getInstance().getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        textNormalColor = SystemUtils.getByColor(R.color.white);
        textSelectColor = SystemUtils.getByColor(R.color.red);
        initViewInfo();

    }

    private void initViewInfo() {
        mTitleList.add("步行");
        mTitleList.add("发现");
        mTitleList.add("社区");
        mTitleList.add("我");


        mIconNormalList.add(R.mipmap.navi_recommend);
        mIconNormalList.add(R.mipmap.navi_recommend);
        mIconNormalList.add(R.mipmap.navi_recommend);
        mIconNormalList.add(R.mipmap.navi_recommend);

        mIconSelectList.add(R.mipmap.navi_recommend_select);
        mIconSelectList.add(R.mipmap.navi_recommend_select);
        mIconSelectList.add(R.mipmap.navi_recommend_select);
        mIconSelectList.add(R.mipmap.navi_recommend_select);

    }

    @Override
    public int getCount() {
        return mTitleList.size();
    }

    @Override
    public View getView(int postion) {
        View view = inflater.inflate(R.layout.item_tabbar, null);
        Holder holder = new Holder();
        holder.ItemTabIm = (ImageView) view.findViewById(R.id.ItemTabIm);
        holder.ItemTabText = (TextView) view.findViewById(R.id.ItemTabText);
        holder.ItemTabMark = (ImageView) view.findViewById(R.id.ItemTabMark);
        holder.ItemTabText.setVisibility(View.GONE);
        holder.ItemTabMark.setVisibility(View.GONE);
        setHolderData(holder, postion);
        mHolderList.add(holder);
        return view;
    }

    public void switchView(int id) {
        for (int i = 0; i < mHolderList.size(); i++) {
            Holder holder = mHolderList.get(i);
            setHolderData(holder, i);
        }
        Holder holder = mHolderList.get(id);
        holder.ItemTabIm.setBackgroundResource(mIconSelectList.get(id));
        holder.ItemTabText.setTextColor(textSelectColor);
    }

    class Holder {
        ImageView ItemTabIm;
        TextView ItemTabText;
        ImageView ItemTabMark;
    }

    private void setHolderData(Holder holder, int postion) {
        holder.ItemTabText.setText(mTitleList.get(postion));
        holder.ItemTabText.setTextColor(textNormalColor);
        holder.ItemTabIm.setBackgroundResource(mIconNormalList.get(postion));
    }

}
