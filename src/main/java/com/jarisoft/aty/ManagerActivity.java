package com.jarisoft.aty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jarisoft.R;
import com.jarisoft.adpter.NumListAdpter;
import com.jarisoft.db.DbDao;
import com.jarisoft.entity.UserNum;

import java.util.List;
import java.util.Map;

/**
 * 管理页面
 */
public class ManagerActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private RelativeLayout btnReturn;
    private RelativeLayout btnAdd;
    private Button btnDelete;
    private Button btnSelectAll;
    private ListView listView;
    private EditText etNum;
    private NumListAdpter numListAdpter;
    private DbDao dbDao;
    private List<UserNum> userNums;
    Activity ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        bindView();
    }

    private void bindView(){
        ctx = this;
        dbDao = DbDao.getInstance(this);
        btnReturn = findViewById(R.id.btnCancle);
        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
        btnSelectAll = findViewById(R.id.btnSelectAll);
        listView = findViewById(R.id.lvListView);
        etNum = findViewById(R.id.etNum);
        btnReturn.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnSelectAll.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        fillListData();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                if (numListAdpter == null) { //第一次加载
                    numListAdpter = new NumListAdpter(ctx,userNums);
                    listView.setAdapter(numListAdpter);
                } else {
                    // 刷新listView 否则仍会从头开始 显示
                    numListAdpter.notifyDataSetChanged();
                }
            }
        }
    };
    private void fillListData(){
        new Thread(){
            @Override
            public void run() {
                if(userNums==null){
                    userNums = dbDao.getAll();
                }
                handler.sendEmptyMessage(1);
            }
        }.start();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.btnCancle:
                intent = new Intent(ManagerActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnAdd:
                String userNumStr = etNum.getText().toString();
                if(TextUtils.isEmpty(userNumStr)){
                    Toast.makeText(ManagerActivity.this,"请输入工号!",Toast.LENGTH_LONG).show();
                    return;
                }else{
                    dbDao.add(userNumStr);
                    numListAdpter.add(new UserNum(userNumStr));
                    numListAdpter.notifyDataSetChanged();
                }
                break;
            case R.id.btnDelete:
                Map<Integer, Boolean> map = numListAdpter.getCheckMap();
                int count = numListAdpter.getCount();
                for (int i = 0; i < count; i++) {
                    //因为List的特性,保证每次删除list第一个元素
                    int positon = i-(count-numListAdpter.getCount());
                    if(map.get(i)!=null&&map.get(i)){
                        UserNum userNum = (UserNum) numListAdpter.getItem(positon);
                        dbDao.deleteNum(userNum.getNum());
                        numListAdpter.getCheckMap().remove(i);
                        numListAdpter.remove(positon);
                    }
                }
                numListAdpter.notifyDataSetChanged();
                break;
            case R.id.btnSelectAll:
                if (btnSelectAll.getText().toString().trim().equals("全选")) {

                    // 所有项目全部选中
                    numListAdpter.configCheckMap(true);

                    numListAdpter.notifyDataSetChanged();

                    btnSelectAll.setText("全不选");
                } else {

                    // 所有项目全部不选中
                    numListAdpter.configCheckMap(false);

                    numListAdpter.notifyDataSetChanged();

                    btnSelectAll.setText("全选");
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (view.getTag() instanceof NumListAdpter.ViewHolder) {
            NumListAdpter.ViewHolder holder = (NumListAdpter.ViewHolder) view.getTag();
            // 会自动出发CheckBox的checked事件
            holder.cbCheck.toggle();
        }
    }
}
