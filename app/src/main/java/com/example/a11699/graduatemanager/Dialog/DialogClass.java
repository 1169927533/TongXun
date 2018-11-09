package com.example.a11699.graduatemanager.Dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.a11699.graduatemanager.R;

public class DialogClass extends Dialog {
    private EditText telNumber;
    private Button telSure;
    private  String tellNumberl;
    private SearchView.OnCloseListener listener;
    public DialogClass(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        initview();
    }
    void initview(){
        telNumber=findViewById(R.id.telNumber);
        telSure=findViewById(R.id.telSure);

        telSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog=new ProgressDialog(getContext());
                dialog.setMessage("添加成功");
                dialog.show();
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                },1000);
            }
        });
    }
}
