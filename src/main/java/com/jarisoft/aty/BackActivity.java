package com.jarisoft.aty;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jarisoft.R;
import com.jarisoft.adpter.PipeCheckListAdpter;
import com.jarisoft.entity.PileInfo;
import com.jarisoft.thread.FtpDownThread;
import com.jarisoft.thread.WebServiceThread;
import com.jarisoft.util.AllCapTransformation;
import com.jarisoft.util.Enum;
import com.jarisoft.util.ScanHandlerManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.sauronsoftware.ftp4j.FTPClient;

/**
 * 回厂业务实现
 */
public class BackActivity extends Activity implements OnClickListener, AdapterView.OnItemClickListener {
    private TextView etQrcode;
    private EditText etProNum;
    private EditText etChartNum;
    private EditText etPipeNum;
    private EditText etBatchNum;
    private ListView lvList;
    private LinearLayout lySearch;
    private Button btnCommit;
    private FTPClient client;
    private static String pdfFileName="";
    private static String pdfFileDirec="";
    private PipeCheckListAdpter pipeListAdpter;
    private List<PileInfo> pileInfos;
    Activity ctx;
    private ScanManager mScanManager;
    private static String scanStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_back);
        bindView();
    }


    private void bindView(){
        Enum.mContext = this;
        ctx = this;
        etQrcode = findViewById(R.id.bar_ser_qr_code);
        etProNum = findViewById(R.id.bar_ser_pro_num);
        etProNum.setTransformationMethod(new AllCapTransformation());
        etChartNum = findViewById(R.id.bar_ser_chart_num);
        etChartNum.setTransformationMethod(new AllCapTransformation());
        etPipeNum = findViewById(R.id.bar_ser_pipe_num);
        etPipeNum.setTransformationMethod(new AllCapTransformation());
        etBatchNum = findViewById(R.id.bar_ser_batch_num);
        lvList = findViewById(R.id.bar_ser_list);
        lySearch = findViewById(R.id.bar_ser_btn_search);
        btnCommit = findViewById(R.id.bar_ser_btn_commit);
        lySearch.setOnClickListener(this);
        btnCommit.setOnClickListener(this);
        lvList.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScanManager = new ScanManager();
        ScanHandlerManager.openScanner(mScanManager);
        IntentFilter filter = ScanHandlerManager.getIntentFilter(mScanManager, Enum.SCAN_ACTION);
        registerReceiver(mScanReceiver,filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mScanManager!=null){
            mScanManager.stopDecode();
        }
        unregisterReceiver(mScanReceiver);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bar_ser_btn_search:
                  queryInfo();
                break;
            case R.id.bar_ser_btn_commit:
                String batchNumStr = etBatchNum.getText().toString().trim();
                if(TextUtils.isEmpty(batchNumStr)){
                    Toast.makeText(this,"请输入批次号!",Toast.LENGTH_SHORT).show();
                    return;
                }
                Thread timeThread = new Thread(new WebServiceThread(timeHandler,"GetServerTime",new String[]{},new String[]{}));
                timeThread.start();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (view.getTag() instanceof PipeCheckListAdpter.ViewHolder) {
            PipeCheckListAdpter.ViewHolder holder = (PipeCheckListAdpter.ViewHolder) view.getTag();
            // 会自动出发CheckBox的checked事件
            holder.cbCheck.toggle();
        }
    }

    Handler timeHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==1){
                String resultStr = (String)msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(resultStr);
                    String time = jsonObject.getString("Time");
                    String dateTime = time.substring(0,time.length()-9);
                    dateTime.replace("-","");
                    //获取批次号
                    String batchNumStr = etBatchNum.getText().toString().trim();
                    batchNumStr = dateTime + batchNumStr;
                    Map<Integer, Boolean> map = pipeListAdpter.getCheckMap();
                    //获取提交的数量
                    int count = pipeListAdpter.getCount();
//                    JSONObject object = new JSONObject();
//                    JSONArray array = new JSONArray();
//                    for (int i = 0; i < count; i++) {
//                        PileInfo pileInfo = (PileInfo) pipeListAdpter.getItem(i);
//                        if(map.get(i)!=null&&map.get(i)){
//                          JSONObject jo = new JSONObject();
//                          jo.put("BatchNum",batchNumStr);
//                          jo.put("BackTime",time);
//                          jo.put("QRCode",pileInfo.getQRCode());
//                          array.put(jo);
//                        }
//                    }
//                    object.put("pipes",array.toString());
//                    String interStr = object.toString();
//                    Thread thread = new Thread(new WebServiceThread(commitHandler,"RecordPipeListBackTime",
//                            new String[]{"pipes"},new String[]{interStr}));
//                    thread.start();

                    for (int i = 0; i < count; i++) {
                        PileInfo pileInfo = (PileInfo) pipeListAdpter.getItem(i);
                        if(map.get(i)!=null&&map.get(i)){
                            String qrCode = pileInfo.getQRCode();
                            String []argNames = {"BatchNum","BackTime","QRCode"};
                            String []args = {batchNumStr,time,qrCode};
                            Thread thread = new Thread(new WebServiceThread(commitHandler,"RecordPipeBackTime",argNames,args));
                            thread.start();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (msg.what==2){
                Toast.makeText(BackActivity.this,(String)msg.obj,Toast.LENGTH_LONG).show();
            }
        }
    };

    Handler commitHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                Toast.makeText(BackActivity.this,"提交回厂信息成功!",Toast.LENGTH_LONG).show();
                queryInfo();
            }
            if (msg.what==2){
                Toast.makeText(BackActivity.this,(String)msg.obj,Toast.LENGTH_LONG).show();
            }
        }
    };
    Handler pipeHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handlerListView(msg);
        }
    };

    Handler qrHandler = new Handler(){
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
            pipeListAdpter = new PipeCheckListAdpter(ctx,pileInfos,onClickListener);
            lvList.setAdapter(pipeListAdpter);
        }
        if (msg.what==2){
            Toast.makeText(this,(String)msg.obj,Toast.LENGTH_LONG).show();
        }
    }

    private void handlerPipeInfos(JSONArray array) throws JSONException {
        pileInfos = new ArrayList<PileInfo>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            PileInfo pileInfo = new PileInfo();
            pileInfo.setHandoverNum(jsonObject.getString("HandoverNum"));
            pileInfo.setChartNum(jsonObject.getString("ChartNum"));
            pileInfo.setPipeNum(jsonObject.getString("PipeNum"));
            pileInfo.setProjectNum(jsonObject.getString("ProjectNum"));
            pileInfo.setPDFUrl(jsonObject.getString("PDFUrl"));
            pileInfo.setQRCode(jsonObject.getString("QRCode"));
            pileInfos.add(pileInfo);
        }
    }
    private  OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            LinearLayout lvBtnPdf = (LinearLayout) view;
            String url = (String) lvBtnPdf.getTag();
            url.trim();
            String []urls = url.split("/");
            pdfFileName = urls[urls.length-1];
	        int start = Enum.ConfigData.getFtpPort()==21?19:22;
            pdfFileDirec = url.substring(start,url.length()-pdfFileName.length()-1);
            File file = new File(Enum.PdfFileTemp+"/"+pdfFileName);
            if(file.exists()){
                Intent intent = new Intent(BackActivity.this, PDFActivity.class);
                intent.putExtra("fileName",pdfFileName);
                startActivity(intent);
            }else{
                Thread thread = new Thread(new FtpDownThread(BackActivity.this,pdfFileDirec,pdfFileName));
                thread.start();
            }
        }
    };

//    public class MyTransferListener implements FTPDataTransferListener {
//        @Override
//        public void started() {
//
//        }
//
//        @Override
//        public void transferred(int i) {
//
//        }
//
//        @Override
//        public void completed() {
//            Intent intent = new Intent(BackActivity.this,PDFActivity.class);
//            intent.putExtra("fileName",pdfFileName);
//            startActivity(intent);
//        }
//
//        @Override
//        public void aborted() {
//
//        }
//
//        @Override
//        public void failed() {
//
//        }
//    }
    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            scanStr = ScanHandlerManager.HandlerScanData(intent).trim();
            etQrcode.setText(scanStr);
            String method1 = "GetPipeInfosByQRCode";
            String []argNames1 = {"QRCode"};
            String []args1 = {scanStr};
            Thread qrThread = new Thread(new WebServiceThread(qrHandler,method1,argNames1,args1));
            qrThread.start();
        }
    };
    private void queryInfo(){
        String proNumStr = etProNum.getText().toString().trim();
        String chartNumStr = etChartNum.getText().toString().trim();
        String pipeNumStr = etPipeNum.getText().toString().trim();
        if(TextUtils.isEmpty(pipeNumStr)){
            Toast.makeText(this,"请输入管件号",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(chartNumStr)){
            Toast.makeText(this,"请输入图号",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(proNumStr)){
            Toast.makeText(this,"请输入工程编号",Toast.LENGTH_SHORT).show();
            return;
        }
        String method = "GetPipeInfoInBack";
        String []argNames = {"ProjectNum","ChartNum","PipeNum"};
        String []args = {proNumStr,chartNumStr,pipeNumStr};
        Thread pipeThread = new Thread(new WebServiceThread(pipeHandler,method,argNames,args));
        pipeThread.start();
    }
}
