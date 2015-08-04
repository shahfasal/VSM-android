package com.getvsm.ava;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * Created by fasal on 28-06-2015.
 */
public class AddAnnotationAsyncTask extends AsyncTask<HashMap<String, String>, Integer, String> {
    JSONObject obj;
    Activity activity;
    ProgressDialog progress = null;
    Bitmap bp;

    public AddAnnotationAsyncTask(Activity activity, Bitmap bp) {
        this.activity = activity;
        this.bp = bp;
    }


    @Override
    protected String doInBackground(HashMap<String, String>... params) {
        publishProgress(0);


        HttpClient httpClient = new DefaultHttpClient();
/*
            hash iterator::start
             */
        Set<String> keys = params[0].keySet();
        Iterator<String> keyIterator = keys.iterator();
        String queryString = new String();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            String value = params[0].get(key);
            if (keyIterator.hasNext()) {
                queryString += key + "=" + value + "&";
            } else {
                queryString += key + "=" + value;
            }
        }
        Log.d("annonation debug", queryString);

            /*
            hash iterator::end
             */
        HttpPost httpPost = new HttpPost("http://ava.getvsm.com/api/v1/annotations/create?" + queryString);
        try {


//            JSONObject addAnnotation = new JSONObject();
//            httpPost.addHeader("oauth_key", activity.getSharedPreferences("Cache", Context.MODE_PRIVATE).getString("oauth", null));
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            if (bp != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                Log.e("byte", byteArray.toString());
                Random r = new Random();
                entityBuilder.addBinaryBody("file", byteArray, ContentType.create("image/*"), "image_" + r.nextInt() + ".png");
                //entityBuilder.addBinaryBody("file", byteArray, ContentType.create("video/png"), "video_" + r.nextInt() + ".png");
            } else {
                entityBuilder.addBinaryBody("file", new byte[]{});
            }
//            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//            entityBuilder.addTextBody("text", params[0].get("text"));
//            entityBuilder.addTextBody("type", params[0].get("type"));
//            entityBuilder.addTextBody("time", params[0].get("time"));
//            entityBuilder.addTextBody("video_id", params[0].get("videoId"));
//
            HttpEntity entity = entityBuilder.build();

            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity httpEntity = response.getEntity();

            String result = EntityUtils.toString(httpEntity);

            Log.d("data12", result);
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
        progress.hide();
        try {
            JSONObject result = new JSONObject(s);
            if (result.getString("status").trim().toLowerCase().contentEquals("sucess")) {
                Toast.makeText(activity.getApplicationContext(), "Annotation added successfully", Toast.LENGTH_LONG).show();
                //((ActionBarActivity) activity).getSupportFragmentManager().popBackStack();

                new AnnotationAsyncTask(activity).execute(((GlobalClass) activity.getApplicationContext()).getVideoId());
                ((ActionBarActivity) activity).getSupportFragmentManager().popBackStack();
                ((ActionBarActivity) activity).getSupportFragmentManager().popBackStack();
               /*
               parse the json and tell teh socket
                */
                JSONObject annotation = result.getJSONObject("result");
                Intent intent = new Intent(activity, BindService.class);
                intent.putExtra("task", "annotation");
                intent.putExtra("data", annotation.toString());
                activity.startService(intent);
            } else {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(
                        activity);
                builder1.setMessage("Annonation Addition Failed.Try Again!");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
