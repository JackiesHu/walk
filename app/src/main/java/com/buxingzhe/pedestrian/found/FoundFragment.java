package com.buxingzhe.pedestrian.found;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
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
import com.buxingzhe.pedestrian.baiduView.OverlayManager;
import com.buxingzhe.pedestrian.baiduView.WalkingRouteOverlay;
import com.buxingzhe.pedestrian.bean.AddressSuggLoca;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;
import com.buxingzhe.pedestrian.utils.EnterActUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;


/**
 * Created by quanjing on 2017/2/5.
 */
public class FoundFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private Toolbar toolbar;
    private RelativeLayout vMunuSearch;
    private BikeNavigateHelper mNaviHelper;
    private MapView vMapView;
    private BaiduMap mBaidumap = null;
    // 初始化全局 bitmap 信息，不用时及时 recycle
    BitmapDescriptor bdnor = BitmapDescriptorFactory
            .fromResource(R.mipmap.ic_map_tuijian_nor);
    BitmapDescriptor bdPre = BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_tuijian_pre);
    private ImageView vImageViewPin;
    private LocationClient mLocClient;
    boolean isFirstLoc = true; // 是否首次定位
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private Marker prevMarker;
    private RouteLine route = null;
    private LinearLayout vTraffic;

    // 搜索相关
    RoutePlanSearch mSearch = null;
    OverlayManager routeOverlay = null;
    private boolean isCloseTraffic;
    private ImageView vImageTraffic;
    public FoundFragment() {

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_found, null);
        mContext = getContext();
        findViewId(view);
        onClick();
        initBaiduMap();
        searchRouteProcess();
        loadData();
        return view;
    }

    private void loadData() {
        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("longitude","116.400244");
        paramsMap.put("latitude","39.993175");
        paramsMap.put("distance","500");

        Subscriber mSubscriber = new Subscriber<String>(){

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        };

        NetRequestManager.getInstance().getStreets(paramsMap, mSubscriber);
    }

    private void onClick() {
        vMunuSearch.setOnClickListener(this);
        mBaidumap.setOnMarkerClickListener(markerClickListener);
        vTraffic.setOnClickListener(this);
    }

    public void findViewId(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        vMunuSearch = (RelativeLayout) view.findViewById(R.id.rl_munu_search);
        vMapView = (MapView) view.findViewById(R.id.map);
        vTraffic = (LinearLayout)view.findViewById(R.id.traffic);
        vImageTraffic = (ImageView) view.findViewById(R.id.iv_traffic);
        mBaidumap = vMapView.getMap();
        toolbar.setTitle("发现");

        setHasOptionsMenu(true);// 标题的文字需在setSupportActionBar之前，不然会无效
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }
    /**
     * 发起路线规划搜索
     */
    public void searchRouteProcess() {
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(myGetRoutePlan);
        // 重置浏览节点的路线数据
        route = null;
        // 设置起终点信息，对于tranist search 来说，城市名无意义
        PlanNode stNode = PlanNode.withLocation(new LatLng(39.993175,116.400244)) ;
        PlanNode enNode = PlanNode.withLocation(new LatLng(39.942821,116.369199)) ;

        mSearch.walkingSearch((new WalkingRoutePlanOption())
                .from(stNode).to(enNode));

    }
    private void initBaiduMap(){
        MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(20);
        mBaidumap.animateMapStatus(u);
        initLocation();
        initOverlay();
        setTraffic();
    }
    public void initOverlay() {
        List<AddressSuggLoca> suggLocas = createSuggData();
        for (AddressSuggLoca location : suggLocas){
            LatLng llA = new LatLng(location.lat, location.lng);
            MarkerOptions ooA = new MarkerOptions().position(llA).icon(bdnor)
                    .zIndex(9).draggable(false);
            mBaidumap.addOverlay(ooA);
        }
    }

    private ImageView createView(){
        if (vImageViewPin == null){
            vImageViewPin = new ImageView(getContext());
            ViewGroup.LayoutParams par = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            vImageViewPin.setLayoutParams(par);
            vImageViewPin.setImageResource(R.mipmap.ic_map_pin_red);
        }
        return vImageViewPin;
    }
    private void initLocation(){
        // 开启定位图层
        mBaidumap.setMyLocationEnabled(true);
        mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
        mBaidumap.setMyLocationConfigeration(new MyLocationConfiguration(
                mCurrentMode, true, null));

        // 定位初始化
        mLocClient = new LocationClient(getContext());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }
    /**
     * 设置是否显示交通图
     */
    public void setTraffic() {
        mBaidumap.setTrafficEnabled(!isCloseTraffic);
        if (isCloseTraffic){
            vImageTraffic.setImageResource(R.mipmap.ic_lukuang_press);
        }else {
            vImageTraffic.setImageResource(R.mipmap.ic_lukuang_nor);
        }
    }
    BaiduMap.OnMarkerClickListener markerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            LatLng ll = marker.getPosition();
            int height = marker.getIcon().getBitmap().getHeight();
            ImageView view = createView();
            InfoWindow mInfoWindow = new InfoWindow(view, ll, -height+20);
            mBaidumap.showInfoWindow(mInfoWindow);
            marker.setIcon(bdPre);
            if (prevMarker != null){
                prevMarker.setIcon(bdnor);
            }
            prevMarker = marker;
            double lat = ll.latitude;
            double lng =  ll.longitude;
            AddressSuggLoca addressSuggLoca = new AddressSuggLoca();
            addressSuggLoca.lat = lat;
            addressSuggLoca.lng = lng;
            goWalkDetail(addressSuggLoca);
            return false;
        }
    };
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
            if ( result.getRouteLines().size() == 1 ) {
                // 直接显示
                route = result.getRouteLines().get(0);
                WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaidumap);
                mBaidumap.setOnMarkerClickListener(overlay);
                routeOverlay = overlay;
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();

            }if (result.getRouteLines().size() > 1 ) {


            }else {
                Log.d("route result", "结果数<0");
                return;
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
                return BitmapDescriptorFactory.fromResource(R.mipmap.ic_luxian_now);
            /*}
            return null;*/
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
           /* if (useDefaultIcon) {*/
                return BitmapDescriptorFactory.fromResource(R.mipmap.ic_luxian_start);
            /*}
            return null;*/
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
            if (isFirstLoc) {
                isFirstLoc = false;

                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                mBaidumap.setMyLocationData(locData);

                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaidumap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
    private void goWalkDetail(AddressSuggLoca suggLoca){
        Intent intent = new Intent(getActivity(),WalkDetailsActivity.class);
        intent.putExtra("loactionData",suggLoca);
        EnterActUtils.startAct(getActivity(),intent);
    }
    private List<AddressSuggLoca> createSuggData(){
        List<AddressSuggLoca> vAddres = new ArrayList<AddressSuggLoca>();

            AddressSuggLoca location1 = new AddressSuggLoca();
            location1.lat = 39.993175;
            location1.lng = 116.400244;
            vAddres.add(location1);
            AddressSuggLoca location2 = new AddressSuggLoca();
            location2.lat = 39.942821;
            location2.lng = 116.369199;
            vAddres.add(location2);
            AddressSuggLoca location3 = new AddressSuggLoca();
            location3.lat = 39.919723;
            location3.lng = 116.425541;
            vAddres.add(location3);
            AddressSuggLoca location4 = new AddressSuggLoca();
            location4.lat = 39.906965;
            location4.lng = 116.371394;
            vAddres.add(location4);

            AddressSuggLoca location5 = new AddressSuggLoca();
            location5.lat = 40.906965;
            location5.lng = 116.501394;
            vAddres.add(location5);

            AddressSuggLoca location6 = new AddressSuggLoca();
            location6.lat = 39.706965;
            location6.lng = 116.401394;
            vAddres.add(location6);
        return vAddres;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.rl_munu_search:
                Intent intent = new Intent(getContext(),ActivitySearchFound.class);
                startActivity(intent);
                break;
            case R.id.traffic:
                isCloseTraffic = !isCloseTraffic;
                setTraffic();
                break;
        }
    }
    @Override
    public void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        vMapView.onPause();
        super.onPause();
    }
    @Override
    public void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        vMapView.onResume();
        super.onResume();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaidumap.setMyLocationEnabled(false);
        vMapView.onDestroy();
        vMapView = null;
    }

}
