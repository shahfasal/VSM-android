package com.getvsm.ava;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageFetcherAsyncTask extends AsyncTask<String, Integer, Bitmap> {

    View view;

    public ImageFetcherAsyncTask(View view) {
        // TODO Auto-generated constructor stub
        this.view = view;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        // TODO Auto-generated method stub

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory
                    .decodeStream((InputStream) new URL(params[0]).getContent());
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            return bitmap;
        }

    }

    @Override
    protected void onPostExecute(Bitmap result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        Drawable drawable = new BitmapDrawable(result);
//        if (view instanceof ImageView) {
//            ((ImageView) this.view).setImageDrawable(drawable);
//        } else {
//            this.view.setBackground(drawable);
//        }
        this.view.setBackground(drawable);
    }

}
