package com.jarisoft.thread;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jarisoft.util.Enum;
import com.jarisoft.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

/**
 * Created by shanwj on 2018/7/18.
 */

public class WebServiceThread implements Runnable{
    private Handler handler;
    private String method;
    private String[] argNames;
    private String[] args;
    public WebServiceThread(Handler handler,String method,String[] argNames,String[] args){
        this.handler = handler;
        this.method = method;
        this.argNames = argNames;
        this.args = args;
    }

    /**
     * msg.what = 1处理正确逻辑
     * msg.what = 2处理错误逻辑
     */
    @Override
    public void run() {
        String returnStr = Utils.HttpWebServiceReturn(method,argNames,args);
        Log.e("WebServiceReturn",returnStr);
        JSONObject log = new JSONObject();

        try {
            log.put("user",Enum.CurrentNum);
            log.put("method",method);
            String time = Utils.getCurrentTime();
            log.put("time",time);
            log.put("result",returnStr);
            String logStr = log.toString();
            Utils.WriteLog(logStr,time+".txt");
            Message msg = new Message();
            if (returnStr.contains(Enum.Regex)){
                msg.what = 2;
                returnStr = returnStr.split(Enum.Regex)[1];
                msg.obj = returnStr;
            }else{
                JSONObject jsonObject = new JSONObject(returnStr);
                boolean b = jsonObject.getBoolean("Flag");
//                if(method.equals("UploadBarCodeAndQRCode")){
//                    b = jsonObject.getBoolean("Result");
//                }else {
//                    b = jsonObject.getBoolean("Flag");
//                }
                if(b){
                    msg.what = 1;
                    msg.obj = returnStr;
                }else{
                    msg.what = 2;
                    msg.obj = jsonObject.getString("Message");
                }
            }
            handler.sendMessage(msg);
        } catch (Exception e) {
            System.out.print(e.getMessage());
            e.printStackTrace();
        }
    }
}
