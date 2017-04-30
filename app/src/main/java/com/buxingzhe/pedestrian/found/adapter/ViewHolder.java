package com.buxingzhe.pedestrian.found.adapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author  静平 on 2016/11/7.
 */
public class ViewHolder {
    private final SparseArray<View> mViews;
    private View mConvertView;

    private ViewHolder(LayoutInflater inflater, int itemRes, ViewGroup parent){
        mConvertView = inflater.inflate(itemRes, parent, false);
        mConvertView.setTag(this);
        mViews = new SparseArray<>();
    }

    public static ViewHolder getHolder(LayoutInflater inflater, int itemRes, View convertView, ViewGroup parent){
        if (convertView == null){
            return new ViewHolder(inflater,itemRes,parent);
        }

        return (ViewHolder) convertView.getTag();
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     */
    public <T extends View> T getView(int viewId)
    {

        View view = mViews.get(viewId);
        if (view == null)
        {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView()
    {
        return mConvertView;
    }

    public void setText(View textView , String text){
        ((TextView)textView).setText(text);
    }

    public void setText(View view , int textResId){
        ((TextView)view).setText(textResId);
    }

    public void setText(int viewId, String text){
        View view = getView(viewId);
        setText(view, text);
    }

    public void setText(int viewId, int textResId){
        View view = getView(viewId);
        setText(view, textResId);
    }

    public void setViewVisible(int viewId,boolean isVisible){
        View view = getView(viewId);
        setViewVisible(view, isVisible);
    }

    public void setViewVisible(View view,boolean isVisible){
        if (isVisible)
            view.setVisibility(View.VISIBLE);
        else
            view.setVisibility(View.GONE);
    }

    public void setVisible(int viewId, int visible){
        View view = getView(viewId);
        setVisible(view,visible);
    }

    public void setVisible(View view, int visible){
        view.setVisibility(visible);
    }

}
