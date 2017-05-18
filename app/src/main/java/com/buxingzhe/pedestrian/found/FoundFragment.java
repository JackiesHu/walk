package com.buxingzhe.pedestrian.found;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.buxingzhe.pedestrian.R;
import com.buxingzhe.pedestrian.bean.RequestResultInfo;
import com.buxingzhe.pedestrian.found.bean.RemarkPoint;
import com.buxingzhe.pedestrian.found.bean.Streets;
import com.buxingzhe.pedestrian.http.manager.NetRequestManager;
import com.buxingzhe.pedestrian.utils.EnterActUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;



/**
 * Created by quanjing on 2017/2/5.
 *
 */
public class FoundFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private Toolbar toolbar;
    private RelativeLayout vMunuSearch;
    private MapView vMapView;
    private BaiduMap mBaidumap = null;
    // 初始化全局 bitmap 信息，不用时及时 recycle
    BitmapDescriptor bdnor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_tuijian_nor);
    BitmapDescriptor bdPre = BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_tuijian_pre);
    BitmapDescriptor click = BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_pin_green);
    private ImageView vImageViewPin;
    private LocationClient mLocClient;
    boolean isFirstLoc = true; // 是否首次定位
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private Marker prevMarker;
    private LinearLayout vTraffic;
    private LinearLayout linear_center;

    private boolean isCloseTraffic;
    private ImageView vImageTraffic;
    public static LatLng ll;
    private PopupWindow popupWindow;

    private List<Marker> overlays = new ArrayList<>();
    private List<Marker> lines = new ArrayList<>();

    private OnGetGeoCoderResultListener geoListener = new OnGetGeoCoderResultListener() {
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有检索到结果
            }
            //获取地理编码结果
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果
            }
            //获取反向地理编码结果
            result.getAddress();
            remarkPoint = new RemarkPoint();
            remarkPoint.setTitle(result.getAddress());
            remarkPoint.setLatitude(result.getLocation().latitude);
            remarkPoint.setLongitude(result.getLocation().longitude);
        }
    };
    private GeoCoder mGeoSearch;
    private RemarkPoint remarkPoint;

    public FoundFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_found, container,false);
        mContext = getContext();
        findViewId(view);
        onClick();
        initBaiduMap();
        initCenterLat();
        return view;
    }

    private void loadNearByPoint(LatLng latLng) {
        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("longitude",String.valueOf(latLng.longitude));
        paramsMap.put("latitude",String.valueOf(latLng.latitude));
        paramsMap.put("distance","5000");

        Subscriber mSubscriber = new Subscriber<String>(){

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final String jsonStr) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 由于服务端的返回数据格式不固定，因此这里采用手动解析
                        RequestResultInfo resultInfo = JSON.parseObject(jsonStr, RequestResultInfo.class);
                        if ("0".equals(resultInfo.getCode())) {
                            Object o = resultInfo.getContent();
                            if (o != null) {
                                initOverlay(JSON.parseArray(o.toString(), RemarkPoint.class));
                            }
                        }
                    }
                }).start();
            }
        };

        NetRequestManager.getInstance().getNearByPoints(paramsMap,mSubscriber);
    }

    private void initCenterLat() {
        // 地图拖拽监听
        mBaidumap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus status) {
                // 获取屏幕中心点在地图上对应的坐标
                LatLng centerLatLng = mBaidumap
                        .getProjection()
                        .fromScreenLocation(new Point(getResources().getDisplayMetrics().widthPixels / 2,vMapView.getHeight()/2));
                loadStreets(centerLatLng);
                loadNearByPoint(centerLatLng);
                showMarker(centerLatLng);
            }

            @Override
            public void onMapStatusChange(MapStatus arg0) {

            }
        });
    }

    private void loadStreets(LatLng latLng) {
        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("longitude",String.valueOf(latLng.longitude));
        paramsMap.put("latitude",String.valueOf(latLng.latitude));
        paramsMap.put("distance","1000");

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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RequestResultInfo resultInfo = JSON.parseObject(jsonStr, RequestResultInfo.class);
                        if ("0".equals(resultInfo.getCode())) {
                            Object o = resultInfo.getContent();
                            if (o != null){
                                List<Streets> streets = JSON.parseArray(o.toString(), Streets.class);
                                // 添加普通折线绘制
                                for (Marker marker : lines){
                                    marker.remove();
                                }
                                lines.clear();
                                for (Streets street : streets) {
                                    List<LatLng> points = new ArrayList<>();
                                    List<Streets.GeometryBean.CoordinatesBeanX.CoordinatesBean> coordinates = street.getGeometry().getCoordinates().get(0).getCoordinates();
                                    double ws = street.getProperties().getWs();
                                    for (int i = 0; i < coordinates.size(); i++) {
                                        Streets.GeometryBean.CoordinatesBeanX.CoordinatesBean bean = coordinates.get(i);
                                        LatLng latLng = turnToBaidu(new LatLng(bean.getY(), bean.getX()));
                                        points.add(latLng);
                                        if (i == 0) {
                                            // 添加文字
                                            OverlayOptions ooText;
                                            if (ws <= 0.3) {
                                                ooText = new TextOptions().bgColor(Color.RED)
                                                        .fontSize(30).fontColor(Color.WHITE).text(String.valueOf(ws))
                                                        .position(latLng);
                                            } else if (ws <= 0.6) {
                                                ooText = new TextOptions().bgColor(Color.rgb(241, 182, 64))
                                                        .fontSize(30).fontColor(Color.WHITE).text(String.valueOf(ws))
                                                        .position(latLng);
                                            } else {
                                                ooText = new TextOptions().bgColor(Color.rgb(42, 202, 142))
                                                        .fontSize(30).fontColor(Color.WHITE).text(String.valueOf(ws))
                                                        .position(latLng);
                                            }

                                            lines.add((Marker) mBaidumap.addOverlay(ooText));
                                        }
                                    }
                                    OverlayOptions ooPolyline;

                                    if (ws <= 0.3) {
                                        ooPolyline = new PolylineOptions().width(8)
                                                .color(Color.RED).points(points);
                                    } else if (ws <= 0.6) {
                                        ooPolyline = new PolylineOptions().width(8)
                                                .color(Color.rgb(241, 182, 64)).points(points);
                                    } else {
                                        ooPolyline = new PolylineOptions().width(8)
                                                .color(Color.rgb(42, 202, 142)).points(points);
                                    }
                                    lines.add((Marker) mBaidumap.addOverlay(ooPolyline));
                                }
                            }
                        }
                    }
                }).start();

            }
        };

        NetRequestManager.getInstance().getStreets(paramsMap,mSubscriber);
    }

    private LatLng turnToBaidu(LatLng sourceLatLng) {
        // 将GPS设备采集的原始GPS坐标转换成百度坐标
        CoordinateConverter converter  = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标
        converter.coord(sourceLatLng);
        return converter.convert();
    }

    private void onClick() {
        vMunuSearch.setOnClickListener(this);
        mBaidumap.setOnMarkerClickListener(markerClickListener);
        vTraffic.setOnClickListener(this);
        linear_center.setOnClickListener(this);
    }

    public void findViewId(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        vMunuSearch = (RelativeLayout) view.findViewById(R.id.rl_munu_search);
        vMapView = (MapView) view.findViewById(R.id.map);
        vTraffic = (LinearLayout)view.findViewById(R.id.traffic);
        vImageTraffic = (ImageView) view.findViewById(R.id.iv_traffic);
        linear_center = (LinearLayout) view.findViewById(R.id.linear_center);
        mBaidumap = vMapView.getMap();
        toolbar.setTitle("发现");

        setHasOptionsMenu(true);// 标题的文字需在setSupportActionBar之前，不然会无效
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    private void initBaiduMap(){
        mGeoSearch = GeoCoder.newInstance();
        MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(20);
        mBaidumap.animateMapStatus(u);
        initLocation();

        setTraffic();
    }



    private void showMarker(LatLng latLng) {

        mGeoSearch.setOnGetGeoCodeResultListener(geoListener);
        mGeoSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
    }

    public void initOverlay(List<RemarkPoint> suggLocas) {
            Bundle bundle;
        for (Marker marker : overlays){
            marker.remove();
        }
        overlays.clear();
        for (RemarkPoint location : suggLocas){
            LatLng llA = new LatLng(location.getLatitude(), location.getLongitude());
            bundle = new Bundle();
            bundle.putParcelable("data",location);
            MarkerOptions ooA = new MarkerOptions().position(llA).icon(bdnor)
                    .zIndex(9).draggable(false).extraInfo(bundle);
            overlays.add((Marker) mBaidumap.addOverlay(ooA));
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

            RemarkPoint remarkPoint = marker.getExtraInfo().getParcelable("data");
            int height = marker.getIcon().getBitmap().getHeight();
            ImageView view = createView();
            InfoWindow mInfoWindow = new InfoWindow(view, new LatLng(remarkPoint.getLatitude(), remarkPoint.getLongitude()), -height + 20);
            mBaidumap.showInfoWindow(mInfoWindow);
            marker.setIcon(bdPre);
            if (prevMarker != null) {
                prevMarker.setIcon(bdnor);
            }
            prevMarker = marker;
            goWalkDetail(remarkPoint);
            return false;
        }
    };

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

                ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                loadStreets(ll);
                loadNearByPoint(ll);
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaidumap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                showMarker(new LatLng(location.getLatitude(),location.getLongitude()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
    private void goWalkDetail(RemarkPoint suggLoca){
        Intent intent = new Intent(mContext,WalkDetailsActivity.class);
        intent.putExtra("locationData",suggLoca);
        intent.putExtra("myLocation",ll);
        EnterActUtils.startAct(getActivity(),intent);
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
            case R.id.linear_center:
                showPop();
                break;
        }
    }

    private void showPop() {
        if (popupWindow == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.found_comment_pop, null);
            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            popupWindow.setBackgroundDrawable(new ColorDrawable());
            LinearLayout linear_recommend = (LinearLayout) view.findViewById(R.id.linear_recommend);
            LinearLayout linear_tucao = (LinearLayout) view.findViewById(R.id.linear_tucao);
            LinearLayout linear_cancel = (LinearLayout) view.findViewById(R.id.linear_cancel);

            linear_recommend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (remarkPoint != null) {
                        Intent intent = new Intent(mContext, WalkDetialsDiscussActivity.class);
                        intent.putExtra("locationData", remarkPoint);
                        intent.putExtra("myLocation", ll);
                        intent.putExtra("type", 0);
                        EnterActUtils.startAct(getActivity(), intent);
                        popupWindow.dismiss();
                    }
                }
            });
            linear_tucao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (remarkPoint != null) {
                        Intent intent = new Intent(mContext, WalkDetialsDiscussActivity.class);
                        intent.putExtra("locationData", remarkPoint);
                        intent.putExtra("myLocation", ll);
                        intent.putExtra("type", 1);
                        EnterActUtils.startAct(getActivity(), intent);
                        popupWindow.dismiss();
                    }
                }
            });
            linear_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
        }
        popupWindow.showAtLocation(popupWindow.getContentView(), Gravity.BOTTOM,0,0);
    }

    @Override
    public void onPause() {
        MobclickAgent.onPageEnd("FoundFragment");
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        vMapView.onPause();
        super.onPause();
    }
    @Override
    public void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        vMapView.onResume();
        super.onResume();
        MobclickAgent.onPageStart("FoundFragment");
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
        mGeoSearch.destroy();
        click.recycle();
        bdPre.recycle();
        bdnor.recycle();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0x101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted

                } else {
                    // Permission Denied
                    Toast.makeText(mContext, "定位权限已被禁止", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }

    }
}
