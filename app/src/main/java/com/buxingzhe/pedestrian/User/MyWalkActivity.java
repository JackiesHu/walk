package com.buxingzhe.pedestrian.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;
import com.buxingzhe.pedestrian.bean.activity.WalkRecordInfo;
import com.buxingzhe.pedestrian.bean.activity.WalkRecordsInfo;
import com.buxingzhe.pedestrian.community.community.CommCircleAdapter;
import com.buxingzhe.pedestrian.community.community.WalkRecordDetailActivity;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;
import com.buxingzhe.pedestrian.listen.SwpipeListViewOnScrollListener;
import com.buxingzhe.pedestrian.utils.EnterActUtils;
import com.buxingzhe.pedestrian.utils.JsonParseUtil;
import com.buxingzhe.pedestrian.widget.TitleBarView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

import static com.buxingzhe.pedestrian.community.community.CommCircleFragment.WALKRECORDINFO;

public class MyWalkActivity extends BaseActivity {

    @BindView(R.id.mListViewW)
    PullToRefreshListView mListViewW;
    @BindView(R.id.swipeLayoutW)
    SwipeRefreshLayout swipeLayoutW;
    @BindView(R.id.emptyWalk)
    TextView emptyWalk;

    private CommCircleAdapter mAdapter;
    private int mPage = 1;
    private final static int pageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_walk);
        ButterKnife.bind(this);

        initView();
        loadData();
        setOnClick();



    }

    private void setOnClick() {
        mListViewW.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData();
            }
        });
        swipeLayoutW.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                loadData();
            }
        });

        mListViewW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position--;
                if (mAdapter != null && position >= 0) {
                    WalkRecordInfo walkRecordInfo = mAdapter.getItem(position);
                    enterWalkRecordDetailActivity(walkRecordInfo);
                }
            }
        });
    }


    private void enterWalkRecordDetailActivity(WalkRecordInfo data) {
        Intent intent = new Intent(MyWalkActivity.this, WalkRecordDetailActivity.class);
        intent.putExtra(WALKRECORDINFO, data);
        EnterActUtils.startAct(MyWalkActivity.this, intent);
    }

    private void loadData() {
        NetRequestManager.getInstance().getMyWalkRecords(baseApp.getUserToken(), baseApp.getUserId(), mPage, pageSize, new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                stopRefreshAnimation();
            }

            @Override
            public void onNext(String jsonStr) {
                // 由于服务端的返回数据格式不固定，因此这里采用手动解析
                Object[] datas = JsonParseUtil.getInstance().parseJsonList(jsonStr, WalkRecordsInfo.class);
                if ((Integer) datas[0] == 0) {
                    WalkRecordsInfo walkRecordsInfo = new Gson().fromJson(datas[1].toString(), WalkRecordsInfo.class);
                    if (walkRecordsInfo != null && walkRecordsInfo.getList() != null) {
                        mAdapter.setDatas(mPage, walkRecordsInfo.getList());
                        mPage++;
                    }
                } else {
                    Toast.makeText(mContext, datas[2].toString(), Toast.LENGTH_SHORT).show();
                }
                stopRefreshAnimation();
            }
        });
    }

    private void stopRefreshAnimation() {

        if (mListViewW != null) {
            mListViewW.onRefreshComplete();
        }
        if (swipeLayoutW != null) {
            swipeLayoutW.setRefreshing(false);
        }
    }

    private void initView() {
        vTitleBar = (TitleBarView) findViewById(R.id.title_bar);
        setTitle("我的步行");
        mListViewW.setMode(PullToRefreshBase.Mode.BOTH);
        SwpipeListViewOnScrollListener scrollListener = new SwpipeListViewOnScrollListener(swipeLayoutW);
        mListViewW.setOnScrollListener(scrollListener);
        swipeLayoutW.setColorSchemeResources(R.color.red);

        //暂无数据
        View emptyView = LayoutInflater.from(MyWalkActivity.this).inflate(R.layout.view_empty, null);
        emptyView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT));
        ((ViewGroup) mListViewW.getParent()).addView(emptyView);
        emptyWalk.setText(getString(R.string.activity_no_data));
        mListViewW.setEmptyView(emptyView);

        mAdapter = new CommCircleAdapter(MyWalkActivity.this, MyWalkActivity.this);
        mListViewW.setAdapter(mAdapter);
    }
}
