package com.getvsm.ava;

import android.app.Activity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fasal on 19-06-2015.
 */
public class AnnonationAdapter extends ArrayAdapter<JSONObject> {
    Activity activity;
    int resource;
    List<JSONObject> annotations;
    public static String annotation_id = new String();

    public AnnonationAdapter(Activity activity, int resource, List<JSONObject> annotations) {
        super(activity, resource, annotations);
        this.activity = activity;
        this.resource = resource;
        this.annotations = annotations;
    }

    @Override
    public int getCount() {
        return annotations.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(resource, null, true);
        final JSONObject annonation = annotations.get(annotations.size() - position - 1);
        try {
            ((TextView) view.findViewById(R.id.annotation_text)).setText(StringDecoder.decode(annonation.getString("annotation_text")));


            String url = StringDecoder.decode(annonation.getString("image_url"));

            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true).build();

//initialize image view
            ImageView imageView = (ImageView) view.findViewById(R.id.img_annotation);

//download and display image from url
            imageLoader.displayImage(url, imageView, options);

            ((TextView) view.findViewById(R.id.view_commets)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        JSONArray comments = annonation.getJSONArray("comments");
                        ArrayList<JSONObject> commentList = new ArrayList<JSONObject>();
                        for (int i = 0; i < comments.length(); i++) {
                            commentList.add(comments.getJSONObject(i));
                        }
                        FragmentTransaction ft = ((ActionBarActivity) activity).getSupportFragmentManager().beginTransaction();
                        CommentsFragment commentsFragment = new CommentsFragment();
                        commentsFragment.setComments(commentList);
                        commentsFragment.annotationId = Integer.parseInt(annonation.getString("annotation_id"));
                        Log.d("comments", comments.toString());
                        ft.replace(R.id.main_fragment_container, commentsFragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            ((TextView) view.findViewById(R.id.add_comment)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = ((ActionBarActivity) activity).getSupportFragmentManager().beginTransaction();
                    AddCommentsFragment addCommentsFragment = new AddCommentsFragment();
                    try {
                        annotation_id = annonation.getString("annotation_id");
                        //addCommentsFragment.setAnnotationId(annonation.getString("annotation_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ft.replace(R.id.main_fragment_container, new AddCommentsFragment());
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return view;
        }

    }
}
