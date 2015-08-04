package com.getvsm.ava;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.SortedMap;

/**
 * Created by brabh on 5/21/2015.
 */
class MyWebSocketClient extends WebSocketClient {

    URI uri;
    public static String oauth;
    public static boolean loggedIn=false;


    public MyWebSocketClient(URI uri, String oauth) {
        super(uri);
        this.uri = uri;
        this.oauth = oauth;
    }

    public MyWebSocketClient(URI uri) {
        super(uri);
        this.uri = uri;
    }

    public void setOauth(String oauth) {
        this.oauth = oauth;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        Log.i("Websocket", "Opened");
        try {
            if ( MyWebSocketClient.loggedIn) {
                JSONObject oauthJson = new JSONObject();
                oauthJson.put("command", "OAUTH");
                oauthJson.put(
                        "data",
                        MainActivity.acivity.getSharedPreferences("Cache", MainActivity.acivity.MODE_PRIVATE).
                                getString("oauth", null));
                send(oauthJson.toString());


            }

            if (! MyWebSocketClient.loggedIn) {
                JSONObject oauthJson = new JSONObject();
                oauthJson.put("command", "OAUTH");
                oauthJson.put(
                        "data",
                        oauth);
                send(oauthJson.toString());


                JSONObject broadcaseLoginJson = new JSONObject();
                broadcaseLoginJson.put("command", "BROADCAST_LOGIN");
                broadcaseLoginJson.put("data", "");
                send(broadcaseLoginJson.toString());
                MyWebSocketClient.loggedIn = true;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void sendMessageTOServer(String message) {

        send(message);


    }

    @Override
    public void onMessage(String s) {


        Log.d("socket", s);

        Intent intent = new Intent();
        intent.putExtra("broadcast_data", s);
        intent.setAction("com.tutorialspoint.CUSTOM_INTENT");
        //SocketService.applicationContext.sendBroadcast(intent);
        BindService.applicationContext.sendBroadcast(intent);


    }

    @Override
    public void onClose(int i, String s, boolean b) {
        Log.i("Websocket", "Closed " + s);

    }

    @Override
    public void onError(Exception e) {
        Log.i("Websocket", "Error " + e.getMessage());
    }


}
