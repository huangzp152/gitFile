package com.example.administrator.iqiyileanbackdemo.presenter;

import android.graphics.Color;
import android.support.v17.leanback.widget.BaseCardView;
import android.support.v17.leanback.widget.Presenter;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.iqiyileanbackdemo.R;

/**
 * Created by Administrator on 2016/8/24.
 */
public class GridItemPresenter extends Presenter {
    private static final int GRID_ITEM_WIDTH=200;
    private static final int GRID_ITEM_HEIGHT=200;



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        TextView textview=new TextView(parent.getContext());
        textview.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH,GRID_ITEM_HEIGHT));
        textview.setFocusable(true);
        textview.setFocusableInTouchMode(true);//???
        textview.setBackgroundColor(parent.getResources().getColor(R.color.default_background));
        textview.setTextColor(Color.WHITE);
        textview.setGravity(Gravity.CENTER);
        return new ViewHolder(textview);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        ((TextView)viewHolder.view).setText((String)item);  //把view的内容设为item的内容
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}
