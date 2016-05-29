package com.guohui.fasttransfer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.guohui.fasttransfer.base.WebFile;
import com.guohui.fasttransfer.utils.NetUtil;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SimpleHttpService extends Service {
    public SimpleHttpService() {
    }

    HttpBinder binder;

    @Override
    public IBinder onBind(Intent intent) {
        binder = new HttpBinder();
        server = new AsyncHttpServer();
        server.get("/", new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                InputStream in = getResources().openRawResource(R.raw.index);
                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                int b;
                try {
                    while ((b = in.read()) != -1) {
                        out.write(b);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                response.send("text/html", out.toByteArray());
                Intent intent = new Intent();
//                    handler.sendEmptyMessage(ANDROID_HTTP_SERVER_ON_RESPONSE);
                intent.setAction(HTTP_REQUEST_RECEIVED);
                sendBroadcast(intent);

            }
        });
//            textView.append("服务器启动"+ NetUtil.getLocalIpAddress(ServerActivity.this)+"\n");

        List<WebSocket> webSockets = new ArrayList<>();
        server.listen(9909);
        server.websocket("/connection", new AsyncHttpServer.WebSocketRequestCallback() {
            @Override
            public void onConnected(final WebSocket webSocket, AsyncHttpServerRequest request) {

                //websocket已经连接
//                webSocket.send("Android WebSocket Server");
                Log.e("websocketServer","有设备连接");
                webSocket.setStringCallback(new WebSocket.StringCallback() {
                    @Override
                    public void onStringAvailable(String s) {
                        Log.e("webSocketReceive",s);
                       Intent fileIntent = new Intent();
                        fileIntent.setAction(ANDROID_WEBSOCKET_FILE_TRANSFER);
                        fileIntent.putExtra(ANDROID_WEBSOCKET_FILE_MSG,s);
                        sendBroadcast(fileIntent);
                    }
                });

                webSocket.setDataCallback(new DataCallback() {
                    @Override
                    public void onDataAvailable(DataEmitter emitter, ByteBufferList bb) {
                        try {
                            String s = new String(bb.getAllByteArray(), "UTF-8");
                            Log.e("s",s);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

        return binder;
    }

    public static final String HTTP_REQUEST_RECEIVED = "com.guohui.fasttransfer.pc_connected";
    public static final String HTTP_SERVER_START = "com.guohui.fasttransfer.start_http";
    public static final String ANDROID_HTTP_SERVER_HOST = "http_host";

    public static final String ANDROID_WEBSOCKET_FILE_TRANSFER = "websocket_file_transfer";

    public static final String ANDROID_WEBSOCKET_FILE_MSG = "websocket_file_msg";

    AsyncHttpServer server;


    public class HttpBinder extends Binder {
        public HttpBinder() {
            Intent s = new Intent();
            s.setAction(HTTP_SERVER_START);
            s.putExtra(ANDROID_HTTP_SERVER_HOST, NetUtil.getLocalIpAddress(SimpleHttpService.this) + ":9909");
            sendBroadcast(s);
            Log.e("server","connected");
        }
    }


}
