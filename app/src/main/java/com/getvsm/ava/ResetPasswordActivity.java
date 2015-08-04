package com.getvsm.ava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by fasal on 25-07-2015.
 */
public class ResetPasswordActivity  extends FragmentActivity {
    Button reset;
    EditText password,confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resetpassword);
        reset = (Button) findViewById(R.id.btReset);
         password = ((EditText)findViewById(R.id.etResetpassword));
         confirm = ((EditText) findViewById(R.id.etResetConfirm));
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        Log.e("deep",data+"  "+ action.toString());
        String datastring = data.toString();
        int code = datastring.indexOf("=");
       final String suffix = datastring.substring(code + 1);
        Log.e("sufix",suffix);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetAsyncTask resetAsyncTask = new ResetAsyncTask(ResetPasswordActivity.this);
                resetAsyncTask.execute(
                        new String[]{
                                suffix,
                                password.getText().toString(),
                                confirm.getText().toString()
                        });
            }
        });



    }
}
