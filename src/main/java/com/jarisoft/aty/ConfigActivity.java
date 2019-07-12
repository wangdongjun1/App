package com.jarisoft.aty;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.jarisoft.R;
import com.jarisoft.util.Enum;
import com.jarisoft.util.Utils;

import org.json.JSONObject;

public class ConfigActivity extends Activity {
    private EditText webService;
    private EditText webNameSpcae;
    private EditText ftpAddress;
    private EditText ftpPort;
    private EditText user;
    private EditText password;
    private CheckBox logOpen;
    private Button btnCommit;
    private EditText logNum;
    private TextView tvAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        bindView();
    }

    private void bindView(){
        webService = findViewById(R.id.config_inter_address);
        webNameSpcae = findViewById(R.id.config_inter_space);
        ftpAddress = findViewById(R.id.config_ftp_address);
        ftpPort = findViewById(R.id.config_ftp_port);
        user = findViewById(R.id.config_ftp_user);
        password = findViewById(R.id.config_ftp_password);
        logOpen = findViewById(R.id.config_checkBox);
        logNum = findViewById(R.id.config_log_size);
        tvAddress = findViewById(R.id.config_tv_inter_address);
        tvAddress.setTextColor(Color.BLUE);
        btnCommit = findViewById(R.id.config_commit);
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject object = new JSONObject();
                try {
                    object.put("WebServiceUrl",webService.getText().toString().trim());
                    object.put("WebNameSapce",webNameSpcae.getText().toString().trim());
                    object.put("FtpAddress",ftpAddress.getText().toString().trim());
                    object.put("FtpPort",Integer.parseInt(ftpPort.getText().toString().trim()));
                    object.put("FtpUser",user.getText().toString().trim());
                    object.put("FtpUserPassword",password.getText().toString().trim());
                    object.put("LogShow",logOpen.isChecked());
                    object.put("LogSize",Integer.parseInt(logNum.getText().toString().trim()));
                    String str = object.toString();
                    Utils.TxtWriteString(str, Enum.ManagerFileTemp+"/"+Enum.ManagerTxt);
                    Utils.UpdateConfig(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        fillViewData();
    }

    private void fillViewData(){
        webService.setText(Enum.ConfigData.getWebService());
        webNameSpcae.setText(Enum.ConfigData.getNameSpcae());
        ftpAddress.setText(Enum.ConfigData.getFtpAddress());
        ftpPort.setText(Enum.ConfigData.getFtpPort()+"");
        user.setText(Enum.ConfigData.getUser());
        password.setText(Enum.ConfigData.getPassword());
        logOpen.setChecked(Enum.ConfigData.isLogOpen());
        logNum.setText(Enum.ConfigData.getLogNum()+"");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);

    }
}
