package com.cdjysd.stopstop;

import android.Manifest;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cdjysd.stopstop.base.BaseActivity;
import com.cdjysd.stopstop.baseconoom.Comm;
import com.cdjysd.stopstop.bean.InserCarBean;
import com.cdjysd.stopstop.mvp.presenter.BasePresenter;
import com.cdjysd.stopstop.widget.dialog.AlertDialog;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.car_insert_textview)
    TextView carInsertTextview;
    @Bind(R.id.car_delect_textview)
    TextView carDelectTextview;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initInjector() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                Observable observable = Observable.create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        List<InserCarBean> carList=  DataSupport.findAll(InserCarBean.class);
                        if (carList!=null && carList.size()>0) {
                            subscriber.onNext(carList.size());
                        }else{
                            subscriber.onNext(0);
                        }
                        subscriber.onCompleted();
                    }
                });

                observable.subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Snackbar.make(view, "你的车场中共有【 "+ integer +" 】辆车", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                });



            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void initEventAndData() {
        MPermissions.requestPermissions(this, Comm.WRITE_EXTERNAL_STORAGE, Manifest.permission
                .WRITE_EXTERNAL_STORAGE);
    }

    @PermissionGrant(Comm.WRITE_EXTERNAL_STORAGE)
    public void requestSdcardSuccess() {

    }

    @PermissionDenied(Comm.WRITE_EXTERNAL_STORAGE)
    public void requestSdcardFailed() {
        new AlertDialog(this).builder().setTitle("提示").setMsg("我们需要写入权限请到设置，请选择信任！").setPositiveButton("确定", new
                View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(intent);

                    }
                }).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_evaluation) {//评价


        } else if (id == R.id.nav_slideshow) {//关于我们

        } else if (id == R.id.nav_manage) {//计时设置

        } else if (id == R.id.nav_share) {//分享

        } else if (id == R.id.nav_send) {//建议

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @OnClick({R.id.car_insert_textview, R.id.car_delect_textview})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.car_insert_textview://车辆入库
                MPermissions.requestPermissions(this, Comm.PREMISSIONS_CAMERA, Manifest.permission.CAMERA);

                break;
            case R.id.car_delect_textview://车辆出库



                Intent intent = new Intent(MainActivity.this, VehicleOutboundListActivity.class);
                startActivity(intent);
                finish();

                break;
        }
    }

    @PermissionGrant(Comm.PREMISSIONS_CAMERA)
    public void requestCameraSucceed() {
        Intent intent = new Intent(MainActivity.this, MemoryCameraActivity.class);
        startActivity(intent);
        finish();

    }

    @PermissionDenied(Comm.PREMISSIONS_CAMERA)
    public void requestCameraFailed() {
        new AlertDialog(this).builder().setTitle("提示")
                .setMsg("拍照会用到相机权限，请确认权限以及相机是否可用！")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).show();
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
