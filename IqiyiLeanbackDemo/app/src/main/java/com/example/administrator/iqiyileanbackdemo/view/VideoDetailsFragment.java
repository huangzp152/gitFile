package com.example.administrator.iqiyileanbackdemo.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.DetailsFragment;
import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.DetailsOverviewRowPresenter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnActionClickedListener;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.administrator.iqiyileanbackdemo.VideoList;
import com.example.administrator.iqiyileanbackdemo.R;
import com.example.administrator.iqiyileanbackdemo.Utils;
import com.example.administrator.iqiyileanbackdemo.model.Video;
import com.example.administrator.iqiyileanbackdemo.presenter.CardPresenter;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/8/24.
 * 展示视频的详细内容view
 */




public class VideoDetailsFragment extends DetailsFragment{

    private final static int DETAIL_THUMB_WIDTH=274;
    private final static int DETAIL_THUMB_HEIGHT=274;
    private final static int ACTION_WATCH_TRAILER=1;
    private final static int ACTION_WATCH_FULL=2;
    private static final int NUM_COLS = 10;


    private BackgroundManager mBackgroundManager;
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private ClassPresenterSelector mPresenterSelector;

    private Video mSelectedVideo;

    private ArrayObjectAdapter mAdapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prepareBackgroundManager();//准备详情页的背景

        mSelectedVideo =(Video)getActivity().getIntent()//获得传过来的字段
                .getParcelableExtra(DetailsActivity.MOVIE);  //获得数据的方式要用getParcelableExtra 而不是Serialzation

        if(mSelectedVideo !=null){
            setupAdapter();//封装适配器

            setupDetailsOverviewRow();//封装详情总览的行
            setupDetailsOverviewRowPresenter();  //括号中要填什么

            setupMovieListRow();//相关影片的行
            setupMovieListRowPresenter();//?????

            updateBackground(mSelectedVideo.getBgImageUrl());//加载背景图片
            setOnItemViewClickedListener(new ItemViewClickedListener());


        }else{//空的话返回主页
            Intent intent=new Intent(getActivity(),MainActivity.class);
            startActivity(intent);
        }


    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void setupAdapter(){
        mPresenterSelector=new ClassPresenterSelector();
        mAdapter=new ArrayObjectAdapter(mPresenterSelector);
        setAdapter(mAdapter);
    }

    private void setupMovieListRow(){
        String subcategories[]={"相关视频"};
        List<Video> list= VideoList.list;
        Collections.shuffle(list);//打乱
        ArrayObjectAdapter listRowAdapter=new ArrayObjectAdapter(new CardPresenter());
        for(int j=0;j<NUM_COLS;j++){
            listRowAdapter.add(list.get(j%5));//设置空格位置 所以类型一定要是Movie  之前弄成了数字
        }
        HeaderItem header=new HeaderItem(0,subcategories[0]);
        mAdapter.add(new ListRow(header,listRowAdapter));
    }


    private void setupDetailsOverviewRow(){
        final DetailsOverviewRow row=new DetailsOverviewRow(mSelectedVideo);
        row.setImageDrawable(getResources().getDrawable(R.drawable.default_background));
        int width= Utils.converDpToPixel(getActivity().getApplicationContext(),DETAIL_THUMB_WIDTH);
        int height=Utils.converDpToPixel(getActivity().getApplicationContext(),DETAIL_THUMB_HEIGHT);
        Glide.with(getActivity())
                .load(mSelectedVideo.getCardImageUrl())
                .centerCrop()
                .error(R.drawable.default_background)
                .into(new SimpleTarget<GlideDrawable>(width,height) {//这是什么意思？转向目标平台？
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                        row.setImageDrawable(resource);
                        mAdapter.notifyArrayItemRangeChanged(0,mAdapter.size());//通知一些范围内的item已经发生了变化
                    }
                });
        row.addAction(new Action(ACTION_WATCH_TRAILER,"观看预告片","免费"));
        row.addAction(new Action(ACTION_WATCH_FULL,"观看完整影片","10元"));
        mAdapter.add(row);

    }

    private void setupDetailsOverviewRowPresenter(){
        // 设置背景和风格
        DetailsOverviewRowPresenter detailsPresenter=new DetailsOverviewRowPresenter(new DetailsDescriptionPresenter());
        detailsPresenter.setBackgroundColor(getResources().getColor(R.color.selected_background));
        detailsPresenter.setStyleLarge(true);
        //Hook up transition element.
        //detailsPresenter为什么会有触发事件？？？？
        detailsPresenter.setOnActionClickedListener(new OnActionClickedListener() {
            @Override
            public void onActionClicked(Action action) {
                //选中并跳转
                if(action.getId()==ACTION_WATCH_TRAILER) {
                    Intent intent=new Intent(getActivity(),PlaybackOverlayActivity.class);
                    intent.putExtra(DetailsActivity.MOVIE, mSelectedVideo);
                    startActivity(intent); //跳转
                }else{
                    Toast.makeText(getActivity(),"看看是什么"+action.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mPresenterSelector.addClassPresenter(DetailsOverviewRow.class,detailsPresenter);

    }

    public void setupMovieListRowPresenter(){
        mPresenterSelector.addClassPresenter(ListRow.class,new ListRowPresenter());
    }

    private void prepareBackgroundManager(){
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mDefaultBackground = getResources().getDrawable(R.drawable.default_background);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void updateBackground(String uri){
        Glide.with(getActivity())
                .load(uri)
                .centerCrop()
                .error(mDefaultBackground)
                .into(new SimpleTarget<GlideDrawable>(mMetrics.widthPixels,mMetrics.heightPixels) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        mBackgroundManager.setDrawable(resource);
                    }
                });

        }

    private final class ItemViewClickedListener implements OnItemViewClickedListener{
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            //点击 转到播放的Activity
       //     Toast.makeText(getActivity(), "点击了", Toast.LENGTH_SHORT).show();
            if(item instanceof Video){
                Video video =(Video)item;
                Intent intent=new Intent(getActivity(),DetailsActivity.class);
                intent.putExtra("Video", mSelectedVideo);
                intent.putExtra("shouldStart",true);
                startActivity(intent);

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                        DetailsActivity.SHARED_ELEMENT_NAME).toBundle();
                getActivity().startActivity(intent,bundle);
            }
        }
    }


    private class DetailsDescriptionPresenter extends AbstractDetailsDescriptionPresenter{

        @Override
        protected void onBindDescription(ViewHolder vh, Object item) {
            Video video =(Video)item;
            //设置视频的一些信息
            if(video !=null) {
                vh.getTitle().setText(video.getTitle());
                vh.getSubtitle().setText(video.getStudio());
                vh.getBody().setText(video.getDescription());
            }
        }
    }
    }



