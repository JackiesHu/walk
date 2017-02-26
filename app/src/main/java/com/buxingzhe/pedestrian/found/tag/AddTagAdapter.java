package com.buxingzhe.pedestrian.found.tag;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.bean.HotUserTag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QJ on 2016/7/6.
 */
public class AddTagAdapter extends BaseAdapter{
    private Context mContext;
    private List<HotUserTag> hotUserTags = new ArrayList<>();

    public AddTagAdapter(Context mContext){
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        if (hotUserTags==null){
            return 0;
        }
        return hotUserTags.size();
    }

    @Override
    public HotUserTag getItem(int position) {
//        position -- ;
//        if (position<0){
//            position = 0;
//        }
        return hotUserTags.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class Holder {
        TextView tv_tag;
        ImageView tv_select;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView==null){
            holder = new Holder();
            convertView = View.inflate(mContext, R.layout.item_addtag,null);
            holder.tv_tag = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_select = (ImageView) convertView.findViewById(R.id.tv_select);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        HotUserTag hotUserTag = hotUserTags.get(position);
        if (!TextUtils.isEmpty(hotUserTag.tag)){
            holder.tv_tag.setText(hotUserTag.tag);
        }

        return convertView;
    }

    public void setHotUserTagDatas(boolean isClean,List<HotUserTag> hotUserTags){
        if (isClean){
            this.hotUserTags.clear();
        }
        this.hotUserTags.addAll(hotUserTags);
        notifyDataSetChanged();
    }
}
