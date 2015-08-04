package com.getvsm.ava;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by brabh on 5/10/2015.
 */
public class SplashFragment extends Fragment {
    private Activity activity = null;

    public SplashFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.splash_layout , container, false);
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
        ImageView image=(ImageView)activity.findViewById(R.id.icon);
        if(image!=null) {
           // image.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotate_1));
        }
    }

}
