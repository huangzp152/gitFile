package com.example.administrator.iqiyileanbackdemo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Administrator on 2016/8/24.
 */
public class Video implements Parcelable {

    private long id;
    private String title;
    private String description;  //对应另一个项目中的desc
    private String bgImageUrl;  //背景图片的url
    private String cardImageUrl;  //当前小窗口的url
    private String videoUrl;
    private String studio;
    private String category;

    //for VideoList

    public Video(final long id,
            final String category,
                 final String title,
                 final String studio,
                 final String videoUrl,
                  final String desc,
                 final String cardImageUrl,
                  final String bgImageUrl
                // final long id,
                  ){
        this.id = id;
        this.category = category;
        this.title = title;
        this.description = desc;
        this.videoUrl = videoUrl;
        this.bgImageUrl = bgImageUrl;
        this.cardImageUrl = cardImageUrl;
        this.studio = studio;

    }

    protected Video(Parcel in){  //把parcel的内容读取出来
        id=in.readLong();
        category=in.readString();
        title = in.readString();
        description = in.readString();
        bgImageUrl = in.readString();
        cardImageUrl = in.readString();
        videoUrl = in.readString();
        studio = in.readString();
    }


    //Parcel里面隐藏着这个连带的接口 需要去实现
    public static final Creator<Video> CREATOR=new Creator<Video>(){

        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }

    };

  //  public static long getCount() {
//        return id;
//    }

  //  public static void incCount(){id++;}
    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getBgImageUrl(){
        return bgImageUrl;
    }
    public URI getBackgroundImageUrl() {
        try{
            return new URI(getBgImageUrl());
        }catch (URISyntaxException e) {
            return null;
        }
    }

    public String getCardImageUrl() {
        return cardImageUrl;
    }

    public URI getCardImageURI(){
        try{
            return new URI(getCardImageUrl());
        }catch(URISyntaxException e){
            return null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(category);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(bgImageUrl);
        dest.writeString(cardImageUrl);
        dest.writeString(videoUrl);
        dest.writeString(studio);
    }

    //构建video 对象的类
    public static class VideoBuilder{
        private static long id=0;
        private String title;
        private String desc;  //对应另一个项目中的desc
        private String bgImageUrl;  //背景图片的url
        private String cardImageUrl;  //当前小窗口的url
        private String videoUrl;
        private String studio;
        private String category;

        public VideoBuilder id(long id){
            this.id=id;
            return this;    //我的理解是封装参数进去对象里面
        }

        public VideoBuilder category(String category) {
            this.category = category;
            return this;
        }

        public VideoBuilder title(String title) {
            this.title = title;
            return this;
        }

        public VideoBuilder description(String desc) {
            this.desc = desc;
            return this;
        }

        public VideoBuilder videoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
            return this;
        }

        public VideoBuilder bgImageUrl(String bgImageUrl) {
            this.bgImageUrl = bgImageUrl;
            return this;
        }

        public VideoBuilder cardImageUrl(String cardImageUrl) {
            this.cardImageUrl = cardImageUrl;
            return this;
        }

        public VideoBuilder studio(String studio) {
            this.studio = studio;
            return this;
        }


        public Video build(){
            return new Video(
                    id,
                    category,
                    title,
                    desc,
                    videoUrl,
                    bgImageUrl,
                    cardImageUrl,
                    studio
            );
        }





    }

    ///////////////////////////////////第一次写leanback的get set方法 begin
    public String getVideoUrl() {
        return videoUrl;
    }

    public String getStudio() {
        return studio;
    }

    public String getCategory() {
        return category;
    }


//    public static void setCount(long count) {
//        Video.id = count;
//    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBgImageUrl(String bgImageUrl) {
        this.bgImageUrl = bgImageUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCardImageUrl(String cardImageUrl) {
        this.cardImageUrl = cardImageUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public void setCategory(String category) {
        this.category = category;
    }
///////////////////////////////////第一次写leanback的get set方法 end

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", backgroundImageUrl='" + bgImageUrl + '\'' +
                ", backgroundImageURI='" + getBackgroundImageUrl().toString() + '\'' +
                ", cardImageUrl='" + cardImageUrl + '\'' +
                '}';
    }

}
