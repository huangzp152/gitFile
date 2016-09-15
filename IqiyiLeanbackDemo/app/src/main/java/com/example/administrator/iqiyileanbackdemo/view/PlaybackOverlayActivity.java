package com.example.administrator.iqiyileanbackdemo.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.administrator.iqiyileanbackdemo.LeanbackPlaybackState;
import com.example.administrator.iqiyileanbackdemo.MediaSessionCallback;
import com.example.administrator.iqiyileanbackdemo.R;
import com.example.administrator.iqiyileanbackdemo.model.Video;


/**
 * Created by Administrator on 2016/8/25.
 * 视频播放的Activity 并且加载PlaybackOverlayFragment
 */
public class PlaybackOverlayActivity extends Activity implements
        PlaybackOverlayFragment.OnPlayPauseClickedListener {

    private VideoView mVideoView;
    private MediaSession mSession;//作为播放器与控制器的交互
    private LeanbackPlaybackState mPlaybackState=LeanbackPlaybackState.IDLE;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playback_control);

        loadViews();  //加载View资源
        setupCallbacks();//设置回调
        mSession=new MediaSession(this,"iQiyiLeanbackTestApp");
        mSession.setCallback(new MediaSessionCallback());//设置回调
        //两个参数什么意思
        mSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS|MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mSession.setActive(true);//激活？

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        //获得资源 判断不同的按键，做播放开关的操作
        PlaybackOverlayFragment playbackOverlayFragment= (PlaybackOverlayFragment) getFragmentManager().findFragmentById(R.id.playback_controls_fragment);
        switch(keyCode){
            case KeyEvent.KEYCODE_MEDIA_PLAY:
                //要播放的  但这样子会暂停的啊，我觉得应该是true
                playbackOverlayFragment.togglePlayback(true);
                return true;
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                if(mPlaybackState==LeanbackPlaybackState.PLAYING)
                    playbackOverlayFragment.togglePlayback(false);
                else
                    playbackOverlayFragment.togglePlayback(true);
            default:
                return super.onKeyUp(keyCode, event);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mVideoView.isPlaying()){
            if(!requestVisibleBehind(true)) {
                //在launcher后面播放,是否要在一个透明的activity中显示可见需要调用到它
                stopPlayback();  //停止播放
            }else {
                requestVisibleBehind(false);//为什么是false？？
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.suspend();
    }

    @Override
    public void onResume() {
        super.onResume();
        mSession.setActive(true);
    }

    @Override
    protected void onStop(){
        super.onStop();
        mSession.release();
    }

    @Override
    public void onVisibleBehindCanceled() {  //在退出后可见
        super.onVisibleBehindCanceled();
    }

    @Override
    public void onFragmentPlayPause(Video video, int position, Boolean playPause) {
        mVideoView.setVideoPath(video.getVideoUrl());//获得视频路径

        if(position==0||mPlaybackState==LeanbackPlaybackState.IDLE){//闲置状态
            setupCallbacks();
            mPlaybackState = LeanbackPlaybackState.IDLE;//设置为IDLE 进行准备
        }
        //playPause为true&&不在播放，则调成播放

        //playPause为true&&在播放，则调成暂停
        //playPause为false&&在播放，则调成暂停
        //playPause为false&&不在播放，则调成暂停
        if(playPause&&mPlaybackState!=LeanbackPlaybackState.PLAYING){//暂停状态 调成播放
            mPlaybackState=LeanbackPlaybackState.PLAYING;
            if(position>0){
                mVideoView.seekTo(position);
                mVideoView.start();
            }
        }else{//播放状态 调成暂停
            mPlaybackState=LeanbackPlaybackState.PAUSED;
            mVideoView.pause();
        }
        //更新播放状态和 元数据??
        updatePlaybackState(position);
        updateMetadata(video);
    }

    public void stopPlayback(){
        if(mVideoView!=null){
            mVideoView.stopPlayback();
        }
    }
    public void updatePlaybackState(int position){
        //建立播放状态的Builder 设置为暂停 设置各种状态等
        PlaybackState.Builder stateBuilder=new PlaybackState.Builder().setActions(getAvailableActions());
        int state=PlaybackState.STATE_PLAYING;
        if(mPlaybackState==LeanbackPlaybackState.PAUSED){
            state=PlaybackState.STATE_PAUSED;
        }
        stateBuilder.setState(state,position,1.0f);
        mSession.setPlaybackState(stateBuilder.build());//设定一个目前的最终状态
    }

    private long getAvailableActions(){  //获得能得到的action
        long actions=PlaybackState.ACTION_PLAY|PlaybackState.ACTION_PLAY_FROM_MEDIA_ID|
                PlaybackState.ACTION_PLAY_FROM_SEARCH;
                //把播放状态改为暂停
        if(mPlaybackState==LeanbackPlaybackState.PLAYING)
            actions=actions|PlaybackState.ACTION_PAUSE;   //这个是什么意思
        return actions;

    }

    public void updateMetadata(Video video){
        //封装元数据 并且加载
        final MediaMetadata.Builder metadataBuilder=new MediaMetadata.Builder();
        String title= video.getTitle().replace("_","-");//可能是要有这样的要求吧
        metadataBuilder.putString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE,title);
        metadataBuilder.putString(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE, video.getDescription());
        metadataBuilder.putString(MediaMetadata.METADATA_KEY_DISPLAY_ICON_URI, video.getCardImageUrl());
        //小标题 以及作者
        metadataBuilder.putString(MediaMetadata.METADATA_KEY_TITLE,title);
        metadataBuilder.putString(MediaMetadata.METADATA_KEY_ARTIST, video.getStudio());
        //加载
        Glide.with(this)
                .load(Uri.parse(video.getCardImageUrl()))
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(500,500) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        metadataBuilder.putBitmap(MediaMetadata.METADATA_KEY_ART,resource);
                        mSession.setMetadata(metadataBuilder.build());
                    }
                });
    }

    private void loadViews(){
        mVideoView=(VideoView)findViewById(R.id.videoView);
        mVideoView.setFocusable(false);//改成true会怎么样
        mVideoView.setFocusableInTouchMode(false);
    }

    private void setupCallbacks(){

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener(){

            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                //先不写
                Toast.makeText(getApplicationContext(),"onError",Toast.LENGTH_LONG).show();
                mVideoView.stopPlayback();
                mPlaybackState=LeanbackPlaybackState.IDLE;
                return false;
            }
        });

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override  //准备完成了的监听器 开播
            public void onPrepared(MediaPlayer mediaPlayer) {
                Toast.makeText(getApplicationContext(),"onPrepared",Toast.LENGTH_LONG).show();
                if(mPlaybackState==LeanbackPlaybackState.PLAYING){
                    mVideoView.start();
                }

            }
        });
        //完成时状态设置为闲置
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mPlaybackState=LeanbackPlaybackState.IDLE;
            }
        });
    }


}
