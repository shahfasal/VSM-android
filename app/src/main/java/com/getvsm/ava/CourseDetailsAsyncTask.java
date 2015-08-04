package com.getvsm.ava;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
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

/**
 * Created by brabh on 5/21/2015.
 */
public class CourseDetailsAsyncTask extends AsyncTask<String, Integer, String> {
    JSONObject obj;
    Activity activity;
    ProgressDialog progress = null;
    String username, password;
    public static String oauth;

    public CourseDetailsAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        publishProgress(0);

        String result = null;
        try {
            HttpClient client = new DefaultHttpClient();

            HttpGet get = new HttpGet(activity.getResources().getString(R.string.get_course_details) + params[0]);
            get.addHeader("Authorization", activity.getSharedPreferences("Cache", Context.MODE_PRIVATE).getString("oauth", null));

            HttpResponse response = client.execute(get);

            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity);
            Log.d("course details", result);
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
        EnrolledCourseFragment enrolledCourseFragment = new EnrolledCourseFragment();
        try {
            enrolledCourseFragment.setCourse(new JSONObject(s));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally {
            ft.replace(R.id.main_fragment_container, enrolledCourseFragment,"course_details");
            ft.addToBackStack(null);
            ft.commit();
            progress.hide();
        }


    }


}