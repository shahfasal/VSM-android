package com.getvsm.ava;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import activities.HomeActivity;

/**
 * Created by brabh on 5/21/2015.
 */
public class LoginAsyncTask extends AsyncTask<String, Integer, String> {
    JSONObject obj;
    Activity activity;
    public MyWebSocketClient mWebSocketClient;
    ProgressDialog progress = null;
    String username, password;
    public static String oauth;

    public LoginAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        publishProgress(0);
        username = params[0];
        password = params[1];
        try {
            HttpClient client = new DefaultHttpClient();

            HttpGet get = new HttpGet(activity.getResources().getString(R.string.geoip_url));
            HttpResponse response = client.execute(get);

            HttpEntity httpEntity = response.getEntity();
            String result = EntityUtils.toString(httpEntity);
            Log.d("geo ip", result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        // TODO Auto-generated method stub
        super.onProgressUpdate(values);
        progress = new ProgressDialog(activity);
        progress.setMessage("please wait.. ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            Log.d("Result", s);
            obj = new JSONObject();

            obj.put("fingerprint", Build.ID);

            obj.put("connection_type", "");

            JSONObject deviceDetails = new JSONObject();
            deviceDetails.put("ua", "Android App");
            JSONObject browser = new JSONObject();
            browser.put("name", "");
            browser.put("version", "");
            deviceDetails.put("browser", browser);
            JSONObject engine = new JSONObject();
            engine.put("name", "");
            engine.put("version", "");
            deviceDetails.put("engine", engine);
            final JSONObject device = new JSONObject();
            device.put("model", Build.MODEL);
            device.put("type", "mobile");
            device.put("vendor", Build.BRAND);
            deviceDetails.put("device", device);
            JSONObject cpu = new JSONObject();
            cpu.put("architecture", Build.CPU_ABI);
            deviceDetails.put("cpu", cpu);
            JSONObject os = new JSONObject();
            os.put("name", "Android " + Build.VERSION.RELEASE);
            os.put("version", Build.VERSION.SDK_INT);
            deviceDetails.put("os", os);
            obj.put("device-details", deviceDetails);
//            JSONObject loc = new JSONObject(s);
//            JSONObject location = new JSONObject();
//            location.put("city", loc.getString("city"));
//            location.put("region", loc.getString("region"));
//            location.put("country", loc.getString("country"));
//            location.put("ip", loc.getString("ip"));
//            location.put("isp", loc.getString("isp"));
//            location.put("timezone", loc.getString("timezone"));
//            obj.put("location-details", location);
            Log.d("login json", obj.toString());
            new AsyncTask<String, Integer, String>() {

                @Override
                protected String doInBackground(String[] params) {

                    HttpClient httpclient = new DefaultHttpClient();

                    HttpPost httppost = new HttpPost("http://" + activity.getResources().getString(R.string.server_ip) + "/vsm/api/v1/login/index.php");


                    try {

                        JSONObject loginObject = new JSONObject();
//                        loginObject.put("username", params[0]);
//                        loginObject.put("password", new String(Base64.encode(params[1].getBytes(), Base64.DEFAULT)));
                        loginObject.put("username", "cse.aamirshah@gmail.com");
                        loginObject.put("password", new String(Base64.encode("a1234567".getBytes(), Base64.DEFAULT)));
                        loginObject.put("user_data", obj);
                        loginObject.put("isAdmin", false);
                        Log.d("encoded password", new String(Base64.encode(params[1].getBytes(), Base64.DEFAULT)));
                        httppost.setEntity(new StringEntity(loginObject.toString()));

                        HttpResponse result = httpclient.execute(httppost);
                        Header[] myHeaders = result.getHeaders("Oauth");
                        Log.d("oauth", myHeaders[0].getValue());
                        oauth = myHeaders[0].getValue();
                        HttpEntity resultEntity = result.getEntity();

                        String resultFromLogin = EntityUtils.toString(resultEntity);
                        Log.d("login result", resultFromLogin);
                        return resultFromLogin;


                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);


                    //Intent myService = new Intent(activity.getBaseContext(), SocketService.class);

                    /*
                    cache the data in shard prefs::start
                     */
                    JSONObject messgae = null;
                    try {
                        messgae = new JSONObject(s);

                        if (messgae.getString("status").contains("Success")) {
                            SharedPreferences cache = activity.getSharedPreferences("Cache", activity.MODE_PRIVATE);
                            SharedPreferences.Editor edit = cache.edit();
                            edit.putString("oauth", oauth);
                            edit.putString("username", username);
                            edit.putString("password", password);

                            edit.putString("fingerprint", Build.ID);
                            edit.commit();
                            //Log.d("oauth", activity.getSharedPreferences(""));


                            try {
                                JSONObject loggedInDevices = new JSONObject(s);
                                JSONArray devices = loggedInDevices.getJSONArray("user_data");
                                MainActivity.loggedInDevices = devices;
                                Log.d("logged in devices", devices.toString());
                                //ArrayList usersDevices = new ArrayList<User>();
                                for (int i = 0; i < devices.length(); i++) {
                                    JSONObject userDevice = devices.getJSONObject(i).getJSONObject("device-details");

                                    if (userDevice.getJSONObject("device").has("type")) {

                                        User user = new User(userDevice.getJSONObject("device").getString("vendor") + " " +
                                                userDevice.getJSONObject("device").getString("model")
                                                , R.drawable.nav_mobile, devices.getJSONObject(i).toString());
                                        if (!MainActivity.usersDevices.contains(user)) {
                                            MainActivity.usersDevices.add(user);
                                        }
                                        // MainActivity.usersDevices.add(user);

                                    } else {
                                        User user = new User(userDevice.getJSONObject("browser").getString("name") + " " +
                                                userDevice.getJSONObject("browser").getString("major")
                                                , R.drawable.nav_laptop, devices.getJSONObject(i).toString());
                                        if (!MainActivity.usersDevices.contains(user)) {
                                            MainActivity.usersDevices.add(user);
                                        }
                                        //MainActivity.usersDevices.add(user);
                                    }
                                    Log.d("device-listing", MainActivity.usersDevices.toString());
                                    MainActivity.mDrawerList.setAdapter(new NavListAdapter(activity, MainActivity.usersDevices));

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                Intent myService = new Intent(activity.getBaseContext(), BindService.class);
                                myService.putExtra("oauth", oauth);
                                myService.putExtra("task", "login");
                                activity.startService(myService);
                                progress.hide();



                    /*
                    update the navigation drawer :: end
                     */

                                Intent intent = new Intent(activity, HomeActivity.class);
                                activity.startActivity(intent);
                                Toast.makeText(activity, "credentials", Toast.LENGTH_LONG).show();
//                                FragmentTransaction ft = MainActivity.fm.beginTransaction();
//                                ft.replace(R.id.main_fragment_container, new HomeTabFragment());
//
//                                ft.commit();

                            }

                            // new EnrollListAsync(activity).execute();


                        } else {
                            progress.hide();
                            FragmentTransaction ft = MainActivity.fm.beginTransaction();
                            ft.replace(R.id.main_fragment_container, new LoginRegisterFragment());
                            ft.commit();
                            Toast.makeText(activity, "wrong credentials", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.execute(new String[]{username, password});


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}