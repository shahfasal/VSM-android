package com.getvsm.ava;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by brabh on 5/22/2015.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent sentIntent) {
        // Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();


        /*
        process the input commands from braodcast:: start
         */
        String messagae = sentIntent.getStringExtra("broadcast_data");
        Log.d("socket sevice", messagae);
        try {


            JSONObject jsonMessage = new JSONObject(messagae);
            if (jsonMessage.has("LOGIN")) {
                Log.d("socket sevice", "you have a background servvice");
                Toast.makeText(context, "you have a background servvice", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, MainActivity.class);
                PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
                String deviceName;


                JSONObject userDevice = jsonMessage.getJSONObject("LOGIN").getJSONObject("device-details");
                if (userDevice.getJSONObject("device").has("type")) {
                    deviceName =
                            jsonMessage.getJSONObject("LOGIN").getJSONObject("device-details").getJSONObject("device").getString("vendor") + " " +
                                    jsonMessage.getJSONObject("LOGIN").getJSONObject("device-details").getJSONObject("device").getString("model");


                    User user = new User(userDevice.getJSONObject("device").getString("vendor") + " " +
                            userDevice.getJSONObject("device").getString("model")
                            , R.drawable.nav_mobile, jsonMessage.getJSONObject("LOGIN").toString());
                    if (!MainActivity.usersDevices.contains(user)) {
                        MainActivity.usersDevices.add(user);
                    }
                    // MainActivity.usersDevices.add(user);

                } else {
                    deviceName =
                            userDevice.getJSONObject("browser").getString("name") + " " +
                                    userDevice.getJSONObject("browser").getString("major");
                    User user = new User(userDevice.getJSONObject("browser").getString("name") + " " +
                            userDevice.getJSONObject("browser").getString("major")
                            , R.drawable.nav_laptop, jsonMessage.getJSONObject("LOGIN").toString());
                    if (!MainActivity.usersDevices.contains(user)) {
                        MainActivity.usersDevices.add(user);
                    }
                    //MainActivity.usersDevices.add(user);
                }
                Log.d("device-listing", MainActivity.usersDevices.toArray().toString());
                MainActivity.mDrawerList.setAdapter(new NavListAdapter(MainActivity.acivity, MainActivity.usersDevices));


                Notification n = new Notification.Builder(context)
                        .setContentTitle("VSM AVA")
                        .setContentText("Logged in from device -> " + deviceName)
                        .setSmallIcon(R.drawable.vsm_logo)
                        .setContentIntent(pIntent)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setLights(Color.GREEN, 3000, 3000)
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setAutoCancel(true).build();
                Toast.makeText(context, "device." + deviceName, Toast.LENGTH_LONG).show();

                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);

                notificationManager.notify(3, n);
            } else if (jsonMessage.has("ANNOTATION")) {
                JSONObject annotation = jsonMessage.getJSONObject("ANNOTATION");
                if (AnnotaionFragment.annonations != null) {
                    AnnotaionFragment.annonations.add(annotation);
                    AnnotaionFragment.annonationAdapter.notifyDataSetChanged();
                    Log.d("annonation", "data set refreshed");
                }

            } else if (jsonMessage.has("COMMENT")) {
                JSONObject comment = jsonMessage.getJSONObject("COMMENT");
                if (AnnotaionFragment.annonations != null && CommentsFragment.comments != null) {
                    for (int i = 0; i < AnnotaionFragment.annonations.size(); i++) {
                        JSONObject annotation = AnnotaionFragment.annonations.get(i);
                        if (Integer.parseInt(annotation.getString("annotation_id")) ==
                                Integer.parseInt(comment.getString("annotation_id"))) {
                            JSONArray comments = annotation.getJSONArray("comments");
                            comments.put(comment);
//                            AnnotaionFragment.annonations.add(annotation);
                            AnnotaionFragment.annonationAdapter.notifyDataSetChanged();
                            break;
                        }

                    }
                    if (Integer.parseInt(comment.getString("annotation_id")) == CommentsFragment.annotationId)
                        CommentsFragment.comments.add(comment);
                    CommentsFragment.commentsAdapter.notifyDataSetChanged();
                    Log.d("comment", "data set refreshed");
                }

            } else if (jsonMessage.has("LOGOUT")) {

                String deviceName = new String();
                if (jsonMessage.getJSONObject("LOGOUT").getJSONObject("device-details").getJSONObject("device").has("vendor")) {
                    deviceName =
                            jsonMessage.getJSONObject("LOGOUT").getJSONObject("device-details").getJSONObject("device").getString("vendor") + " " +
                                    jsonMessage.getJSONObject("LOGOUT").getJSONObject("device-details").getJSONObject("device").getString("model");
                } else {

                    deviceName =
                            jsonMessage.getJSONObject("LOGOUT").getJSONObject("device-details").getJSONObject("browser").getString("name") + " " +
                                    jsonMessage.getJSONObject("LOGOUT").getJSONObject("device-details").getJSONObject("browser").getString("version");
                }
                Intent intent = new Intent(context, MainActivity.class);
                PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
                Notification n = new Notification.Builder(context)
                        .setContentTitle("VSM AVA")
                        .setContentText("Logged out from device -> " + deviceName)
                        .setSmallIcon(R.drawable.vsm_logo)
                        .setContentIntent(pIntent)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setLights(Color.GREEN, 3000, 3000)
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setAutoCancel(true).build();
                Toast.makeText(context, "device." + deviceName, Toast.LENGTH_LONG).show();

                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);

                notificationManager.notify(4, n);

                for (int i = 0; i < MainActivity.usersDevices.size(); i++) {
                    if (MainActivity.usersDevices.get(i).toString().contains(jsonMessage.getJSONObject("LOGOUT").toString())) {
                        MainActivity.usersDevices.remove(i);
                    }
                }
                MainActivity.mDrawerList.setAdapter(new NavListAdapter(MainActivity.acivity, MainActivity.usersDevices));

            } else if (jsonMessage.has("LOGOUT_ALL")) {
                /*
                close te drawer
                 */
                if (MainActivity.mDrawerLayout.isDrawerOpen(Gravity.START)) {
                    MainActivity.mDrawerLayout.closeDrawer(Gravity.START);
                }
                Intent intent = new Intent(context, MainActivity.class);
                PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
                Notification n = new Notification.Builder(context)
                        .setContentTitle("VSM AVA")
                        .setContentText("Logged out from all devices ")
                        .setSmallIcon(R.drawable.vsm_logo)
                        .setContentIntent(pIntent)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setLights(Color.GREEN, 3000, 3000)
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setAutoCancel(true).build();
                Toast.makeText(context, "Logged out from all devices", Toast.LENGTH_LONG).show();

                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);

                notificationManager.notify(4, n);

                BindService.mWebSocketClient.close();

                MainActivity.usersDevices.clear();
                MainActivity.usersDevices = new ArrayList();
                MainActivity.mDrawerList.setAdapter(new NavListAdapter(MainActivity.acivity, MainActivity.usersDevices));
                FragmentTransaction ft = MainActivity.fm.beginTransaction();
                ft.replace(R.id.main_fragment_container, new LoginRegisterFragment());
                ft.commit();


            } else if (jsonMessage.has("LOGOUT_FINGERPRINT")) {


                String fingerprint = jsonMessage.getString("LOGOUT_FINGERPRINT");
                int pos = 0;
                for (int i = 0; i < MainActivity.usersDevices.size(); i++) {

                    ((User) MainActivity.usersDevices.get(i)).getFingerprint();
                    if (((User) MainActivity.usersDevices.get(i)).getFingerprint().contains(fingerprint)) {
                        pos = i;
                        MainActivity.usersDevices.remove(i);
                    }
                }
                MainActivity.mDrawerList.setAdapter(new NavListAdapter(MainActivity.acivity, MainActivity.usersDevices));


                SharedPreferences cache = MainActivity.acivity.getSharedPreferences("Cache", MainActivity.acivity.MODE_PRIVATE);
                if (cache.getString("fingerprint", "finger print not found").contains(fingerprint)) {
                    Log.d("Logout_fingerrint", "finher print matched your device");
                    if (MainActivity.mDrawerLayout.isDrawerOpen(Gravity.START)) {
                        MainActivity.mDrawerLayout.closeDrawer(Gravity.START);
                    }

                    Intent intent = new Intent(context, MainActivity.class);
                    PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
                    Notification n = new Notification.Builder(context)
                            .setContentTitle("VSM AVA")
                            .setContentText("you are  logged out  ")
                            .setSmallIcon(R.drawable.vsm_logo)
                            .setContentIntent(pIntent)
                            .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                            .setLights(Color.GREEN, 3000, 3000)
                            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                            .setAutoCancel(true).build();
                    // Toast.makeText(context, "you are logged out", Toast.LENGTH_LONG).show();

                    NotificationManager notificationManager =
                            (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);

                    notificationManager.notify(4, n);
                    BindService.mWebSocketClient.close();
                    BindService.mWebSocketClient = null;
                    Intent i = new Intent(MainActivity.acivity, BindService.class);
                    MainActivity.acivity.stopService(i);
                    FragmentTransaction ft = MainActivity.fm.beginTransaction();
                    ft.replace(R.id.main_fragment_container, new LoginRegisterFragment());
                    ft.commit();

                }


            }


        } catch (Exception e) {
            e.printStackTrace();

        }

        /*
        process the input commands from braodcast:: end
         */

    }

}

