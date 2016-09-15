package com.example.administrator.iqiyileanbackdemo.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.example.administrator.iqiyileanbackdemo.R;

/**
 * Created by Administrator on 2016/8/31.
 */
public class SearchActivity extends Activity{
    private SearchFragment mFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        mFragment=(SearchFragment) getFragmentManager().findFragmentById(R.id.search_fragment);

    }

    @Override   //Android 自带的搜索
    public boolean onSearchRequested() {
        if(mFragment.hasResults()){  //有结果了 还是跳转到搜索页面
            startActivity(new Intent(this,SearchActivity.class));
        }else {//没有结果 就开启语音查询
            mFragment.startRecognition();
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_DPAD_LEFT&&!mFragment.hasResults()){
            mFragment.focusOnSearch();//还没有结果时 按了右边 就进行搜索
        }
        return super.onKeyDown(keyCode, event);
    }
}
