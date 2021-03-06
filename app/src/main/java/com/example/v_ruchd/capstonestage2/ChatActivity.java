package com.example.v_ruchd.capstonestage2;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.v_ruchd.capstonestage2.adapters.ChatAdapter;
import com.example.v_ruchd.capstonestage2.data.NewsContract;
import com.example.v_ruchd.capstonestage2.listener.DataSaveListener;
import com.example.v_ruchd.capstonestage2.luis.LuisDataUpdateListener;
import com.example.v_ruchd.capstonestage2.luis.LuisHandler;
import com.example.v_ruchd.capstonestage2.modal.ChatMessage;
import com.example.v_ruchd.capstonestage2.modal.ChatMessageResponse;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ChatActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, DataSaveListener {
    private static final int MESSAGE_LOADER = 1;
    private static final String TAG = ChatActivity.class.getSimpleName();
    private EditText messageET;
    private RecyclerView messagesContainer;
    private Button sendBtn;
    private ChatAdapter adapter;

    private LinearLayoutManager mLayoutManager;
    InterstitialAd mInterstitialAd;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.addunitid));
        getSupportLoaderManager().initLoader(
                MESSAGE_LOADER, null, ChatActivity.this);
        setUpActionBar();
        initComponents();


    }

    @Override
    protected void onResume() {
        super.onResume();
        String screenName = getString(R.string.chat_screen);
        Log.i(TAG, "Setting screen name: " + screenName);
        mTracker.setScreenName("Image~" + screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }


    private void initComponents() {

        messagesContainer = (RecyclerView) findViewById(R.id.messagesContainer);
        adapter = new ChatAdapter(ChatActivity.this);
        messagesContainer.setAdapter(adapter);

        mLayoutManager = new LinearLayoutManager(this);

        mLayoutManager.setStackFromEnd(true);
        messagesContainer.setLayoutManager(mLayoutManager);
        messageET = (EditText) findViewById(R.id.messageEdit);
        sendBtn = (Button) findViewById(R.id.chatSendButton);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory(getString(R.string.action))
                        .setAction(getString(R.string.send_message_action))
                        .build());
                String messageText = messageET.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }
                if (!Utils.isNetworkAvailable(ChatActivity.this)) {
                    Toast.makeText(ChatActivity.this, getString(R.string.no_internet_connectivity), Toast.LENGTH_LONG).show();
                    return;
                }
                processentMessageFromUser(messageText);
                ChatMessage chatMessage = new ChatMessage();

                chatMessage.setMessage(messageText);
                chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                chatMessage.setMessageType(1);
                chatMessage.setFrom(getString(R.string.user));
                messageET.setText("");
                ChatMessageResponse chatMessageResponse = new ChatMessageResponse();
                chatMessageResponse.setChatMessages(new ChatMessage[]{chatMessage});
                DataSaverTask saverTask = new DataSaverTask(ChatActivity.this, chatMessageResponse);
                saverTask.setDataSaveListener(new DataSaveListener() {
                    @Override
                    public void onDataSave() {
                        getSupportLoaderManager().restartLoader(MESSAGE_LOADER, null, ChatActivity.this);
                    }
                });
                saverTask.execute();
            }
        });


    }

    private void processentMessageFromUser(final String messageText) {
        LuisHandler luisHandler = new LuisHandler(this);
        luisHandler.sendMessageToLuis(messageText, new LuisDataUpdateListener() {
            @Override
            public void onLuisDataUpdate(String messageContent, int messageType) {
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setMessage(messageContent);
                chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                chatMessage.setMessageType(messageType);
                chatMessage.setFrom(getString(R.string.bot));
                ChatMessageResponse chatMessageResponse = new ChatMessageResponse();
                chatMessageResponse.setChatMessages(new ChatMessage[]{chatMessage});

                DataSaverTask saverTask = new DataSaverTask(ChatActivity.this, chatMessageResponse);
                saverTask.setDataSaveListener(new DataSaveListener() {
                    @Override
                    public void onDataSave() {
                        getSupportLoaderManager().restartLoader(MESSAGE_LOADER, null, ChatActivity.this);
                    }
                });
                saverTask.execute();
            }

            @Override
            public void onLuisDataErrorListener(String errorMessage) {

            }
        });
    }

    /***
     * Method to set up action bar.
     */
    private void setUpActionBar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = NewsContract.MessageEntry.CONTENT_URI;
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
        scroll();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }


    private void scroll() {
        messagesContainer.scrollToPosition(messagesContainer.getAdapter().getItemCount() - 1);

    }

    @Override
    public void onDataSave() {
        getSupportLoaderManager().restartLoader(0, null, this);
    }


    public void onCategorySelection(String selectedData) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(getString(R.string.action))
                .setAction(getString(R.string.newsresult))
                .build());
        Intent intent = new Intent(ChatActivity.this, NewsListActivity.class);
        intent.putExtra(getString(R.string.selected_channel_key), selectedData);
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle();

        startActivity(intent, bundle);


    }




}

