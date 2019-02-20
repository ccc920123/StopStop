package com.cdjysd.stopstop.mvp.model;

import com.cdjysd.stopstop.base.MVPCallBack;
import com.cdjysd.stopstop.bean.HphmTable;
import com.cdjysd.stopstop.bean.InserCarBean;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * @类名: $classname$
 * @功能描述:
 * @作者: $zuozhe$
 * @时间: $date$
 * @最后修改者:
 * @最后修改内容:
 */


public class VehiceOutboundListModel implements VehiceOutboundListModelImp {
    //处理数据
    @Override
    public void getData(final String hphm, final MVPCallBack<List<InserCarBean>> callBack) {


        Observable observable = Observable.create(new Observable.OnSubscribe<List<InserCarBean>>() {

            @Override
            public void call(Subscriber<? super List<InserCarBean>> subscriber) {
                List<InserCarBean> carData = DataSupport.where("hphm like ?", "%" + hphm + "%").find(InserCarBean.class);

                subscriber.onNext(carData);

                subscriber.onCompleted();
            }
        });
        observable.subscribe(new Action1<List<InserCarBean>>() {
            @Override
            public void call(List<InserCarBean> inserCarBeen) {

                if (inserCarBeen != null && inserCarBeen.size() > 0) {
                    callBack.succeed(inserCarBeen);

                } else {
                    callBack.failed("未查询到数据");
                }


            }


        });


    }

    @Override
    public void getCollectionData(final MVPCallBack<List<InserCarBean>> callBack) {

        Observable observable = Observable.create(new Observable.OnSubscribe<List<InserCarBean>>() {

            @Override
            public void call(Subscriber<? super List<InserCarBean>> subscriber) {
                List<InserCarBean> carData = DataSupport.limit(10).find(InserCarBean.class);

                subscriber.onNext(carData);

                subscriber.onCompleted();
            }
        });
        observable.subscribe(new Action1<List<InserCarBean>>() {
            @Override
            public void call(List<InserCarBean> inserCarBeen) {

                if (inserCarBeen != null && inserCarBeen.size() > 0) {
                    callBack.succeed(inserCarBeen);

                } else {
                    callBack.failed("未查询到数据");
                }


            }


        });


    }

    /**
     * 在网络上查询数据
     *
     * @param phone
     * @param callBack
     */
    @Override
    public void selectNetData(String phone, final MVPCallBack<List<InserCarBean>> callBack) {


        BmobQuery<HphmTable> query = new BmobQuery<HphmTable>();
//查询playerName叫“比目”的数据
        query.addWhereEqualTo("phone", phone);
//返回10条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(10);
//执行查询方法
        query.findObjects(new FindListener<HphmTable>() {
            @Override
            public void done(List<HphmTable> object, BmobException e) {
                if (e == null) {
                    List<InserCarBean> inserCarBeans = new ArrayList<>();
                    for (HphmTable neam : object) {
                        InserCarBean inserCar = new InserCarBean();
                        inserCar.setHphm(neam.getHphm());
                        inserCar.setInserttime(neam.getCreatetime());
                        inserCarBeans.add(inserCar);
                    }
                    callBack.succeed(inserCarBeans);

                } else {
                    callBack.failed("未查询到数据");
                }
            }
        });


    }

    @Override
    public void SelectNetLikeData(String hphm, String phone, final MVPCallBack<List<InserCarBean>> callBack) {
        String sql = "select * from HphmTable where phone='" + phone + "' and hphm='" + hphm + "'";
        BmobQuery<HphmTable> query = new BmobQuery<HphmTable>();
        query.doSQLQuery(sql, new SQLQueryListener<HphmTable>() {
            @Override
            public void done(BmobQueryResult<HphmTable> bmobQueryResult, BmobException e) {
                if (e == null) {

                    List<HphmTable> list = (List<HphmTable>) bmobQueryResult.getResults();
                    if (list != null && list.size() > 0) {

                        List<InserCarBean> inserCarBeans = new ArrayList<>();
                        for (HphmTable neam : list) {
                            InserCarBean inserCar = new InserCarBean();
                            inserCar.setHphm(neam.getHphm());
                            inserCar.setInserttime(neam.getCreatetime());
                            inserCarBeans.add(inserCar);
                        }
                        callBack.succeed(inserCarBeans);
                    } else {
                        callBack.failed("未查询到数据");
                    }


                } else {
                    callBack.failed("未查询到数据");
                }
            }
        });
    }
}
