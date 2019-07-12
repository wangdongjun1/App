package com.jarisoft.aty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jarisoft.R;
import com.jarisoft.entity.PileInfo;

/**
 * Created by robot on 2019/6/25.
 */

public class HandoverDetail extends Activity {

    private TextView qrcode;
    private  TextView insideCode;
    private TextView subsection;
    private  TextView installTrayNum;
    private TextView pileType;
    private TextView pileMaterial;
    private TextView pileSpec;
    private TextView surfaceCode;
    private TextView pileNum;
    private TextView pileLength;
    private TextView pileWeight;
    private TextView barCode;
    private PileInfo pileInfo;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.list_pip_handover_detail);
        bindView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void bindView(){
        Intent intent = getIntent();
        pileInfo = (PileInfo)intent.getSerializableExtra("pileInfo");
        qrcode = findViewById(R.id.pile_qrcode);
        qrcode.setText(pileInfo.getQRCode());

        insideCode = findViewById(R.id.inside_code);
        insideCode.setText(pileInfo.getInsideCode());

        subsection = findViewById(R.id.sub_section);
        subsection.setText(pileInfo.getSubsection());

        installTrayNum = findViewById(R.id.install_tray_num);
        installTrayNum.setText(pileInfo.getInstallTrayNum());

        pileType = findViewById(R.id.pile_type);
        pileType.setText(pileInfo.getPipeType());

        pileMaterial = findViewById(R.id.pile_material);
        pileMaterial.setText(pileInfo.getPipeMaterial());

        pileSpec = findViewById(R.id.pile_spec);
        pileSpec.setText(pileInfo.getPipeSpec());

        surfaceCode = findViewById(R.id.surface_code);
        surfaceCode.setText(pileInfo.getSurfaceCode());

        pileNum = findViewById(R.id.pile_num);
        pileNum.setText(pileInfo.getPipeNum());

        pileLength = findViewById(R.id.pile_length);
        pileLength.setText(pileInfo.getPipeLength());

        pileWeight = findViewById(R.id.pile_weight);
        pileWeight.setText(pileInfo.getPipeWeight());

        qrcode = findViewById(R.id.pile_qrcode);
        qrcode.setText(pileInfo.getQRCode());

        barCode = findViewById(R.id.bar_code);
        barCode.setText(pileInfo.getBarCode());
    }
}
