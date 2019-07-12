package com.jarisoft.aty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.jarisoft.R;
import com.jarisoft.adpter.HandoverStatusAdapter;

/**
 * 菜单页面
 */
public class MenuActivity extends Activity implements View.OnClickListener {

    private LinearLayout qrBind;
    private LinearLayout backDiscern;
    private LinearLayout pillPile;
    private LinearLayout handover;
    private LinearLayout handoverStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        bindView();
    }

    private void bindView(){
        qrBind = findViewById(R.id.qrScan);
        backDiscern = findViewById(R.id.backDiscern);
        pillPile = findViewById(R.id.pillPile);
        handover = findViewById(R.id.handover);
        handoverStatus = findViewById(R.id.handoverStatus);


        qrBind.setOnClickListener(this);
        backDiscern.setOnClickListener(this);
        pillPile.setOnClickListener(this);
        handover.setOnClickListener(this);
        handoverStatus.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.qrScan:
                intent = new Intent(this,QrScanActivity.class);
                startActivity(intent);
                break;
            case R.id.backDiscern:
                intent = new Intent(this,BackActivity.class);
                startActivity(intent);
                break;
            case R.id.pillPile:
                intent = new Intent(this,PileActivity.class);
                startActivity(intent);
                break;
            case R.id.handover:
            intent = new Intent(this,HandoverSearchActivity.class);
            startActivity(intent);
            break;
            case R.id.handoverStatus:
            intent = new Intent(this,HandoverStatusActivity.class);
            startActivity(intent);
            break;
        }
    }
}
