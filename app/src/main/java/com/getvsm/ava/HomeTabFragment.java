package com.getvsm.ava;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.PrintWriter;
import java.io.StringWriter;


public class HomeTabFragment extends Fragment implements View.OnClickListener {

        Activity activity;
        Button allCourses, completedCourses, inProgressCourses;

        public HomeTabFragment() {
        // Required empty public constructor
    }

        @Override
        public View onCreateView (LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

                return inflater.inflate(R.layout.home_tab_layout, container, false);


        }


        @Override
        public void onAttach (Activity activity){
        super.onAttach(activity);
        this.activity = activity;
    }


        @Override
        public void onActivityCreated (@Nullable Bundle savedInstanceState){
            try {
                super.onActivityCreated(savedInstanceState);

                Toolbar toolbar
                        = (Toolbar) activity.findViewById(R.id.app_bar);

                ((ActionBarActivity) activity).setSupportActionBar(toolbar);
                toolbar.setNavigationIcon(R.drawable.ic_drawer);
                toolbar.setTitleTextColor(Color.BLACK);
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

        /*
        call async task
         */

                new ListAsync(activity, getChildFragmentManager()).execute(new String[]{});

                allCourses = (Button) activity.findViewById(R.id.tab_1);
                completedCourses = (Button) activity.findViewById(R.id.tab_2);
                inProgressCourses = (Button) activity.findViewById(R.id.tab_3);

                allCourses.setOnClickListener(this);
                completedCourses.setOnClickListener(this);
                inProgressCourses.setOnClickListener(this);
            }catch (Exception e){

                e.printStackTrace();
                createDialogStackTrace(activity,e);
            }





    }


        @Override
        public void onClick (View v){
            try{
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();

        switch (v.getId()) {
            case R.id.tab_1:

                allCourses.setBackgroundResource(R.drawable.red_background);
                allCourses.setTextColor(Color.BLACK);
                completedCourses.setBackgroundResource(R.drawable.gray_border);
                completedCourses.setTextColor(Color.GRAY);
                inProgressCourses.setBackgroundResource(R.drawable.gray_border);
                inProgressCourses.setTextColor(Color.GRAY);
                ft.replace(R.id.sub_tab_fragent_container, new ListCourseFragment());

                break;
            case R.id.tab_2:

                allCourses.setBackgroundResource(R.drawable.gray_border);
                allCourses.setTextColor(Color.GRAY);
                completedCourses.setBackgroundResource(R.drawable.red_background);
                completedCourses.setTextColor(Color.BLACK);
                inProgressCourses.setBackgroundResource(R.drawable.gray_border);
                inProgressCourses.setTextColor(Color.GRAY);
                ft.replace(R.id.sub_tab_fragent_container, new CompletedListCourseFragment());

                break;
            case R.id.tab_3:

                allCourses.setBackgroundResource(R.drawable.gray_border);
                allCourses.setTextColor(Color.GRAY);
                completedCourses.setBackgroundResource(R.drawable.gray_border);
                completedCourses.setTextColor(Color.GRAY);
                inProgressCourses.setBackgroundResource(R.drawable.red_background);
                inProgressCourses.setTextColor(Color.BLACK);
                ft.replace(R.id.sub_tab_fragent_container, new InProgressListCourseFragment());

                break;
        }
        ft.commit();
            }catch(Exception e){
                e.printStackTrace();
            }
    }

//    @Override
//    public void uncaughtException(Thread thread, Throwable ex) {
//        Log.e("tag", "Received exception '" + ex.getMessage() + "' from thread " + thread.getName(), ex);
//
//    }
    public void createDialogStackTrace(Activity activity, Exception e) {
        // Converts the stack trace into a string.
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));

        // Show the stack trace on Logcat.
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        // Add the buttons
        builder.setMessage(errors.toString());
        // Set other dialog properties

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}


