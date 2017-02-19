package com.buxingzhe.pedestrian.found;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;
import com.buxingzhe.pedestrian.common.StarBarBean;
import com.buxingzhe.pedestrian.widget.MWTStarBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jackie on 2017/2/11.
 */

public class WalkDetailsActivity extends BaseActivity implements View.OnClickListener{
    TextView vAddressName,vAddresscost,vAddressDetail,vAddressDepict;
    MWTStarBar vStressStar,vEnviromentStar,vSafetyStar;
    RecyclerView vRecyPcdepict;
    ImageView vDiscuss,vWalkBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        mContext = this;
        setContentView(R.layout.walk_detail);
        findId();
        setPcdepict();
        setClick();
    }
    private void findId() {
        vAddressName = (TextView)findViewById(R.id.walk_add_name);
        vAddresscost = (TextView) findViewById(R.id.walkde_cost);
        vAddressDetail = (TextView) findViewById(R.id.walkde_addr_detail);
        vStressStar = (MWTStarBar) findViewById(R.id.walkde_stress_star);
        vEnviromentStar = (MWTStarBar) findViewById(R.id.walkde_environment_star);
        vSafetyStar = (MWTStarBar) findViewById(R.id.walkde_safety_star);
        vAddressDepict = (TextView) findViewById(R.id.walkde_addr_depict);
        vRecyPcdepict = (RecyclerView) findViewById(R.id.walkde_pc_depict);
        vDiscuss = (ImageView) findViewById(R.id.iv_discuss);
        vWalkBack =  (ImageView) findViewById(R.id.walk_de_back);

        List<StarBarBean> starbars = new ArrayList<StarBarBean>();
        for (int i =0;i<5;i++){
            StarBarBean starBaarBean = new StarBarBean(R.mipmap.ic_xq_star_big_yello);
            starbars.add(starBaarBean);
        }
        vStressStar.setStarBarBeanList(starbars);
        vEnviromentStar.setStarBarBeanList(starbars);
        vSafetyStar.setStarBarBeanList(starbars);
    }

    private void setClick() {
        vDiscuss.setOnClickListener(this);
        vWalkBack.setOnClickListener(this);
    }
    private void setPcdepict(){
        LinearLayoutManager linearLayoutManger =  new LinearLayoutManager(mContext);
        linearLayoutManger.setOrientation(LinearLayoutManager.HORIZONTAL);
        vRecyPcdepict.setLayoutManager(linearLayoutManger);//这里用线性显示 类似于listview
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));//这里用线性宫格显示 类似于grid view
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));//这里用线性宫格显示 类似于瀑布流
        vRecyPcdepict.setAdapter(new RecyPcdepictAdapter());
    }
    class RecyPcdepictAdapter  extends RecyclerView.Adapter<RecyPcdepictAdapter.RecyPcdepictViewHolder>{
        @Override
        public RecyPcdepictAdapter.RecyPcdepictViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
            View view = mLayoutInflater.inflate(R.layout.walk_pic_depict, parent, false);
            return new RecyPcdepictAdapter.RecyPcdepictViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyPcdepictAdapter.RecyPcdepictViewHolder holder, int position) {

        }
        @Override
        public int getItemCount() {
            return 6;
        }
       public class RecyPcdepictViewHolder extends RecyclerView.ViewHolder {
           ImageView vMIvPic;
           RecyPcdepictViewHolder(View view) {
                super(view);
                vMIvPic = (ImageView) view.findViewById(R.id.pic_depict);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("NormalTextViewHolder", "onClick--> position = " + getPosition());
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_discuss:
                Intent intent = new Intent(mContext,WalkDetialsDiscussActivity.class);
                startActivity(intent);
                break;
            case R.id.walk_de_back:
                finish();
                break;
        }
    }
}
