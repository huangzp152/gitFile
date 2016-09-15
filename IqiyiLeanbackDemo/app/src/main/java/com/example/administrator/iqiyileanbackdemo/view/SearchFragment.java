package com.example.administrator.iqiyileanbackdemo.view;

import android.Manifest;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.speech.SpeechRecognizer;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.CursorObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.SearchBar;
import android.support.v17.leanback.widget.SpeechRecognitionCallback;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.iqiyileanbackdemo.BuildConfig;
import com.example.administrator.iqiyileanbackdemo.R;
import com.example.administrator.iqiyileanbackdemo.data.VideoContract;
import com.example.administrator.iqiyileanbackdemo.model.Video;
import com.example.administrator.iqiyileanbackdemo.model.VideoCursorMapper;
import com.example.administrator.iqiyileanbackdemo.presenter.CardPresenter;

/**
 * Created by Administrator on 2016/8/31.
 * 搜索的Fragment,实现数据加载的回调
 * 搜索结果的提供器
 *
 */



public class SearchFragment extends android.support.v17.leanback.app.SearchFragment
    implements android.support.v17.leanback.app.SearchFragment.SearchResultProvider,
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = "SearchFragment";
    private ArrayObjectAdapter mRowsAdapter;
    private SpeechRecognitionCallback mSpeechRecognitionCallback;
    private SpeechRecognizer mSpeechRecognizer;
    private SearchBar mSearchBar;//搜索bar  就是那个圆图型
    private final CursorObjectAdapter mVideoCursorAdapter=new CursorObjectAdapter(new CardPresenter());
    private static final int REQUEST_SPEECH=0x00000010;  //一个标志 用于辨别是哪个Activity返回的
    private final Handler mHandler=new Handler();
    private boolean mResultsFound=false;
    private static final boolean FINISH_ON_RECOGNIZER_CANCELED=true;
    private static final boolean DEBUG= BuildConfig.DEBUG;
    private String mQuery;
    private int mSearchLoaderId=1;  //搜索加载器的id

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置行的adapter 、相关结果的video的adapter、搜索结果Provider、点击监听器、语音识别回调事件（如果允许）
        mRowsAdapter=new ArrayObjectAdapter(new ListRowPresenter());
        mVideoCursorAdapter.setMapper(new VideoCursorMapper());//绑定数据和view
        setSearchResultProvider(this);//负责为查询返回结果的搜索Provider
        setOnItemViewClickedListener(new ItemViewClickedListener());//设置监听事件
        //语音识别模块
        if(!hasPermission(Manifest.permission.RECORD_AUDIO)){//没有录音的权限
            //提示
            Log.d(TAG, "Does not have RECORD_AUDIO, using SpeechRecognitionCallback");
        }else{
            //设置语音识别回调
            setSpeechRecognitionCallback(new SpeechRecognitionCallback() {
                @Override
                public void recognizeSpeech() {
                    try{
                        Log.e(TAG, "find activity for speech recognizer");
                        startActivityForResult(getRecognizerIntent(), REQUEST_SPEECH);//开启搜索的activity
                    }catch(ActivityNotFoundException e){
                        Log.e(TAG, "Cannot find activity for speech recognizer", e);
                    }
                }
            });

        }
    }

    //把子模块的数据完成了之后交给Activity处理，这个回调函数处理搜索数据业务逻辑，接收那个startActivityForResult 的 requestcode

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case REQUEST_SPEECH:
                switch(requestCode){
                    case Activity.RESULT_OK:
                        setSearchQuery(data,true);
                        break;
                    default://识别失败时的处理  显示圆圈
                        if(FINISH_ON_RECOGNIZER_CANCELED){
                            if(!hasResults()){
                                if (DEBUG) Log.v(TAG, "Voice search canceled");
                               getView().findViewById(R.id.lb_search_bar_speech_orb);
                            }
                        }
                        break;
                }
                break;
        }
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacksAndMessages(null);
        super.onPause();
    }

    public boolean hasPermission(final String permission){
        final Context context=getActivity();
        return PackageManager.PERMISSION_GRANTED==context.getPackageManager().checkPermission(permission,context.getPackageName());
                //不是特别理解
    }
    //让fragment传递语音识别要求给activity
    public void setSpeechRecognitionCallback(SpeechRecognitionCallback callback){
        mSpeechRecognitionCallback=callback;
        if(mSearchBar!=null){  //searchBar存在 则绑定回调方法
            mSearchBar.setSpeechRecognitionCallback(mSpeechRecognitionCallback);
        }
        if(callback!=null){//释放掉这个识别器
            releaseRecognizer();
        }
    }

    public void releaseRecognizer(){//销毁识别器
        if(null!=mSpeechRecognizer){
            mSearchBar.setSpeechRecognizer(null);
            mSpeechRecognizer.destroy();
            mSpeechRecognizer=null;
        }
    }

    public boolean hasResults(){
        return mRowsAdapter.size()>0&&mResultsFound;
    }

    public void focusOnSearch(){
       // Log.i("hzpp","Search text submitted: %s");
        getView().findViewById(R.id.lb_search_bar).requestFocus();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String query=mQuery;
        return new CursorLoader(getActivity(),
                                VideoContract.VideoEntry.CONTENT_URI,
                                null,
                                VideoContract.VideoEntry.COLUMN_NAME+" LIKE ? OR " +
                                VideoContract.VideoEntry.COLUMN_DESC+" LIKE ? ",
                                new String[]{"%"+query+"%","%"+query+"%"},
                                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int titleRes;//加载完成的事件
        if(cursor!=null && cursor.moveToFirst()){
            mResultsFound=true;//光标存在 且能移动到头 说明有结果了
            titleRes=R.string.search_results;  //Search results for \'%1$s\'
        }else{
            mResultsFound=false;
            titleRes=R.string.no_search_results;
        }
        mVideoCursorAdapter.changeCursor(cursor);//设置并且要摆上去了
        HeaderItem headerItem=new HeaderItem(getString(titleRes,mQuery));
        mRowsAdapter.clear();
        ListRow row=new ListRow(headerItem,mVideoCursorAdapter);
        mRowsAdapter.add(row); //加上去
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {//重新加载光标的事件
        mVideoCursorAdapter.changeCursor(null);//基本上是置空
    }

    @Override
    public ObjectAdapter getResultsAdapter() {
        return mRowsAdapter;
    }

    @Override
    public boolean onQueryTextChange(String newQuery) {//当查询文字变化的时候 需要重新加载查询
        if(DEBUG) Log.i(TAG,String.format("Search text changed: %s",newQuery));
        loadQuery(newQuery);
        return true;
    }


    private void loadQuery(String query){//异步查询 加载数据
        if(!TextUtils.isEmpty(query)&&!query.equals("nil")){//用TextUtils的目的是方便 省去了写if
            mQuery=query;
            getLoaderManager().initLoader(mSearchLoaderId++,null,this);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(DEBUG) Log.i(TAG,String.format("Search text submitted:%s",query));
        loadQuery(query);
        return true;
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener{

        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            if(item instanceof Video){//是movie就跳转页面
                Video video =(Video)item;
                Intent intent=new Intent(getActivity(),DetailsActivity.class);
                intent.putExtra(DetailsActivity.MOVIE, video);
                //打包参数
                Bundle bundle= ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(), ((ImageCardView)itemViewHolder.view).getMainImageView(),
                        DetailsActivity.SHARED_ELEMENT_NAME).toBundle();
                getActivity().startActivity(intent,bundle);//去掉getActivity()可以么 ？
            }
            else{//不是就打印提示
                Toast.makeText(getActivity(),((String)item),Toast.LENGTH_LONG).show();
            }
        }
    }
}
