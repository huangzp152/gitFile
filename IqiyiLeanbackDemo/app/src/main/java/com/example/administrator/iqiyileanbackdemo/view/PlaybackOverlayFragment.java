package com.example.administrator.iqiyileanbackdemo.view;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.ControlButtonPresenterSelector;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnActionClickedListener;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.PlaybackControlsRow;
import android.support.v17.leanback.widget.PlaybackControlsRowPresenter;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.administrator.iqiyileanbackdemo.VideoList;
import com.example.administrator.iqiyileanbackdemo.model.Video;
import com.example.administrator.iqiyileanbackdemo.presenter.CardPresenter;
import com.example.administrator.iqiyileanbackdemo.presenter.PlayDescriptionPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/8/25.
 *
 * 播放的Fragment
 *
 */


public class PlaybackOverlayFragment extends android.support.v17.leanback.app.PlaybackOverlayFragment{


    private static final String TAG = "PlaybackControlsFragment";
    private static final boolean SHOW_DETAIL=true;   //已经展示了detail?
    private static final boolean SHOW_IMAGE=true;
    private static final boolean HIDE_MORE_ACTIONS=false;

    private static final int CARD_WIDTH=200;
    private static final int CARD_HEIGHT=240;



    private static final int BACKGROUND_TYPE=PlaybackOverlayFragment.BG_LIGHT;//一个半透明的背景光

    private ArrayObjectAdapter mRowsAdapter;
    private ArrayObjectAdapter mPrimaryActionAdapter;  //播放控制的第一行
    private ArrayObjectAdapter mSecondaryActionAdapter;//播放控制的第二行

    private PlaybackControlsRow.PlayPauseAction mPlayPauseAction;
    private PlaybackControlsRow.SkipNextAction mSkipNextAction;
    private PlaybackControlsRow.SkipPreviousAction mSkipPreviousAction;
    private PlaybackControlsRow.FastForwardAction mFastForwardAction;
    private PlaybackControlsRow.RewindAction mRewindAction;

    private PlaybackControlsRow mPlaybackControlRow;

    private ArrayList<Video> mItems=new ArrayList<Video>();
    private Video mSelectedVideo;
    private int mCurrentItem;
    private Handler mHandler;

    private OnPlayPauseClickedListener mCallback;

    @Override
    public void onAttach(Context context){//API23 up
        super.onAttach(context);
        if (context instanceof OnPlayPauseClickedListener) {
             mCallback=(OnPlayPauseClickedListener)context;
//            Toast.makeText(context, "context", Toast.LENGTH_SHORT)
//                    .show();
        }else{
            throw new RuntimeException(context.toString()+" must implement OnPlayPauseClickedListener");
        }
    }
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        Context context=activity.getApplicationContext();
//        if (context instanceof OnPlayPauseClickedListener) {
//           // mCallback=(OnPlayPauseClickedListener)context;
//            Toast.makeText(context, "context", Toast.LENGTH_SHORT)
//                .show();
//        }else{
//            throw new RuntimeException(context.toString()+" must implement OnPlayPauseClickedListener");
//        }
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItems=new ArrayList<Video>();//获得字段
        mSelectedVideo =(Video)getActivity().getIntent().getSerializableExtra(DetailsActivity.MOVIE);

        List<Video> movies= VideoList.list;//列表

        for(int j=0;j<movies.size();j++){
            mItems.add(movies.get(j));
            if(mSelectedVideo.getTitle().contentEquals(movies.get(j).getTitle())){
                mCurrentItem=j;  //标记选中的item
            }
        }
        mHandler=new Handler();

        setBackgroundType(BACKGROUND_TYPE);  //设置背景的类型

        setFadingEnabled(false);  //设置渐变的效果

        setupRows();

        setOnItemViewClickedListener(new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                Log.v("playbackFrag","otClcked"+ item +"row"+ row);

            }
        });

        setOnItemViewSelectedListener(new OnItemViewSelectedListener() {
            @Override
            public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                Log.v("playbackFrag","otSelece"+ item +"row"+ row);
            }
        });

    }

    private void setupRows(){

        ClassPresenterSelector classPresenterSelector=new ClassPresenterSelector();//类别选择器

        PlaybackControlsRowPresenter playbackControlsRowPresenter;

        if(SHOW_DETAIL){
            playbackControlsRowPresenter=new PlaybackControlsRowPresenter(new PlayDescriptionPresenter());
        }else{
            playbackControlsRowPresenter=new PlaybackControlsRowPresenter();
        }//OnActionClicked 和 OnClicked有什么区别
        playbackControlsRowPresenter.setOnActionClickedListener(new OnActionClickedListener() {
            @Override
            public void onActionClicked(Action action) {
                //判断按下去不同的键
                if(action.getId()==mPlayPauseAction.getId()) { //按下播放或者是暂停按键
                    Toast.makeText(getActivity(),"播放开关",Toast.LENGTH_LONG).show();
                    //播放的时候，传入的是true；否则传入的是false
                    togglePlayback(mPlayPauseAction.getIndex()== PlaybackControlsRow.PlayPauseAction.PLAY);//播放的开关
                }else if(action.getId()==mSkipNextAction.getId()){//下一个
                    Toast.makeText(getActivity(),"下一个",Toast.LENGTH_LONG).show();
                    next();
                }else if(action.getId()==mSkipPreviousAction.getId()){//上一个
                    Toast.makeText(getActivity(),"上一个",Toast.LENGTH_LONG).show();
                    prev();
                }else if(action.getId()==mFastForwardAction.getId()){//快进
                    Toast.makeText(getActivity(),"快进",Toast.LENGTH_LONG).show();
                }else if(action.getId()==mRewindAction.getId()){//快退
                    Toast.makeText(getActivity(),"快退",Toast.LENGTH_LONG).show();
                }
                if(action instanceof PlaybackControlsRow.MultiAction){  //这块是什么意思
                    ((PlaybackControlsRow.MultiAction)action).nextIndex();
                    notifyChanged(action);  //通知变化
                }
            }
        });
        //设置“更多设置”的隐藏，当被调起时就会显示出来
        playbackControlsRowPresenter.setSecondaryActionsHidden(HIDE_MORE_ACTIONS);
        //增加到类别Presenter中
        classPresenterSelector.addClassPresenter(PlaybackControlsRow.class,playbackControlsRowPresenter);
        //？？？？？
        classPresenterSelector.addClassPresenter(ListRow.class,new ListRowPresenter());

        mRowsAdapter=new ArrayObjectAdapter(classPresenterSelector);
        //加入播放控制行
        addPlaybackControlsRow();
        //增加其他行
        addOtherRows();

        setAdapter(mRowsAdapter);
    }

    public void next(){
        if(++mCurrentItem>=mItems.size())
            mCurrentItem=0;//如果到最后一首，则转到第一首
        if(mPlayPauseAction.getIndex()== PlaybackControlsRow.PlayPauseAction.PLAY){//是播放状态，那么暂停
            mCallback.onFragmentPlayPause(mItems.get(mCurrentItem),0,false);
        }else{//如果是暂停状态
            mCallback.onFragmentPlayPause(mItems.get(mCurrentItem),0,true);
        }
        updatePlaybackRow(mCurrentItem);//更新播放
    }

    public void prev(){
        if(--mCurrentItem<0)
            mCurrentItem=mItems.size()-1;
        if(mPlayPauseAction.getIndex()== PlaybackControlsRow.PlayPauseAction.PLAY){
            mCallback.onFragmentPlayPause(mItems.get(mCurrentItem),0,false);
        }
        else{
            mCallback.onFragmentPlayPause(mItems.get(mCurrentItem),0,false);
        }
        updatePlaybackRow(mCurrentItem);//更新播放
    }
    private void addPlaybackControlsRow(){
        if(SHOW_DETAIL){//实例化控制行
            mPlaybackControlRow=new PlaybackControlsRow(mSelectedVideo);
        }else{
            mPlaybackControlRow=new PlaybackControlsRow();
        }
        //加入adapter中
        mRowsAdapter.add(mPlaybackControlRow);
        //更新播放行
        updatePlaybackRow(mCurrentItem);
        //控制按键Presenter的选择器
        ControlButtonPresenterSelector controlButtonPresenterSelector=new ControlButtonPresenterSelector();

        mPrimaryActionAdapter = new ArrayObjectAdapter(controlButtonPresenterSelector);
        mSecondaryActionAdapter= new ArrayObjectAdapter(controlButtonPresenterSelector);
        mPlaybackControlRow.setPrimaryActionsAdapter(mPrimaryActionAdapter);
        mPlaybackControlRow.setSecondaryActionsAdapter(mSecondaryActionAdapter);

        mPlayPauseAction=new PlaybackControlsRow.PlayPauseAction(getActivity());
        //各个按键 支持/反对/下一个/上一个/快进/快退/打乱/重复
        //先省略
        mSkipNextAction=new PlaybackControlsRow.SkipNextAction(getActivity());
        mSkipPreviousAction=new PlaybackControlsRow.SkipPreviousAction(getActivity());
        mRewindAction=new PlaybackControlsRow.RewindAction(getActivity());
        mFastForwardAction=new PlaybackControlsRow.FastForwardAction(getActivity());

        mPrimaryActionAdapter.add(mRewindAction);

        mPrimaryActionAdapter.add(mSkipPreviousAction);
        //加入播放
        mPrimaryActionAdapter.add(mPlayPauseAction);

        mPrimaryActionAdapter.add(mSkipNextAction);

        mPrimaryActionAdapter.add(mFastForwardAction);

        //还有第二行的一些选项

    }

    public void togglePlayback(boolean playPause){
        if(playPause){//传入true的时候
            mCallback.onFragmentPlayPause(mItems.get(mCurrentItem),mPlaybackControlRow.getCurrentTime(),playPause);
            setFadingEnabled(true);//实现播放之后的工具栏消失的效果
        }else{
            mCallback.onFragmentPlayPause(mItems.get(mCurrentItem),mPlaybackControlRow.getCurrentTime(),playPause);
        }
        notifyChanged(mPlayPauseAction);
    }

    private void updatePlaybackRow(int index){  //更新播放
        if(mPlaybackControlRow.getItem()!=null){
            Video item=(Video)mPlaybackControlRow.getItem();  //播放控制项目映射到电影项目?
            item.setTitle(mItems.get(mCurrentItem).getTitle());
            item.setStudio(mItems.get(mCurrentItem).getStudio());
        }
        if(SHOW_IMAGE){  //是否已经展示了图片  展示了就更新Video图像
            updateVideoImage(mItems.get(mCurrentItem).getCardImageURI().toString());
        }
        //更新范围
        mRowsAdapter.notifyArrayItemRangeChanged(0,1);
        //设置条目的播放总事件 目前时间 进度条
        mPlaybackControlRow.setTotalTime(getDuration());
        mPlaybackControlRow.setCurrentTime(0);
        mPlaybackControlRow.setBufferedProgress(0);
    }

    public int getDuration(){//设置数据源 取得返回总体时间
        Video video =mItems.get(mCurrentItem);
        MediaMetadataRetriever mmr=new MediaMetadataRetriever();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mmr.setDataSource(video.getVideoUrl(),new HashMap<String, String>());
        } else {
            mmr.setDataSource(video.getVideoUrl());
        }
        String time=mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long duration=Long.parseLong(time);
        return (int)duration;
    }

    public void updateVideoImage(String uri){
        Glide.with(getActivity())
                .load(uri)
                .centerCrop()
                .into(new SimpleTarget<GlideDrawable>(CARD_WIDTH,CARD_HEIGHT){

                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        mPlaybackControlRow.setImageDrawable(resource);
                        mRowsAdapter.notifyArrayItemRangeChanged(0,mRowsAdapter.size());
                    }
                });
    }

    private void notifyChanged(Action action){
        ArrayObjectAdapter adapter=mPrimaryActionAdapter;
        if(adapter.indexOf(action)>=0) {
            adapter.notifyArrayItemRangeChanged(adapter.indexOf(action),1);
            return;
        }
        //第二行的状态变化 写法和上面一样

    }

    private void addOtherRows(){
        //增加其他行(相关的视频，可是之前不是加了么？？？难道是别处的？)  标题
        ArrayObjectAdapter listRowAdapter=new ArrayObjectAdapter(new CardPresenter());
        for(Video video :mItems){
            listRowAdapter.add(video);
        }
        HeaderItem headerItem=new HeaderItem(0,"相关视频2");
        mRowsAdapter.add(new ListRow(headerItem,listRowAdapter));
    }
    //播放暂停的接口
    public interface OnPlayPauseClickedListener {
        void onFragmentPlayPause(Video video, int position, Boolean playPause);
    }
}
