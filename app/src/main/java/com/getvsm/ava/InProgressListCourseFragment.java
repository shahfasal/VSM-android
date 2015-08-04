package com.getvsm.ava;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by brabh on 5/10/2015.
 */
public class InProgressListCourseFragment extends Fragment implements AdapterView.OnItemClickListener {
    private Activity activity = null;
    public static ListView coursesListView;

    public InProgressListCourseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_courses_layout, container, false);
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
        coursesListView = (ListView) activity.findViewById(R.id.course_list_view);

        // new EnrollListAsync(activity).execute(new String[]{});
        ArrayList<JSONObject> courses = ((GlobalClass) activity.getApplicationContext()).getCourses();
        ArrayList<JSONObject> inProgressCourses = new ArrayList<>();
        for (int i = 0; i < courses.size(); i++) {
            JSONObject course = courses.get(i);
            try {
                if (course.getBoolean("is_enroll") && course.getInt("progress") < 100) {
                    inProgressCourses.add(course);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ;
            }
        }
        AllCoursesAdapter adapter = new AllCoursesAdapter(activity, R.layout.list_course_item_layout, inProgressCourses);
        coursesListView.setAdapter(adapter);
        coursesListView.setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FragmentTransaction ft = MainActivity.fm.beginTransaction();
         /*
                slide ottom to top
                 */
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_left);
        ft.replace(R.id.main_fragment_container, new EnrolledCourseFragment());
        ft.commit();
    }


}
