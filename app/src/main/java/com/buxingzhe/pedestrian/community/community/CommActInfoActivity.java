package com.buxingzhe.pedestrian.community.community;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;
import com.buxingzhe.pedestrian.activity.BaseAdapter;
import com.buxingzhe.pedestrian.activity.ILoadCallback;
import com.buxingzhe.pedestrian.activity.LoadMoreAdapterWrapper;
import com.buxingzhe.pedestrian.activity.OnLoad;
import com.buxingzhe.pedestrian.bean.activity.WalkActivitiesInfo;
import com.buxingzhe.pedestrian.bean.activity.WalkActivityInfo;
import com.buxingzhe.pedestrian.bean.activity.WalkRecordsByActivity;
import com.buxingzhe.pedestrian.common.GlobalParams;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;
import com.buxingzhe.pedestrian.utils.JsonParseUtil;
import com.buxingzhe.pedestrian.utils.PicassManager;
import com.buxingzhe.pedestrian.utils.SystemUtils;
import com.buxingzhe.pedestrian.widget.TitleBarView;
import com.google.gson.Gson;

import rx.Subscriber;

/**
 * Created by jackie on 2017/3/12.
 */

public class CommActInfoActivity extends BaseActivity implements View.OnClickListener {
    private WalkActivityInfo walkActivityInfo;
    private SwipeRefreshLayout mRefresh;
    private RecyclerView vRecyclerView;
    private BaseAdapter mAdapter;
    private CommActInfoAdapter commActInfoAdapter;
    private int currentIndex = 1;
    private final static int pageSize = 10;

    private View headView;
    private ImageView iv_banner;
    private TextView tv_attend;
    private RelativeLayout introductionRL;
    private TextView tv_introduction;
    private ImageView iv_up;
    private ImageView iv_down;
    private String simple_introdution = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commact_info);
        mContext = this;
        walkActivityInfo = (WalkActivityInfo) getIntent().getSerializableExtra(CommActFragment.WALKACTIVITYINFO);
        initView();
        setData();
        initPullRefresh();
        setTitleBar();
//        setHeaderView(vRecyclerView);
    }

    private void initView() {
        vTitleBar = (TitleBarView) findViewById(R.id.actinfo_title_bar);
        mRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        mRefresh.setColorSchemeResources(R.color.red);
        vRecyclerView = (RecyclerView) findViewById(R.id.detail_list);

//        commActInfoAdapter = new CommActInfoAdapter(this, mContext);
//        LinearLayoutManager linearLayoutManger =  new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        vRecyclerView.setLayoutManager(linearLayoutManger);//这里用线性显示 类似于listview
//        vRecyclerView.setAdapter(mAdapter);


//        initHeadView();
    }

    private void setTitleBar() {
        if (!TextUtils.isEmpty(walkActivityInfo.getTitle())) {
            setTitle(walkActivityInfo.getTitle());
        }
        setRightIco(R.mipmap.ic_shequ_share);
    }

    private void initHeadView() {
        headView = View.inflate(mContext, R.layout.activity_info_head, null);
        iv_banner = (ImageView) headView.findViewById(R.id.iv_banner);
        tv_attend = (TextView) headView.findViewById(R.id.tv_attend);
        introductionRL = (RelativeLayout) headView.findViewById(R.id.introductionRL);
        tv_introduction = (TextView) headView.findViewById(R.id.tv_introduction);
        iv_up = (ImageView) headView.findViewById(R.id.iv_up);
        iv_down = (ImageView) headView.findViewById(R.id.iv_down);

//        vRecyclerView.addHeaderView(headView);
//        setHeadData();
    }

    private void setHeaderView(RecyclerView view) {
        View header = LayoutInflater.from(this).inflate(R.layout.activity_info_head, view,false);
//        commActInfoAdapter.setHeaderView(header);
    }

    private void setHeadData() {
        if (walkActivityInfo == null) {
            return;
        }
        if (!TextUtils.isEmpty(walkActivityInfo.getBanner())) {
            PicassManager.getInstance().load(mContext, walkActivityInfo.getBanner(), iv_banner);
        }
        if (!TextUtils.isEmpty(walkActivityInfo.getIsOutDate())) {
            //活动是否过期，0 没有 1 有
            if (walkActivityInfo.getIsOutDate().equals("0")) {
                tv_attend.setText(getString(R.string.activity_unoutdate));
                tv_attend.setBackgroundResource(R.drawable.bg_unoutdate);
                tv_attend.setOnClickListener(this);
            }
            if (walkActivityInfo.getIsOutDate().equals("1")) {
                tv_attend.setText(getString(R.string.activity_over));
                tv_attend.setBackgroundResource(R.drawable.bg_outdate);
                tv_attend.setOnClickListener(null);
            }
        }
        if (!TextUtils.isEmpty(walkActivityInfo.getIntroduction())) {
            setContentText(walkActivityInfo.getIntroduction());
        } else {
            introductionRL.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tv_attend:
                break;
            case R.id.iv_up:
                isShowAllIntro(false);
                break;
            case R.id.iv_down:
                isShowAllIntro(true);
                break;
        }
    }


    /**
     * 只显示三行，超过三行显示“全文”按钮
     *
     * @param str
     */
    private void setContentText(String str) {
        str = "民网悉尼4月18日电据澳大利亚广播公司报道，澳大利亚总理特恩布尔18日宣布，人民民网悉尼4月18日电据澳大利亚广播公司报道，澳大利亚总理特恩布尔18日宣布，人民民网悉尼4月18日电据澳大利亚广播公司报道，澳大利亚总理特恩布尔18日宣布，人民人民网悉尼4月18日电据澳大利亚广播公司报道，澳大利亚总理特恩布尔18日宣布，人民网悉尼4月18日电据澳大利亚广播公司报道，澳大利亚总理特恩布尔18日宣布，澳联邦政为增加本地就业机会，申请两年临时技术签证的工作种类也将有所减少。（实习生 唐艺瑜）";
        String newstr = "";
        int scWidth = SystemUtils.getDisplayWidth(mContext)[0];
        int tvWidth = scWidth - SystemUtils.dip2px(mContext, 30);
        int textsize = SystemUtils.sp2px(mContext, 14);
        int linecount = tvWidth / textsize;

        if (str.length() > linecount * 3) {
            newstr = str.substring(0, linecount * 3);
            simple_introdution = newstr + "...\n";
            isShowAllIntro(false);
        } else {
            tv_introduction.setText(walkActivityInfo.getIntroduction());
            iv_up.setVisibility(View.GONE);
            iv_down.setVisibility(View.GONE);
        }
    }

    /**
     * 是否展示所有的介绍
     *
     * @param isFlag
     */
    private void isShowAllIntro(boolean isFlag) {
        if (isFlag) {
            if (!TextUtils.isEmpty(walkActivityInfo.getIntroduction())) {
                tv_introduction.setText(walkActivityInfo.getIntroduction());
            }
        } else {
            iv_up.setVisibility(View.GONE);
            iv_down.setVisibility(View.VISIBLE);
            tv_introduction.setText(simple_introdution);
        }
    }


    private void setData() {
//        //创建被装饰者类实例
        commActInfoAdapter = new CommActInfoAdapter(this, mContext);

        //创建装饰者实例，并传入被装饰者和回调接口
        mAdapter = new LoadMoreAdapterWrapper(commActInfoAdapter, new OnLoad() {
            @Override
            public void load(int pagePosition, int pageSize, final ILoadCallback callback) {
                NetRequestManager.getInstance().getWalkRecordsByActivity(GlobalParams.USER_ID, walkActivityInfo.getId(), pagePosition, pageSize, new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure();
                    }

                    @Override
                    public void onNext(String jsonStr) {
                        // 由于服务端的返回数据格式不固定，因此这里采用手动解析
                        Object[] datas = JsonParseUtil.getInstance().parseJsonList(jsonStr, WalkRecordsByActivity.class);
                        if ((Integer) datas[0] == 0) {
                            WalkRecordsByActivity walkRecordsByActivity = new Gson().fromJson(datas[1].toString(), WalkRecordsByActivity.class);
                            if (walkRecordsByActivity != null && walkRecordsByActivity.getList() != null) {
                                commActInfoAdapter.appendData(walkRecordsByActivity.getList());
                                callback.onSuccess();
                            }
                        } else {
                            callback.onFailure();
                        }
                    }
                });
            }
        });

        LinearLayoutManager linearLayoutManger = new LinearLayoutManager(this);
        vRecyclerView.setLayoutManager(linearLayoutManger);//这里用线性显示 类似于listview
        vRecyclerView.setAdapter(mAdapter);
    }


    private void initPullRefresh() {
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NetRequestManager.getInstance().getActivities(currentIndex, pageSize, new Subscriber<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onNext(String jsonStr) {
                                // 由于服务端的返回数据格式不固定，因此这里采用手动解析
                                Object[] datas = JsonParseUtil.getInstance().parseJsonList(jsonStr, WalkActivitiesInfo.class);
                                if ((Integer) datas[0] == 0) {
                                    WalkActivitiesInfo walkActivitiesInfo = new Gson().fromJson(datas[1].toString(), WalkActivitiesInfo.class);
                                    if (walkActivitiesInfo != null && walkActivitiesInfo.getList() != null) {
//                                        new CommActAdapter(getActivity(),mContext).updateData(walkActivitiesInfo.getList());
                                        commActInfoAdapter.updateData(walkActivitiesInfo.getList());
                                    }
                                }
                            }
                        });
                        //刷新完成
                        mRefresh.setRefreshing(false);
                    }

                }, 2000);

            }
        });
    }

}
