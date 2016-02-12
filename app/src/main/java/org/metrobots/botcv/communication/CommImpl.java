package org.metrobots.botcv.communication;

import android.app.AlertDialog;
import android.widget.Toast;

import org.metrobots.botcv.MainActivity;

/**
 * Created by Tasgo on 2/3/16.
 */
public class CommImpl implements CommInterface {
    private MainActivity activity;

    public CommImpl(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void print(String text) {
        //Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
        System.out.println(text);
        activity.toast(text);
    }
}
