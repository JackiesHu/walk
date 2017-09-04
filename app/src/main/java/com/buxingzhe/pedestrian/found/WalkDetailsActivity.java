package com.buxingzhe.pedestrian.found;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.activity.BaseActivity;
import com.buxingzhe.pedestrian.baiduView.WalkingRouteOverlay;
import com.buxingzhe.pedestrian.bean.RequestResultInfo;
import com.buxingzhe.pedestrian.common.StarBarBean;
import com.buxingzhe.pedestrian.found.adapter.PointCommentAdapter;
import com.buxingzhe.pedestrian.found.bean.Comment;
import com.buxingzhe.pedestrian.found.bean.PageContent;
import com.buxingzhe.pedestrian.found.bean.RemarkPoint;
import com.buxingzhe.pedestrian.found.bean.WalkRecord;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;
import com.buxingzhe.pedestrian.utils.PicassManager;
import com.buxingzhe.pedestrian.widget.MWTStarBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by jackie on 2017/2/11.
 */

public class WalkDetailsActivity extends BaseActivity implements View.OnClickListener {
    TextView vAddressName, vAddresscost, vAddressDetail, vAddressDepict, tv_star;
    MWTStarBar vStressStar, vEnviromentStar, vSafetyStar;
    RecyclerView vRecyPcdepict;
    ImageView vDiscuss, vWalkBack;
    private RemarkPoint remarkPoint;
    private MapView vMapView;
    private RecyclerView recycle_point_comments;
    private BaiduMap mBaidumap = null;
    private RoutePlanSearch mSearch;
    private LatLng myLocation;
    public MyLocationListenner myListener = new MyLocationListenner();
    private LocationClient mLocClient;
    private RefreshThread refreshThread = null;  //刷新地图线程以获取实时点
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        mContext = this;
        setContentView(R.layout.walk_detail);
        getExtr();
        findId();
        initMapView();
        setClick();
        if (remarkPoint != null) {
            setPcdepict();
            setData();
            loadComments();
        }

        //定位
        // 定位初始化
        mLocClient = new LocationClient(WalkDetailsActivity.this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        if (refreshThread == null) {
            refreshThread = new RefreshThread();
        }
        // refreshThread.start();
    }

    private class RefreshThread extends Thread {


        protected boolean refresh = true;

        public void run() {

            while (refresh) {
                mLocClient.start();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
            }

        }

    }

    private void loadComments() {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("remarkPoint", remarkPoint.getId());
        paramsMap.put("token", baseApp.getUserToken());
        paramsMap.put("userId", baseApp.getUserId());

        Subscriber mSubscriber = new Subscriber<String>() {

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
                    if (o != null) {
                        PageContent pointComment = JSON.parseObject(o.toString(), PageContent.class);
                        if (pointComment.getList() != null) {
                            List<Comment> comments = JSON.parseArray(pointComment.getList().toString(), Comment.class);
                            LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                            recycle_point_comments.setLayoutManager(manager);
                            PointCommentAdapter adapter = new PointCommentAdapter(mContext, comments, R.layout.item_walkdetail_discuss);
                            recycle_point_comments.setAdapter(adapter);
                        }
                    }
                }

            }
        };

        NetRequestManager.getInstance().getPointComments(paramsMap, mSubscriber);
    }

    private void setData() {
        if (remarkPoint != null) {
            vAddressName.setText(remarkPoint.getTitle());
//        vAddresscost.setText(remarkPoint.get);
            vAddressDepict.setText(remarkPoint.getIntroduction());
            tv_star.setText(remarkPoint.getBrief());
            List<StarBarBean> starbars = new ArrayList<>();
            for (int i = 0; i < remarkPoint.getStreetStar(); i++) {
                StarBarBean starBaarBean = new StarBarBean(R.mipmap.ic_xq_star_big_yello);
                starbars.add(starBaarBean);
            }
            vStressStar.setStarBarBeanList(starbars);
            starbars = new ArrayList<>();
            for (int i = 0; i < remarkPoint.getEnvirStar(); i++) {
                StarBarBean starBaarBean = new StarBarBean(R.mipmap.ic_xq_star_big_yello);
                starbars.add(starBaarBean);
            }
            vEnviromentStar.setStarBarBeanList(starbars);
            starbars = new ArrayList<>();
            for (int i = 0; i < remarkPoint.getSafeStar(); i++) {
                StarBarBean starBaarBean = new StarBarBean(R.mipmap.ic_xq_star_big_yello);
                starbars.add(starBaarBean);
            }
            vSafetyStar.setStarBarBeanList(starbars);
        }
    }

    private void findId() {
        vAddressName = (TextView) findViewById(R.id.walk_add_name);
        vAddresscost = (TextView) findViewById(R.id.walkde_cost);
        vAddressDetail = (TextView) findViewById(R.id.walkde_addr_detail);
        tv_star = (TextView) findViewById(R.id.tv_star);
        vStressStar = (MWTStarBar) findViewById(R.id.walkde_stress_star);
        vEnviromentStar = (MWTStarBar) findViewById(R.id.walkde_environment_star);
        vSafetyStar = (MWTStarBar) findViewById(R.id.walkde_safety_star);
        vAddressDepict = (TextView) findViewById(R.id.walkde_addr_depict);
        vRecyPcdepict = (RecyclerView) findViewById(R.id.walkde_pc_depict);
        vDiscuss = (ImageView) findViewById(R.id.iv_discuss);
        vWalkBack = (ImageView) findViewById(R.id.walk_de_back);
        vMapView = (MapView) findViewById(R.id.walkDetail_map);
        recycle_point_comments = (RecyclerView) findViewById(R.id.recycle_point_comments);
        mBaidumap = vMapView.getMap();


    }

    private void setClick() {
        vDiscuss.setOnClickListener(this);
        vWalkBack.setOnClickListener(this);
    }

    private void initMapView() {
        mBaidumap = vMapView.getMap();
        vMapView.showZoomControls(false);
        MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(15);
        mBaidumap.animateMapStatus(u);

        searchRouteProcess();
    }

    private void getExtr() {
        Object locationData = getIntent().getParcelableExtra("locationData");
        if (locationData instanceof WalkRecord) {
            remarkPoint = (WalkRecord) locationData;
        } else if (locationData instanceof RemarkPoint) {
            remarkPoint = (RemarkPoint) locationData;
        }
        myLocation = getIntent().getParcelableExtra("myLocation");
    }

    /**
     * 发起路线规划搜索
     */
    public void searchRouteProcess() {
        // 初始化搜索模块，注册事件监听

        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(myGetRoutePlan);
        // 重置浏览节点的路线数据
        // 设置起终点信息，对于tranist search 来说，城市名无意义
        PlanNode stNode = PlanNode.withLocation(myLocation);
        if (remarkPoint != null) {
            PlanNode enNode = PlanNode.withLocation(new LatLng(remarkPoint.getLatitude(), remarkPoint.getLongitude()));

            mSearch.walkingSearch((new WalkingRoutePlanOption())
                    .from(stNode).to(enNode));

        }

    }

    private void setPcdepict() {
        LinearLayoutManager linearLayoutManger = new LinearLayoutManager(mContext);
        linearLayoutManger.setOrientation(LinearLayoutManager.HORIZONTAL);
        vRecyPcdepict.setLayoutManager(linearLayoutManger);//这里用线性显示 类似于listview
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));//这里用线性宫格显示 类似于grid view
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));//这里用线性宫格显示 类似于瀑布流
        if (remarkPoint != null) {
            if (remarkPoint.getViews() != null) {
                String[] split = remarkPoint.getViews().split(";");
                vRecyPcdepict.setAdapter(new RecyPcdepictAdapter(split));
            }
        }
    }

    class RecyPcdepictAdapter extends RecyclerView.Adapter<RecyPcdepictAdapter.RecyPcdepictViewHolder> {
        private String[] split;

        public RecyPcdepictAdapter(String[] split) {
            this.split = split;
        }

        @Override
        public RecyPcdepictAdapter.RecyPcdepictViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
            View view = mLayoutInflater.inflate(R.layout.walk_pic_depict, parent, false);
            return new RecyPcdepictAdapter.RecyPcdepictViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyPcdepictAdapter.RecyPcdepictViewHolder holder, int position) {
            PicassManager.getInstance().load(mContext, split[position], holder.vMIvPic);
        }

        @Override
        public int getItemCount() {
            return split.length;
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
        switch (view.getId()) {
            case R.id.iv_discuss:
                Intent intent = new Intent(mContext, PointCommentActivity.class);
                intent.putExtra("locationData", remarkPoint);
                startActivity(intent);
                break;
            case R.id.walk_de_back:
                finish();
                break;
        }
    }

    OnGetRoutePlanResultListener myGetRoutePlan = new OnGetRoutePlanResultListener() {
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(mContext, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                // result.getSuggestAddrInfo()
                return;
            }
            if (result.getRouteLines() != null) {
                if (result.getRouteLines().size() == 1) {
                    // 直接显示
                    WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaidumap);
                    mBaidumap.setOnMarkerClickListener(overlay);
                    overlay.setData(result.getRouteLines().get(0));
                    overlay.addToMap();
                    overlay.zoomToSpan();

                }
                if (result.getRouteLines().size() > 1) {

                } else {
                    Log.d("route result", "结果数<0");
                    return;
                }
            }
        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

        }

        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

        }
    };

    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
           /* if (useDefaultIcon) {*/
            return BitmapDescriptorFactory.fromResource(R.mipmap.ic_luxian_start);
            /*}
            return null;*/
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
           /* if (useDefaultIcon) {*/
            return BitmapDescriptorFactory.fromResource(R.mipmap.ic_luxian_end);
            /*}
            return null;*/
        }
    }

    protected void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || vMapView == null) {
                return;
            }

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaidumap.setMyLocationData(locData);

            myLocation = new LatLng(location.getLatitude(),
                    location.getLongitude());


        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        refreshThread = null;
    }
}
