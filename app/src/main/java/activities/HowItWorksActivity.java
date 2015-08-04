package activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.getvsm.ava.R;

import adapters.WorkAdapter;

/**
 * Created by fasal on 01-08-2015.
 */
public class HowItWorksActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.howitworks_activity);

        ViewPager viewpager = (ViewPager) findViewById(R.id.pager);
        viewpager.setAdapter(new WorkAdapter(this));
    }
}
