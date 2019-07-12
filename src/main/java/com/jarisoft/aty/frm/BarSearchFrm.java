package com.jarisoft.aty.frm;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jarisoft.R;
import com.jarisoft.aty.PDFActivity;
import com.jarisoft.thread.FtpDownThread;
import com.jarisoft.util.Enum;

import java.io.File;

public class BarSearchFrm extends Fragment {
    private TextView scanBarQr;
    private EditText inputBar;
    private EditText inputQr;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bar_search_frm, container, false);
        bindView(view);
        return view;
    }

    private void bindView(View view){
        scanBarQr = view.findViewById(R.id.etScanBarQr);
        inputBar = view.findViewById(R.id.etInputNum);
        inputQr = view.findViewById(R.id.etSearchQr);
        handOverNumSR= view.findViewById(R.id.scan_handOverNum);
        projectNumSR = view.findViewById(R.id.scan_projectNum);
        chartNumSR=view.findViewById(R.id.scan_chartNum);
        pipeNumSR = view.findViewById(R.id.scan_pipeNum);
        barCodeSR = view.findViewById(R.id.scan_barCode);
        QRCodeSR = view.findViewById(R.id.scan_QRCode);
        PDFUrlSR = view.findViewById(R.id.scan_PDFUrl);
        btnPdf = view.findViewById(R.id.sacn_ly_btn_click);
        btnPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });
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

    public TextView getScanBarQr() {
        return scanBarQr;
    }

    public EditText getInputBar() {
        return inputBar;
    }

    public EditText getInputQr() {
        return inputQr;
    }

}
