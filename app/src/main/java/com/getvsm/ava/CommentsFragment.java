package com.getvsm.ava;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by brabh on 5/13/2015.
 */
public class CommentsFragment extends Fragment {

    Activity activity;
    Button allCourses, enrolledCourses, completedCourses;
    public static ArrayList<JSONObject> comments;
    public static CommentsAdapter commentsAdapter;
    public static int annotationId;
    public CommentsFragment() {
        // Required empty public constructor
        comments=new ArrayList<>();
    }

    public void setComments(ArrayList<JSONObject> comments) {
        this.comments = comments;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.comment_view, container, false);
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

         commentsAdapter=new CommentsAdapter(activity,R.layout.comment_list_item,comments);
        ((ListView)activity.findViewById(R.id.comment_list)).setAdapter(commentsAdapter);


    }


}


