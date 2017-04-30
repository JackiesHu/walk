package com.buxingzhe.pedestrian.found.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class MyBaseAdapter<T> extends BaseAdapter {
	
	protected List<T> list;
	protected Context context;
	protected LayoutInflater inflater;
	protected int itemRes;
	
	public MyBaseAdapter(Context context, List<T> list, int itemRes) {
		this.context = context;
		this.list = list;
		this.itemRes = itemRes;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public MyBaseAdapter(Context context, List<T> list) {
		this.context = context;
		this.list = list;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public <T extends View> T findView(View view,int id){
		return (T)view.findViewById(id);
	}

	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder holder = ViewHolder.getHolder(inflater, itemRes, convertView, parent);
		convert(holder,list.get(position),position);

		return holder.getConvertView();
	}

	public abstract void convert(ViewHolder holder, T t, int position);

}
