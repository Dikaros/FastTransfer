package com.guohui.fasttransfer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.guohui.fasttransfer.utils.NetUtil;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

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
        return binder;
    }



    class HttpBinder extends Binder{
        public HttpBinder(){
            AsyncHttpServer server = new AsyncHttpServer();
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

                    response.send("text/html",out.toByteArray());
//                    handler.sendEmptyMessage(ANDROID_HTTP_SERVER_ON_RESPONSE);
                }
            });
//            textView.append("服务器启动"+ NetUtil.getLocalIpAddress(ServerActivity.this)+"\n");

            List<WebSocket> webSockets = new ArrayList<>();
            server.listen(9909);
            server.websocket("/connection", new AsyncHttpServer.WebSocketRequestCallback() {
                @Override
                public void onConnected(WebSocket webSocket, AsyncHttpServerRequest request) {

                    //websocket已经连接
                    webSocket.send("Android WebSocket Server");

                    webSocket.setStringCallback(new WebSocket.StringCallback() {
                        @Override
                        public void onStringAvailable(String s) {

                        }
                    });

                    webSocket.setDataCallback(new DataCallback() {
                        @Override
                        public void onDataAvailable(DataEmitter emitter, ByteBufferList bb) {
                            try {
                                String s = new String(bb.getAllByteArray(),"UTF-8");

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            });

        }
    }


}
