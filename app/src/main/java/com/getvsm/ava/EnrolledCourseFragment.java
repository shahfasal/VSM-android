package com.getvsm.ava;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

/**
 * Created by brabh on 5/13/2015.
 */
public class EnrolledCourseFragment extends Fragment implements View.OnClickListener {

    Activity activity;
    Button allCourses, enrolledCourses, completedCourses;
    JSONObject course;
    public EnrolledCourseFragment() {
        // Required empty public constructor
    }

    public void setCourse(JSONObject course)
    {
        this.course=course;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.course_deatil_view_1, container, false);
        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Toolbar toolbar
                = (Toolbar) activity.findViewById(R.id.app_bar);

        ((ActionBarActivity) activity).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_drawer);
        toolbar.setTitleTextColor(Color.GRAY);
        // toolbar.setBackgroundResource(R.drawable.transparent_gray_border);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.mDrawerLayout.isDrawerOpen(Gravity.START)) {
                    MainActivity.mDrawerLayout.openDrawer(Gravity.START);
                } else {
                    MainActivity.mDrawerLayout.closeDrawer(Gravity.START);
                }
            }
        });

        try
        {



            ((TextView)activity.findViewById(R.id.title)).
                    setText(StringDecoder.decode(course.getJSONObject("description").getString("title")));
            ((TextView)activity.findViewById(R.id.description)).
                    setText(StringDecoder.decode(course.getJSONObject("description").getString("description")));
            ((TextView)activity.findViewById(R.id.author_name)).
                    setText(StringDecoder.decode(course.getJSONObject("author").getString("name")));



            new ImageFetcherAsyncTask((ImageView)activity.findViewById(R.id.author_img)).
                    execute(StringDecoder.decode(course.getJSONObject("author").getString("image_big")));

            new ImageFetcherAsyncTask((LinearLayout)activity.findViewById(R.id.course_meta_container)).
                    execute(StringDecoder.decode(course.getJSONObject("description").getString("bg_image")));


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        CourseDescriptionFragment courseDescriptionFragment=new CourseDescriptionFragment();
        courseDescriptionFragment.setCourse(course);
        ft.replace(R.id.sub_fragment_container, courseDescriptionFragment);
        //ft.addToBackStack(null);
        ft.commit();
    }


    @Override
    public void onClick(View v) {
    }
}


