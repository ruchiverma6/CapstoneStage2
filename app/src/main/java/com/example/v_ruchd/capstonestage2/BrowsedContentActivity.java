package com.example.v_ruchd.capstonestage2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.v_ruchd.capstonestage2.data.NewsDebHelper;
import com.example.v_ruchd.capstonestage2.fragments.BrowsedContentFragment;
import com.example.v_ruchd.capstonestage2.fragments.NewDetailFragment;
import com.example.v_ruchd.capstonestage2.listener.DataSaveListener;
import com.example.v_ruchd.capstonestage2.listener.DataUpdateListener;
import com.example.v_ruchd.capstonestage2.modal.Articles;
import com.example.v_ruchd.capstonestage2.modal.NewsChannelResponse;
import com.example.v_ruchd.capstonestage2.modal.NewsResponse;
import com.example.v_ruchd.capstonestage2.modal.Sources;
import com.example.v_ruchd.capstonestage2.retrofitcalls.ApiClient;
import com.example.v_ruchd.capstonestage2.retrofitcalls.ApiInterface;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrowsedContentActivity extends AppCompatActivity implements BrowsedContentFragment.OnFragmentInteractionListener {
    private static final String TAG = BrowsedContentActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "detailfragment";
    private static final String ARTICLEFRAGMENTTAG = "newsarticletag";
    private boolean mTwoPane;
    private Context mContext;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private String selectedChannel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        selectedChannel = getIntent().getStringExtra("selectedchannel");
        setContentView(R.layout.activity_browsed_content);
        setUpActionBar();
        if (findViewById(R.id.news_detail_container) != null) {

            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.news_detail_container, new NewDetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Utils.fetchArticleResponse(this, selectedChannel, new DataUpdateListener() {
            @Override
            public void onDataRetrieved() {
                Fragment browseContentFragment = getSupportFragmentManager().findFragmentByTag(ARTICLEFRAGMENTTAG);
                if (null != browseContentFragment && browseContentFragment instanceof BrowsedContentFragment) {
                    ((BrowsedContentFragment) browseContentFragment).onDataRetrieved(selectedChannel);
                }
            }

            @Override
            public void onDataError(String message) {

            }
        });
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
    public void onFragmentInteraction(Uri uri) {
        if (mTwoPane) {
           /* Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI, contentUri);*/

            NewDetailFragment fragment = new NewDetailFragment();
            // fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.news_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, NewsDetailActivity.class);
            // .setData(contentUri);
            startActivity(intent);
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
                onBackPressed();
                exportDatabse("news");
                return true;
            case R.id.clear_menu_item:
                copyDBToSDCard();
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


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("BrowsedContent Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}





