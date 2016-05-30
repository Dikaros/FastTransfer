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
import com.koushikdutta.async.callback.WritableCallback;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SimpleHttpService extends Service {
    public SimpleHttpService() {
    }

    HttpBinder binder;
    WebSocket mWebSocket;
    WebFile currentFile;
    ArrayList<WebFile> webFiles = new ArrayList<>();



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
                mWebSocket = webSocket;
                Log.e("websocketServer","有设备连接");
                webSocket.setStringCallback(new WebSocket.StringCallback() {
                    @Override
                    public void onStringAvailable(String s) {
                        Log.e("webSocketReceive",s);
                       Intent fileIntent = new Intent();
                        fileIntent.setAction(ANDROID_WEBSOCKET_FILE_TRANSFER);
                        fileIntent.putExtra(ANDROID_WEBSOCKET_FILE_MSG,s);
                        sendBroadcast(fileIntent);
                        currentFile = null;
                        webFiles.clear();
                        webSocket.send("200");
                        Gson gson = new Gson();
                        try {
                            JSONObject object = new JSONObject(s);
                            JSONArray array = object.getJSONArray("files");
                            for (int i=0;i<array.length();i++){
                                JSONObject o = array.getJSONObject(i);
                                currentFile= gson.fromJson(o.toString(), WebFile.class);
                                currentFile.setSize(Long.parseLong(currentFile.getLength()));
                                currentFile.setReaded(0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                webSocket.setDataCallback(new DataCallback() {
                    @Override
                    public void onDataAvailable(DataEmitter emitter, ByteBufferList bb) {
//                            String s = new String(bb.getAllByteArray(), "UTF-8");
//                            Log.e("s", String.valueOf(bb.getAllByteArray().length));
                        try {
                            RandomAccessFile file = new RandomAccessFile(Config.getPersonalFile().getAbsolutePath()+"/"+currentFile.getName(),"rw");
                            file.seek(currentFile.getReaded());
                            if (!webFiles.contains(webFiles)) {
                                webFiles.add(currentFile);
                            }
                            byte[] t = bb.getAllByteArray();
                            file.write(t);
                            currentFile.setReaded(currentFile.getReaded()+t.length);
                            currentFile.setProgress(String.format("%.2f",(float)((float)currentFile.getReaded()*100/(float)currentFile.getSize()))+"%");
                            Intent rfIntent = new Intent();
                            rfIntent.setAction(ON_FILE_TRANSFERING);
                            rfIntent.putExtra(ON_TRANSFERING_FILE,currentFile);
                            sendBroadcast(rfIntent);
                        } catch (FileNotFoundException e) {

                        } catch (IOException e) {
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
    public static final String ON_FILE_TRANSFERING = "com.guohui.fasttransfer.on_transfer";
    public static final String ON_TRANSFERING_FILE="websocket_transfering_file";


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

        /**
         * 发送消息
         * @param msg 发送
         */
        public void send(String msg){
            if (mWebSocket!=null)
                mWebSocket.send(msg);
        }
    }


}
