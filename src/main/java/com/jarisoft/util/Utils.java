package com.jarisoft.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.jarisoft.aty.BackActivity;
import com.jarisoft.aty.PDFActivity;
import com.jarisoft.entity.Config;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by shanwj on 2018/7/17.
 */

public class Utils {

    /**
     * make true current connect service is wifi
     * @param mContext
     * @return
     */
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 通过http方式请求WebService接口获取返回值
     * @param method 方法名称
     * @param argNames 参数名称数组
     * @param args 参数数组
     * @return
     */
    public static String HttpWebServiceReturn(String method,String[] argNames,String[] args){
        String nodeStr = "";
        if(!isWifi(Enum.mContext)){
            Log.e("Error","网络异常，请确认wifi已连接!");
            nodeStr = Enum.Regex+"网络异常，请确认wifi已连接!";
            return nodeStr;
        }
        if(argNames.length!=args.length){
            Log.e("Error","参数与参数值数量不一致,请联系管理员!");
            nodeStr = Enum.Regex+"参数与参数值数量不一致!";
            return nodeStr;
        }
        try {
            URL url = new URL(Enum.ConfigData.getWebService());

            HttpURLConnection cnn = (HttpURLConnection) url.openConnection();
            cnn.setDoInput(true);
            cnn.setDoOutput(true);
            cnn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            cnn.addRequestProperty("SOAPAction",Enum.ConfigData.getNameSpcae()+method);
            cnn.setRequestMethod("POST");
            cnn.setConnectTimeout(10000);
            OutputStream ops = cnn.getOutputStream();
            StringBuffer buffer = new StringBuffer(5000);
            buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
                    .append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">")
                    .append("<soap:Body>")
                    .append("<"+method+" xmlns=\"http://tempuri.org/\">");
            for (int i = 0; i < argNames.length; i++) {
                buffer.append("<"+argNames[i]+">"+args[i]+"</"+argNames[i]+">");
            }
            buffer.append("</"+method+"></soap:Body></soap:Envelope>");
            ops.write(buffer.toString().getBytes());
            int code = cnn.getResponseCode();
            if(code == 200){
                InputStream ios = cnn.getInputStream();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(ios);
                NodeList list = document.getElementsByTagName(method+"Result");
                if(list.getLength()>0){
                    Node n = list.item(0);
                    NodeList childNodes = n.getChildNodes();
                    if(childNodes.getLength()>0){
                        Node cn = childNodes.item(0);
                        nodeStr = cn.getTextContent();
                    }else{
                        nodeStr = Enum.Regex+"接口返回数据异常，请联系管理员！";
                    }

                }else{
                    nodeStr = Enum.Regex+"接口返回数据异常，请联系管理员！";
                }
            }else{
                nodeStr = Enum.Regex+"网络请求超时请检查网络!";
            }
        }catch (Exception e){
            nodeStr = Enum.Regex+e.getMessage();
        }finally {
            return  nodeStr;
        }
    }

    /**
     * 第一次启动app时初始化使用
     */
    public static String InitConfigData() throws JSONException {
        JSONObject object = new JSONObject();
//        object.put("WebServiceUrl","http://192.168.20.13:8090/LogisticTracking.asmx");
//        object.put("WebNameSapce","http://tempuri.org/");
//        object.put("FtpAddress","192.168.20.13");
//        object.put("FtpPort",21);
//        object.put("FtpUser","administrator");
//        object.put("FtpUserPassword","Pmeise@438");
//        object.put("LogShow",true);
//        object.put("LogSize",20);
//        object.put("WebServiceUrl","http://192.168.16.83:23852/JariCsicInfo/Reference/LogisticTracking.asmx");


//        object.put("WebServiceUrl","http://192.168.16.96:85/JariCsicInfo/Reference/LogisticTracking.asmx");
//        object.put("WebNameSapce","http://tempuri.org/");
//        object.put("FtpAddress","192.168.16.96");
//        object.put("FtpPort",30);
//        object.put("FtpUser","ftpadmin");
//        object.put("FtpUserPassword","jarisoft@123");
//        object.put("LogShow",true);
//        object.put("LogSize",20);
        object.put("WebServiceUrl","http://218.92.85.58:2837/jariIMS/Common/LogisticTracking.asmx");
        object.put("WebNameSapce","http://tempuri.org/");
        object.put("FtpAddress","192.168.16.96");
        object.put("FtpPort",30);
        object.put("FtpUser","ftpadmin");
        object.put("FtpUserPassword","jarisoft@123");
        object.put("LogShow",true);
        object.put("LogSize",20);
        String str = object.toString();
        return str;
    }

    /**
     * 每次app启动时候初始化数据
     * @param str
     * @throws JSONException
     */
    public static void UpdateConfig(String str) throws JSONException {
        JSONObject object = new JSONObject(str);
        Enum.ConfigData.setFtpAddress(object.getString("FtpAddress"));
        Enum.ConfigData.setFtpPort(object.getInt("FtpPort"));
        Enum.ConfigData.setLogOpen(object.getBoolean("LogShow"));
        Enum.ConfigData.setNameSpcae(object.getString("WebNameSapce"));
        Enum.ConfigData.setPassword(object.getString("FtpUserPassword"));
        Enum.ConfigData.setUser(object.getString("FtpUser"));
        Enum.ConfigData.setWebService(object.getString("WebServiceUrl"));
        Enum.ConfigData.setLogNum(object.getInt("LogSize"));
    }
    /**
     * 把字符串写入txt文件
     * @param str
     * @param fileName
     */
    public static void TxtWriteString(String str,String fileName) throws IOException {
        FileWriter fw = new FileWriter(fileName);
        fw.write(str);
        fw.flush();
        fw.close();
    }

    /**
     * 读取txt文件内容
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String TxtReadFile(String fileName) throws IOException {
        String str = "";
        FileReader reader = new FileReader(fileName);
        char[] cbuf = new char[1];
        while(reader.read(cbuf) != -1){
            str = str+String.copyValueOf(cbuf);
        }
        reader.close();
        return str;
    }

    public static String getCurrentTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String str = format.format(date);
        return str;
    }


    /**
     * 最大日志存放数量 达到数量了每次先做删除了再做新增
     * @param str
     * @param fileName
     * @throws IOException
     */
    public static void WriteLog(String str,String fileName) throws IOException {
        if(!Enum.ConfigData.isLogOpen()){
            return;
        }
        File file = new File(Enum.LogFileTemp);
        File[] files = file.listFiles();
        if(files.length>=Enum.ConfigData.getLogNum()){
            Arrays.sort(files, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    long diff = f1.lastModified() - f2.lastModified();
                    if (diff > 0)
                        return 1;
                    else if (diff == 0)
                        return 0;
                    else
                        return -1;//如果 if 中修改为 返回-1 同时此处修改为返回 1  排序就会是递减
                }
                public boolean equals(Object obj) {
                    return true;
                }
            });
            File currentFile = files[0];
            if(currentFile.delete()){
                TxtWriteString(str,Enum.LogFileTemp+"/"+fileName);
            }
        }
        File newFile = new File(Enum.LogFileTemp,fileName);
        if(newFile.createNewFile()){
            TxtWriteString(str,Enum.LogFileTemp+"/"+fileName);
        }
    }


}
