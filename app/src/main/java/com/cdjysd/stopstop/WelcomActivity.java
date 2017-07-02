package com.cdjysd.stopstop;

import android.Manifest;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cdjysd.stopstop.baseconoom.Comm;
import com.cdjysd.stopstop.widget.dialog.AlertDialog;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

public class WelcomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);
        MPermissions.requestPermissions(this, Comm.READ_PHONE_STATE, Manifest.permission
                .READ_PHONE_STATE,Manifest.permission
                .WRITE_EXTERNAL_STORAGE,Manifest.permission
                .ACCESS_COARSE_LOCATION);
    }




    @PermissionGrant(Comm.READ_PHONE_STATE)
    public void requestSdcardSuccess() {



        Intent intent = new Intent(WelcomActivity.this, MainActivity.class);
        startActivity(intent);
        finish();




    }

    @PermissionDenied(Comm.READ_PHONE_STATE)
    public void requestSdcardFailed() {
        new AlertDialog(this).builder().setTitle("提示").setMsg("我们需要手机定位，请选择信任！").setPositiveButton("确定", new
                View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(intent);

                    }
                }).show();
    }

}
