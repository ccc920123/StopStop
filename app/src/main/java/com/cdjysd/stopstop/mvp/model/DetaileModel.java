package com.cdjysd.stopstop.mvp.model;

import android.util.Log;

import com.cdjysd.stopstop.base.MVPCallBack;
import com.cdjysd.stopstop.bean.HphmTable;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 描述：
 * 公司：四川星盾科技股份有限公司
 * 编写人：陈渝金-pc:64550
 * 时间： 2018/11/28
 * 修改人：
 * 修改时间：
 */


public class DetaileModel implements DetaileModwlImp {
    @Override
    public void delectData(String phone, String hphm, final MVPCallBack<String> callBack) {


        String bql = "select * from HphmTable where phone='" + phone + "' and hphm='" + hphm + "'";//查询所有的游戏得分记录

        new BmobQuery<HphmTable>().doSQLQuery(bql, new SQLQueryListener<HphmTable>() {

            @Override
            public void done(BmobQueryResult<HphmTable> result, BmobException e) {
                if (e == null) {
                    List<HphmTable> list = (List<HphmTable>) result.getResults();
                    if (list != null && list.size() > 0) {

                        delete(list.get(0).getObjectId(), callBack);


                    } else {
                        callBack.failed("无数据");
                    }
                } else {
                    callBack.failed("失败");
                }
            }
        });


    }


    /**
     * 删除一个对象
     */
    private void delete(String mObjectId, final MVPCallBack<String> callBack) {
        HphmTable category = new HphmTable();
        category.delete(mObjectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    callBack.succeed("成功");

                } else {
                    callBack.failed("失败");
                }
            }
        });
    }
}
