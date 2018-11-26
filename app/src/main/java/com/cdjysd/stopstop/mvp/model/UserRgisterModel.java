package com.cdjysd.stopstop.mvp.model;

import android.util.Log;

import com.cdjysd.stopstop.MainActivity;
import com.cdjysd.stopstop.UserRgisterActivity;
import com.cdjysd.stopstop.base.MVPCallBack;
import com.cdjysd.stopstop.bean.USER;
import com.cdjysd.stopstop.utils.SharedPreferencesHelper;
import com.cdjysd.stopstop.utils.ToastUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 描述：
 * 编写人：陈渝金-pc:64550
 * 时间： 2018/11/26
 * 修改人：
 * 修改时间：
 */


public class UserRgisterModel implements UserRgisterModelImp {
    @Override
    public void setUserRgister(final USER user, final MVPCallBack<String> callBack) {

        BmobQuery<USER> query = new BmobQuery<USER>();
//        query.add
        query.addWhereEqualTo("phone",user.getPhone());
        query.findObjects(new FindListener<USER>() {
            @Override
            public void done(List<USER> list, BmobException e) {
                if(e==null){
                    if(list==null||list.size()==0)
                    {
                        user.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    callBack.succeed(user.getPhone());
                                } else {
                                    callBack.failed("失败,请反馈到相关人员");
                                }
                            }
                        });
                    }else {
                        String objid = list.get(0).getObjectId();
                        user.update(objid, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    callBack.succeed(user.getPhone());

                                } else {
                                    callBack.failed("失败,请反馈到相关人员");
                                }
                            }
                        });
                    }
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });


    }
}
