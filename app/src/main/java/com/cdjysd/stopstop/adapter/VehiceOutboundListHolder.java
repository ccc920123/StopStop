package com.cdjysd.stopstop.adapter;

import android.view.View;
import android.widget.TextView;

import com.cdjysd.stopstop.R;
import com.cdjysd.stopstop.bean.InserCarBean;

/**
 * Created by Administrator on 2017/3/7.
 */
public class VehiceOutboundListHolder extends BaseHolder<InserCarBean> {

    TextView carHphmText;
    TextView carTimeText;

    private InserCarBean mData;

    public VehiceOutboundListHolder(View view) {
        super(view);
    }

    @Override
    public void setData(InserCarBean mData) {
        super.setData(mData);

        carHphmText=mView.findViewById(R.id.car_hphm_text);
        carTimeText=mView.findViewById(R.id.car_time_text);
            this.mData = mData;
            carHphmText.setText(mData.getHphm());
            carTimeText.setText(mData.getInserttime());
    }

    @Override
    public void init() {
        super.init();
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, getPosition(), mData);
            }
        });
    }
}
