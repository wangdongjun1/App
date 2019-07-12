package com.jarisoft;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;

import com.jarisoft.entity.Config;
import com.jarisoft.util.Enum;
import com.jarisoft.util.Utils;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.model.HttpParams;

import java.io.File;
import java.io.IOException;

/**
 * 初始化app 事件处理
 * Created by shanwj on 2018/7/17.
 */

public class GApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initFile();//初始化创建文件夹
        initSound();//初始化扫码声音
        initVibrator();//初始化设备震动
//        initOkHttp();//初始化HTTP请求
    }

    private void initFile(){
        try {
            File file = new File(Enum.PdfFileTemp);
            if (!file.exists()){
                file.mkdirs();
            }
            File logFile = new File(Enum.LogFileTemp);
            if (!logFile.exists()){
                logFile.mkdirs();
            }
            File managerFile = new File(Enum.ManagerFileTemp);
            if (!managerFile.exists()){
                managerFile.mkdirs();
            }
            String []list = managerFile.list();
            String str = "";
            if(list.length==0){
                File txtFile =new File(Enum.ManagerFileTemp,Enum.ManagerTxt);
                boolean b = txtFile.createNewFile();
                if(b){
                    str = Utils.InitConfigData();
                    Utils.TxtWriteString(str,Enum.ManagerFileTemp+"/"+Enum.ManagerTxt);
                }
            }else{
                str = Utils.TxtReadFile(Enum.ManagerFileTemp+"/"+Enum.ManagerTxt);
            }
            Enum.ConfigData = new Config();
            Utils.UpdateConfig(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSound(){
        Enum.soundPool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION,100);
        Enum.soundId = Enum.soundPool.load("etc/Scan_new.ogg",1);
    }

    private void initVibrator(){
        Enum.mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }


    private void initOkHttp(){
        HttpParams params = new HttpParams();
        OkHttpUtils.init(this);
        OkHttpUtils.getInstance()//
                .debug("OkHttpUtils")                                              //是否打开调试
                .setConnectTimeout(OkHttpUtils.DEFAULT_MILLISECONDS)               //全局的连接超时时间
                .setReadTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                  //全局的读取超时时间
                .setWriteTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                 //全局的写入超时时间
                .addCommonParams(params);                                          //设置全局公共头
    }

}
