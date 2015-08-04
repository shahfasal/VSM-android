package com.getvsm.ava;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.nostra13.universalimageloader.utils.IoUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * Created by fasal on 28-06-2015.
 */
public class AddCommentsAsyncTask extends AsyncTask<HashMap<String, String>, Integer, String> {
    JSONObject obj;
    Activity activity;
    ProgressDialog progress = null;
    Bitmap bp;
    String videoFilePaht;
    public AddCommentsAsyncTask(Activity activity, Bitmap bp, String videoFilePaht) {
        this.activity = activity;
        this.bp=bp;
        this.videoFilePaht=videoFilePaht;
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

            /*
            hash iterator::end
             */
        HttpPost httpPost = new HttpPost("http://ava.getvsm.com/api/v1/comments/create?"+queryString);
        try {

//            JSONObject addAnnotation = new JSONObject();
//            httpPost.addHeader("oauth_key", activity.getSharedPreferences("Cache", Context.MODE_PRIVATE).getString("oauth", null));
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            if (bp != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Random r = new Random();
                entityBuilder.addBinaryBody("file", byteArray, ContentType.create("image/*"),
                        "image_" + r.nextInt() + ".png");
                //entityBuilder.addBinaryBody("file", byteArray, ContentType.create("video/png"), "video_" + r.nextInt() + ".png");
            } else {
                byte[] byteArray=readContentIntoByteArray(new File(videoFilePaht));
//                FileBody filebodyVideo = new FileBody(new File(videoFilePaht));
//                entityBuilder.addPart("file", filebodyVideo);
                Random r = new Random();
                entityBuilder.addBinaryBody("file", byteArray, ContentType.create("video/*"),
                        "video_" + r.nextInt() + ".mp4");

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

            Log.d("data_add", result);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }
    private static byte[] readContentIntoByteArray(File file)
    {
        FileInputStream fileInputStream = null;
        byte[] bFile = new byte[(int) file.length()];
        try
        {
            //convert file into array of bytes
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
            for (int i = 0; i < bFile.length; i++)
            {
                System.out.print((char) bFile[i]);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bFile;
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





        progress.hide();
        try {
            JSONObject result = new JSONObject(s);
            if (result.getString("status").trim().toLowerCase().contentEquals("sucess")) {

                new AnnotationAsyncTask(activity).execute(((GlobalClass) activity.getApplicationContext()).getVideoId());
                ((ActionBarActivity) activity).getSupportFragmentManager().popBackStack();
                ((ActionBarActivity) activity).getSupportFragmentManager().popBackStack();
               /*
               parse the json and tell teh socket
                */
                JSONObject comment = result.getJSONObject("result");
                Intent intent = new Intent(activity, BindService.class);
                intent.putExtra("task", "comment");
                intent.putExtra("data", comment.toString());
                activity.startService(intent);
            } else {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(
                        activity);
                builder1.setMessage("Comment Addition Failed.Try Again!");
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
