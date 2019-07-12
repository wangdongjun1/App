package com.jarisoft.aty.frm;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jarisoft.R;
import com.jarisoft.adpter.PipeListAdpter;
import com.jarisoft.aty.BackActivity;
import com.jarisoft.aty.PDFActivity;
import com.jarisoft.entity.PileInfo;
import com.jarisoft.thread.FtpDownThread;
import com.jarisoft.thread.WebServiceThread;
import com.jarisoft.util.AllCapTransformation;
import com.jarisoft.util.Enum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.view.View.OnClickListener;

public class PileSearchFrm extends Fragment {

    private TextView pileQr;
    private EditText inputPilenum;
    private EditText inputImage;
    private EditText inputProject;
    private LinearLayout lvSearch;
    private EditText inputBar;
    private EditText inputQr;
    private ListView  pileList;
    private PipeListAdpter pipeListAdpter;

    Activity ctx;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pile_search_frm, container, false);
        bindView(view);
        return view;
    }

    private void bindView(View view) {
        ctx = getActivity();
        pileQr = view.findViewById(R.id.etScanPileQr);
        pileQr.setTransformationMethod(new AllCapTransformation());
        inputPilenum = view.findViewById(R.id.pile_num);
        inputPilenum.setTransformationMethod(new AllCapTransformation());
        inputImage = view.findViewById(R.id.pile_image);
        inputImage.setTransformationMethod(new AllCapTransformation());
        inputProject = view.findViewById(R.id.pile_project);
        inputProject.setTransformationMethod(new AllCapTransformation());
        lvSearch = view.findViewById(R.id.btn_search);
        inputBar = view.findViewById(R.id.pile_barcode);
        inputQr = view.findViewById(R.id.pile_qrcode);
        pileList = view.findViewById(R.id.pile_list);

        lvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Error","11111");
                String pileNum = inputPilenum.getText().toString().trim();
                String imageNum = inputImage.getText().toString().trim();
                String projectNum = inputProject.getText().toString().trim();
                if(TextUtils.isEmpty(pileNum)){
                    Toast.makeText(getActivity(),"请输入管件号",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(imageNum)){
                    Toast.makeText(getActivity(),"请输入图号",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(projectNum)){
                    Toast.makeText(getActivity(),"请输入工程编号",Toast.LENGTH_SHORT).show();
                    return;
                }
                String [] argnames = {"ProjectNum","ChartNum","PipeNum"};
                String [] args = {projectNum,imageNum,pileNum};
                System.out.print("工程号"+projectNum+"。图号"+imageNum+"。管件号"+pileNum);
                System.out.print("工程号"+projectNum+"。图号"+imageNum+"。管件号"+pileNum);
                System.out.print("工程号"+projectNum+"。图号"+imageNum+"。管件号"+pileNum);
                System.out.print("工程号"+projectNum+"。图号"+imageNum+"。管件号"+pileNum);
                System.out.print("工程号"+projectNum+"。图号"+imageNum+"。管件号"+pileNum);
                Thread thread = new Thread(new WebServiceThread(queryHandler,"GetPipeInfo",argnames,args));
                thread.start();
            }
        });
        pileList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (view.getTag() instanceof PipeListAdpter.ViewHolder) {
                    PipeListAdpter.ViewHolder holder = (PipeListAdpter.ViewHolder) view.getTag();
                    // 会自动出发CheckBox的checked事件
                    inputBar.setText(holder.barCode.getText().toString());
                }
            }
        });

    }


    Handler queryHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==2){
                Toast.makeText(getActivity(),(String)msg.obj,Toast.LENGTH_LONG).show();
                return;
            }
            if (msg.what==1){
                String str = (String)msg.obj;
                Log.e("result",str);
                try {
                    JSONObject jsonObject = new JSONObject(str);
                    JSONArray array = new JSONArray(jsonObject.getString("PipeList"));
                    if(array.length()==0){
                        Toast.makeText(getActivity(),"查询结果为空!",Toast.LENGTH_LONG).show();
                        return;
                    }
                    List<PileInfo> list = getPipeList(array);
                    pipeListAdpter = new PipeListAdpter(ctx,list,onClickListener);
                    pileList.setAdapter(pipeListAdpter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    };

    private List<PileInfo> getPipeList(JSONArray jsonArray) throws JSONException {
        List<PileInfo> list = new ArrayList<PileInfo>();
        for (int i = 0; i <jsonArray.length() ; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            PileInfo pileInfo = new PileInfo();
            pileInfo.setBarCode(jsonObject.getString("BarCode"));
            pileInfo.setChartNum(jsonObject.getString("ChartNum"));
            pileInfo.setPipeNum(jsonObject.getString("PipeNum"));
            pileInfo.setProjectNum(jsonObject.getString("ProjectNum"));
            pileInfo.setPDFUrl(jsonObject.getString("PDFUrl"));
            list.add(pileInfo);
        }
        return list;
    }

    public EditText getInputBar() {
        return inputBar;
    }

    public EditText getInputQr() {
        return inputQr;
    }

    public TextView getPileQr() {
        String[] argNmae={"barCode"};
        String[] arg={pileQr.getText().toString()};
        Thread barThread = new Thread(new WebServiceThread(queryHandler,"GetPipeInfoByBarCode",argNmae,arg));
        barThread.start();
        return pileQr;
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            LinearLayout lvBtnPdf = (LinearLayout) view;
            String url = (String) lvBtnPdf.getTag();
            url.trim();
            String []urls = url.split("/");
            String pdfFileName = urls[urls.length-1];
            int start = Enum.ConfigData.getFtpPort()==21?19:22;
            String pdfFileDirec = url.substring(start,url.length()-pdfFileName.length()-1);
            File file = new File(Enum.PdfFileTemp+"/"+pdfFileName);
            if(file.exists()){
                Intent intent = new Intent(getActivity(), PDFActivity.class);
                intent.putExtra("fileName",pdfFileName);
                startActivity(intent);
            }else{
                Thread thread = new Thread(new FtpDownThread(getActivity(),pdfFileDirec,pdfFileName));
                thread.start();
            }
        }
    };
}
