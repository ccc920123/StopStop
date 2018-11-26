package com.cdjysd.stopstop;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdjysd.stopstop.adapter.BaseRecyclerAdapter;
import com.cdjysd.stopstop.adapter.VehiceOutboundListHolder;
import com.cdjysd.stopstop.base.BaseActivity;
import com.cdjysd.stopstop.base.ItemListener;
import com.cdjysd.stopstop.bean.InserCarBean;
import com.cdjysd.stopstop.mvp.presenter.BasePresenter;
import com.cdjysd.stopstop.mvp.presenter.VehiceOutboundListPresenter;
import com.cdjysd.stopstop.mvp.view.VehiceOutboundListView;
import com.cdjysd.stopstop.utils.ToastUtils;

import java.util.List;

/**
 * C车辆出库
 * Error:Execution failed for task ':app:transformDexArchiveWithExternalLibsDexMergerForQihuDebug'.
 * > java.lang.RuntimeException: com.android.builder.dexing.DexArchiveMergerException: Unable to merge dex
 */
public class VehicleOutboundListActivity extends BaseActivity implements VehiceOutboundListView, ItemListener<InserCarBean> {


    ImageView titleBack;
    TextView titleTv;
    EditText numberEd;
    Button listSearch;
    RecyclerView mRecyclerView;
    BaseRecyclerAdapter mAdapter;
    TextView showNotext;
    RelativeLayout noLayout;
    LinearLayout activityVehicleOutbound;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vehicle_outbound;
    }

    @Override
    protected void initInjector() {
        titleBack = findViewById(R.id.title_back);
        titleTv = findViewById(R.id.title_tv);
        numberEd = findViewById(R.id.number_ed);
        listSearch = findViewById(R.id.list_search);
        mRecyclerView = findViewById(R.id.list);
        BaseRecyclerAdapter mAdapter;
        showNotext = findViewById(R.id.show_notext);
        noLayout = findViewById(R.id.no_layout);
        activityVehicleOutbound = findViewById(R.id.activity_vehicle_outbound);

        titleBack.setOnClickListener(click);
        listSearch.setOnClickListener(click);

        titleTv.setText("入库查询");
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
                outRect.set(10, 2, 10, 2);

            }
        });
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {

        //查询数据库只查询前5条数据

        ((VehiceOutboundListPresenter) mPresenter).selectDBCollection();
    }

    @Override
    public BasePresenter getPresenter() {
        return new VehiceOutboundListPresenter();
    }


   private View.OnClickListener click=new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           switch (view.getId()) {
               case R.id.title_back:
                   Intent intent = new Intent(VehicleOutboundListActivity.this, MainActivity.class);
                   startActivity(intent);
                   finish();

                   break;
               case R.id.list_search:
                   //查找数据库的车辆
                   if ("".equals(numberEd.getText().toString())) {
                       ToastUtils.showToast(VehicleOutboundListActivity.this, "请输入查询条件");
                   } else {
                       ((VehiceOutboundListPresenter) mPresenter).selectDBDate(numberEd.getText().toString());
                   }
                   break;
           }
       }
   };


    @Override
    public void setAdapter(List<InserCarBean> data) {

        if (mRecyclerView == null)
            return;
        if (mAdapter == null) {
            mAdapter = new BaseRecyclerAdapter(data, R.layout.item_businesslist, VehiceOutboundListHolder.class, this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            if ((mAdapter.getItem(0) == null) && (data.size() == 0))
                return;
            mAdapter.setmDatas(data);
        }


    }

    @Override
    public void showNOData(boolean isShow, String string) {

        if (isShow) {
            noLayout.setVisibility(View.VISIBLE);
            showNotext.setText(string);
        } else {
            noLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public void showLoadProgressDialog(String str) {

        showLoading(str);
    }

    @Override
    public void disDialog() {
        dissLoadDialog();
    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public void onItemClick(View view, int postion, InserCarBean mdata) {

//        ToastUtils.showToast(this, "点击了");


        Intent intent = new Intent(VehicleOutboundListActivity.this, DetailedActivity.class);
        intent.putExtra("BEAN", mdata);
        startActivity(intent);
        finish();


    }
}
