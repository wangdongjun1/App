package com.jarisoft.aty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jarisoft.R;
import com.jarisoft.db.DbDao;
import com.jarisoft.util.Enum;

/**
 * 登陆页面
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    int count = 0;
    long previousTime = 0;
    long latestTime = 0;

    int count1 = 0;
    long previousTime1 = 0;
    long latestTime1 = 0;
    private TextView tvTitle;
    private EditText etNum;
    private Button btnLogin;
    private TextView tvUserNum;
    private SpannableString msp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindView();
    }

    private void bindView(){
        tvTitle = findViewById(R.id.loginTitle);
        etNum = findViewById(R.id.loginNum);
        btnLogin = findViewById(R.id.loginBtn);
        tvUserNum = findViewById(R.id.login_tv_user_num);
        tvTitle.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        tvUserNum.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.loginTitle:
                if (count1 == 0){
                    previousTime1 = System.currentTimeMillis();
                    count1++;
                }else{
                    latestTime1 = System.currentTimeMillis();
                    if (latestTime1 - previousTime1> 800){ //按键间隔大于800ms，则取消
                        previousTime1 = latestTime1;
                        count1 = 1;
                    }else {
                        count1++;
                        previousTime1 = latestTime1;
                    }
                }
                if (count1 >= 3) {
                    count1 = 0;
                    intent = new Intent(this,ConfigActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.login_tv_user_num:
                if (count == 0){
                    previousTime = System.currentTimeMillis();
                    count++;
                }else{
                    latestTime = System.currentTimeMillis();
                    if (latestTime - previousTime > 800){ //按键间隔大于800ms，则取消
                        previousTime = latestTime;
                        count = 1;
                    }else {
                        count++;
                        previousTime = latestTime;
                    }
                }
                if (count >= 3) {
                    count = 0;
                    intent = new Intent(this,ManagerActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.loginBtn:
                String etNumStr = etNum.getText().toString().trim();
                if(TextUtils.isEmpty(etNumStr)){
                    Toast.makeText(this,"请输入工号!",Toast.LENGTH_LONG).show();
                    return;
                }
                DbDao dbDao = DbDao.getInstance(this);
                boolean b = dbDao.isExitByNum(etNumStr);
                if(!b){
                    Toast.makeText(this,"当前工号尚未录入，请联系管理员进行添加!",Toast.LENGTH_LONG).show();
                    return;
                }
                Enum.CurrentNum = etNumStr;
                intent = new Intent(this,MenuActivity.class);
                startActivity(intent);
                break;
        }
    }
}