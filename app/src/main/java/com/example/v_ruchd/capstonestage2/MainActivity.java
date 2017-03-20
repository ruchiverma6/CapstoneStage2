package com.example.v_ruchd.capstonestage2;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView mLoadingTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        new CountDownTimer(5000,1000){
            @Override
            public void onTick(long millisUntilFinished){
                Log.v(TAG,"onTick"+millisUntilFinished);
                if(millisUntilFinished>=3000){
                    setSplashLoadingText(getString(R.string.loading_text)+".");
                }else if(millisUntilFinished>=2000){
                    setSplashLoadingText(getString(R.string.loading_text)+"..");
                }else {
                    setSplashLoadingText(getString(R.string.loading_text)+"...");
                }
            }

            @Override
            public void onFinish(){
                startHomeActivity();
            }
        }.start();

    }

    private void initComponents() {
        mLoadingTextView=(TextView)findViewById(R.id.splash_loading_text_view);
    }

    private void setSplashLoadingText(String loadingText) {
        mLoadingTextView.setText(loadingText);
    }

    /**
     * Method to start HomeActivity.
     */
    private void startHomeActivity() {
        Intent homeActivityIntent=new Intent(this,HomeActivity.class);
        startActivity(homeActivityIntent);
    }
}
