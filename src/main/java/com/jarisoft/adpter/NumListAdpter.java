package com.jarisoft.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jarisoft.R;
import com.jarisoft.entity.UserNum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理界面绑定listView
 * Created by shanwj on 2018/7/18.
 */

public class NumListAdpter extends BaseAdapter{

    private Context context = null;
    private List<UserNum> data = null;
    /**
     * CheckBox 是否选择的存储集合,key 是 position , value 是该position是否选中
     */
    private Map<Integer, Boolean> isCheckMap = new HashMap<Integer, Boolean>();

    public NumListAdpter(Context context, List<UserNum> data) {
        this.context = context;
        this.data = data;
        configCheckMap(false);
    }

    /**
     * 首先,默认情况下,所有项目都是没有选中的.这里进行初始化
     */
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
        /**
         * 进行ListView 的优化
         */
        if (view == null) {
            layout = (ViewGroup) LayoutInflater.from(context).inflate(
                    R.layout.listview_item_layout, viewGroup, false);
        } else {
            layout = (ViewGroup) view;
        }
        UserNum userNum = data.get(i);
		/*
		 * 设置每一个item的文本
		 */
        TextView tvTitle = (TextView) layout.findViewById(R.id.tvTitle);
        tvTitle.setText(userNum.getNum());

		/*
		 * 获得单选按钮
		 */
        CheckBox cbCheck = (CheckBox) layout.findViewById(R.id.cbCheckBox);

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

        cbCheck.setVisibility(View.VISIBLE);
        if (isCheckMap.get(i) == null) {
            isCheckMap.put(i, false);
        }
        cbCheck.setChecked(isCheckMap.get(i));
        ViewHolder holder = new ViewHolder();
        holder.cbCheck = cbCheck;
        holder.tvTitle = tvTitle;
        /**
         * 将数据保存到tag
         */
        layout.setTag(holder);
        return layout;
    }

    /**
     * 增加一项的时候
     */
    public void add(UserNum userNum) {
        this.data.add(0, userNum);
        // 让所有项目都为不选择
        configCheckMap(false);
    }

    // 移除一个项目的时候
    public void remove(int position) {
        this.data.remove(position);
    }

    public Map<Integer, Boolean> getCheckMap() {
        return this.isCheckMap;
    }

    public List<UserNum> getData() {
        return data;
    }

    public static class ViewHolder {
        public TextView tvTitle = null;
        public CheckBox cbCheck = null;
        public Object data = null;
    }

}
