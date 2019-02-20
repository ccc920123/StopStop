package com.cdjysd.stopstop.mvp.model;

import com.cdjysd.stopstop.base.MVPCallBack;
import com.cdjysd.stopstop.bean.HphmTable;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

/**
 * 描述：
 * 编写人：陈渝金-pc:64550
 * 时间： 2018/12/6
 * 修改人：
 * 修改时间：
 */


public class ClundModel implements ClundModelImp {
    @Override
    public void selectData(String phone, final MVPCallBack<List<HphmTable>> callBack) {

     String sql="select * from HphmTable where phone=? order by -createdAt desc";
        BmobQuery<HphmTable> query=new BmobQuery<HphmTable>();
//设置SQL语句
        query.setSQL(sql);
//设置占位符参数
        query.setPreparedParams(new Object[]{phone});
        query.doSQLQuery(new SQLQueryListener<HphmTable>(){

            @Override
            public void done(BmobQueryResult<HphmTable> result, BmobException e) {
                if(e ==null){
                    List<HphmTable> list = (List<HphmTable>) result.getResults();
                    if(list!=null && list.size()>0){

                        callBack.succeed(list);

                    }else{
//                        Log.i("smile", "查询成功，无数据返回");
                        callBack.failed("0");
                    }
                }else{
//                    Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                    callBack.failed("链接错误");
                }
            }
        });

    }
}
