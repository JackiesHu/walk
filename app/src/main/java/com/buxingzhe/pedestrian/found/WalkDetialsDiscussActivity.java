package com.buxingzhe.pedestrian.found;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;
import com.buxingzhe.pedestrian.bean.HotUserTag;
import com.buxingzhe.pedestrian.bean.RequestResultInfo;
import com.buxingzhe.pedestrian.common.GlobalParams;
import com.buxingzhe.pedestrian.common.StarBarBean;
import com.buxingzhe.pedestrian.found.adapter.PointCommentAdapter;
import com.buxingzhe.pedestrian.found.bean.HotTagBean;
import com.buxingzhe.pedestrian.found.bean.PointComment;
import com.buxingzhe.pedestrian.found.bean.RemarkPoint;
import com.buxingzhe.pedestrian.found.tag.TagAddActivity;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;
import com.buxingzhe.pedestrian.utils.EnterActUtils;
import com.buxingzhe.pedestrian.utils.SystemUtils;
import com.buxingzhe.pedestrian.widget.FlowLayout;
import com.buxingzhe.pedestrian.widget.MWTStarBar;
import com.buxingzhe.pedestrian.widget.MWTStarOnclick;
import com.buxingzhe.pedestrian.widget.TitleBarView;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.activity.ImagesGridActivity;
import com.pizidea.imagepicker.bean.ImageItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

/**·
 * Created by jackie on 2017/2/12.
 */

public class WalkDetialsDiscussActivity extends BaseActivity implements View.OnClickListener,
        AndroidImagePicker.OnPictureTakeCompleteListener,AndroidImagePicker.OnImagePickCompleteListener{
    private LinearLayout vAddress;
    private AndroidImagePicker mImagePicker;
    private MWTStarBar vStressStar,vEnviromentStar,vSafetyStar;
    private TextView vAddTag;
    private TextView tv_address;
    private EditText et_content;
    private List<HotUserTag> hotSelectTags;
    private RemarkPoint remarkPoint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wakl_discuss);
        getExt();
        findId();
        setData();
        setOnclick();
        mImagePicker = AndroidImagePicker.getInstance();
    }

    private void setData() {
//        tv_address.setText(remarkPoint.getTitle());
    }

    private void setOnclick() {
        vAddress.setOnClickListener(this);
        vAddTag.setOnClickListener(this);
    }

    private void findId() {
        vTitleBar = (TitleBarView) findViewById(R.id.title_bar);
        vAddress = (LinearLayout) findViewById(R.id.addAddress);

        vStressStar = (MWTStarBar) findViewById(R.id.walkde_stress_star);
        vEnviromentStar = (MWTStarBar) findViewById(R.id.walkde_environment_star);
        vSafetyStar = (MWTStarBar) findViewById(R.id.walkde_safety_star);

        vAddTag =(TextView)findViewById(R.id.tag_add);
        tv_address =(TextView)findViewById(R.id.tv_address);
        et_content = (EditText) findViewById(R.id.et_content);
        setTitle("评论");
        setRightTitle("完成");
        setTextRightOnclick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> paramsMap = new HashMap<>();
                paramsMap.put("userId",GlobalParams.USER_ID);
                paramsMap.put("token", GlobalParams.TOKEN);
//                paramsMap.put("remarkPoint",remarkPoint.getId());
                paramsMap.put("streetStar",String.valueOf(vStressStar.getStarSize()));
                paramsMap.put("envirStar",String.valueOf(vEnviromentStar.getStarSize()));
                paramsMap.put("safeStar",String.valueOf(vSafetyStar.getStarSize()));
                paramsMap.put("content",et_content.getText().toString());

                Subscriber mSubscriber = new Subscriber<String>(){

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(final String jsonStr) {
                        // 由于服务端的返回数据格式不固定，因此这里采用手动解析
                        RequestResultInfo resultInfo = JSON.parseObject(jsonStr, RequestResultInfo.class);
                        if ("0".equals(resultInfo.getCode())) {
                            Object o = resultInfo.getContent();
                            if (o != null){
                                PointComment pointComment = JSON.parseObject(o.toString(), PointComment.class);
                                if (pointComment.getList() != null) {

                                }
                            }
                        }

                    }
                };

                NetRequestManager.getInstance().getPointComments(paramsMap,mSubscriber);
            }
        });
        initStar();
    }
    private void initStar(){
        List<StarBarBean> starbars = new ArrayList<>();
        for (int i =0;i<5;i++){
            StarBarBean starBaarBean = new StarBarBean(R.mipmap.ic_pingjia_star_grey);
            starBaarBean.dividerHeight = SystemUtils.dip2px(mContext,5);
            starbars.add(starBaarBean);
        }
        vStressStar.setStarBarBeanList(starbars);
        vStressStar.setMwtStarOnclick(new OnStarClick(vStressStar));
        vEnviromentStar.setStarBarBeanList(starbars);
        vEnviromentStar.setMwtStarOnclick(new OnStarClick(vEnviromentStar));
        vSafetyStar.setStarBarBeanList(starbars);
        vSafetyStar.setMwtStarOnclick(new OnStarClick(vSafetyStar));
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addAddress:
                selectImage();
                break;
            case R.id.tag_add:
                Intent intent = new Intent(mContext, TagAddActivity.class);
                EnterActUtils.startForResultAct(mActivity, intent, 1);
                break;
        }
    }
    private void selectImage(){
        mImagePicker.setOnImagePickCompleteListener(this);
        mImagePicker.setSelectMode(AndroidImagePicker.Select_Mode.MODE_MULTI);
        mImagePicker.setShouldShowCamera(true);
        Intent intent = new Intent();
        intent.putExtra("isCrop", false);
        intent.setClass(this, ImagesGridActivity.class);
        EnterActUtils.startAct(mActivity, intent);

    }

    public void getExt() {
        remarkPoint = getIntent().getParcelableExtra("locationData");
    }

    class OnStarClick implements MWTStarOnclick{
        MWTStarBar mwtStarBar;

        OnStarClick(MWTStarBar mwtStarBar) {
            this.mwtStarBar = mwtStarBar;
        }

        @Override
        public void upStarIco(int selectSize) {
            List<StarBarBean> starbars = new ArrayList<>();
            for (int i =0;i<5;i++){
                StarBarBean starBaarBean = new StarBarBean(R.mipmap.ic_pingjia_star_grey);
                starBaarBean.dividerHeight = SystemUtils.dip2px(mContext,5);
                if (i < selectSize){
                    starBaarBean.pict =  R.mipmap.ic_pingjia_star_yello;
                }
                starbars.add(starBaarBean);
            }
            mwtStarBar.setStarBarBeanList(starbars);
        }
    }
    @Override
    public void onImagePickComplete(List<ImageItem> items) {

    }

    @Override
    public void onPictureTakeComplete(String picturePath) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK) {
                    HotTagBean tagBean = data.getParcelableExtra("data");
                    if (tagBean != null) {
                        List<HotUserTag> hotSelectTags = tagBean.hotSelectTags;
                        StringBuilder sb = new StringBuilder();
                        for (HotUserTag tag : hotSelectTags) {
                            sb.append(tag.tag);
                            sb.append(",");
                        }
                        sb.deleteCharAt(sb.length()-1);
                        vAddTag.setText(sb.toString());
                    }
                }
                break;
        }
    }
}
