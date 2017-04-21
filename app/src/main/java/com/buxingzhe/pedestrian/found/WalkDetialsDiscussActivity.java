package com.buxingzhe.pedestrian.found;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;
import com.buxingzhe.pedestrian.bean.HotUserTag;
import com.buxingzhe.pedestrian.common.StarBarBean;
import com.buxingzhe.pedestrian.found.bean.HotTagBean;
import com.buxingzhe.pedestrian.found.tag.TagAddActivity;
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
import java.util.List;

/**·
 * Created by jackie on 2017/2/12.
 */

public class WalkDetialsDiscussActivity extends BaseActivity implements View.OnClickListener,
        AndroidImagePicker.OnPictureTakeCompleteListener,AndroidImagePicker.OnImagePickCompleteListener{
    private LinearLayout vAddress;
    private AndroidImagePicker mImagePicker;
    private MWTStarBar vStressStar,vEnviromentStar,vSafetyStar;
    private TextView vAddTag;
    private FlowLayout vFlowLayout;
    private List<HotUserTag> hotSelectTags;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wakl_discuss);
        findId();
        setOnclick();
        mImagePicker = AndroidImagePicker.getInstance();
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

        vFlowLayout = (FlowLayout) findViewById(R.id.localTag);

        vAddTag =(TextView)findViewById(R.id.tag_add);
        setTitle("评论");
        initStar();
    }
    private void initStar(){
        List<StarBarBean> starbars = new ArrayList<StarBarBean>();
        for (int i =0;i<5;i++){
            StarBarBean starBaarBean = new StarBarBean(R.mipmap.ic_pingjia_star_grey);
            starBaarBean.height = SystemUtils.dip2px(mContext,88);
            starBaarBean.width = SystemUtils.dip2px(mContext,88);
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
               Object o = data.getSerializableExtra("data");
                if (o != null){
                    HotTagBean tagBean = (HotTagBean) data.getSerializableExtra("data");
                    List<HotUserTag> hotSelectTags = tagBean.hotSelectTags;

                }
                break;
        }
    }
}
