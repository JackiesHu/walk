package com.buxingzhe.pedestrian.utils;

import android.view.View;
import android.widget.TextView;

import com.buxingzhe.pedestrian.R;

/**
 * Created by chinaso on 2017/7/17.
 */

public class SelectPicPopupWindow extends BasePopupWindow {
    private View view;
    private TextView mTakeCamera;
    private TextView mTakePhoto;
    private TextView mTakeCancle;
    private void initView(View view) {
        mTakeCamera = (TextView) view.findViewById(R.id.register_set_camera);
        mTakePhoto = (TextView) view.findViewById(R.id.register_take_photo);
        mTakeCancle = (TextView) view.findViewById(R.id.register_set_cancle);
    }

    public void setOnclick(View.OnClickListener itemOnclick) {
        mTakeCancle.setOnClickListener(itemOnclick);
        mTakeCamera.setOnClickListener(itemOnclick);
        mTakePhoto.setOnClickListener(itemOnclick);
    }

    @Override
    public View getView() {
        view = layoutInflater.inflate(R.layout.item_register_photo, null);
        initView(view);
        return view;
    }

}
