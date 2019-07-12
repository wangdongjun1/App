package com.jarisoft.aty.frm;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jarisoft.R;
import com.jarisoft.aty.PDFActivity;
import com.jarisoft.thread.FtpDownThread;
import com.jarisoft.thread.WebServiceThread;
import com.jarisoft.util.Enum;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;


public class BarcodeScanFrm extends Fragment {
    private TextView scanText;
    private EditText barCodeText;
    private TextView qrCodeText;
    /**
     * 编辑页面参数
     * @param savedInstanceState
     */
    private TextView handOverNumSR;
    private TextView projectNumSR;
    private TextView chartNumSR;
    private TextView pipeNumSR;
    private TextView barCodeSR;
    private TextView QRCodeSR;
    private TextView PDFUrlSR;
    private LinearLayout btnPdf;
    private LinearLayout btnET;
    String ss="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_barcode_scan_frm, container, false);
        bindView(view);
        return view;
    }

    private void bindView(View view){
        scanText = view.findViewById(R.id.etScanQr);
        barCodeText = view.findViewById(R.id.etBarcode);
        qrCodeText = view.findViewById(R.id.etQrcode);
        handOverNumSR= view.findViewById(R.id.scan_handOverNum);
        projectNumSR = view.findViewById(R.id.scan_projectNum);
        chartNumSR=view.findViewById(R.id.scan_chartNum);
        pipeNumSR = view.findViewById(R.id.scan_pipeNum);
        barCodeSR = view.findViewById(R.id.scan_barCode);
        QRCodeSR = view.findViewById(R.id.scan_QRCode);
        PDFUrlSR = view.findViewById(R.id.scan_PDFUrl);
        btnET = view.findViewById(R.id.search_et);
        btnET.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String[] argNmae={"barCode"};
                String[] arg={barCodeText.getText().toString()};
                @SuppressLint("HandlerLeak") Handler callHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        String resultStr = (String)msg.obj;
                        if (msg.what==1){
                            try {
                                JSONObject jsonObject = new JSONObject(resultStr);
                                String pileInfo = jsonObject.getString("PipeInfo");
                                JSONObject pi = new JSONObject(pileInfo);
                                handOverNumSR.setText(pi.getString("HandoverNum"));
                                projectNumSR.setText(pi.getString("ProjectNum"));
                                chartNumSR.setText(pi.getString("ChartNum"));
                                pipeNumSR.setText(pi.getString("PipeNum"));
                                barCodeSR.setText(pi.getString("BarCode"));
                                QRCodeSR.setText(pi.getString("QRCode"));
                                PDFUrlSR.setText(pi.getString("PDFUrl"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (msg.what==2){

                        }
                    }
                };
                Thread callThread = new Thread(new WebServiceThread(callHandler,"GetSinglePipeInfoByBarCode",argNmae,arg));
                callThread.start();

            }
        });


        btnPdf = view.findViewById(R.id.sacn_ly_btn_click);
        btnPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String url = PDFUrlSR.getText().toString().trim();
                    String []urls = url.split("/");
                    String pdfFileName = urls[urls.length-1];
                    int start = Enum.ConfigData.getFtpPort()==21?19:22;
                    String pdfFileDirec = url.substring(start,url.length()-pdfFileName.length()-1);
                    File file = new File(Enum.PdfFileTemp+"/"+pdfFileName);
                    if(file.exists()){
                        Intent intent = new Intent(getActivity(), PDFActivity.class);
                        intent.putExtra("fileName",pdfFileName);
                        startActivity(intent);
                    }else{
                        Thread thread = new Thread(new FtpDownThread(getActivity(),pdfFileDirec,pdfFileName));
                        thread.start();
                    }
                }catch (Exception e){
                    System.out.print(e.getMessage());
                }

            }
        });
    }

    public TextView getScanText() {
        return scanText;
    }

    public EditText getBarCodeText() {
        return barCodeText;
    }

    public TextView getQrCodeText() {
        return qrCodeText;
    }

    public TextView getHandOverNumSR() {
        return handOverNumSR;
    }

    public TextView getProjectNumSR() {
        return projectNumSR;
    }

    public TextView getChartNumSR() {
        return chartNumSR;
    }

    public TextView getPipeNumSR() {
        return pipeNumSR;
    }

    public TextView getBarCodeSR() {
        return barCodeSR;
    }

    public TextView getQRCodeSR() {
        return QRCodeSR;
    }

    public TextView getPDFUrlSR() {
        return PDFUrlSR;
    }
}
