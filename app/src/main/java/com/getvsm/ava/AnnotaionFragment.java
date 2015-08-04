package com.getvsm.ava;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by brabh on 5/13/2015.
 */
public class AnnotaionFragment extends Fragment {

    Activity activity;
    Button allCourses, enrolledCourses, completedCourses;
    public static ArrayList<JSONObject> annonations;
    public static AnnonationAdapter annonationAdapter;

    public AnnotaionFragment() {
        // Required empty public constructor
        annonations=new ArrayList<>();
    }

    public void setAnnonations(ArrayList<JSONObject> annonations) {
        this.annonations = annonations;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.annotation_view, container, false);
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

        annonationAdapter=new AnnonationAdapter(activity, R.layout.annotation_list_item, annonations);
        ((ListView)activity.findViewById(R.id.annotation_list)).setAdapter(annonationAdapter);

       activity.findViewById(R.id.add_annonation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = ((ActionBarActivity) activity).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.main_fragment_container, new AddAnnotationFragment());
                ft.addToBackStack(null);
                ft.commit();

            }
        });

    }


}

