package com.buxingzhe.pedestrian.community.community;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.utils.SystemUtils;

/**
 * Created by QJ on 2017/4/13.
 */

public class HorizontalAdapter extends BaseAdapter {
    private Context mContext;
    int currentId = 0;
//    ArrayList<CircleCommentContentData> assets;

    public HorizontalAdapter(Context mContext) {
        this.mContext = mContext;
//        this.assets = assets;
    }

    @Override
    public int getCount() {
//        if (assets != null) {
//            return assets.size();
//        } else {
//            return 0;
//        }
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        HoldeImage holdeImage = null;
        if (view == null){
            holdeImage = new HoldeImage();
            view = View.inflate(mContext, R.layout.item_scroll_linearlayout, null);
            holdeImage.iv_pic = (ImageView) view.findViewById(R.id.index_gallery_item_image);
            view.setTag(holdeImage);
        } else {
            holdeImage = (HoldeImage) view.getTag();
        }
        int[] display = SystemUtils.getDisplayWidth(mContext);
//        int width = (display[0] - SystemUtils.dip2px(mContext, 60)) / 3 - SystemUtils.dip2px(mContext, 10);
        int width = (display[0] - SystemUtils.dip2px(mContext, 42)) / 3;
        int height = width/3*2;

        RelativeLayout.LayoutParams imgvwMargin = new RelativeLayout.LayoutParams(width, height);
        imgvwMargin.setMargins(0, 0, SystemUtils.dip2px(mContext, 6), 0);
//        if (position == 0) {
//            imgvwMargin.setMargins(0, 0, SystemUtils.dip2px(mContext, 6), 0);
//        } else if (position == assets.size()) {
//            imgvwMargin.setMargins(SystemUtils.dip2px(mContext, 6), 0, 0, 0);
//        } else {
//            imgvwMargin.setMargins(SystemUtils.dip2px(mContext, 6), 0, SystemUtils.dip2px(mContext, 6), 0);
//        }
//        if (position == currentId) {
//            setBackgroudPaddingt(holdeImage.imageView);
//        }
        holdeImage.iv_pic.setLayoutParams(imgvwMargin);

//        if (assets.get(position).url.contains("file://")) {
//            Picasso.with(mContext)
//                    .load(assets.get(position).url)
//                    .config(Bitmap.Config.RGB_565)
//                    .into(holdeImage.imageView);
//        } else {
//            String url = ImageLoaderManager.getImageLoaderManager(mContext).smallImageSrc(assets.get(position).url, 160, 160);
//            ImageLoaderManager.getImageLoaderManager(mContext).setDisplayImage(url, holdeImage.imageView,
//                    ImageLoaderManager.DEFAULT, ImageLoaderManager.DEFAULT, ImageLoaderManager.PICTURE);
//        }

        holdeImage.iv_pic.setImageResource(R.mipmap.fengjing1);
        return view;
    }

    public void setCurrentId(int currentId) {
        this.currentId = currentId;
        notifyDataSetChanged();
    }
}

class HoldeImage {
    ImageView iv_pic;
}
