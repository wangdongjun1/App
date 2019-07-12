package com.jarisoft.util;

import android.content.Context;
import android.device.ScanManager;
import android.media.SoundPool;
import android.os.Environment;
import android.os.Vibrator;

import com.jarisoft.entity.Config;

import org.json.JSONObject;

/**
 * 用户定义全局数据
 * Created by shanwj on 2018/7/17.
 */

public class Enum {
    public static Vibrator mVibrator;
    public static SoundPool soundPool = null;
    public static int soundId;
    public static final String SCAN_ACTION = ScanManager.ACTION_DECODE;
    public static final String AppFileUrl = Environment.getExternalStorageDirectory() +"/logistic";
    public static final String PdfFileTemp = AppFileUrl +"/temp";
    public static final String ManagerFileTemp = AppFileUrl +"/manager";
    public static final String LogFileTemp = AppFileUrl +"/log";
    public static final String ManagerTxt = "manager.txt";
    public static String CurrentNum = "";//当前登陆用户工号
    public static Config ConfigData ;
    public static final String Regex = "WCGYAPPERROR";
    public static Context mContext;
}
