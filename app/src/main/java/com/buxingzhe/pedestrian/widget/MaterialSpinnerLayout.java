package com.buxingzhe.pedestrian.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.buxingzhe.pedestrian.R;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jaredrummler.materialspinner.MaterialSpinnerListener;

/**
 * Created by zhaishaoping on 02/03/2017.
 */

public class MaterialSpinnerLayout extends LinearLayout implements MaterialSpinnerListener, MaterialSpinner.OnNothingSelectedListener{

    private MaterialSpinner mMaterialSpinner;
    private ImageView mImageViewArrow;

    private boolean mIsShowSPinner = false;

    public MaterialSpinnerLayout(Context context) {
        this(context, null);
    }

    public MaterialSpinnerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialSpinnerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MaterialSpinnerLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMaterialSpinner = (MaterialSpinner) findViewById(R.id.walk_spinner);
        mImageViewArrow = (ImageView) findViewById(R.id.walk_spinner_arrow);
        mMaterialSpinner.setOnMaterialSpinnerListener(this);
        mMaterialSpinner.setOnNothingSelectedListener(this);
    }

    @Override
    public int[] getSuperLocationOnScreen() {
        int[] location = new int[2];
        getLocationOnScreen(location);
        return location;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mMaterialSpinner != null) {
                mIsShowSPinner = mMaterialSpinner.isShow();
                setArrow(mIsShowSPinner);
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    public void setArrow(boolean isUp){
        if(isUp){
            mImageViewArrow.setImageResource(R.mipmap.ic_bushu_arrow_up);
        }else{
            mImageViewArrow.setImageResource(R.mipmap.ic_bushu_arrow_down);
        }
    }

    @Override
    public void onNothingSelected(MaterialSpinner spinner) {
        setArrow(false);
    }


}
