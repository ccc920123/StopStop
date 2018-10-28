package com.cdjysd.stopstop.baseconoom;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;


import org.litepal.LitePal;

import java.util.concurrent.TimeUnit;

import cn.bmob.v3.Bmob;

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
//        //进行okhttp初始化配置
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                //                .addInterceptor(new LoggerInterceptor("TAG"))
//                .connectTimeout(10000L, TimeUnit.MILLISECONDS).readTimeout(10000L, TimeUnit.MILLISECONDS)
//                //其他配置
//                .build();
//
//        OkHttpUtils.initClient(okHttpClient);


        LitePal.initialize(this);//初始化数据库
        SQLiteDatabase db = LitePal.getDatabase();//创建数据库/表
        try {

        Bmob.initialize(this, "135082596f438d2e326730cd97edfd2e");//初始化bmob
        }catch (Exception e)
        {
            e.getMessage();
        }
    }
}
