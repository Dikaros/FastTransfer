package com.guohui.fasttransfer.aty;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.guohui.fasttransfer.R;
import com.guohui.fasttransfer.SimpleHttpService;
import com.guohui.fasttransfer.adapter.PcFileAdapter;
import com.guohui.fasttransfer.base.WebFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WebTransferActivity extends AppCompatActivity {

    ListView lvFile;
    TextView tvIp;

    List<WebFile> files;
    PcFileAdapter adapter;

    SimpleHttpService.HttpBinder binder;

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (SimpleHttpService.HttpBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    HttpBroadcastReceiver receiver;

    @Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(SimpleHttpService.HTTP_REQUEST_RECEIVED);
        filter.addAction(SimpleHttpService.HTTP_SERVER_START);
        filter.addAction(SimpleHttpService.ANDROID_WEBSOCKET_FILE_TRANSFER);
        filter.addAction(SimpleHttpService.ON_FILE_TRANSFERING);
        registerReceiver(receiver, filter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_web_transfer);
        initViews();
        Intent i = new Intent(WebTransferActivity.this,SimpleHttpService.class);
        bindService(i,conn, Context.BIND_AUTO_CREATE);
        receiver = new HttpBroadcastReceiver();
    }


    /**
     * 初始化控件
     */
    private void initViews() {
        lvFile = (ListView) findViewById(R.id.lv_pc_accepter);
        tvIp = (TextView) findViewById(R.id.tv_ip);
        files = new ArrayList<>();
        adapter = new PcFileAdapter(this,files);
        lvFile.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        this.unbindService(conn);
        super.onDestroy();
    }

    class HttpBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.e("receiver",intent.getAction());
            switch (intent.getAction()){
                case SimpleHttpService.HTTP_REQUEST_RECEIVED:
                    break;
                case SimpleHttpService.HTTP_SERVER_START:
                    String ip = intent.getStringExtra(SimpleHttpService.ANDROID_HTTP_SERVER_HOST);
                    tvIp.setText(ip);
                    break;
                case SimpleHttpService.ANDROID_WEBSOCKET_FILE_TRANSFER:
                    String s = intent.getStringExtra(SimpleHttpService.ANDROID_WEBSOCKET_FILE_MSG);
                    if (s!=null){
                        Gson gson = new Gson();
                        try {
                            JSONObject object = new JSONObject(s);
                            JSONArray array = object.getJSONArray("files");
                            for (int i=0;i<array.length();i++){
                                JSONObject o = array.getJSONObject(i);
                                gson.fromJson(o.toString(), WebFile.class);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case SimpleHttpService.ON_FILE_TRANSFERING:
                    WebFile file = (WebFile) intent.getSerializableExtra(SimpleHttpService.ON_TRANSFERING_FILE);

                    if (!files.contains(file)){
                        files.add(file);
                    }else {
                        adapter.updateFile(file,0);
                    }
                    adapter.notifyDataSetChanged();

                    break;
            }
        }
    }
}
