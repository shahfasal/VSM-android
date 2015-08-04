package com.getvsm.ava;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by brabh on 5/10/2015.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    private Activity activity = null;
    public LoginAsyncTask loginAsyncTask;// = new LoginAsyncTask(getActivity());

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_layout, container, false);
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
        Button signinButton = (Button) activity.findViewById(R.id.signin_button);

        signinButton.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin_button:
                EditText usernameTextView = ((EditText) activity.findViewById(R.id.email_edit_text));
                EditText passwordTextView = ((EditText) activity.findViewById(R.id.password_edit_text));

                loginAsyncTask = new LoginAsyncTask(getActivity());
                loginAsyncTask.execute(
                        new String[]{
                                usernameTextView.getText().toString(),
                                passwordTextView.getText().toString()});

                break;

        }
    }

}
