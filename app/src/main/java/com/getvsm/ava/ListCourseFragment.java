package com.getvsm.ava;

import android.app.Activity;
import android.content.Entity;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brabh on 5/10/2015.
 */
public class ListCourseFragment extends Fragment implements AdapterView.OnItemClickListener {
    private Activity activity = null;
    static ListView coursesListView;

    public ListCourseFragment() {
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




        AllCoursesAdapter coursesAdapter=new AllCoursesAdapter(activity,R.layout.list_course_item_layout,
                ((GlobalClass)activity.getApplicationContext()).getCourses());
        coursesListView.setAdapter(coursesAdapter);
        coursesListView.setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FragmentTransaction ft = MainActivity.fm.beginTransaction();
        /*
                slide ottom to top
                 */

        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_left);


        ft.replace(R.id.main_fragment_container, new EnrollCourseFragment());
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

    public class ListAsyncTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {

            HttpResponse response = null;
            String result = null;
            String url = "http://ava.getvsm.com/api/v1/courses/details?course_id=" + params[0];
            HttpGet httpGet = new HttpGet();
            try {
                HttpClient httpClient = new DefaultHttpClient();
                httpGet.setURI(new URI(url));
                response = httpClient.execute(httpGet);
                result = EntityUtils.toString(response.getEntity());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }
    }


}
