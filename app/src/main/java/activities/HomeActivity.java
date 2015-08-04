package activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.getvsm.ava.AllCoursesAdapter;
import com.getvsm.ava.GlobalClass;
import com.getvsm.ava.R;

/**
 * Created by fasal on 02-08-2015.
 */
public class HomeActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    static ListView coursesListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_courses_layout);
        coursesListView = (ListView) findViewById(R.id.course_list_view);




        AllCoursesAdapter coursesAdapter=new AllCoursesAdapter(this,R.layout.all_course_activity,
                ((GlobalClass)getApplicationContext()).getCourses());
        coursesListView.setAdapter(coursesAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
