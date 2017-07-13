package com.buxingzhe.pedestrian.found;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;
import com.buxingzhe.pedestrian.bean.HotUserTag;
import com.buxingzhe.pedestrian.bean.RequestResultInfo;
import com.buxingzhe.pedestrian.common.GlobalParams;
import com.buxingzhe.pedestrian.common.StarBarBean;
import com.buxingzhe.pedestrian.found.adapter.PicAdapter;
import com.buxingzhe.pedestrian.found.bean.HotTagBean;
import com.buxingzhe.pedestrian.found.bean.RemarkPoint;
import com.buxingzhe.pedestrian.found.bean.Tag;
import com.buxingzhe.pedestrian.found.tag.TagAddActivity;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;
import com.buxingzhe.pedestrian.utils.EnterActUtils;
import com.buxingzhe.pedestrian.utils.SystemUtils;
import com.buxingzhe.pedestrian.widget.FullGridView;
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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    private FullGridView gv_pic ;
    private List<HotUserTag> hotSelectTags;
    private RemarkPoint remarkPoint;
    private int type;
    private List<String> pics = new ArrayList<>();;
    private List<String> picNames = new ArrayList<>();;
    private List<MultipartBody.Part> picParts = new ArrayList<>();


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
        tv_address.setText(remarkPoint.getTitle());
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
        gv_pic = (FullGridView) findViewById(R.id.gv_pic);
        if (type == 0) {
            setTitle("推荐");
        }else {
            setTitle("吐槽");
        }
        setRightTitle("完成");
        initStar();
    }

    @Override
    public void onRightListener(View v) {
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("title",remarkPoint.getTitle());
        paramsMap.put("type",String.valueOf(type));
        paramsMap.put("userId",GlobalParams.USER_ID);
        paramsMap.put("token", GlobalParams.TOKEN);
        paramsMap.put("streetStar",String.valueOf(vStressStar.getStarSize()));
        paramsMap.put("envirStar",String.valueOf(vEnviromentStar.getStarSize()));
        paramsMap.put("safeStar",String.valueOf(vSafetyStar.getStarSize()));
        paramsMap.put("longitude",String.valueOf(remarkPoint.getLongitude()));
        paramsMap.put("latitude",String.valueOf(remarkPoint.getLatitude()));
        paramsMap.put("brief",et_content.getText().toString());
        paramsMap.put("introduction",et_content.getText().toString());
        paramsMap.put("remarkPointTags",vAddTag.getText().toString());


        if (pics != null) {
            for(int i=0;i<pics.size();i++){
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), pics.get(i));

                MultipartBody.Part  filePart = MultipartBody.Part.createFormData("viewUrls", picNames.get(i), requestFile);
                picParts.add(filePart);
            }


        } else {
            picParts = null;
        }


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
                    Toast.makeText(mContext,"评价成功",Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        };

        NetRequestManager.getInstance().foundComment(paramsMap,picParts,mSubscriber);
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
        type = getIntent().getIntExtra("type",0);
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
        pics.clear();
        if (items != null && items.size() > 0) {
            for (ImageItem item : items){
                pics.add(item.path);
                picNames.add(item.name);
            }
        }
        PicAdapter adapter = new PicAdapter(mContext,pics,R.layout.item_pic);
        gv_pic.setAdapter(adapter);
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
                    ArrayList<Tag> tags = (ArrayList<Tag>) data.getSerializableExtra("data");
                    if (tags != null) {
                        StringBuilder sb = new StringBuilder();
                        for (Tag tag : tags) {
                            sb.append(tag.getName());
                            sb.append(";");
                        }
                        if (sb!=null&&sb.length() > 0)
                            sb.deleteCharAt(sb.length()-1);
                        vAddTag.setText(sb.toString());
                    }
                }
                break;
        }
    }
}
