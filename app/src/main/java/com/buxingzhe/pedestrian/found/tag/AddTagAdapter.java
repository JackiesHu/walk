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
import com.buxingzhe.pedestrian.utils.SystemUtils;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
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
        final HotUserTag hotUserTag = hotUserTags.get(position);
        if (!TextUtils.isEmpty(hotUserTag.tag)){
            holder.tv_tag.setText(hotUserTag.tag);
        }
        final ImageView tvSelect = holder.tv_select;
        final TextView tvTag = holder.tv_tag;
        setVisible(holder.tv_select,holder.tv_tag,hotUserTag.isSelect);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hotUserTags.get(position).isSelect = !hotUserTags.get(position).isSelect;
                setVisible(tvSelect,tvTag, hotUserTag.isSelect);
            }
        });
        return convertView;
    }
    private void setVisible(ImageView weekSelect,TextView tv,boolean select){
        if (select){
            weekSelect.setVisibility(View.VISIBLE);
            tv.setTextColor(SystemUtils.getByColor(R.color.deep_orange));
        }else {
            tv.setTextColor(SystemUtils.getByColor(R.color.dark_back));
            weekSelect.setVisibility(View.GONE);
        }

    }
    public void setHotUserTagDatas(boolean isClean,List<HotUserTag> hotUserTags){
        if (isClean){
            this.hotUserTags.clear();
        }
        this.hotUserTags.addAll(hotUserTags);
        notifyDataSetChanged();
    }

    public List<HotUserTag> getHotUserTags() {
        return hotUserTags;
    }
}
