package com.cdjysd.stopstop.mvp.model;

import com.cdjysd.stopstop.base.MVPCallBack;
import com.cdjysd.stopstop.bean.HphmTable;
import com.cdjysd.stopstop.bean.InserCarBean;
import com.cdjysd.stopstop.utils.SharedPreferencesHelper;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 描述：
 * 公司：四川星盾科技股份有限公司
 * 编写人：陈渝金-pc:64550
 * 时间： 2018/11/27
 * 修改人：
 * 修改时间：
 */


public class MemoryResulModel implements MemoryResulModelImp {
    @Override
    public void insetNet(InserCarBean carBean, String phone, final MVPCallBack<String> callBack) {

        //  在这里查询数据库中的数据

        HphmTable tableBean = new HphmTable();
        tableBean.setCreatetime(carBean.getInserttime());
        tableBean.setHphm(carBean.getHphm());
        tableBean.setPhone(phone);
        tableBean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {

                if (e == null) {
                    callBack.succeed("成功");
                } else {
                    callBack.failed("失败");
                }


            }
        });


    }
}
