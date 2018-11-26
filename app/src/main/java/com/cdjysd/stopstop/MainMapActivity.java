package com.cdjysd.stopstop;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Poi;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.cdjysd.stopstop.base.BaseActivity;
import com.cdjysd.stopstop.mvp.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.List;

public class MainMapActivity extends BaseActivity implements AMapLocationListener, LocationSource, PoiSearch.OnPoiSearchListener, AMap.OnPOIClickListener, AMap.OnMarkerClickListener {


    MapView mapView;

    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    MyLocationStyle myLocationStyle;
    private OnLocationChangedListener mListener;
    AMap aMap;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_map;
    }

    @Override
    protected void initInjector() {

        mapView=findViewById(R.id.map);
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。

    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        if (aMap == null) {
            aMap = mapView.getMap();
            //设置点击POI与Marker
//            aMap.setOnPOIClickListener(this);//地图上得所有poi都可以点击
            aMap.setOnMarkerClickListener(this);

        }
//设置成中文地图
        aMap.setMapLanguage(AMap.CHINESE);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setLocationSource(this);// 设置定位监听  ,会实现两个方法activate（）、 deactivate（），在两个方法中进行操作
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.getUiSettings().setZoomControlsEnabled(false);  //设置缩放按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。


    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                aMap.moveCamera(CameraUpdateFactory.zoomTo(17));    //设置显示级数//将视图中心点自动移动到次此置
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
                doSearchQuery("停车场", "028", amapLocation.getLatitude(), amapLocation.getLongitude());
                deactivate();  //我只定位了一次，所以成功之后就调用此方法，如果想一直重复定位不需要写此方法




            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;

        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            mLocationOption.setNeedAddress(true);            //设置定位监听
            mlocationClient.setLocationListener(this);      //定位成功会回调onLocationChanged（）方法
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setInterval(2000);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }


    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    PoiSearch.Query query;
    PoiSearch poiSearch;

    /**
     * poi  城市
     *
     * @param keyWord   150900，150906
     * @param cityCode  028
     * @param latitude
     * @param longitude
     */
    private void doSearchQuery(String keyWord, String cityCode, double latitude, double longitude) {

        query = new PoiSearch.Query(keyWord, "", cityCode);
//keyWord表示搜索字符串，
//第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
//cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query.setPageSize(30);// 设置每页最多返回多少条poiitem
        query.setPageNum(1);//设置查询页码

        //Log.e("--Main--","doSearchQuery()");
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        // 设置中心点及检索范围
        PoiSearch.SearchBound bound = new PoiSearch.SearchBound(new LatLonPoint(latitude, longitude), 1000);
        //设置查询条件,mTableID是将数据存储到数据管理台后获得。
        try {
            poiSearch.searchPOIAsyn();// 异步搜索
        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }
    }

    //在此方法会获取到周边的数据，进行操作即可
    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {


        ArrayList<PoiItem> pois = poiResult.getPois();
        if (pois != null && pois.size() > 0) {
            for (PoiItem item : pois) {   //循环遍历获取数据//Log.e("--Main--", item.getTitle());
               LatLng latLng=new LatLng(item.getLatLonPoint().getLatitude(), item.getLatLonPoint().getLongitude());
                Marker marker = aMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapstop))
                        .position(latLng)
                        .draggable(true));


                //调用addMarker（）方法添加大头针。   .title(item.getTitle())

            }
        } else {
            //
        }

    }

    //由于是检索具体的某一个POI，直接回调该POI对象 PoiItem(通过索引条件查询的)
    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
        Log.e("e", i + "");
    }

    /**
     * POI 回调
     * @param poi
     */

    @Override
    public void onPOIClick(Poi poi) {
        aMap.clear();
        Log.i("MY", poi.getPoiId()+poi.getName());
        MarkerOptions markOptiopns = new MarkerOptions();
        markOptiopns.position(poi.getCoordinate());
        TextView textView = new TextView(getApplicationContext());
        textView.setText("到"+poi.getName()+"去");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundResource(R.drawable.custom_info_bubble);
        markOptiopns.icon(BitmapDescriptorFactory.fromView(textView));
        aMap.addMarker(markOptiopns);

    }

    /**er
     * mark 回调
     * @param marker
     * @return
     */

    @Override
    public boolean onMarkerClick(Marker marker) {

        MarkerOptions markOptiopns = new MarkerOptions();
        markOptiopns.position(marker.getPosition());
        TextView textView = new TextView(getApplicationContext());
        textView.setText("到"+marker.getTitle()+"去");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundResource(R.drawable.custom_info_bubble);
        markOptiopns.icon(BitmapDescriptorFactory.fromView(textView));
        aMap.addMarker(markOptiopns);
        List<Marker> mapmarker = aMap.getMapScreenMarkers();


//
//        // 构造导航参数
//        NaviPara naviPara = new NaviPara();
//        // 设置终点位置
//        naviPara.setTargetPoint(marker.getPosition());
//        // 设置导航策略，这里是避免拥堵
//        naviPara.setNaviStyle(AMapUtils.DRIVING_AVOID_CONGESTION);
//        try {
//            // 调起高德地图导航
//            AMapUtils.openAMapNavi(naviPara, getApplicationContext());
//        } catch (com.amap.api.maps.AMapException e) {
//            // 如果没安装会进入异常，调起下载页面
//            AMapUtils.getLatestAMapApp(getApplicationContext());
//        }
//        aMap.clear();
        return false;
    }
}
