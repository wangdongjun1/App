package com.jarisoft.aty;

import android.app.Activity;
import android.content.Intent;
import android.device.ScanManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jarisoft.R;
import com.jarisoft.adpter.PipeHandoverAdapter;
import com.jarisoft.entity.PileInfo;
import com.jarisoft.thread.WebServiceThread;
import com.jarisoft.util.AllCapTransformation;
import com.jarisoft.util.Enum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 交接单号获取数据
 */

public class HandoverSearchActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener{
    private ScanManager mScanManager;
    private static String scanStr;
    private EditText handoverNum;
    private LinearLayout lySearch;
    private TextView tProjectNum;
    private  TextView tType;
    private TextView tHandoverNum;
    private  TextView tFrameNum;
    private TextView tDate;
    private List<PileInfo> pileInfos;
    private PipeHandoverAdapter pipeHandoverAdapter;
    Activity ctx;
    private ListView lvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.list_pipe_handover_search);
        bindView();
    }
    private void bindView(){
        Enum.mContext = this;
        ctx = this;
        tProjectNum = findViewById(R.id.bar_ser_pro_num);
        tType = findViewById(R.id.bar_ser_type);
        tFrameNum = findViewById(R.id.bar_ser_fram_num);
        tDate = findViewById(R.id.bar_ser_date);

        handoverNum = findViewById(R.id.bar_handover_num);
        handoverNum.setTransformationMethod(new AllCapTransformation());
        lvList = findViewById(R.id.bar_ser_list);
        lySearch = findViewById(R.id.bar_ser_btn_search);
        lySearch.setOnClickListener(this);
        lvList.setOnItemClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bar_ser_btn_search:
                String prohandoverNum = handoverNum.getText().toString().trim();
                if(TextUtils.isEmpty(prohandoverNum)){
                    Toast.makeText(this,"请输入交接单号",Toast.LENGTH_SHORT).show();
                    return;
                }
                String method = "GetPipeInfoByHandoverNum";
                String []argNames={"HandoverNum"};
                String []args ={prohandoverNum,};
                Thread pipeThread = new Thread(new WebServiceThread(pipeHandler,method,argNames,args));
                pipeThread.start();
                break;
        }
    }
    Handler pipeHandler = new Handler(){
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
                String HandoverInfoStr= jsonObject.getString("HandoverInfo");
                //新增编号
                JSONObject pi = new JSONObject(HandoverInfoStr);
                tProjectNum.setText(pi.getString("ProjectNum"));
                tFrameNum.setText(pi.getString("FrameNum"));
                tType.setText(pi.getString("Type"));
                tDate.setText(pi.getString("Date"));

                String pileListStr = jsonObject.getString("PipeList");
                JSONArray array = new JSONArray(pileListStr);
                if(array.length()==0){
                    Toast.makeText(this,"查询结果为空!",Toast.LENGTH_LONG).show();
                    return;
                }
                handlerPipeInfos(array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pipeHandoverAdapter = new PipeHandoverAdapter(ctx,pileInfos,onClickListener);
            lvList.setAdapter(pipeHandoverAdapter);
        }
        if (msg.what==2){
            Toast.makeText(this,(String)msg.obj,Toast.LENGTH_LONG).show();
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            LinearLayout lvBtn = (LinearLayout) view;
            PileInfo pileInfoDate = (PileInfo) lvBtn.getTag();
            Intent intent = new Intent(HandoverSearchActivity.this, HandoverDetail.class);
            intent.putExtra("pileInfo",pileInfoDate);
            startActivity(intent);
        }
    };


    private void handlerPipeInfos(JSONArray array) throws JSONException {
        pileInfos = new ArrayList<PileInfo>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            PileInfo pileInfo = new PileInfo();
            pileInfo.setInsideCode(jsonObject.getString("InsideCode"));
            pileInfo.setSubsection(jsonObject.getString("Subsection"));
            pileInfo.setInstallTrayNum(jsonObject.getString("InstallTrayNum"));
            pileInfo.setPipeType(jsonObject.getString("PipeType"));
            pileInfo.setPipeMaterial(jsonObject.getString("PipeMaterial"));
            pileInfo.setPipeSpec(jsonObject.getString("PipeSpec"));
            pileInfo.setPipeLength(jsonObject.getString("PipeLength"));
            pileInfo.setPipeWeight(jsonObject.getString("PipeWeight"));
            pileInfo.setSurfaceCode(jsonObject.getString("SurfaceCode"));
            pileInfo.setPipeNum(jsonObject.getString("PipeNum"));
            pileInfo.setBarCode(jsonObject.getString("BarCode"));
            pileInfo.setQRCode(jsonObject.getString("QRCode"));

            pileInfos.add(pileInfo);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
