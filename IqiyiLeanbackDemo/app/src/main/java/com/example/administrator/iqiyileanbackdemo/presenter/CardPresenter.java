package com.example.administrator.iqiyileanbackdemo.presenter;

import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.util.Log;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.administrator.iqiyileanbackdemo.model.Video;
import com.example.administrator.iqiyileanbackdemo.R;

/**
 * Created by Administrator on 2016/8/24.
 * CardPresenter用来产生View和绑定对象到它们上去
 * 包含一个图像的CardView
 */
public class CardPresenter extends Presenter {

    private static final int CARD_WIDTH=313;
    private static final int CARD_HEIGHT = 176;
    private static int sSelectedBackgroundColor;
    private static int sDefaultBackgroundColor;
    private Drawable mDefaultCardImage;



    private static void updateCardBackgroundColor(ImageCardView view,boolean selected){

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        sDefaultBackgroundColor = parent.getResources().getColor(R.color.default_background);
        sSelectedBackgroundColor = parent.getResources().getColor(R.color.selected_background);
        mDefaultCardImage=parent.getResources().getDrawable(R.drawable.moive);
        ImageCardView cardView=new ImageCardView(parent.getContext()){
            @Override
            public void setSelected(boolean selected) {
                //选中的时候要更新背景颜色
                updateCardBackgroundColor(this,selected);
                super.setSelected(selected);
            }
        };
        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);//什么意思
        updateCardBackgroundColor(cardView,false);//为何调用第二次而且特意设置不更新?
        return new ViewHolder(cardView);  //调用的对象是不是采取以上的设置在onCreateViewHolder中
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        Video video =(Video)item;//获取电影item

            Log.i("error+hzp",item.toString());

        ImageCardView cardView=(ImageCardView)viewHolder.view;//获取电影的cardView

        if(video.getCardImageUrl() != null){
            cardView.setTitleText(video.getTitle());
            cardView.setContentText(video.getStudio());
            cardView.setMainImageDimensions(CARD_WIDTH,CARD_HEIGHT);  //设定卡片宽高
            Glide.with(viewHolder.view.getContext())//一个超强的缓存图片的图片加载库
                    .load(video.getCardImageUrl())
                    .centerCrop()
                    .error(mDefaultCardImage)
                    .into(cardView.getMainImageView());
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        ImageCardView cardView=(ImageCardView)viewHolder.view;
        //置空好让内存可以释放
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }
}
