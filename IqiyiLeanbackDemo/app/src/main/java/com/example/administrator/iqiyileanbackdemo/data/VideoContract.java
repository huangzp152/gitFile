package com.example.administrator.iqiyileanbackdemo.data;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Administrator on 2016/9/8.
 * 存储到video 到 sqlite 数据库 的 约定规则等
 */
public class VideoContract {
    //完整的内容提供者名称
public static final String CONTENT_AUTHORITY = "com.example.administrator.iqiyileanbackdemo";
    //用来联系content Provider的 URI
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);
    //内容的路径
    public static final String PATH_VIDEO="video";

    //BaseColumns提供了 id和count的列
    public static final class VideoEntry implements BaseColumns {
        //建立content 地址   buildUpon是组建属性到uri的builder   build()是构建的方法
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_VIDEO).build();
        //组建内容类型
        public static final String CONTENT_TYPE= ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"."+PATH_VIDEO;
        //video表格的名字
        public static final String TABLE_NAME="video";
        //具有导入分类表的外键的列
        public static final String COLUMN_CATEGORY="category";
        //video的名字
        public static final String COLUMN_NAME= SearchManager.SUGGEST_COLUMN_TEXT_1;
        //video的描述
        public static final String COLUMN_DESC=SearchManager.SUGGEST_COLUMN_TEXT_2;
        //video内容的url
        public static final String COLUMN_VIDEO_URL="video_url";
        //video背景url
        public static final String COLUMN_BG_IMAGE_URL="bg_image_url";
        //studio的名称
        public static final String COLUMN_STUDIO = "studio";
        //video的card image
        public static final String COLUMN_CARD_IMG=SearchManager.SUGGEST_COLUMN_RESULT_CARD_IMAGE;
        //video的内容类型
        public static final String COLUMN_CONTENT_TYPE = SearchManager.SUGGEST_COLUMN_CONTENT_TYPE;
        //video是不是live
        public static final String COLUMN_IS_LIVE = SearchManager.SUGGEST_COLUMN_IS_LIVE;
        //video的高度
        public static final String COLUMN_VIDEO_HEIGHT = SearchManager.SUGGEST_COLUMN_VIDEO_HEIGHT;
        //video的宽度
        public static final String COLUMN_VIDEO_WIDTH = SearchManager.SUGGEST_COLUMN_VIDEO_WIDTH;
        //音频频道的配置
        public static final String COLUMN_AUDIO_CHANNEL_CONFIG = SearchManager.SUGGEST_COLUMN_AUDIO_CHANNEL_CONFIG;
        //video的购买价格
        public static final String COLUMN_PURCHASE_PRICE = SearchManager.SUGGEST_COLUMN_PURCHASE_PRICE;
        //video的租借价格
        public static final String COLUMN_RENTAL_PRICE = SearchManager.SUGGEST_COLUMN_RENTAL_PRICE;
        //video的rating 风格
        public static final String COLUMN_RATING_STYLE = SearchManager.SUGGEST_COLUMN_RATING_STYLE;
        //rating的分数
        public static final String COLUMN_RATING_SCORE = SearchManager.SUGGEST_COLUMN_RATING_SCORE;
        //video的出产年份
        public static final String COLUMN_PRODUCTION_YEAR = SearchManager.SUGGEST_COLUMN_PRODUCTION_YEAR;
        //video的持续时间
        public static final String COLUMN_DURATION = SearchManager.SUGGEST_COLUMN_DURATION;
        //结果的action intent
        public static final String COLUMN_ACTION = SearchManager.SUGGEST_COLUMN_INTENT_ACTION;
        //返回引用带有特定id的video的uri
        public static Uri buildVideoUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);  //在路径后面跟上id
        }
    }
}
