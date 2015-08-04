package com.getvsm.ava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

/**
 * Created by fasal on 24-07-2015.
 */
public class ActivationActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activation);

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        Log.e("deep",data+"  "+ action.toString());
        String datastring = data.toString();
       int code = datastring.indexOf("=");
        String suffix = datastring.substring(code + 1);
        Log.e("sufix",suffix);

        ActivationAsyncTask activationAsyncTask = new ActivationAsyncTask(ActivationActivity.this);
        activationAsyncTask.execute(
                new String[]{
                        suffix
                        });


    }
}
