package com.getvsm.ava;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by brabh on 5/10/2015.
 */
public class LoginRegisterFragment extends Fragment implements View.OnClickListener {
    private Activity activity = null;
    private Button signupButton, loginButton;
    TextView forgot;

    public LoginRegisterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_register_layout, container, false);

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


        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.sub_fragment_container, new LoginFragment());
        ft.commit();
        forgot= (TextView) activity.findViewById(R.id.tvForgot1);
        signupButton = (Button) activity.findViewById(R.id.signup_menu_button);
        loginButton = (Button) activity.findViewById(R.id.login_menu_button);
        signupButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        forgot.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.login_menu_button:

                ft.setCustomAnimations(R.anim.slide_in_right_1,R.anim.slide_in_left_1);
                loginButton.setBackgroundResource(R.drawable.white_background);
                signupButton.setBackgroundResource(R.drawable.gray_border);
                loginButton.setTextColor(Color.BLACK);
                signupButton.setTextColor(Color.GRAY);
                ft.replace(R.id.sub_fragment_container, new LoginFragment());

                break;
            case R.id.signup_menu_button:
                ft.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_in_left);
                loginButton.setBackgroundResource(R.drawable.gray_border);
                signupButton.setBackgroundResource(R.drawable.white_background);
                loginButton.setTextColor(Color.GRAY);
                signupButton.setTextColor(Color.BLACK);
                ft.replace(R.id.sub_fragment_container, new SignUpFragment());

                break;
            case R.id.tvForgot1:


                ft.setCustomAnimations(R.anim.slide_in_right_1,R.anim.slide_in_left_1);
                ft.replace(R.id.sub_fragment_container, new ForgotFragment());
                ft.addToBackStack(null);

                break;

        }
        ft.commit();
    }
}
