package com.example.v_ruchd.capstonestage2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.List;

public class NewsListActivity extends AppCompatActivity implements NewsListFragment.OnFragmentInteractionListener {
    private static final String TAG = NewsListActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "detailfragment";
    private static final String NEWSLISTFRAGMENTTAG = "newsarticletag";
    private static final int QUERY_ARTICLES = 121;
    private boolean mTwoPane;
    private Context mContext;

    public String selectedNewsCategory;
    private SharedPreferences sharedPref;
    private Tracker mTracker;

    private Toolbar toolbar;
    private TextView titleTextView;
    private String title;
    private SharedPreferences mSharedPreferences;
    private AnalyticsApplication application;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_layout);
        mContext = this;
        initComponents();
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


        fetchDataFromerver();
    }

    private void fetchDataFromerver() {
        final String channelForCategory = Utils.retrieveChannelForCategory(this, selectedNewsCategory);
        final Fragment newsListFragment = getSupportFragmentManager().findFragmentByTag(NEWSLISTFRAGMENTTAG);

        Utils.fetchArticleResponse(this, channelForCategory, new DataUpdateListener<Articles>() {
            @Override
            public void onDataRetrieved(List<Articles> resultList) {

                if (null != newsListFragment && newsListFragment instanceof NewsListFragment) {
                    ((NewsListFragment) newsListFragment).onDataRetrieved(channelForCategory, resultList);
                }

                if (mTwoPane) {


                    CustomAsyncQueryHandler customAsyncQueryHandler = new CustomAsyncQueryHandler(getContentResolver());
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
                                String sourceUrl = cursor.getString(cursor.getColumnIndex(NewsContract.ArticleEntry.COLUMN_URL));
                                String title = cursor.getString(cursor.getColumnIndex(NewsContract.ArticleEntry.COLUMN_TITLE));
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
                    customAsyncQueryHandler.startQuery(QUERY_ARTICLES, null, NewsContract.ArticleEntry.buildNewsArticleWithChannel(channelForCategory), null, null, null, null);


                }
            }

            @Override
            public void onDataError(String message) {
                if (null != newsListFragment && newsListFragment instanceof NewsListFragment) {
                    ((NewsListFragment) newsListFragment).onDataError(message);
                }
            }
        });

    }

    private void initComponents() {
        mSharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        selectedNewsCategory = getIntent().getStringExtra(getString(R.string.selected_channel_key));
        if (selectedNewsCategory == null) {
            selectedNewsCategory = mSharedPreferences.getString(getString(R.string.selected_channel_for_news_result), getString(R.string.channelid_for_general_category));
        }
        saveSelectedNewsCategory(selectedNewsCategory);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        titleTextView = (TextView) findViewById(R.id.tool_bar_title);
        if (null != selectedNewsCategory) {
            title = selectedNewsCategory.substring(0, 1).toUpperCase() + selectedNewsCategory.substring(1, selectedNewsCategory.length()) + " " + getString(R.string.news_label);
        }
        titleTextView.setText(title);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
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
    }


    @Override
    protected void onResume() {
        super.onResume();
        String screenName = getString(R.string.news_article_list);
        Log.i(TAG, "Setting screen name: " + screenName);
        mTracker.setScreenName("Image~" + screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void saveSelectedNewsCategory(String newsCategory) {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(getString(R.string.selected_channel_for_news_result), newsCategory);
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


    @Override
    public void onFragmentInteraction(Bundle result) {
        if (mTwoPane) {


            NewsDetailFragment fragment = new NewsDetailFragment();
            fragment.setArguments(result);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.news_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle();
            Intent intent = new Intent(this, NewsDetailActivity.class);
            intent.putExtras(result);
            startActivity(intent, bundle);
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


    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();


    }


}





