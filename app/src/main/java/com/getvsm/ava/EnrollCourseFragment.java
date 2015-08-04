package com.getvsm.ava;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by brabh on 5/13/2015.
 */
public class EnrollCourseFragment extends Fragment implements View.OnClickListener {

    Activity activity;
    Button allCourses, enrolledCourses, completedCourses;
    JSONObject course;
    public EnrollCourseFragment() {
        // Required empty public constructor
    }
    public void setCourse(JSONObject course)
    {
        this.course=course;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.course_deatil_view, container, false);
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
        //Toast.makeText(MainActivity.acivity.getApplicationContext(),"hey",Toast.LENGTH_LONG).show();
        LinearLayout mainLayout=(LinearLayout)activity.findViewById(R.id.main_layout);
        //mainLayout.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_in_botton));


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
        new ImageFetcherAsyncTask((LinearLayout)activity.findViewById(R.id.course_meta_container)).
                execute(StringDecoder.decode(course.getJSONObject("details").getString("bg_image")));
           ((TextView)activity.findViewById(R.id.title)).
                   setText(StringDecoder.decode(course.getJSONObject("details").getString("title")));
           String metaInfo="Enrolled "+StringDecoder.decode(course.getString("total_views"))+" | Views "+
                   StringDecoder.decode(course.getString("total_enrolled"));
           ((TextView)activity.findViewById(R.id.meta_info)).
                   setText(metaInfo);
           }
       catch(Exception e)
       {
           e.printStackTrace();
       }

        //;
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.sub_fragment_container, new CourseDescriptionFragment());
        ft.commit();

        Button enrollButton=(Button)activity.findViewById(R.id.enroll_button);
        enrollButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.enroll_button:
                FragmentTransaction ft=MainActivity.fm.beginTransaction();
                /*
                slide ottom to top
                 */
                try {
                    new CourseDetailsAsyncTask(activity).execute(course.getString("course_id"));
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

                break;
        }
    }
}


