package com.getvsm.ava;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by fasal on 30-06-2015.
 */
public class ForgotAsyncTask extends AsyncTask<String,Integer,String> {
    Activity activity;
    ProgressDialog progress = null;
    String email;
   public ForgotAsyncTask(Activity activity){
        this.activity = activity;
    }
    @Override
    protected String doInBackground(String... params) {
        email= params[0];
        publishProgress(0);
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://ava.getvsm.com/api/v1/passwords/forgot");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email);

            httpPost.setEntity(new StringEntity(jsonObject.toString()));
            HttpResponse result = httpClient.execute(httpPost);
            HttpEntity resultEntity = result.getEntity();
            String resultFromForgot = EntityUtils.toString(resultEntity);
            Log.d("login result", resultFromForgot);
            return resultFromForgot;
        }catch (Exception e){
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
