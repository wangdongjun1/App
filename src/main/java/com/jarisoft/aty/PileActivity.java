package com.jarisoft.aty;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.jarisoft.R;
import com.jarisoft.thread.FtpDownThread;
import com.jarisoft.thread.WebServiceThread;
import com.jarisoft.util.Enum;
import com.jarisoft.util.ScanHandlerManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

/**
 * 分堆功能实现
 */
public class PileActivity extends Activity implements View.OnClickListener {
    private LinearLayout lySendInfo;
    private TextView  tvProNum;
    private TextView tvChartNum;
    private TextView tvPipeNum;
    private TextView tvQrcode;
    private TextView tvInstallNum;
    private TextView etQrcode;
    private TextView tvPDFUrl;
    private ScanManager mScanManager;
    private static String scanStr;
    private static String pdfFileName="";
    private static String pdfFileDirec="";
    private FTPClient client;
    private LinearLayout btnPdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_pile);
        bindView();
    }

    private void bindView(){
        Enum.mContext = this;
        lySendInfo = findViewById(R.id.btn_send_info);
        etQrcode = findViewById(R.id.pipe_et_QrCode);
        tvProNum = findViewById(R.id.pipe_pro_num);
        tvChartNum = findViewById(R.id.pipe_char_num);
        tvPipeNum = findViewById(R.id.pipe_pipe_num);
        tvQrcode = findViewById(R.id.pipe_qrcode);
        tvPDFUrl = findViewById(R.id.pipe_PDF);
        btnPdf = findViewById(R.id.pipe_PDF_btn_click);
        btnPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String url = tvPDFUrl.getText().toString().trim();
                    String []urls = url.split("/");
                    String pdfFileName = urls[urls.length-1];
                    int start = Enum.ConfigData.getFtpPort()==21?19:22;
                    String pdfFileDirec = url.substring(start,url.length()-pdfFileName.length()-1);
                    File file = new File(Enum.PdfFileTemp+"/"+pdfFileName);
                    if(file.exists()){
                        Intent intent = new Intent(PileActivity.this, PDFActivity.class);
                        intent.putExtra("fileName",pdfFileName);
                        startActivity(intent);
                    }else{
                        Thread thread = new Thread(new FtpDownThread(PileActivity.this,pdfFileDirec,pdfFileName));
                        thread.start();
                    }
                }catch (Exception e){
                    System.out.print(e.getMessage());
                }

            }
        });
        tvInstallNum = findViewById(R.id.pipe_install_num);
        lySendInfo.setOnClickListener(this);
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
            case R.id.btn_send_info:
                Thread timeThread = new Thread(new WebServiceThread(timeHandler,"GetServerTime",new String[]{},new String[]{}));
                timeThread.start();
                break;
            case R.id.pipe_PDF_btn_click:

        }
    }


    Handler callHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String resultStr = (String)msg.obj;
            if (msg.what==1){
                try {
                    JSONObject jsonObject = new JSONObject(resultStr);
                    String pileInfo = jsonObject.getString("PipeInfo");
                    JSONObject pi = new JSONObject(pileInfo);
                    tvProNum.setText(pi.getString("ProjectNum"));
                    tvChartNum.setText(pi.getString("ChartNum"));
                    tvPipeNum.setText(pi.getString("PipeNum"));
                    tvQrcode.setText(pi.getString("QRCode"));
                    tvInstallNum.setText(pi.getString("InstallTrayNum"));
                    tvPDFUrl.setText(pi.getString("PDFUrl"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            if (msg.what==2){
                Toast.makeText(PileActivity.this,resultStr,Toast.LENGTH_LONG).show();
            }
        }
    };

    Handler timeHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==1){
                String resultStr = (String)msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(resultStr);
                    String time = jsonObject.getString("Time");
                    time = time.substring(0,time.length()-9);
                    Log.e("Time",time);
                    String qrCode = etQrcode.getText().toString().trim();
                    String []argNames = {"QRCode","CollectorNum","CollectionDate"};
                    String []args = {qrCode,Enum.CurrentNum,time};
                    Thread thread = new Thread(new WebServiceThread(sendHandler,"GetCollectionInfo",argNames,args));
                    thread.start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (msg.what==2){
                Toast.makeText(PileActivity.this,(String)msg.obj,Toast.LENGTH_LONG).show();
            }
        }
    };

    Handler sendHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                Toast.makeText(PileActivity.this,"集配时间已成功返回!",Toast.LENGTH_LONG).show();
            }
            if (msg.what==2){
                Toast.makeText(PileActivity.this,(String)msg.obj,Toast.LENGTH_LONG).show();
            }
        }
    };

    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            scanStr = ScanHandlerManager.HandlerScanData(intent).trim();
            etQrcode.setText(scanStr);
            String method = "GetPipeInfoByQRCode";
            String [] argNames = {"QRCode"};
            String [] args = {scanStr};
            Thread callThread = new Thread(new WebServiceThread(callHandler,method,argNames,args));
            callThread.start();
        }
    };
//    private View.OnClickListener onClickListener = new View.OnClickListener() {
//
//        @Override
//        public void onClick(View view) {
//            LinearLayout lvBtnPdf = (LinearLayout) view;
//            String url = (String) lvBtnPdf.getTag();
//            url.trim();
//            Log.e("Url",url);
//            String []urls = url.split("/");
//            pdfFileName = urls[urls.length-1];
//            int start = Enum.ConfigData.getFtpPort()==21?19:22;
//            pdfFileDirec = url.substring(start,url.length()-pdfFileName.length()-1);
//            File file = new File(Enum.PdfFileTemp+"/"+pdfFileName);
//            if(file.exists()){
//                Intent intent = new Intent(PileActivity.this, PDFActivity.class);
//                intent.putExtra("fileName",pdfFileName);
//                startActivity(intent);
//            }else{
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        client = new FTPClient();
//                        try {
//                            client.connect(Enum.ConfigData.getFtpAddress(),Enum.ConfigData.getFtpPort());
//                            client.login(Enum.ConfigData.getUser(),Enum.ConfigData.getPassword());
//                            client.changeDirectory(pdfFileDirec);
//                            File cfile = new File(Enum.PdfFileTemp+"/"+pdfFileName);
//                            client.download(pdfFileName,cfile,new MyTransferListener());
//                        }catch (Exception e){
//
//                        }
//                    }
//                });
//                thread.start();
//            }
//
//        }
//    };
    public class MyTransferListener implements FTPDataTransferListener {
        @Override
        public void started() {

        }

        @Override
        public void transferred(int i) {

        }

        @Override
        public void completed() {
            Intent intent = new Intent(PileActivity.this,PDFActivity.class);
            intent.putExtra("fileName",pdfFileName);
            startActivity(intent);
        }

        @Override
        public void aborted() {

        }

        @Override
        public void failed() {

        }
    }
}
