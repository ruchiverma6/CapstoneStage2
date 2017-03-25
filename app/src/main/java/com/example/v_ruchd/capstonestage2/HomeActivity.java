package com.example.v_ruchd.capstonestage2;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.v_ruchd.capstonestage2.adapters.ChatAdapter;
import com.example.v_ruchd.capstonestage2.data.NewsContract;
import com.example.v_ruchd.capstonestage2.fragments.BrowsedContentFragment;
import com.example.v_ruchd.capstonestage2.listener.DataSaveListener;
import com.example.v_ruchd.capstonestage2.modal.ChatMessage;
import com.example.v_ruchd.capstonestage2.modal.ChatMessageResponse;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,DataSaveListener{
    private static final int MESSAGE_LOADER = 1;
    private EditText messageET;
    private RecyclerView messagesContainer;
    private Button sendBtn;
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setUpActionBar();
        initComponents();
    }

    private void initComponents() {
        messagesContainer = (RecyclerView) findViewById(R.id.messagesContainer);
        mLayoutManager = new LinearLayoutManager(this);
        //   mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        messagesContainer.setLayoutManager(mLayoutManager);
        messageET = (EditText) findViewById(R.id.messageEdit);
        sendBtn = (Button) findViewById(R.id.chatSendButton);

       // TextView meLabel = (TextView) findViewById(R.id.meLbl);
       // TextView companionLabel = (TextView) findViewById(R.id.friendLabel);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
      //  companionLabel.setText("My Buddy");
        getSupportLoaderManager().initLoader(MESSAGE_LOADER,null,this);
        loadDummyHistory();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageET.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(122);//dummy
                chatMessage.setMessage(messageText);
                chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                chatMessage.setMessageType(1);

                messageET.setText("");

                //displayMessage(chatMessage);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.clear_menu_item:
                return true;
            case R.id.browsed_content_menu_item:
                showBrowsedContentScreen();
                return true;



            default:
                return super.onOptionsItemSelected(item);
        }

    }



    private void showBrowsedContentScreen() {
        Intent browsedContentActivityintent=new Intent(this,BrowsedContentActivity.class);
        startActivity(browsedContentActivityintent);
    }


    private void loadDummyHistory(){

        chatHistory = new ArrayList<ChatMessage>();

        ChatMessage msg = new ChatMessage();
        msg.setId(1);
        msg.setMessageType(0);
        msg.setMessage("Hi");
        msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg);
        ChatMessage msg1 = new ChatMessage();
        msg1.setId(2);
        msg1.setMessageType(0);
        msg1.setMessage("How r u doing???");
        msg1.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg1);
        ChatMessage msg3 = new ChatMessage();
        msg3.setId(3);
        msg3.setMessageType(1);
        msg3.setMessage("Hi");
        msg3.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg3);
        ChatMessage msg4 = new ChatMessage();
        msg4.setId(4);
        msg4.setMessageType(1);
        msg4.setMessage("How r u doing???");
        msg4.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg4);
        ChatMessage msg5 = new ChatMessage();
        msg5.setId(5);
        msg5.setMessageType(1);
        msg5.setMessage("Hi");
        msg5.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg5);
        ChatMessage msg6 = new ChatMessage();
        msg6.setId(6);
        msg6.setMessageType(2);
        msg6.setMessage("How r u doing???");
        msg6.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg6);


        ChatMessage msg7 = new ChatMessage();
        msg7.setId(7);
        msg7.setMessageType(3);
        msg7.setMessage("How r u doing???");
        msg7.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg7);

        adapter = new ChatAdapter(HomeActivity.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

        ChatMessage[] array = new ChatMessage[chatHistory.size()];
        chatHistory.toArray(array);
        ChatMessageResponse chatMessageResponse=new ChatMessageResponse();
        chatMessageResponse.setChatMessages(array);


        /*for(int i=0; i<chatHistory.size(); i++) {
            ChatMessage message = chatHistory.get(i);
            displayMessage(message);
        }


*/



        DataSaverTask saverTask = new DataSaverTask(this, chatMessageResponse);
        saverTask.setDataSaveListener(this);
        saverTask.execute();


    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri= NewsContract.MessageEntry.CONTENT_URI;
        return new CursorLoader(this,uri,null,null,null,null);
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


    /*public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }*/

    private void scroll() {
        messagesContainer.scrollToPosition(messagesContainer.getAdapter().getItemCount() - 1);
        //  messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    @Override
    public void onDataSave() {
        getSupportLoaderManager().restartLoader(0,null,this);
    }
}
