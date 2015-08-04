package com.getvsm.ava;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by brabh on 5/22/2015.
 */
public class SocketService extends Service {

    public MyWebSocketClient mWebSocketClient;
    public static Context applicationContext;
    public String oauth;



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        Log.d("connection", "you gt connected");
        oauth = (String) intent.getExtras().get("oauth");
        applicationContext = this;


        Intent myService = new Intent(this, SocketService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                myService, 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentText("service is on")
                .setContentIntent(pendingIntent).build();
        startForeground(1, notification);
        connectWebSocket();
        stopForeground(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        oauth = (String) intent.getExtras().get("oauth");
        applicationContext = this;


        Intent myService = new Intent(this, SocketService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                myService, 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentText("service is on")
                .setContentIntent(pendingIntent).build();
        startForeground(1, notification);
        connectWebSocket();
        stopForeground(true);


        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    public void connectWebSocket() {
        URI uri;
        try {
            uri = new URI(getResources().getString(R.string.socket_url));
//            mWebSocketClient = new MyWebSocketClient(getResources().getString(R.string.socket_url));
//            mWebSocketClient = new MyWebSocketClient(uri, oauth);
            mWebSocketClient.setOauth(oauth);
            mWebSocketClient.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }


    }
}
