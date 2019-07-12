package com.jarisoft.thread;

import android.content.Context;

import com.jarisoft.util.Enum;

import java.io.File;

import it.sauronsoftware.ftp4j.FTPClient;

/**
 * Created by robot on 2018/7/27.
 */

public class FtpDownThread implements Runnable{
    private String pdfFileDirec;
    private String pdfFileName;
    private Context context;

    public FtpDownThread(Context context,String pdfFileDirec,String pdfFileName){
        this.context = context;
        this.pdfFileDirec = pdfFileDirec;
        this.pdfFileName = pdfFileName;
    }
    @Override
    public void run() {
        FTPClient client = new FTPClient();
        try {
            client.connect(Enum.ConfigData.getFtpAddress(),Enum.ConfigData.getFtpPort());
            client.login(Enum.ConfigData.getUser(),Enum.ConfigData.getPassword());
            client.changeDirectory(pdfFileDirec);
            File cfile = new File(Enum.PdfFileTemp+"/"+pdfFileName);
            client.download(pdfFileName,cfile,new MyTransferListener(context,pdfFileName));
        }catch (Exception e){

        }
    }


}
