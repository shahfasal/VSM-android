package com.getvsm.ava;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by fasal on 19-06-2015.
 */
public class ListAsync extends AsyncTask<String ,Integer,String> {
    Activity activity;
    public static ArrayList<JSONObject> courses;
    ProgressDialog progress = null;
    FragmentManager fm;
    public ListAsync(Activity activity,FragmentManager fm) {
        this.activity = activity;
        this.fm=fm;
    }
    @Override
    protected String doInBackground(String... params) {
        publishProgress(0);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://ava.getvsm.com/api/v1/courses/all");

        httpGet.addHeader("Authorization",activity.getSharedPreferences("Cache", Context.MODE_PRIVATE).getString("oauth",null));

        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity);
            Log.d("TAG",result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("TAG",s);
        try
        {
            JSONObject object=new JSONObject(s);
            JSONArray allCourses=object.getJSONArray("all_courses");
            ArrayList<JSONObject> courses=new ArrayList<>();
            for(int i=0;i<allCourses.length();i++)
            {
                try {
                    JSONObject course = allCourses.getJSONObject(i);
                    courses.add(course);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }

            final GlobalClass globalClass=(GlobalClass)activity.getApplicationContext();
            globalClass.setCourses(courses);


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally {
            progress.hide();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.sub_tab_fragent_container, new ListCourseFragment());
            ft.commit();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        // TODO Auto-generated method stub
        super.onProgressUpdate(values);
        progress = new ProgressDialog(activity);
        progress.setMessage("fetching courses.. ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();

    }
}
