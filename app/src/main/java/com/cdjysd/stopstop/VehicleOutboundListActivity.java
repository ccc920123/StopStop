package com.cdjysd.stopstop;

import android.content.Context;
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
import com.cdjysd.stopstop.utils.SharedPreferencesHelper;
import com.cdjysd.stopstop.utils.ToastUtils;
import com.parkingwang.vehiclekeyboard.AsyscHphm;
import com.parkingwang.vehiclekeyboard.PopupKeyboard;
import com.parkingwang.vehiclekeyboard.ScenClick;
import com.parkingwang.vehiclekeyboard.view.InputView;

import java.util.List;

/**
 * C车辆出库
 * Error:Execution failed for task ':app:transformDexArchiveWithExternalLibsDexMergerForQihuDebug'.
 * > java.lang.RuntimeException: com.android.builder.dexing.DexArchiveMergerException: Unable to merge dex
 */
public class VehicleOutboundListActivity extends BaseActivity implements VehiceOutboundListView, ItemListener<InserCarBean>, ScenClick, AsyscHphm {

    private final int HPHM_SCAN_REQUEST_CODE = 200;
    ImageView titleBack;
    TextView titleTv;
    InputView numberEd;
    Button listSearch;
    RecyclerView mRecyclerView;
    BaseRecyclerAdapter mAdapter;
    TextView showNotext;
    RelativeLayout noLayout;
    LinearLayout activityVehicleOutbound;
    private PopupKeyboard mPopupKeyboard;

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
// 创建弹出键盘
        mPopupKeyboard = new PopupKeyboard(this);
        // 弹出键盘内部包含一个KeyboardView，在此绑定输入两者关联。
        mPopupKeyboard.attach(numberEd, this);
        //查询数据库只查询前5条数据

        ((VehiceOutboundListPresenter) mPresenter).selectDBCollection(this, SharedPreferencesHelper.getString(this, "PHONE", ""));
//        ((VehiceOutboundListPresenter) mPresenter).selectNetData(SharedPreferencesHelper.getString(this, "PHONE", ""));
    }

    @Override
    public BasePresenter getPresenter() {
        return new VehiceOutboundListPresenter();
    }


    private View.OnClickListener click = new View.OnClickListener() {
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
                    if ("".equals(numberEd.getNumber())) {
                        ToastUtils.showToast(VehicleOutboundListActivity.this, "请输入查询条件");
                    } else {
                        ((VehiceOutboundListPresenter) mPresenter).selectDBDate(VehicleOutboundListActivity.this, numberEd.getNumber(), SharedPreferencesHelper.getString(VehicleOutboundListActivity.this, "PHONE", ""));

//                        ((VehiceOutboundListPresenter) mPresenter).selectNetData(SharedPreferencesHelper.getString(VehicleOutboundListActivity.this, "PHONE", ""), numberEd.getText().toString());//通过数据再次查找数据

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
    public void onBackPressed() {
        Intent intent = new Intent(VehicleOutboundListActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
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

    @Override
    public void scenClick(Context mContext) {
        Intent intent = new Intent(this,
                MemoryCameraActivity.class);

        startActivityForResult(intent, HPHM_SCAN_REQUEST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HPHM_SCAN_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String hphm = data.getCharSequenceExtra("number").toString();
            numberEd.updateNumber(hphm);    //号码返回结果
        }
    }

    @Override
    public void gethphm() {

    }
}
