package com.example.v_ruchd.capstonestage2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.v_ruchd.capstonestage2.adapters.ChatListAdapter;

public class HomeActivity extends AppCompatActivity {
private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ChatListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setUpActionBar();
        initComponents();
    }

    private void initComponents() {
        mRecyclerView=(RecyclerView)findViewById(R.id.recyler_view);


        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ChatListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
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
}
