package com.example.administrator.iqiyileanbackdemo.presenter;

import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;

import com.example.administrator.iqiyileanbackdemo.model.Video;

/**
 * Created by Administrator on 2016/8/25.
 */
public class PlayDescriptionPresenter extends AbstractDetailsDescriptionPresenter{
    @Override
    protected void onBindDescription(ViewHolder vh, Object item) {
        vh.getTitle().setText(((Video)item).getTitle());
        vh.getSubtitle().setText(((Video)item).getStudio());
    }
}
