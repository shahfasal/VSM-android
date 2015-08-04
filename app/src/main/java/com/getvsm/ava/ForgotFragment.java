package com.getvsm.ava;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by fasal on 30-06-2015.
 */
public class ForgotFragment extends Fragment {
    Activity activity;
    EditText et;
    Button bt;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.forgot_password,container,false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        et = (EditText) activity.findViewById(R.id.etMail);
        bt = (Button) activity.findViewById(R.id.btForgot);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotAsyncTask forgotAsyncTask = new ForgotAsyncTask(getActivity());
                forgotAsyncTask.execute(new String[]{
                    et.getText().toString()
                });
            }
        });

    }
}
