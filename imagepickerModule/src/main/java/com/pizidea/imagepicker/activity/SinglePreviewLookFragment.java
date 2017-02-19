package com.pizidea.imagepicker.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.pizidea.imagepicker.PicassoImagePresenter;
import com.pizidea.imagepicker.bean.ImageItem;
import com.pizidea.imagepicker.widget.TouchImageView;

/**
 * Created by QJ on 2016/10/12.
 */
@SuppressLint("ValidFragment")
public class SinglePreviewLookFragment extends Fragment {
    public static final String KEY_URL = "key_url";
    private TouchImageView imageView;

    private Context mContext;
    private String url;
    private PicassoImagePresenter mImagePresenter;
    public SinglePreviewLookFragment(Context context,String url){
        this.mContext = context;
        this.url = url;
        mImagePresenter = new PicassoImagePresenter();
        getDisplayWidth();

    }
    public  int getDisplayWidth(){
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);

        int screenWidth = wm.getDefaultDisplay().getWidth();
        int screenHeigh = wm.getDefaultDisplay().getHeight();

        int[] display = {screenWidth,screenHeigh};
        return screenWidth;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        ImageItem imageItem = (ImageItem) bundle.getSerializable(KEY_URL);

        imageView = new TouchImageView(mContext);
        imageView.setBackgroundColor(0xff000000);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        if (imageItem.location){
            if (imageItem.locaWidth > 0){
                int width = imageItem.locaWidth > getDisplayWidth()?getDisplayWidth():imageItem.locaWidth;
                mImagePresenter.loadLocal(mContext, imageItem.locaUrl,width,imageView);
            }else {
                mImagePresenter.loadLocal(mContext, imageItem.locaUrl,imageView);
            }
        }else {
            if (imageItem.width > 0 && imageItem.height > 0){
                int width = imageItem.width > getDisplayWidth()?getDisplayWidth():imageItem.width;
                int height = width/imageItem.width*imageItem.height;
                mImagePresenter.load(mContext, imageItem.path,width,height, imageView);
            }else {
                mImagePresenter.load(mContext, imageItem.path, imageView);
            }
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return imageView;
    }

}