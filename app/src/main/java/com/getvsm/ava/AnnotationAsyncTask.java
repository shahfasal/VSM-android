package com.getvsm.ava;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by brabh on 5/21/2015.
 */
public class AnnotationAsyncTask extends AsyncTask<String, Integer, String> {
    JSONObject obj;
    Activity activity;
    ProgressDialog progress = null;
    String username, password;
    public static String oauth;
    public static String videoId=new String();
    public AnnotationAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        publishProgress(0);

        String result = null;
        try {
            HttpClient client = new DefaultHttpClient();


            HttpGet get = new HttpGet(activity.getResources().getString(R.string.get_annonations) + params[0]);
            videoId=params[0];
            ((GlobalClass)activity.getApplicationContext()).setVideoId(params[0]);
            get.addHeader("Authorization", activity.getSharedPreferences("Cache", Context.MODE_PRIVATE).getString("oauth", null));

            HttpResponse response = client.execute(get);

            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity);
            Log.d("annonation details", result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return result;
        }

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


        FragmentTransaction ft = ((ActionBarActivity) activity).getSupportFragmentManager().beginTransaction();
                 /*
                slide bottom to top
                 */
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_left);
        AnnotaionFragment annotaionFragment = new AnnotaionFragment();
        try {
            JSONObject result=new JSONObject(s);
            JSONArray annonations=result.getJSONArray("annotations");
            ArrayList<JSONObject> annnationsList=new ArrayList<>();
            for(int i=0;i<annonations.length();i++)
            {
                annnationsList.add(annonations.getJSONObject(i));
            }
            annotaionFragment.setAnnonations(annnationsList);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally {
            ft.replace(R.id.main_fragment_container, annotaionFragment,"annonation_fragment");
            ft.addToBackStack(null);
            ft.commit();
            progress.hide();
        }
    }


}