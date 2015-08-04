package com.getvsm.ava;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fasal on 25-07-2015.
 */
public class ResetAsyncTask  extends AsyncTask<String, Integer, String> {
    Activity activity;
    String confirm, reset_password,code;
    public ResetAsyncTask(ResetPasswordActivity resetPasswordActivity) {
        this.activity= resetPasswordActivity;
    }

    @Override
    protected String doInBackground(String... params) {

        code = params[0];
        reset_password= params[1];
        confirm = params[2];
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://ava.getvsm.com/vsm/api/v1/forgotpassword/resetpassword.php");

        try{
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("code", code));
            nameValuePairs.add(new BasicNameValuePair("password", reset_password));
            nameValuePairs.add(new BasicNameValuePair("confirm_password", confirm));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpClient.execute(httpPost);
        }catch(Exception e){
            e.printStackTrace();
        }




        return null;
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //progress.hide();
        Toast.makeText(activity.getApplicationContext(), "Password reset successfully", Toast.LENGTH_LONG).show();


        Intent intent=new Intent(activity,MainActivity.class);
        activity.startActivity(intent);
        activity.finish();


    }
}
