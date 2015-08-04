//package com.getvsm.ava;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.graphics.Bitmap;
//import android.os.AsyncTask;
//import android.support.v4.app.FragmentTransaction;
//import android.util.Log;
//import android.widget.Toast;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;
//import org.json.JSONObject;
//
//import java.io.ByteArrayOutputStream;
//import java.util.HashMap;
//import java.util.Random;
//
//import yogamitra.quadvision.yogamitra.R;
//
///**
// * Created by abhishek on 13-06-2015.
// */
//public class Register extends AsyncTask<HashMap<String, String>, Integer, String> {
//    ProgressDialog progress = null;
//    Bitmap image = null;
//    Activity activity;
//
//    public Register(Activity activity, Bitmap image) {
//        this.activity = activity;
//        this.image = image;
//    }
//
//    @Override
//    protected void onProgressUpdate(Integer... values) {
//        // TODO Auto-generated method stub
//        super.onProgressUpdate(values);
//        progress = new ProgressDialog(activity);
//        progress.setMessage(".Registering.. ");
//        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progress.setIndeterminate(true);
//        progress.setCancelable(false);
//        progress.show();
//
//    }
//
//    @Override
//    protected String doInBackground(HashMap<String, String>... parameters) {
//        //Toast.makeText(myactivity, params[0].toString(), Toast.LENGTH_LONG).show();
//        try {
//            publishProgress(0);
//            HttpClient client = new DefaultHttpClient();
//
//            HttpPost post = new HttpPost(activity.getResources().getString(R.string.signup_url));
//
//            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
//            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//
//            entityBuilder.addTextBody("name", parameters[0].get("name"));
//            entityBuilder.addTextBody("age", parameters[0].get("age"));
//            entityBuilder.addTextBody("gender", parameters[0].get("gender"));
//            entityBuilder.addTextBody("mobile", parameters[0].get("mobile"));
//            entityBuilder.addTextBody("password", parameters[0].get("password"));
//            entityBuilder.addTextBody("email", parameters[0].get("email"));
//            if (image != null) {
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                byte[] byteArray = stream.toByteArray();
//
//                Random r = new Random();
//                entityBuilder.addBinaryBody("profile_image", byteArray, ContentType.create("image/png"), "image_" + r.nextInt() + ".png");
//            } else {
//                entityBuilder.addBinaryBody("profile_image", new byte[]{});
//            }
//
//            HttpEntity entity = entityBuilder.build();
//
//            post.setEntity(entity);
//
//            HttpResponse response = client.execute(post);
//
//            HttpEntity httpEntity = response.getEntity();
//
//            String result = EntityUtils.toString(httpEntity);
//
//            Log.d("registration", result);
//            return result;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(String result) {
//        // TODO Auto-generated method stub
//        super.onPostExecute(result);
//        String message = "";
//        try {
//            JSONObject object = new JSONObject(result);
//            String status = object.getString("status");
//
//            if (status.contentEquals("200")) {
//                message = "registered successfully!!";
//            } else {
//                message = "registration failed";
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            Log.d("register", result);
//            progress.hide();
//
//            AlertDialog.Builder builder1 = new AlertDialog.Builder(
//                    activity);
//            builder1.setMessage(message);
//            builder1.setCancelable(true);
//            builder1.setPositiveButton("Ok",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog,
//                                            int id) {
//                            dialog.cancel();
//                            activity.finish();
//                        }
//                    });
//            AlertDialog alert11 = builder1.create();
//            alert11.show();
//
//
//        }
//
//
//    }
//
//
//}
