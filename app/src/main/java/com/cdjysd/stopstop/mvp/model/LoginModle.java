package com.cdjysd.stopstop.mvp.model;

import android.util.Log;

import com.cdjysd.stopstop.base.MVPCallBack;
import com.cdjysd.stopstop.bean.USER;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 描述：
 * 公司：四川星盾科技股份有限公司
 * 编写人：陈渝金-pc:64550
 * 时间： 2018/9/27
 * 修改人：
 * 修改时间：
 */


public class LoginModle implements LoginModelImp {

    @Override
    public void logService(String phone, final String pwd, final MVPCallBack<String> callBack) {


        BmobQuery<USER> query = new BmobQuery<USER>();
//查询playerName叫“比目”的数据
        query.addWhereEqualTo("phone", phone);
//执行查询方法
        query.findObjects(new FindListener<USER>() {
            @Override
            public void done(List<USER> object, BmobException e) {
                if (e == null) {
                    if (object.size() > 0)//表示有这个用户数据，
                    {
                        if (object.get(0).getPassword().equals(pwd))//登录成功
                        {
                            callBack.succeed(object.get(0).getPhone());


                        } else {//用户或者密码不正确
                            callBack.failed("用户或者密码不正确");

                        }


                    } else { //无用户数据，提示请注册。

                        callBack.failed("请先注册");

                    }

                } else {
                    callBack.failed("服务器出现异常");
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });


    }
}
