package com.guohui.fasttransfer.aty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.guohui.fasttransfer.R;

public class ChooseActivity extends AppCompatActivity {

    Button btnConnectToPc;
    Button btnConnectToAndroid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        initViews();
    }

    private void initViews() {
        btnConnectToAndroid = (Button) findViewById(R.id.btn_connect_to_android);
        btnConnectToPc = (Button) findViewById(R.id.btn_connect_to_pc);

        btnConnectToAndroid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChooseActivity.this,AcceptActivity.class);
                startActivity(i);
            }
        });

        btnConnectToPc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChooseActivity.this,WebTransferActivity.class);
                startActivity(i);
            }
        });
    }
}
