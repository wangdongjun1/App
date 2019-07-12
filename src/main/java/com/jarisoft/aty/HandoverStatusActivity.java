package com.jarisoft.aty;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jarisoft.R;
import com.jarisoft.adpter.HandoverStatusAdapter;
import com.jarisoft.adpter.PipeHandoverAdapter;
import com.jarisoft.thread.WebServiceThread;
import com.jarisoft.util.Enum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robot on 2019/6/25.
 */

public class HandoverStatusActivity extends Activity{

    private RadioButton radioButtonIs;
    private RadioButton radioButtonNO;
    private RadioGroup radioStatus;
    Activity ctx;
    private HandoverStatusAdapter handoverStatusAdapter;

    List<String> listHandover;
    private ListView lvList;

    @Override
    public String[] databaseList() {
        return super.databaseList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.handover_status);
        bindView();
    }

    private void bindView(){
        Enum.mContext = this;
        ctx = this;
        lvList = findViewById(R.id.handover_num_list);
        radioStatus = findViewById(R.id.radio_group);
        radioButtonIs = (RadioButton)findViewById(R.id.isStatus);
        radioButtonNO = (RadioButton)findViewById(R.id.notStatus);

        radioStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                if(checkId==R.id.isStatus){
                    String method = "GetHandoverNumListByStatus";
                    String []argNames={"HandoverStatus"};
                    String []args ={"1"};
                    try{
                        Thread pipeThread = new Thread(new WebServiceThread(handoverHandler,method,argNames,args));
                        pipeThread.start();
                    }catch (Exception e){
                        System.out.print(e.getMessage());
                    }

                }
                else
//                else if(checkId==R.id.notStatus)
                {
                    String method = "GetHandoverNumListByStatus";
                    String []argNames={"HandoverStatus"};
                    String []args ={"0"};
                    Thread pipeThread = new Thread(new WebServiceThread(handoverHandler,method,argNames,args));
                    pipeThread.start();
                }
            }
        });
    }


    Handler handoverHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handlerListView(msg);
        }
    };

    private void handlerListView(Message msg){
        if(msg.what==1){
            String resultStr = (String)msg.obj;
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(resultStr);

                String handoverNumListSt = jsonObject.getString("HandoverNumList");
                JSONArray array = new JSONArray(handoverNumListSt);
                if(array.length()==0){
                    Toast.makeText(this,"查询结果为空!",Toast.LENGTH_LONG).show();
                    return;
                }
                handlerPipeInfos(array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            handoverStatusAdapter = new HandoverStatusAdapter(ctx,listHandover);
            lvList.setAdapter(handoverStatusAdapter);
        }
        if (msg.what==2){
            Toast.makeText(this,(String)msg.obj,Toast.LENGTH_LONG).show();
        }
    }

    private void handlerPipeInfos(JSONArray array) throws JSONException{
        listHandover = new ArrayList<String>();
        for (int i = 0; i < array.length(); i++) {
            String param = array.getString(0);
            listHandover.add(param);
        }
    }

}
