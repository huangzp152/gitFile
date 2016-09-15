package com.example.administrator.iqiyileanbackdemo.model;

import android.database.Cursor;
import android.support.v17.leanback.database.CursorMapper;

import com.example.administrator.iqiyileanbackdemo.data.VideoContract;

/**
 * Created by Administrator on 2016/9/8.
 * 关联data和video的映射
 */
public class VideoCursorMapper extends CursorMapper{

    private int idIndex;
    private int nameIndex;
    private static int descIndex;
    private static int videoUrlIndex;
    private static int bgImageUrlIndex;
    private static int cardImageUrlIndex;
    private static int studioIndex;
    private static int categoryIndex;

    @Override
    protected void bindColumns(Cursor cursor) {//获得对应列的下标 绑定列
        idIndex=cursor.getColumnIndex(VideoContract.VideoEntry._ID);
        nameIndex=cursor.getColumnIndex(VideoContract.VideoEntry.COLUMN_NAME);
        descIndex=cursor.getColumnIndex(VideoContract.VideoEntry.COLUMN_DESC);
        videoUrlIndex=cursor.getColumnIndex(VideoContract.VideoEntry.COLUMN_VIDEO_URL);
        bgImageUrlIndex=cursor.getColumnIndex(VideoContract.VideoEntry.COLUMN_BG_IMAGE_URL);
        cardImageUrlIndex=cursor.getColumnIndex(VideoContract.VideoEntry.COLUMN_CARD_IMG);
        studioIndex=cursor.getColumnIndex(VideoContract.VideoEntry.COLUMN_STUDIO);
        categoryIndex=cursor.getColumnIndex(VideoContract.VideoEntry.COLUMN_CATEGORY);
    }

    @Override
    protected Object bind(Cursor cursor) {
        //获得video的各项参数
        long id=cursor.getLong(idIndex);
        String category=cursor.getString(categoryIndex);
        String title=cursor.getString(nameIndex);
        String desc=cursor.getString(descIndex);
        String videoUrl=cursor.getString(videoUrlIndex);
        String bgImageUrl=cursor.getString(bgImageUrlIndex);
        String cardImageUrl=cursor.getString(cardImageUrlIndex);
        String studio=cursor.getString(studioIndex);
        //建立一个用于Video对象
        return new Video.VideoBuilder()
                .id(0)
                .title(title)
                .category(category)
                .description(desc)
                .videoUrl(videoUrl)
                .bgImageUrl(bgImageUrl)
                .cardImageUrl(cardImageUrl)
                .studio(studio)
                .build();
    }

}
