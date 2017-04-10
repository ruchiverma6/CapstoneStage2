package com.example.v_ruchd.capstonestage2;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.v_ruchd.capstonestage2.fragments.NewDetailFragment;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class NewsDetailActivity extends AppCompatActivity {

    private static final String TAG = NewsDetailActivity.class.getSimpleName();
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

// Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        setUpActionBar();
        if (savedInstanceState == null) {
           Bundle arguments = getIntent().getExtras();

            NewDetailFragment fragment = new NewDetailFragment();
           fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.news_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String screenName=getString(R.string.news_detail);
        Log.i(TAG, "Setting screen name: " + screenName);
        mTracker.setScreenName("Image~" + screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    /***
     * Method to set up action bar.
     */
    private void setUpActionBar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public void setActionBarTitle(String title) {

        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (!Utils.returnBackStackImmediate(getSupportFragmentManager())) {
            super.onBackPressed();
        }
    }



}
