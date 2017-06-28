package com.cdjysd.stopstop.baseconoom;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.zhy.http.okhttp.OkHttpUtils;

import org.litepal.LitePal;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * @类名: $classname$
 * @功能描述:
 * @作者: $zuozhe$
 * @时间: $date$
 * @最后修改者:
 * @最后修改内容:
 */


public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        init();


    }


    /**
     * 初始化
     */
    private void init() {
        //进行okhttp初始化配置
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS).readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);


        LitePal.initialize(this);//初始化数据库
        SQLiteDatabase db = LitePal.getDatabase();//创建数据库/表


    }
}
