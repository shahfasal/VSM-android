package com.getvsm.ava;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brabh on 5/10/2015.
 */
public class EnrolledListCourseFragment extends Fragment implements AdapterView.OnItemClickListener {
    private Activity activity = null;

    public EnrolledListCourseFragment() {
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


        ListView coursesListView = (ListView) activity.findViewById(R.id.course_list_view);

        /*
        create array list of courses :
         */

        ArrayList<Course> courses = new ArrayList<Course>();
        courses.add(new Course(R.drawable.course1, getString(R.string.sample_course_name), getString(R.string.sample_course_description)));
        courses.add(new Course(R.drawable.course1, getString(R.string.sample_course_name), getString(R.string.sample_course_description)));
        courses.add(new Course(R.drawable.course1, getString(R.string.sample_course_name), getString(R.string.sample_course_description)));
        courses.add(new Course(R.drawable.course1, getString(R.string.sample_course_name), getString(R.string.sample_course_description)));



        /*
        set the list view adapter
         */
        coursesListView.setAdapter(new CourseListAdapter(courses));
        coursesListView.setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FragmentTransaction ft=MainActivity.fm.beginTransaction();
         /*
                slide ottom to top
                 */
        ft.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_in_left);
        ft.replace(R.id.main_fragment_container,new EnrolledCourseFragment());
        ft.commit();
    }

    public class CourseListAdapter extends BaseAdapter {

        List<Course> courses;

        public CourseListAdapter(List<Course> courses) {
            this.courses = courses;
        }

        @Override
        public int getCount() {
            return courses.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            long duration = 100;
            LayoutInflater inflater = activity
                    .getLayoutInflater();
            View item = inflater.inflate(R.layout.list_course_item_layout, parent,
                    false);
            Course course = courses.get(position);
            ((ImageView) item.findViewById(R.id.course_image)).setImageResource(course.courseIcon);
            ((TextView) item.findViewById(R.id.course_title)).setText(course.courseName);
            ((TextView) item.findViewById(R.id.course_description)).setText(course.courseDescription);

            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);
            animation.setDuration(duration * (position + 1));
            item.startAnimation(animation);

            return item;
        }
    }
}
