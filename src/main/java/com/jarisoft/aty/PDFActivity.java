package com.jarisoft.aty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.jarisoft.R;
import com.jarisoft.util.Enum;

import java.io.File;

/**
 * pdf文件展示页面
 */
public class PDFActivity extends Activity implements OnPageChangeListener {

    private PDFView pdfView;
    private String pdfName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        bindView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void bindView(){
        pdfView = findViewById(R.id.pdfView);
        Intent intent = getIntent();
        pdfName = intent.getStringExtra("fileName");
        display(Enum.PdfFileTemp,false);
    }

    private void display(String fileName,boolean isToFirstPage){
        if(isToFirstPage)
            setTitle(fileName);
        File file = new File(fileName, pdfName);
        pdfView.fromFile(file)
                .defaultPage(1)
                .onPageChange(this)
                .enableDoubletap(true)
                .load();

    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
