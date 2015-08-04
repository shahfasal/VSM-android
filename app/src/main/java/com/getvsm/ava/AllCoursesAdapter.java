package com.getvsm.ava;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by fasal on 19-06-2015.
 */
public class AllCoursesAdapter extends ArrayAdapter<JSONObject> {
    Activity activity;
    int resource;
    List<JSONObject> courses;

    public AllCoursesAdapter(Activity activity, int resource, List<JSONObject> courses) {
        super(activity, resource, courses);
        this.activity = activity;
        this.resource = resource;
        this.courses = courses;
    }

    @Override
    public int getCount() {
        return courses.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(resource, null, true);
        final JSONObject course = courses.get(position);
        try {
            ((TextView) view.findViewById(R.id.course_title)).setText(StringDecoder.decode(course.getJSONObject("details").getString("title")));
            ((TextView) view.findViewById(R.id.course_description)).setText(StringDecoder.decode(course.getJSONObject("details").getString("description")));





            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (course.getBoolean("is_enroll")) {
                            new CourseDetailsAsyncTask(activity).execute(course.getString("course_id"));
                        } else {
                            FragmentTransaction ft = ((ActionBarActivity) activity).getSupportFragmentManager().beginTransaction();
                 /*
                slide bottom to top
                 */
                            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_left);
                            EnrollCourseFragment enrollCourseFragment = new EnrollCourseFragment();
                            enrollCourseFragment.setCourse(course);
                            ft.replace(R.id.main_fragment_container, enrollCourseFragment);
                            ft.addToBackStack(null);
                            ft.commit();


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            String url = StringDecoder.decode(course.getJSONObject("details").getString("bg_image"));

            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true).build();
            ImageView imageView = (ImageView) view.findViewById(R.id.course_image);
            imageLoader.displayImage(url, imageView, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
}
