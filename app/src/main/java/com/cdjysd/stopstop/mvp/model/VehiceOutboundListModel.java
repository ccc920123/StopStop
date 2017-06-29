package com.cdjysd.stopstop.mvp.model;

import com.cdjysd.stopstop.base.MVPCallBack;
import com.cdjysd.stopstop.bean.InserCarBean;

import org.litepal.crud.DataSupport;

import java.util.List;

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

                if(inserCarBeen!=null && inserCarBeen.size()>0) {
                    callBack.succeed(inserCarBeen);

                }else{
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
                List<InserCarBean> carData = DataSupport.limit(5).find(InserCarBean.class);

                subscriber.onNext(carData);

                subscriber.onCompleted();
            }
        });
        observable.subscribe(new Action1<List<InserCarBean>>() {
            @Override
            public void call(List<InserCarBean> inserCarBeen) {

                if(inserCarBeen!=null && inserCarBeen.size()>0) {
                    callBack.succeed(inserCarBeen);

                }else{
                    callBack.failed("未查询到数据");
                }


            }


        });




    }
}
