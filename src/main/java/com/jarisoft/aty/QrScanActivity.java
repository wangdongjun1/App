package com.jarisoft.aty;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jarisoft.R;
import com.jarisoft.aty.frm.BarSearchFrm;
import com.jarisoft.aty.frm.BarcodeScanFrm;
import com.jarisoft.aty.frm.PileSearchFrm;
import com.jarisoft.thread.WebServiceThread;
import com.jarisoft.util.Enum;
import com.jarisoft.util.ScanHandlerManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

/**
 * 出厂业务实现
 */
public class QrScanActivity extends Activity implements View.OnClickListener {
    private LinearLayout barScan;
    private LinearLayout barSearch;
    private LinearLayout pileSearch;
    private LinearLayout qrBind;
    private FragmentManager fragmentManager;
    private BarcodeScanFrm barcodeScanFrm;
    private BarSearchFrm barSearchFrm;
    private PileSearchFrm pileSearchFrm;
    private ScanManager mScanManager;
    private static String scanStr;
    private static int ScanTimes = 0;
    private static int Index = 1;//标识当前展现fragment页面
    private String pdfFileName="";
    private String pdfFileDirec="";
    private FTPClient client;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_qrscan);
        bindView();
    }

    private void bindView(){
        Enum.mContext = this;
        Index = 1;
        barScan = findViewById(R.id.barcode_scan);
        pileSearch = findViewById(R.id.pile_search);
        qrBind = findViewById(R.id.qr_bind);
        barScan.setOnClickListener(this);
        pileSearch.setOnClickListener(this);
        qrBind.setOnClickListener(this);
        fragmentManager = getFragmentManager();
        setTabSelection(Index);
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
            case R.id.barcode_scan:
                Index = 1;
                ScanTimes = 0;
                setTabSelection(Index);
                break;
            case R.id.pile_search:
                Index = 3;
                ScanTimes = 0;
                setTabSelection(Index);
                break;
            case R.id.qr_bind:
                bindCodeCommit();
                break;
        }
    }

    private void setTabSelection(int index){
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index){
            case 1:
                if(barcodeScanFrm==null){
                    barcodeScanFrm = new BarcodeScanFrm();
                    transaction.add(R.id.fl_content,barcodeScanFrm);
                }else{
                    transaction.show(barcodeScanFrm);
                }
                break;
            case 2:
                if(barSearchFrm==null){
                    barSearchFrm = new BarSearchFrm();
                    transaction.add(R.id.fl_content,barSearchFrm);
                }else{
                    transaction.show(barSearchFrm);
                }
                break;
            case 3:
                if(pileSearchFrm==null){
                    pileSearchFrm = new PileSearchFrm();
                    transaction.add(R.id.fl_content,pileSearchFrm);
                }else{
                    transaction.show(pileSearchFrm);
                }
                break;
        }
        transaction.commit();
    }
    private void hideFragments(FragmentTransaction transaction)
    {
        if(barcodeScanFrm != null){
            transaction.hide(barcodeScanFrm);
        }
        if(barSearchFrm != null){
            transaction.hide(barSearchFrm);
        }
        if(pileSearchFrm != null){
            transaction.hide(pileSearchFrm);
        }
    }

    private void bindCodeCommit(){
        String barCodeStr = "";
        String qrCodeStr = "";
        switch (Index){
            case 1:
                barCodeStr = barcodeScanFrm.getBarCodeText().getText().toString();
                qrCodeStr = barcodeScanFrm.getQrCodeText().getText().toString();
                break;
            case 2:
                barCodeStr = barSearchFrm.getInputBar().getText().toString();
                qrCodeStr = barSearchFrm.getInputQr().getText().toString();
                break;
            case 3:
                barCodeStr = pileSearchFrm.getInputBar().getText().toString();
                qrCodeStr = pileSearchFrm.getInputQr().getText().toString();
                break;
        }
        if (TextUtils.isEmpty(barCodeStr)){
            Toast.makeText(this,"请扫描条码!",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(qrCodeStr)){
            Toast.makeText(this,"请扫码二维码!",Toast.LENGTH_LONG).show();
            return;
        }
        String []argNames = {"BarCode","QRCode"};
        String []args = {barCodeStr,qrCodeStr};
        Thread thread = new Thread(new WebServiceThread(commitHandler,"UploadBarCodeAndQRCode",argNames,args));
        thread.start();
    }

    /**
     *
     */
//    private PileInfo getPipeMessafe(){
//
//        return null;
//    }

    Handler commitHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                Toast.makeText(QrScanActivity.this,"绑定成功!",Toast.LENGTH_LONG).show();
            }
            if(msg.what==2){
                Toast.makeText(QrScanActivity.this,(String)msg.obj,Toast.LENGTH_LONG).show();
            }
        }
    };

    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            scanStr = ScanHandlerManager.HandlerScanData(intent);

            String checkBarCode=barcodeScanFrm.getBarCodeText().getText().toString();
            String checkQrCodeText=barcodeScanFrm.getQrCodeText().getText().toString();
            switch (Index){
                case 1:
//                    if(ScanTimes==0&&checkBarCode.isEmpty()){
                        if(checkBarCode.isEmpty()){
                        barcodeScanFrm.getScanText().setText("请扫描二维码!");
                        barcodeScanFrm.getBarCodeText().setText(scanStr);
                        //根据条形码查询
                        String[] argNmae={"barCode"};
                        String[] arg={scanStr};
                        Thread callThread = new Thread(new WebServiceThread(callHandler,"GetSinglePipeInfoByBarCode",argNmae,arg));
                        callThread.start();
                        ScanTimes++;
//                    }else if (ScanTimes==1||!checkBarCode.isEmpty()){
                        }else if (!checkBarCode.isEmpty()&&checkQrCodeText.isEmpty()){
                        barcodeScanFrm.getScanText().setText("扫描完毕!");
                        barcodeScanFrm.getQrCodeText().setText(scanStr);
                        ScanTimes=0;
                    }
                    break;
                case 2:
                    barSearchFrm.getScanBarQr().setText("扫描完毕!");
                    barSearchFrm.getInputQr().setText(scanStr);
                    break;
                case 3:
                    if(TextUtils.isEmpty(pileSearchFrm.getPileQr().getText())){
                        pileSearchFrm.getInputBar().setText(scanStr);
                        pileSearchFrm.getPileQr().setText("扫描二维码!");
                    }else{
                        pileSearchFrm.getPileQr().setText("扫描完毕!");
                        pileSearchFrm.getInputQr().setText(scanStr);
                    }

                    break;
            }
        }
    };
    @SuppressLint("HandlerLeak")
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
                    barcodeScanFrm.getHandOverNumSR().setText(pi.getString("HandoverNum"));
                    barcodeScanFrm.getProjectNumSR().setText(pi.getString("ProjectNum"));
                    barcodeScanFrm.getChartNumSR().setText(pi.getString("ChartNum"));
                    barcodeScanFrm.getPipeNumSR().setText(pi.getString("PipeNum"));
                    barcodeScanFrm.getBarCodeSR().setText(pi.getString("BarCode"));
                    barcodeScanFrm.getQRCodeSR().setText(pi.getString("QRCode"));
                    barcodeScanFrm.getPDFUrlSR().setText(pi.getString("PDFUrl"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (msg.what==2){
                Toast.makeText(QrScanActivity.this,resultStr,Toast.LENGTH_LONG).show();
            }
        }
    };
    public class MyTransferListener implements FTPDataTransferListener {
        @Override
        public void started() {

        }

        @Override
        public void transferred(int i) {

        }

        @Override
        public void completed() {
            Intent intent = new Intent(QrScanActivity.this,PDFActivity.class);
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
