package com.buxingzhe.pedestrian.found;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.model.LatLng;
import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;
import com.buxingzhe.pedestrian.bean.RequestResultInfo;
import com.buxingzhe.pedestrian.common.GlobalParams;
import com.buxingzhe.pedestrian.common.StarBarBean;
import com.buxingzhe.pedestrian.found.bean.RemarkPoint;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;
import com.buxingzhe.pedestrian.utils.SystemUtils;
import com.buxingzhe.pedestrian.widget.MWTStarBar;
import com.buxingzhe.pedestrian.widget.MWTStarOnclick;
import com.buxingzhe.pedestrian.widget.TitleBarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

public class PointCommentActivity extends BaseActivity {

    private RemarkPoint remarkPoint;
    private LatLng myLocation;
    private TextView tv_address;
    private MWTStarBar walked_stress_star;
    private MWTStarBar walked_environment_star;
    private MWTStarBar walked_safety_star;
    private EditText et_content;

    class OnStarClick implements MWTStarOnclick {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_comment);
        getExtr();
        findId();
        setData();
    }

    private void setData() {
        tv_address.setText(remarkPoint.getTitle());
        List<StarBarBean> starbars = new ArrayList<>();
        for (int i =0;i<5;i++){
            StarBarBean starBaarBean = new StarBarBean(R.mipmap.ic_pingjia_star_grey);
            starBaarBean.dividerHeight = SystemUtils.dip2px(mContext,5);
            starbars.add(starBaarBean);
        }
        walked_stress_star.setStarBarBeanList(starbars);
        walked_stress_star.setMwtStarOnclick(new OnStarClick(walked_stress_star));
        walked_environment_star.setStarBarBeanList(starbars);
        walked_environment_star.setMwtStarOnclick(new OnStarClick(walked_environment_star));
        walked_safety_star.setStarBarBeanList(starbars);
        walked_safety_star.setMwtStarOnclick(new OnStarClick(walked_safety_star));

        setTitle("评论");
        setRightTitle("完成");
    }

    @Override
    public void onRightListener(View v) {
        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("userId", GlobalParams.USER_ID);
        paramsMap.put("token", GlobalParams.TOKEN);
        paramsMap.put("remarkPoint",remarkPoint.getId());
        paramsMap.put("streetStar",String.valueOf(walked_stress_star.getStarSize()));
        paramsMap.put("envirStar",String.valueOf(walked_environment_star.getStarSize()));
        paramsMap.put("safeStar",String.valueOf(walked_safety_star.getStarSize()));
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
                    Toast.makeText(mContext,"评价成功",Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        };

        NetRequestManager.getInstance().pointComment(paramsMap,mSubscriber);
    }

    private void findId() {
        tv_address = (TextView)findViewById(R.id.tv_address);
        walked_stress_star = (MWTStarBar)findViewById(R.id.walked_stress_star);
        walked_environment_star = (MWTStarBar)findViewById(R.id.walked_environment_star);
        walked_safety_star = (MWTStarBar)findViewById(R.id.walked_safety_star);
        et_content = (EditText) findViewById(R.id.et_content);
        vTitleBar = (TitleBarView) findViewById(R.id.title_bar);
    }

    private void getExtr() {
        remarkPoint = getIntent().getParcelableExtra("locationData");
        myLocation = getIntent().getParcelableExtra("myLocation");
    }
}
