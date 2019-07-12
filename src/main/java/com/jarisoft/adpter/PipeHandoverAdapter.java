package com.jarisoft.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jarisoft.R;
import com.jarisoft.entity.PileInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by robot on 2018/8/8.
 */

public class PipeHandoverAdapter extends BaseAdapter {
    private Context context = null;
    private List<PileInfo> data = null;
    private Map<Integer, Boolean> isCheckMap = new HashMap<Integer, Boolean>();
    private View.OnClickListener onClickListener;

    public PipeHandoverAdapter(Context context, List<PileInfo> data,View.OnClickListener onClickListener) {
        this.context = context;
        this.data = data;
        this.onClickListener = onClickListener;
        configCheckMap(false);
    }
    public void configCheckMap(boolean bool) {

        for (int i = 0; i < data.size(); i++) {
            isCheckMap.put(i, bool);
        }
    }

    @Override
    public int getCount() {
        return data==null?0:data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
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
                    R.layout.list_pipe_handover, viewGroup, false);
        } else {
            layout = (ViewGroup) view;
        }
        PileInfo pileInfo = data.get(i);
        TextView hdBarcode =(TextView)layout.findViewById(R.id.pipe_check_bar_num);
        hdBarcode.setText(pileInfo.getBarCode());
        TextView hdQrCode = (TextView) layout.findViewById(R.id.pipe_check_qr_code);
        hdQrCode.setText(pileInfo.getQRCode());
        TextView hdPipeNum = (TextView) layout.findViewById(R.id.pipe_check_pipe_num);
        hdPipeNum.setText(pileInfo.getPipeNum());
        LinearLayout hdShowDetail = (LinearLayout)layout.findViewById(R.id.pipe_show_detail);

        ViewHolder holder = new ViewHolder();
        holder.hdBarcode = hdBarcode;
        holder.hdPipeNum = hdPipeNum;
        holder.hdShowDetail = hdShowDetail;
        holder.hdShowDetail.setTag(pileInfo);
        holder.hdQrCode = hdQrCode;
        holder.hdShowDetail.setOnClickListener(this.onClickListener);
        layout.setTag(holder);
        return layout;
    }

    public static class ViewHolder {
        public TextView hdBarcode = null;
        public TextView hdQrCode = null;
        public TextView hdPipeNum= null;
        public LinearLayout hdShowDetail = null;
        public Object data = null;
    }

    public List<PileInfo> getData() {
        return data;
    }
}
