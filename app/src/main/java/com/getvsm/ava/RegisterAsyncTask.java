package com.getvsm.ava;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
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

/**
 * Created by brabh on 5/21/2015.
 */
public class RegisterAsyncTask extends AsyncTask<String, Integer, String> {
    JSONObject obj;
    Activity activity;
    ProgressDialog progress = null;
    String email, password,profile_name;
    public static String oauth;

    public RegisterAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        String resultFromRegister;
        publishProgress(0);
        profile_name = params[0];
        email= params[1];
        password = params[2];
        try {
            HttpResponse result=null;
           HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://ava.getvsm.com/api/v1/signup");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("profile_name",profile_name);
            jsonObject.put("username",email);
            jsonObject.put("password",new String(Base64.encode(params[1].getBytes(), Base64.DEFAULT)));
            httpPost.setEntity(new StringEntity(jsonObject.toString()));
            result = httpClient.execute(httpPost);
            HttpEntity resultEntity = result.getEntity();
            resultFromRegister = EntityUtils.toString(resultEntity);
            Log.d("login result", resultFromRegister);
            return resultFromRegister;
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
        progress.hide();
        Toast.makeText(activity, "Email has been sent", Toast.LENGTH_LONG).show();

    }
}