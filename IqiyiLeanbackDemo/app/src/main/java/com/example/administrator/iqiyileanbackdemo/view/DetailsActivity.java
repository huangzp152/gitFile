package com.example.administrator.iqiyileanbackdemo.view;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.SearchEvent;
import android.widget.Toast;

import com.example.administrator.iqiyileanbackdemo.R;

/**
 * Created by Administrator on 2016/8/24.
 */
public class DetailsActivity extends Activity{
    public static final String MOVIE = "Video";
    public static final String SHARED_ELEMENT_NAME = "hero";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "DetailsActivity", Toast.LENGTH_SHORT)
                .show();
        setContentView(R.layout.activity_details);
    }

    //搜索请求
    @Override
    public boolean onSearchRequested() {
        startActivity(new Intent(this,SearchActivity.class));
        return true;
    }
}
