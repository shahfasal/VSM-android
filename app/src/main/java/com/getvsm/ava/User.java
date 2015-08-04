package com.getvsm.ava;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by brabh on 5/13/2015.
 */
public class User {
    public String device;
    public int icon;
    public String json;

    public User(String device, int icon, String json) {
        this.device = device;
        this.icon = icon;
        this.json = json;
    }

    @Override
    public String toString() {
        return json;
    }

    public String getFingerprint() {
        try {
            Log.d("user",json);
            String fingerprint = new JSONObject(json).getString("fingerprint");
            return fingerprint;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {

        return getFingerprint().contentEquals(((User)o).getFingerprint());

    }
}
