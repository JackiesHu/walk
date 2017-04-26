package com.buxingzhe.pedestrian.found.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 *
 * Created by hasee on 2017/4/24.
 */

public abstract class RecycleBaseAdapter<T> extends RecyclerView.Adapter<RecycleViewHolder> {
    protected List<T> list;
    protected Context context;
    protected int resId;

    private OnItemClickListener onItemClickListener;

    public RecycleBaseAdapter(Context context, List<T> list, int resId){
        this.list = list;
        this.context = context;
        this.resId = resId;
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resId,null);

        return new RecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecycleViewHolder holder, final int position) {
        convert(holder,list.get(position),position);
        if (onItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            });
        }
    }

    public abstract void convert(RecycleViewHolder holder, T t, int position);

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
