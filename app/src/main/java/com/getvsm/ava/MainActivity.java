package com.getvsm.ava;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;

import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.splunk.mint.Mint;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    static FragmentManager fm = null;
    private static int SPLASH_TIME_OUT = 2000;
    private ActionBarDrawerToggle mDrawerToggle;
    public static DrawerLayout mDrawerLayout;
    public static ListView mDrawerList;
    public static NavListAdapter mAdapter;
    public static Activity acivity;
    public static Handler handler;
    public static JSONArray loggedInDevices;
    public static ArrayList usersDevices = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Thread t = Thread.currentThread();
//        t.setDefaultUncaughtExceptionHandler(new HomeTabFragment());
        Mint.initAndStartSession(MainActivity.this, "539a2e5c");
        if (getSharedPreferences("Cache", MODE_PRIVATE).getString("username", null) == null &&
                getSharedPreferences("Cache", MODE_PRIVATE).getString("password", null) == null) {

            setContentView(R.layout.activity_main);
            acivity = this;
        /*
         * drawer :: start
		 */
            mDrawerList = (ListView) findViewById(R.id.navList);

            mDrawerList
                    .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {

                        }
                    });

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        getSupportActionBar().setHomeButtonEnabled(true);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                    R.drawable.ic_action_new, R.string.drawer_open,
                    R.string.drawer_close) {
                /**
                 * Called when a drawer has settled in a completely open state.
                 */
                public void onDrawerOpened(View drawerView) {
                }

                /**
                 * Called when a drawer has settled in a completely closed state.
                 */
                public void onDrawerClosed(View view) {
                }
            };


            mDrawerLayout.setDrawerListener(mDrawerToggle);
        /*
         * drawer :: end
		 */


            fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.main_fragment_container, new SplashFragment());
            ft.commit();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    FrameLayout mainLayout = (FrameLayout) findViewById(R.id.main_fragment_container);
                    mainLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_1));
                    fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.main_fragment_container, new LoginRegisterFragment());
                    ft.commit();

                }
            }, SPLASH_TIME_OUT);
        } else {
            setContentView(R.layout.activity_main);
            acivity = this;
            fm = getSupportFragmentManager();
        /*
         * drawer :: start
		 */
            mDrawerList = (ListView) findViewById(R.id.navList);

            mDrawerList
                    .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {

                        }
                    });

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        getSupportActionBar().setHomeButtonEnabled(true);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                    R.drawable.ic_action_new, R.string.drawer_open,
                    R.string.drawer_close) {
                /**
                 * Called when a drawer has settled in a completely open state.
                 */
                public void onDrawerOpened(View drawerView) {
                }

                /**
                 * Called when a drawer has settled in a completely closed state.
                 */
                public void onDrawerClosed(View view) {
                }
            };


            mDrawerLayout.setDrawerListener(mDrawerToggle);
        /*
         * drawer :: end
		 */
            MyWebSocketClient.loggedIn=true;
            new LoginAsyncTask(this).execute(new String[]{
                    getSharedPreferences("Cache", MODE_PRIVATE).getString("username", null),
                    getSharedPreferences("Cache", MODE_PRIVATE).getString("password", null)

            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        //Toast.makeText(this,"sample menu",Toast.LENGTH_LONG).show();
        switch (item.getItemId()) {
            case R.id.logout:


                new LogoutAsyncTask(this).execute(new String[]{});
                break;
            case R.id.logout_all_devices:
                new LogoutFromAllAsyncTask(this).execute(new String[]{});
                break;
        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {

            return true;
        }
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


}
