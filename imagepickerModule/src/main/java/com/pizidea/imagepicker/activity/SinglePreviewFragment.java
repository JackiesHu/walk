package com.pizidea.imagepicker.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pizidea.imagepicker.PicassoImagePresenter;
import com.pizidea.imagepicker.widget.TouchImageView;


/**
 * Created by QJ on 2016/10/12.
 */
@SuppressLint("ValidFragment")
public class SinglePreviewFragment extends Fragment {
    public static final String KEY_URL = "key_url";
    private TouchImageView imageView;

    private Context context;
    private String url;
    private PicassoImagePresenter mImagePresenter;
    public SinglePreviewFragment(Context context,String url){
        this.context = context;
        this.url = url;
        mImagePresenter = new PicassoImagePresenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageView = new TouchImageView(context);
        imageView.setBackgroundColor(0xff000000);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);

//        imageView.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
//            @Override
//            public boolean onSingleTapConfirmed(MotionEvent e) {
//                onImageSingleTap(e);
//                return false;
//            }
//
//            @Override
//            public boolean onDoubleTapEvent(MotionEvent e) {
//                return false;
//            }
//
//            @Override
//            public boolean onDoubleTap(MotionEvent e) {
//                return false;
//            }
//
//        });
        mImagePresenter.loadLocal(context,url,imageView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return imageView;
    }
}