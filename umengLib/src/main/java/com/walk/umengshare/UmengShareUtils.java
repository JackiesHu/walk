package com.walk.umengshare;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

/**
 * Created by Administrator on 2016/8/18.
 */
public class UmengShareUtils {
    private static UmengShareUtils umengShareUtils = null;
    private Activity mActivity;
    private Context mContext;

    private UmengShareUtils(Activity activity) {
        mActivity = activity;
        mContext = activity.getApplicationContext();

        // 添加第三方平台
        PlatformConfig.setWeixin("wx590e347558c7e8c8", "d61f4a3091dcf619eee88d21ada9a49b");
        PlatformConfig.setQQZone("1105525301", "fJDWIASa4QxMaFsA");

    }

    public static UmengShareUtils getInstall(Activity activity) {
        umengShareUtils = new UmengShareUtils(activity);
        return umengShareUtils;
    }

    public void shareContentInfo(String title, String content, String imageUrl, String targetUrl) {
        UMImage urlImage = new UMImage(mActivity, imageUrl);
        SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
                {
                        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                        SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
                };
        new ShareAction(mActivity).setDisplayList(displaylist)
                .withText(content)
                .withMedia(urlImage)
                .setListenerList(umShareListener)
                .open();
    }


    /**
     * 朋友圈动态分享
     *
     * @param title
     * @param content
     * @param imageUrl
     */
    public void shareImageInfo(final String title, final String content, final String imageUrl, final ShareBoardlistener clickListen, final UMShareListener click) {
        // 添加第三方平台
        SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
                {
                        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ
                };
        new ShareAction(mActivity).setDisplayList(displaylist)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, final SHARE_MEDIA share_media) {
                        final UMImage urlImage = new UMImage(mActivity, imageUrl);
                        if (share_media == SHARE_MEDIA.WEIXIN || share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
                            //微信分享先压缩图片
//                            HttpEaseService.getInstance().downloadbitmap(imageUrl, new OnAsyncResultListener<Bitmap>() {
//                                @Override
//                                public void onSuccess(Bitmap result) {
////                                    progressBar.dismiss();
//                                    UMImage urlImage = new UMImage(mActivity, result);
//                                    new ShareAction(mActivity).setPlatform(share_media).setCallback(umShareListener)
//                                            .withTitle(title)
//                                            .withText(content)
//                                            .withMedia(urlImage)
//                                            .share();
//                                }
//
//                                @Override
//                                public void onFailure(MWTError message) {
//                                }
//
//                                @Override
//                                public void onCache(int code, Bitmap result) {
//                                }
//                            });
                            new ShareAction(mActivity).setPlatform(share_media).setCallback(umShareListener)
                                    .withText(content)
                                    .withMedia(urlImage)
                                    .share();
                        } else if (share_media == SHARE_MEDIA.QQ) {
                            new ShareAction(mActivity).setPlatform(share_media).setCallback(umShareListener)
                                    .withText(content)
                                    .withMedia(urlImage)
                                    .share();
                        }
                        clickListen.onclick(snsPlatform, share_media);
                    }
                })
                .setListenerList(umShareListener).open();
    }



    /**
     * 微信分享
     *
     * @param title
     * @param content
     * @param imageUrl
     * @param targetUrl
     */
    public void shareWithWX(String title, String content, String imageUrl, String targetUrl) {
        UMImage urlImage = new UMImage(mActivity, imageUrl);
        SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
                {
                        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE
                };
        new ShareAction(mActivity).setDisplayList(displaylist)
                .withText(content)
                .withMedia(urlImage)
                .setListenerList(umShareListener)
                .open();
    }

    /**
     * QQ分享
     *
     * @param title
     * @param content
     * @param imageUrl
     * @param targetUrl
     */
    public void shareWithQQ(String title, String content, String imageUrl, String targetUrl) {
        UMImage urlImage = new UMImage(mActivity, imageUrl);
        SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
                {
                        SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
                };
        new ShareAction(mActivity).setDisplayList(displaylist)
                .withText(content)
                .withMedia(urlImage)
                .setListenerList(umShareListener)
                .open();
    }


    UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            Toast.makeText(mActivity, " 分享开始了", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(mActivity, " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(mActivity, " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mActivity, " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

}
