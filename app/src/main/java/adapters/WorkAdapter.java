package adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.getvsm.ava.R;

import activities.LoginActivity;
import activities.LoginRegisterActivity;


/**
 * Created by abhishek on 25-06-2015.
 */
public class WorkAdapter extends PagerAdapter {
//    int[] mResources = {
//            R.drawable.page_1,
//            R.drawable.page_2,
//            R.drawable.page_3,
//            R.drawable.page_4,
//            R.drawable.page_5,
//            //R.drawable.sixth
//    };
   Activity activity;
    LayoutInflater mLayoutInflater;

    public WorkAdapter(Activity activity) {
        this.activity=activity;
        mLayoutInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        if(position==0)
        {
            Log.d("position", position+" "+getCount());
            View view = mLayoutInflater.inflate(R.layout.home_screen_1, container, false);
            /*
            set up the listener

             */
            container.addView(view);
            return view;
        }
        if(position==1)
        {
            Log.d("position", position+" "+getCount());
            View view = mLayoutInflater.inflate(R.layout.home_screen_2, container, false);
            /*
            set up the listener

             */
            container.addView(view);
            return view;
        }
        if(position==2)
        {
            Log.d("position", position+" "+getCount());
            View view = mLayoutInflater.inflate(R.layout.home_screen_3, container, false);
            Button bt = (Button) view.findViewById(R.id.btGetStarted);
            /*
            set up the listener

             */
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, LoginRegisterActivity.class);
                   activity.startActivity(intent);
                   activity.finish();
//                    FragmentTransaction ft = ((ActionBarActivity) activity).getSupportFragmentManager().beginTransaction();
//                    ft.replace(R.id.fragment_container, new LoginFragment(), "login_fragment");
//                    ft.commit();
                }
            });


            container.addView(view);
            return view;
        }
        else {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
//            imageView.setImageResource(mResources[position]);

            container.addView(itemView);

            return itemView;
        }

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
