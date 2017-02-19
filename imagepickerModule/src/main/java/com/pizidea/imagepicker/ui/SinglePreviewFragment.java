package com.pizidea.imagepicker.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pizidea.imagepicker.R;
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

    public SinglePreviewFragment(Context context,String url){
        this.context = context;
        this.url = url;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//            Bundle bundle = getArguments();
//            ImageItem imageItem = (ImageItem) bundle.getSerializable(KEY_URL);
//            url = imageItem.path;

//        Log.i(TAG, "=====current show image path:" + url);

        imageView = new TouchImageView(context);
        imageView.setBackgroundColor(0xff000000);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);

//        imageView.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
//            @Override
//            public boolean onSingleTapConfirmed(MotionEvent e) {
//                if (enableSingleTap) {
//                    if (mContext instanceof OnImageSingleTapClickListener) {
//                        ((OnImageSingleTapClickListener) mContext).onImageSingleTap(e);
//                    }
//                }
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
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
//        PicassoImagePresenter picassoImagePresenter = new PicassoImagePresenter();
//        picassoImagePresenter.loadLocalImage(context,imageView,url);
        Glide.with(context).load(url).placeholder(R.drawable.default_img).into(imageView);


        //((UilImagePresenter)mImagePresenter).onPresentImage2(imageView, url, imageView.getWidth());//display the image with your own ImageLoader

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return imageView;
    }

}
