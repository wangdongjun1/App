package com.jarisoft.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jarisoft.R;
import com.jarisoft.entity.PileInfo;

import java.util.List;

/**
 * 出厂界面绑定listView
 * Created by shanwj on 2018/7/20.
 */

public class PipeListAdpter  extends BaseAdapter {
    private Context context = null;
    private List<PileInfo> data = null;
    private OnClickListener onClickListener;
    public PipeListAdpter(Context context, List<PileInfo> data,OnClickListener onClickListener) {
        this.context = context;
        this.data = data;
        this.onClickListener = onClickListener;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewGroup layout = null;
        /**
         * 进行ListView 的优化
         */
        if (view == null) {
            layout = (ViewGroup) LayoutInflater.from(context).inflate(
                    R.layout.list_pipe_layout, viewGroup, false);
        } else {
            layout = (ViewGroup) view;
        }
        PileInfo pileInfo = data.get(i);
        TextView tvBarcode = (TextView) layout.findViewById(R.id.pipe_barcode);
        tvBarcode.setText(pileInfo.getBarCode());
        TextView tvProNum = (TextView) layout.findViewById(R.id.pipe_pronum);
        tvProNum.setText(pileInfo.getProjectNum());
        TextView tvCharNum = (TextView) layout.findViewById(R.id.pipe_charnum);
        tvCharNum.setText(pileInfo.getChartNum());
        TextView tvPipeNum = (TextView) layout.findViewById(R.id.pipe_pipenum);
        tvPipeNum.setText(pileInfo.getPipeNum());
        LinearLayout lvShowPdf = (LinearLayout)layout.findViewById(R.id.pipe_check_btn_pdf);

        ViewHolder holder = new ViewHolder();
        holder.barCode = tvBarcode;
        holder.charNum = tvCharNum;
        holder.pipeNum = tvPipeNum;
        holder.proNum = tvProNum;
        holder.lvShowPdf = lvShowPdf;
        holder.lvShowPdf.setTag(pileInfo.getPDFUrl());
        holder.lvShowPdf.setOnClickListener(this.onClickListener);
        layout.setTag(holder);
        return layout;
    }

    public static class ViewHolder {
        public TextView barCode = null;
        public TextView proNum = null;
        public TextView charNum = null;
        public TextView pipeNum = null;
        public LinearLayout lvShowPdf = null;
        public Object data = null;
    }

    public List<PileInfo> getData() {
        return data;
    }
}
