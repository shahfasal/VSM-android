package com.getvsm.ava;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;


/**
 * Created by brabh on 5/22/2015.
 */
public class BindService extends Service {
    public static MyWebSocketClient mWebSocketClient;//=new MyWebSocketClient(new URI(getResources().getString(R.string.socket_url)), oauth);
    public static Context applicationContext;
    public static String oauth = null;

    static {
        try {
            mWebSocketClient = new MyWebSocketClient(new URI("ws://52.74.168.43:443"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Service created", Toast.LENGTH_LONG).show();

        if (MyWebSocketClient.loggedIn) {
            Toast.makeText(this, "connecting to socket", Toast.LENGTH_LONG).show();

            mWebSocketClient.connect();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        applicationContext = this;
        try {
            if (intent != null) {
                String task = (String) intent.getExtras().get("task");
                if (task.contains("login")) {
                    oauth = (String) intent.getExtras().get("oauth");
                    mWebSocketClient.setOauth(oauth);
                    mWebSocketClient.connect();

                } else if (task.contains("annotation")) {
                    String annotation = intent.getStringExtra("data");
                    JSONObject message = new JSONObject();
                    message.put("command", "BROADCAST_FOR_ANNOTATION");
                    message.put("data", new JSONObject(annotation));
                    Log.d("sending", message.toString());
                    mWebSocketClient.sendMessageTOServer(message.toString());

                }
                else if (task.contains("comment")) {
                    String comment = intent.getStringExtra("data");
                    JSONObject message = new JSONObject();
                    message.put("command", "BROADCAST_FOR_COMMENT");
                    message.put("data", new JSONObject(comment));
                    Log.d("sending", message.toString());
                    mWebSocketClient.sendMessageTOServer(message.toString());

                }
                else if (task.contains("logout_fingerprint")) {
                    String fingerprint = (String) intent.getExtras().get("fingerprint");

                    JSONObject message = new JSONObject();
                    message.put("command", "BROADCAST_LOGOUT_FINGERPRINT");
                    message.put("data", fingerprint);
                    Log.d("sending", message.toString());
                    mWebSocketClient.sendMessageTOServer(message.toString());


                } else if (task.contains("logout_all")) {

                    JSONObject message = new JSONObject();
                    message.put("command", "BROADCAST_LOGOUT_ALL");
                    message.put("data", "");
                    Log.d("sending", message.toString());
                    mWebSocketClient.sendMessageTOServer(message.toString());
                    mWebSocketClient.close();
                    mWebSocketClient = null;
                    stopSelf();


                } else if (task.contains("logout")) {

                    JSONObject message = new JSONObject();
                    message.put("command", "BROADCAST_LOGOUT");
                    message.put("data", "");
                    Log.d("sending", message.toString());
                    mWebSocketClient.sendMessageTOServer(message.toString());
                    mWebSocketClient.close();
                    mWebSocketClient = null;
                    stopSelf();


                }
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    public void connectWebSocket() {
        try {
            mWebSocketClient = new MyWebSocketClient(new URI(getResources().getString(R.string.socket_url)), oauth);
            mWebSocketClient.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }
}

