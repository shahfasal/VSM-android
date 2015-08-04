package com.getvsm.ava;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by brabh on 5/13/2015.
 */
public class CourseDescriptionFragment extends Fragment {
    private Activity activity = null;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<JSONObject>> listDataChild;
    JSONObject course;
    public CourseDescriptionFragment() {
    }
    public void setCourse(JSONObject course)
    {
        this.course=course;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.description_layout, container, false);
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
       // prepareListData();
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<JSONObject>>();
        try {
            JSONArray chapters = course.getJSONArray("chapters");
            for(int i=0;i<chapters.length();i++)
            {
                JSONObject chapter=chapters.getJSONObject(i);
                listDataHeader.add(StringDecoder.decode(chapter.getString("name")));
                JSONArray topics=chapter.getJSONArray("topics");
                ArrayList<JSONObject> topicsList=new ArrayList<>();
                for(int j=0;j<topics.length();j++){
                    topicsList.add(topics.getJSONObject(j));
                }

                listDataChild.put(StringDecoder.decode(chapter.getString("name")),topicsList);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        listAdapter = new ExpandableListAdapter(super.getActivity(), listDataHeader, listDataChild);
        expListView = (ExpandableListView) activity.findViewById(R.id.course_chapter_list);
        expListView.setAdapter(listAdapter);
    }



}
