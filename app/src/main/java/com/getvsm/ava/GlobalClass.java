package com.getvsm.ava;

import android.app.Application;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONObject;

import java.util.ArrayList;



/**
 * Created by fasal on 02-07-2015.
 */



public class GlobalClass extends Application {

    public ArrayList<JSONObject> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<JSONObject> courses) {
        this.courses = courses;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String videoId;

    public String getAnnonationId() {
        return annonationId;
    }

    public void setAnnonationId(String annonationId) {
        this.annonationId = annonationId;
    }

    public String annonationId;

    private ArrayList<JSONObject> courses;
    @Override
    public void onCreate() {
        super.onCreate();

    setupuniversalLoader();
    }
    public void setupuniversalLoader()
    {
        // UNIVERSAL IMAGE LOADER SETUP::start
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // UNIVERSAL IMAGE LOADER SETUP::end

    }


}