package com.guohui.fasttransfer.aty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.WindowManager;

import com.guohui.fasttransfer.R;
import com.guohui.fasttransfer.SocketServerService;

/**
 * Created by nangua on 2016/4/13 0013.
 */
public class InitActivity extends Activity{
    android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            Intent i = new Intent(InitActivity.this,MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.initactivity_open, 0);
            Intent s = new Intent(InitActivity.this, SocketServerService.class);
            startService(s);
            finish();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.init_layout);
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                    handler.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
