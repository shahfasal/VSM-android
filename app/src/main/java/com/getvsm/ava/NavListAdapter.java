package com.getvsm.ava;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
;
/**
 * Created by brabh on 5/22/2015.
 */
public class NavListAdapter extends ArrayAdapter<User> {
    ArrayList<User> users;
    Context context;
    public NavListAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
        this.context=context;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size()+1;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        if(position==0)
        {
            View item = inflater.inflate(R.layout.profile_view_layout, parent,
                    false);

            return item;
        }
        else {
            View item = inflater.inflate(R.layout.nav_list_item, parent,
                    false);
            User user = users.get(position-1);
            ((TextView) item.findViewById(R.id.textView)).setText(user.device);
            ((ImageView) item.findViewById(R.id.imageView)).setImageResource(user.icon);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(MainActivity.acivity,((User)MainActivity.usersDevices.get(position-1)).getFingerprint(),Toast.LENGTH_SHORT).show();
                    new LogoutSpecificAsyncTask(MainActivity.acivity).execute(new String[]{((User)MainActivity.usersDevices.get(position-1)).getFingerprint()});
                }
            });
            return item;
        }
    }
}
