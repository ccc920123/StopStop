package com.cdjysd.stopstop.widget.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdjysd.stopstop.R;
import com.cdjysd.stopstop.adapter.BaseArrayListAdapter;
import com.cdjysd.stopstop.bean.KeyVauesBean;

import java.util.List;


public class SimpleListDialogAdapter extends BaseArrayListAdapter {
    private boolean Flag;

    public SimpleListDialogAdapter(Context context, List<String> datas) {
        super(context, datas);

    }
    public SimpleListDialogAdapter(Context contexts, List<KeyVauesBean> data, Boolean b) {
    	super(contexts,data,b);

        Flag = b;

    }

    public SimpleListDialogAdapter(Context context, String... datas) {
        super(context, datas);
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        if (arg1 == null) {
            arg1 = mInflater.inflate(R.layout.listitem_dialog, null);
        }
        if (Flag) {
            ((TextView) arg1.findViewById(R.id.listitem_dialog_text)).setText(((KeyVauesBean)getItem(arg0)).getValue());
        } else {
            ((TextView) arg1.findViewById(R.id.listitem_dialog_text)).setText((CharSequence) getItem(arg0));
        }
        return arg1;
    }
}
