package com.jarisoft.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jarisoft.R;
import com.jarisoft.entity.PileInfo;

import android.view.View.OnClickListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 回厂界面绑定listView
 * Created by shanwj on 2018/7/23.
 */

public class PipeCheckListAdpter extends BaseAdapter {
    private Context context = null;
    private List<PileInfo> data = null;
    private Map<Integer, Boolean> isCheckMap = new HashMap<Integer, Boolean>();
    private OnClickListener onClickListener;
    public PipeCheckListAdpter(Context context, List<PileInfo> data,OnClickListener onClickListener) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewGroup layout = null;
        if (view == null) {
            layout = (ViewGroup) LayoutInflater.from(context).inflate(
                    R.layout.list_pipe_check_layout, viewGroup, false);
        } else {
            layout = (ViewGroup) view;
        }
        PileInfo pileInfo = data.get(i);
        TextView tvHandnum = (TextView) layout.findViewById(R.id.pipe_check_hand_num);
        tvHandnum.setText(pileInfo.getHandoverNum());
        TextView tvProNum = (TextView) layout.findViewById(R.id.pipe_check_pro_num);
        tvProNum.setText(pileInfo.getProjectNum());
        TextView tvCharNum = (TextView) layout.findViewById(R.id.pipe_check_char_num);
        tvCharNum.setText(pileInfo.getChartNum());
        TextView tvPipeNum = (TextView) layout.findViewById(R.id.pipe_check_pipe_num);
        tvPipeNum.setText(pileInfo.getPipeNum());
        TextView tvChartUrl = (TextView) layout.findViewById(R.id.pipe_check_chart_url);
        tvChartUrl.setText(pileInfo.getPDFUrl());
        TextView tvQrCode = (TextView) layout.findViewById(R.id.pipe_check_qr_code);
        tvQrCode.setText(pileInfo.getQRCode());
        CheckBox cbCheck = (CheckBox) layout.findViewById(R.id.pipe_check_check);
        LinearLayout lvShowPdf = (LinearLayout)layout.findViewById(R.id.pipe_check_btn_pdf);
        cbCheck.setVisibility(View.VISIBLE);
        if (isCheckMap.get(i) == null) {
            isCheckMap.put(i, false);
        }
        /*
		 * 设置单选按钮的选中
		 */
        cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

				/*
				 * 将选择项加载到map里面寄存
				 */
                isCheckMap.put(i, isChecked);
            }
        });
        ViewHolder holder = new ViewHolder();
        holder.cbCheck = cbCheck;
        holder.lvShowPdf = lvShowPdf;
        holder.tvChartNum = tvCharNum;
        holder.tvChartUrl = tvChartUrl;
        holder.tvHandNum = tvHandnum;
        holder.tvPipeNum = tvPipeNum;
        holder.tvProNum = tvProNum;
        holder.tvQrCode = tvQrCode;
        holder.lvShowPdf.setTag(pileInfo.getPDFUrl());
        holder.lvShowPdf.setOnClickListener(this.onClickListener);
        layout.setTag(holder);
        return layout;
    }

    public static class ViewHolder {
        public TextView tvHandNum = null;
        public TextView tvProNum = null;
        public TextView tvPipeNum= null;
        public TextView tvChartNum = null;
        public TextView tvChartUrl = null;
        public TextView tvQrCode= null;
        public CheckBox cbCheck = null;
        public LinearLayout lvShowPdf = null;
        public Object data = null;
    }

    public Map<Integer, Boolean> getCheckMap() {
        return this.isCheckMap;
    }

    public List<PileInfo> getData() {
        return data;
    }
}
