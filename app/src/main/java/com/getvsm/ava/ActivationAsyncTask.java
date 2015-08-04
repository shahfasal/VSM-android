package com.getvsm.ava;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by fasal on 24-07-2015.
 */
public class ActivationAsyncTask extends AsyncTask<String, Integer, String> {
    Activity activity;
   // ProgressDialog progress = null;
//   FragmentManager fm = null;
    public ActivationAsyncTask(Activity activity) {
        this.activity=activity;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://ava.getvsm.com/vsm/api/activation.php?activation_code="+params[0]);
        try{
            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity httpEntity = response.getEntity();

            String result = EntityUtils.toString(httpEntity);

            Log.d("data_active", result);
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
//    @Override
//    protected void onProgressUpdate(Integer... values) {
//        // TODO Auto-generated method stub
//        super.onProgressUpdate(values);
//        progress = new ProgressDialog(activity);
//        progress.setMessage("please wait.. ");
//        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progress.setIndeterminate(true);
//        progress.setCancelable(false);
//        progress.show();
//
//    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //progress.hide();
        Toast.makeText(activity.getApplicationContext(), "Activated successfully", Toast.LENGTH_LONG).show();


        Intent intent=new Intent(activity,MainActivity.class);
        activity.startActivity(intent);
        activity.finish();


    }
}
