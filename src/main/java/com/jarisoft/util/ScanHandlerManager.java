package com.jarisoft.util;

import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.device.scanner.configuration.PropertyID;

/**
 * 处理扫码涉及的处理逻辑
 * Created by shanwj on 2018/7/23.
 */

public class ScanHandlerManager {
    //处理扫描数据
    public static String  HandlerScanData(Intent intent){
        Enum.soundPool.play(Enum.soundId, 1, 1, 0, 0, 1);
        Enum.mVibrator.vibrate(100);
        String str="";
        byte[] barcode = intent.getByteArrayExtra(ScanManager.DECODE_DATA_TAG);
        int barcodelen = intent.getIntExtra(ScanManager.BARCODE_LENGTH_TAG, 0);
        byte temp = intent.getByteExtra(ScanManager.BARCODE_TYPE_TAG, (byte) 0);
        String Type= intent.getStringExtra(ScanManager.BARCODE_TYPE_TAG);
        android.util.Log.i("debug", "----codetype--" + temp);
        str = new String(barcode, 0, barcodelen);
        return str;
    }

    //打开设备
    public static void openScanner(ScanManager s){
        s.openScanner();
        s.switchOutputMode( 0);
    }

    //设置广播接受的过滤
    public static IntentFilter getIntentFilter(ScanManager s,String action){
        IntentFilter filter = new IntentFilter();
        int[] idbuf = new int[]{PropertyID.WEDGE_INTENT_ACTION_NAME, PropertyID.WEDGE_INTENT_DATA_STRING_TAG};
        String[] value_buf = s.getParameterString(idbuf);
        if(value_buf != null && value_buf[0] != null && !value_buf[0].equals("")) {
            filter.addAction(value_buf[0]);
        } else {
            filter.addAction(action);
        }
        return filter;
    }
}
