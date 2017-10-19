package io.hasura.orange;

import android.util.Log;

import com.facebook.Profile;

/**
 * Created by HARIHARAN on 10-07-2017.
 */

public class DrawerListItems {
    String msg;
    int id;

    public DrawerListItems(String msg, int id) {
        this.msg = msg;
        this.id = id;

    }

    public String getMsg() {
        return msg;
    }

    public int getId() {
        return id;
    }
}
