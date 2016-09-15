package com.example.administrator.iqiyileanbackdemo.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.administrator.iqiyileanbackdemo.VideoList;
import com.example.administrator.iqiyileanbackdemo.R;
import com.example.administrator.iqiyileanbackdemo.model.Video;
import com.example.administrator.iqiyileanbackdemo.presenter.CardPresenter;
import com.example.administrator.iqiyileanbackdemo.presenter.GridItemPresenter;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/8/23.
 * 主要的Fragment
 */


public class MainFragment extends BrowseFragment{
    //////////////////leanback的类
    private BackgroundManager mBackgroundManager;
    private ArrayObjectAdapter mRowsAdapter;


    //ListRowPresenter
    //Presenter
    //HeaderItem
    //ListRow
    //ClassPresenterSelector
    //DetailsOverviewRowPresenter
    //PlaybackOverlayFragment
    //PlaybackControlsRowPresenter
    //PlaybackControlsRow
    //SearchFragment
    //////////////////leanback的类
    private URI mBackgroundURI;
    private Timer mBackgroundTimer;

    private final Handler mHandler=new Handler();
    private static final int BACKGROUND_UPDATE_DELAY = 300;
    private static final int NUM_ROWS=5;
    private static final int NUM_COLS=10;

    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        prepareBackgroundManager();

        setupUIElements();

        loadRows();

        setupEventListener();

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(null!=mBackgroundTimer){
            mBackgroundTimer.cancel();
        }
    }

    public void prepareBackgroundManager(){  //窗口、背景、像素点
        mBackgroundManager=BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mDefaultBackground=getResources().getDrawable(R.drawable.default_background);
        mMetrics= new DisplayMetrics();//获得像素点
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }


    public void setupUIElements(){  //标题 元素颜色
        setTitle("爱奇艺Test");//设置标题
        setHeadersState(HEADERS_ENABLED);//展示出来
        setHeadersTransitionOnBackEnabled(true);//后键支持？？？？
        setBrandColor(getResources().getColor(R.color.fastlane_background));//可不可以加，null?
        setSearchAffordanceColor(getResources().getColor(R.color.search_opaque));//搜索功能可见性的颜色
    }

    public void loadRows(){
        List<Video> list= VideoList.setupMovies();  // 数据都存在这里面了
        mRowsAdapter=new ArrayObjectAdapter(new ListRowPresenter());
        CardPresenter cardPresenter=new CardPresenter();
        int i;
        for(i=0;i<NUM_ROWS;i++){//封装频道
            if(i!=0)
                Collections.shuffle(list);
            ArrayObjectAdapter listRowAdapter=new ArrayObjectAdapter(cardPresenter);//做好了连接模型
            for(int j=0;j<NUM_COLS;j++){//每行15个
                listRowAdapter.add(list.get(j%5));//  只是重复设置而已
            }
            HeaderItem header=new HeaderItem(i, VideoList.MOVIE_CATEGORY[i]);
            mRowsAdapter.add(new ListRow(header,listRowAdapter));//一行一行封装

        }
        //封装最后一行的设置等view  ,先不写
        HeaderItem gridHeader=new HeaderItem(i,"偏好");
        GridItemPresenter mGridPresenter=new GridItemPresenter();
        ArrayObjectAdapter gridRowAdapter=new ArrayObjectAdapter(mGridPresenter);
        gridRowAdapter.add("个人设置");
        gridRowAdapter.add("关于我们");
        gridRowAdapter.add("意见反馈");
        mRowsAdapter.add(new ListRow(gridHeader,gridRowAdapter));
        setAdapter(mRowsAdapter);

    }

    public void setupEventListener(){//设定搜索点击、Item选择、Item点击事件

        setOnSearchClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "点击进入搜索并实现", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getActivity(),SearchActivity.class);
                startActivity(intent);

            }
        });
        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());

    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener{

        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            if(item instanceof Video){//转到详情页
                Video video =(Video)item;
                Intent intent=new Intent(getActivity(),DetailsActivity.class);
                intent.putExtra(DetailsActivity.MOVIE, video);
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                        DetailsActivity.SHARED_ELEMENT_NAME).toBundle();
                getActivity().startActivity(intent,bundle);
            }else if(item instanceof String){
                //判断点击的是否为error 跳转到错误页面
                Toast.makeText(getActivity(), ((String) item), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {

        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            if(item instanceof Video){
                mBackgroundURI=((Video)item).getBackgroundImageUrl();
                startBackgroundTimer();
            }
        }


    }

    private void startBackgroundTimer(){
        if(null!=mBackgroundTimer){
            mBackgroundTimer.cancel();
        }
        mBackgroundTimer=new Timer();
        mBackgroundTimer.schedule(new UpdateBackgroundTask(),BACKGROUND_UPDATE_DELAY);
    }

    private class UpdateBackgroundTask extends TimerTask {
        @Override

        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(mBackgroundURI!=null) {
                        updateBackground(mBackgroundURI.toString());
                    }
                }
            });

        }
    }

    protected void updateBackground(String uri) {
        int width=mMetrics.widthPixels;
        int height=mMetrics.heightPixels;
        Glide.with(getActivity())
                .load(uri)
                .centerCrop()
                .error(mDefaultBackground)
                .into(new SimpleTarget<GlideDrawable>(width,height) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        mBackgroundManager.setDrawable(resource);
                    }
                });
        mBackgroundTimer.cancel();
    }

}
