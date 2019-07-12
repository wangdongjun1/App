package com.jarisoft.thread;

import android.content.Context;
import android.content.Intent;

import com.jarisoft.aty.PDFActivity;

import it.sauronsoftware.ftp4j.FTPDataTransferListener;

/**
 * Created by robot on 2018/7/27.
 */

public class MyTransferListener implements FTPDataTransferListener {
    private Context ctx;
    private String pdfFileName;
    public MyTransferListener(Context ctx, String pdfFileName){
        this.ctx = ctx;
        this.pdfFileName = pdfFileName;
    }
    @Override
    public void started() {

    }

    @Override
    public void transferred(int i) {

    }

    @Override
    public void completed() {

    }

    @Override
    public void aborted() {

    }

    @Override
    public void failed() {
        Intent intent = new Intent(this.ctx,PDFActivity.class);
        intent.putExtra("fileName",this.pdfFileName);
        ctx.startActivity(intent);
    }
}
