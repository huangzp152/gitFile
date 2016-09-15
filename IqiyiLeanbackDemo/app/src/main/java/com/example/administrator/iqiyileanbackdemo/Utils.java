package com.example.administrator.iqiyileanbackdemo;

import android.content.Context;

/**
 * Created by Administrator on 2016/8/24.
 */
public class Utils {
    public static int converDpToPixel(Context ctx,int dp){
        float density=ctx.getResources().getDisplayMetrics().density;
        return Math.round((float)dp*density);
    }
}

