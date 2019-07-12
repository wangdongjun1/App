package com.jarisoft.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jarisoft.R;

import java.util.List;

/**
 * Created by robot on 2019/6/26.
 */

public class HandoverStatusAdapter extends BaseAdapter {

    private Context context = null;
    private List<String> data=null;

    public HandoverStatusAdapter(Context context, List<String> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data==null?0:data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewGroup layout = null;
        if (view == null) {
            layout = (ViewGroup) LayoutInflater.from(context).inflate(
                    R.layout.list_handover_status, viewGroup, false);
        } else {
            layout = (ViewGroup) view;
        }
        String handOverNum=data.get(i);
        PipeHandoverAdapter.ViewHolder holder = new PipeHandoverAdapter.ViewHolder();
        TextView hdBarcode =(TextView)layout.findViewById(R.id.pipe_handover_num);
        hdBarcode.setText(handOverNum);
        layout.setTag(handOverNum);
        return layout;
    }
}
