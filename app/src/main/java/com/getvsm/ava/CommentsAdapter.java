package com.getvsm.ava;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by fasal on 19-06-2015.
 */
public class CommentsAdapter extends ArrayAdapter<JSONObject> {
    Activity activity;
    int resource;
    List<JSONObject> comments;

    public CommentsAdapter(Activity activity, int resource, List<JSONObject> comments) {
        super(activity, resource, comments);
        this.activity = activity;
        this.resource = resource;
        this.comments = comments;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(resource, null, true);
        JSONObject comment = comments.get(comments.size() - 1 - position);
        try {
            ((TextView) view.findViewById(R.id.comment_text)).setText(StringDecoder.decode(comment.getString("comment_text")));
            if (comment.getString("image_url") != null) {
                String url = StringDecoder.decode(comment.getString("image_url"));

                ImageLoader imageLoader = ImageLoader.getInstance();
                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                        .cacheOnDisc(true).resetViewBeforeLoading(true).build();
                ImageView imageView = (ImageView) view.findViewById(R.id.img_comment);
                imageLoader.displayImage(url, imageView, options);
            }

            if (comment.getString("video_url") != null) {
                String file = StringDecoder.decode(comment.getString("video_url"));
                VideoView videoView = (VideoView) activity.findViewById(R.id.vd_play);
                videoView.setVideoPath(file);
                MediaController vidControl = new MediaController(activity);
                vidControl.setAnchorView(videoView);
                videoView.setMediaController(vidControl);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return view;
        }
    }
}
