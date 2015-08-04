package com.getvsm.ava;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by brabh on 5/10/2015.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener {
    private Activity activity = null;

    public SignUpFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sugnup_layout, container, false);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button signinButton = (Button) activity.findViewById(R.id.btRegister);
        signinButton.setOnClickListener(this);
        
    }

    @Override
    public void onClick(View v) {
        EditText name = ((EditText) activity.findViewById(R.id.etName));
        EditText email = ((EditText) activity.findViewById(R.id.etEmail));
        EditText password = ((EditText) activity.findViewById(R.id.etPassword));

        RegisterAsyncTask registerAsyncTask = new RegisterAsyncTask(getActivity());
        registerAsyncTask.execute(
                new String[]{
                        name.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString()});
    }
}
