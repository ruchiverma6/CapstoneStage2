package com.example.v_ruchd.capstonestage2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.v_ruchd.capstonestage2.data.NewsContract;
import com.example.v_ruchd.capstonestage2.fragments.NewsDetailFragment;
import com.example.v_ruchd.capstonestage2.fragments.NewsListFragment;
import com.example.v_ruchd.capstonestage2.helper.AsyncQueryHandlerListener;
import com.example.v_ruchd.capstonestage2.helper.CustomAsyncQueryHandler;
import com.example.v_ruchd.capstonestage2.listener.DataUpdateListener;
import com.example.v_ruchd.capstonestage2.modal.Articles;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.List;

public class NewsListActivity extends AppCompatActivity implements NewsListFragment.OnFragmentInteractionListener {
    private static final String TAG = NewsListActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "detailfragment";
    private static final String ARTICLEFRAGMENTTAG = "newsarticletag";
    private static final int QUERY_ARTICLES = 121;
    private boolean mTwoPane;
    private Context mContext;

    public String selectedChannel;
    private SharedPreferences sharedPref;
    private Tracker mTracker;

    private Toolbar toolbar;
    private TextView titleTextView;
    private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        selectedChannel = getIntent().getStringExtra(getString(R.string.selected_channel_key));
        setContentView(R.layout.activity_news_layout);

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        titleTextView=(TextView)findViewById(R.id.tool_bar_title);

        title=selectedChannel.substring(0,1).toUpperCase()+selectedChannel.substring(1,selectedChannel.length()) + " " + getString(R.string.news_label);
        titleTextView.setText(title);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(title);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });

        setUpActionBar();
        if (findViewById(R.id.news_detail_container) != null) {

            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.news_detail_container, new NewsDetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        String screenName=getString(R.string.news_article_list);
        Log.i(TAG, "Setting screen name: " + screenName);
        mTracker.setScreenName("Image~" + screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        final String channelForCategory = Utils.retrieveChannelForCategory(this, selectedChannel);
       final Fragment browseContentFragment = getSupportFragmentManager().findFragmentByTag(ARTICLEFRAGMENTTAG);
        final Fragment newsDetailFragment = getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
        saveSelectedChannel(channelForCategory);
        Utils.fetchArticleResponse(this, channelForCategory, new DataUpdateListener<Articles>() {
            @Override
            public void onDataRetrieved(List<Articles> resultList) {

                if (null != browseContentFragment && browseContentFragment instanceof NewsListFragment) {
                    ((NewsListFragment) browseContentFragment).onDataRetrieved(channelForCategory,resultList);
                }

              if(mTwoPane) {


                  CustomAsyncQueryHandler customAsyncQueryHandler=new CustomAsyncQueryHandler(getContentResolver());
                  customAsyncQueryHandler.setAsyncQueryHandlerListener(new AsyncQueryHandlerListener() {
                      @Override
                      public void onInsertComplete(int token, Object cookie, Uri uri) {

                      }

                      @Override
                      public void onDeleteComplete(int token, Object cookie, int result) {

                      }

                      @Override
                      public void onQueryComplete(int token, Object cookie, Cursor cursor) {
                          if (null != cursor && cursor.moveToFirst()) {
cursor.moveToPosition(0);
                              String sourceUrl=cursor.getString(cursor.getColumnIndex(NewsContract.ArticleEntry.COLUMN_URL));
                              String title=cursor.getString(cursor.getColumnIndex(NewsContract.ArticleEntry.COLUMN_TITLE));
                              Bundle bundle = new Bundle();
                              bundle.putString(getString(R.string.source_url_key), sourceUrl);
                              bundle.putString(getString(R.string.title_key), title);
                              onFragmentInteraction(bundle);
                          }
                      }

                      @Override
                      public void onUpdateComplete(int token, Object cookie, int result) {

                      }
                  });
                  customAsyncQueryHandler.startQuery(QUERY_ARTICLES,null, NewsContract.ArticleEntry.buildNewsArticleWithChannel(channelForCategory),null,null,null,null);



                }
            }

            @Override
            public void onDataError(String message) {
                if (null != browseContentFragment && browseContentFragment instanceof NewsListFragment) {
                    ((NewsListFragment) browseContentFragment).onDataError(message);
                }
            }
        });
    }

    private void saveSelectedChannel(String channelForCategory) {
        sharedPref = mContext.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.selected_channel_for_news_result), channelForCategory);
        editor.commit();
    }


    /***
     * Method to set up action bar.
     */
    private void setUpActionBar() {
        setSupportActionBar(toolbar);
      getSupportActionBar().setTitle(" ");


  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void setActionBarTitle(String title) {
        String formattedTitle = selectedChannel + " " + title;
        getSupportActionBar().setTitle(formattedTitle.toUpperCase());
    }


    @Override
    public void onFragmentInteraction(Bundle result) {
        if (mTwoPane) {


            NewsDetailFragment fragment = new NewsDetailFragment();
            fragment.setArguments(result);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.news_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Bundle bundle= ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle();
            Intent intent = new Intent(this, NewsDetailActivity.class);
            intent.putExtras(result);
            startActivity(intent,bundle);
        }
    }


    @Override
    public void onBackPressed() {
        if (!Utils.returnBackStackImmediate(getSupportFragmentManager())) {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void copyDBToSDCard() {
        try {
            String DATABASE_NAME = "news";///data/data/com.example.v_ruchd.capstonestage2/databases/news.db
            InputStream myInput = new FileInputStream("/data/data/com.example.v_ruchd.capstonestage2/databases/" + DATABASE_NAME);

            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + DATABASE_NAME);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    Log.i("FO", "File creation failed for " + file);
                }
            }

            OutputStream myOutput = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/" + DATABASE_NAME);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
            Log.i("FO", "copied");

        } catch (Exception e) {
            Log.i("FO", "exception=" + e);
        }


    }

    public void exportDatabse(String databaseName) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + getPackageName() + "//databases//" + databaseName + ".db";
                String backupDBPath = "backupnamenews.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }
    }




    @Override
    public void onStart() {
        super.onStart();



    }

    @Override
    public void onStop() {
        super.onStop();



    }


}





