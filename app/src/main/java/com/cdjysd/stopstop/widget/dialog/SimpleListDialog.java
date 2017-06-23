package com.cdjysd.stopstop.widget.dialog;


import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.cdjysd.stopstop.R;


public class SimpleListDialog extends BaseDialog implements OnItemClickListener {
    private ListView mLvDisplay;
    private BaseAdapter mAdapter;
    private onSimpleListItemClickListener mOnSimpleListItemClickListener;
    private onSimpleListItemClickListenerView onSimpleListItemClickListenerView;
    private View v;
    private EditText editSerch;

    public SimpleListDialog(Context context) {
        super(context);
        setDialogContentView(R.layout.include_dialog_simplelist);
        //        View v = LayoutInflater.from(context).inflate(R.layout.include_dialog_simplelist, null);
        mLvDisplay = (ListView)findViewById(R.id.dialog_simplelist_list);
        mLvDisplay.setOnItemClickListener(this);
    }

    /**
     * 控制是否有搜索框
     * @param context
     * @param isSerch
     */
    public SimpleListDialog(Context context, boolean isSerch) {
        super(context);
        setDialogContentView(R.layout.include_dialog_simplelist);
        mLvDisplay = (ListView)findViewById(R.id.dialog_simplelist_list);
        editSerch= (EditText) findViewById(R.id.serchcllx);
        editSerch.setVisibility(View.VISIBLE);
        mLvDisplay.setOnItemClickListener(this);
    }

    public void setAdapter(BaseAdapter adapter) {
        mAdapter = adapter;
        if (mAdapter != null) {
            mLvDisplay.setAdapter(mAdapter);
        }
    }

    public  EditText  getEditSerchText()
    {
        return  editSerch;
    }

    public void notifyDataSetChanged() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }


    public void setOnSimpleListItemClickListener(onSimpleListItemClickListener listener) {
        mOnSimpleListItemClickListener = listener;
    }

    public void setOnSimpleListItemClickListener(onSimpleListItemClickListenerView listener, View v) {
        onSimpleListItemClickListenerView = listener;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (mOnSimpleListItemClickListener != null) {
            mOnSimpleListItemClickListener.onItemClick(arg2);
            dismiss();
        } else if (onSimpleListItemClickListenerView != null) {
            onSimpleListItemClickListenerView.onItemClick(arg2, v);
            dismiss();
        }
    }

    public interface onSimpleListItemClickListener {
        void onItemClick(int position);
    }

    public interface onSimpleListItemClickListenerView {
        void onItemClick(int position, View v);
    }

}
