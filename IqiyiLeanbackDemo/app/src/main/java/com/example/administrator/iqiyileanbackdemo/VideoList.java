package com.example.administrator.iqiyileanbackdemo;

import com.example.administrator.iqiyileanbackdemo.model.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/24.
 */
public class VideoList {
    public static List<Video> list;
    public static final String MOVIE_CATEGORY[]={
            "推荐","综艺","电影","电视剧","动画","音乐"
    };



    public static List<Video> setupMovies(){
        list=new ArrayList<Video>();
        String title[]={
                "视频1",
                "视频2",
                "视频3",
                "视频4",
                "视频5"
        };
        String description="描述的语言";
        String videoUrl[]={
                "http://downmp413.ffxia.com/mp413/%E8%82%96%E5%AE%B6%E6%B0%B8-%E5%A4%9C%E8%92%B2[68mtv.com].mp4",
                "http://downmp413.ffxia.com/mp413/%E8%82%96%E5%AE%B6%E6%B0%B8-%E5%A4%9C%E8%92%B2[68mtv.com].mp4",
                "http://downmp413.ffxia.com/mp413/%E8%82%96%E5%AE%B6%E6%B0%B8-%E5%A4%9C%E8%92%B2[68mtv.com].mp4",
                "http://downmp413.ffxia.com/mp413/%E8%82%96%E5%AE%B6%E6%B0%B8-%E5%A4%9C%E8%92%B2[68mtv.com].mp4",
                "http://downmp413.ffxia.com/mp413/%E8%82%96%E5%AE%B6%E6%B0%B8-%E5%A4%9C%E8%92%B2[68mtv.com].mp4"
        };

        String bgImageUrl[]={
                "http://pic9.ptqy.gitv.tv/common/lego/20160829/b75ce469dd124be09d9f07a95bb0b338.jpg",
                "http://pic5.ptqy.gitv.tv/common/lego/20160829/f3295bdfaaf14865b0d7e339accccf65.jpg",
                "http://pic5.ptqy.gitv.tv/common/lego/20160829/e166c7b21a75453583182f41b3eb7c00.jpg",
                "http://pic1.ptqy.gitv.tv/common/lego/20160830/4b81edfc4cca4047b28688a23cd11880.jpg",
                "http://pic104.nipic.com/file/20160720/3717505_104228824245_2.jpg"
        };

        String cardImageUrl[]={
                "http://pic9.ptqy.gitv.tv/common/lego/20160829/b75ce469dd124be09d9f07a95bb0b338.jpg",
                "http://pic5.ptqy.gitv.tv/common/lego/20160829/f3295bdfaaf14865b0d7e339accccf65.jpg",
                "http://pic5.ptqy.gitv.tv/common/lego/20160829/e166c7b21a75453583182f41b3eb7c00.jpg",
                "http://pic1.ptqy.gitv.tv/common/lego/20160830/4b81edfc4cca4047b28688a23cd11880.jpg",
                "http://pic104.nipic.com/file/20160720/3717505_104228824245_2.jpg"
        };
        for(int i=0;i<5;i++)
        list.add(buildMovieInfo((long)i,"种类",title[i],description,"studio"+i,videoUrl[i],cardImageUrl[i],bgImageUrl[i]));
        return list;
    }

    private static Video buildMovieInfo(long id,String category, String title, String description, String studio,
                                        String videoUrl, String cardImageUrl, String bgImageUrl){
        //封装每个Video对象
        Video video =new Video(id,category,title,description,studio,
                videoUrl,cardImageUrl,bgImageUrl);
       // video.setId(video.getCount());
      //  video.incCount();
//        video.setCategory(category);
//        video.setDescription(description);
//        video.setTitle(title);
//        video.setStudio(studio);
//        video.setVideoUrl(videoUrl);
//        video.setCardImageUrl(cardImageUrl);
//        video.setBgImageUrl(bgImageUrl);
        return video;

    }

}
