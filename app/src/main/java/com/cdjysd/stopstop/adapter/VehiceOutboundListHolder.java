package com.cdjysd.stopstop.adapter;

import android.view.View;
import android.widget.TextView;

import com.cdjysd.stopstop.R;
import com.cdjysd.stopstop.bean.InserCarBean;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/3/7.
 */
public class VehiceOutboundListHolder extends BaseHolder<InserCarBean> {

    @BindView(R.id.car_hphm_text)
    TextView carHphmText;
    @BindView(R.id.car_color_text)
    TextView carColorText;
    @BindView(R.id.car_time_text)
    TextView carTimeText;

    private InserCarBean mData;

    public VehiceOutboundListHolder(View view) {
        super(view);
    }

    @Override
    public void setData(InserCarBean mData) {
        super.setData(mData);
        this.mData = mData;
        carHphmText.setText(mData.getHphm());
        carColorText.setText(mData.getHpys());
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
