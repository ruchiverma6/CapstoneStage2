package com.example.v_ruchd.capstonestage2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ruchi on 19/3/17.
 */

public class NewsDebHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=2;

    private static final String DATABASE_NAME="news.db";
    private static final String TAG = NewsDebHelper.class.getSimpleName();

    public NewsDebHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_NEWSCHANNEL_TABLE = "CREATE TABLE " + NewsContract.NewsChannelEntry.TABLE_NAME + " (" +
                NewsContract.NewsChannelEntry._ID + " INTEGER PRIMARY KEY," +  NewsContract.NewsChannelEntry.COLUMN_NEWSCHANNEL_SOURCE_ID+ " TEXT UNIQUE NOT NULL, " +
                NewsContract.NewsChannelEntry.COLUMN_NEWSCHANNEL_NAME + " TEXT NOT NULL, " +
                NewsContract.NewsChannelEntry.COLUMN_NEWSCHANNEL_DESCRIPTION  + " TEXT NOT NULL, " +
                NewsContract.NewsChannelEntry.COLUMN_NEWSCHANNEL_URL  + " TEXT NOT NULL, " +
                NewsContract.NewsChannelEntry.COLUMN_NEWSCHANNEL_URL_TO_LOGOS + " Text NOT NULL, " +

                " UNIQUE (" + NewsContract.NewsChannelEntry.COLUMN_NEWSCHANNEL_SOURCE_ID + ") ON CONFLICT REPLACE);";




        final String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE " + NewsContract.CategoryEntry.TABLE_NAME + " (" +

                NewsContract.CategoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +


                NewsContract.CategoryEntry.COLUMN_NEWSCHANNEL_KEY + " TEXT NOT NULL, " +
                NewsContract.CategoryEntry.COLUMN_CATEGORY_TYPE + " TEXT NOT NULL, " +

                " FOREIGN KEY (" + NewsContract.CategoryEntry.COLUMN_NEWSCHANNEL_KEY + ") REFERENCES " +
                NewsContract.NewsChannelEntry.TABLE_NAME + " (" + NewsContract.NewsChannelEntry.COLUMN_NEWSCHANNEL_SOURCE_ID + "), " + " UNIQUE (" + NewsContract.CategoryEntry.COLUMN_NEWSCHANNEL_KEY +", "+ NewsContract.CategoryEntry.COLUMN_CATEGORY_TYPE+ ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_ARTICLES_TABLE = "CREATE TABLE " + NewsContract.ArticleEntry.TABLE_NAME + " (" +

                NewsContract.ArticleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +


                NewsContract.ArticleEntry.COLUMN_ARTICLE_SOURCE_CHANNEL_ID + " TEXT NOT NULL, " +
                NewsContract.ArticleEntry.COLUMN_AUTHOR+ " TEXT, " +
                NewsContract.ArticleEntry.COLUMN_TITLE + " TEXT NOT NULL, " +

                NewsContract.ArticleEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                NewsContract.ArticleEntry.COLUMN_URL + " TEXT NOT NULL, " +

                NewsContract.ArticleEntry.COLUMN_URL_TO_IMAGE + " TEXT NOT NULL, " +
                NewsContract.ArticleEntry.COLUMN_PUBLISHEDAT + " TEXT NOT NULL,  " +

                " FOREIGN KEY (" + NewsContract.ArticleEntry.COLUMN_ARTICLE_SOURCE_CHANNEL_ID + ") REFERENCES " +
                NewsContract.NewsChannelEntry.TABLE_NAME + " (" + NewsContract.NewsChannelEntry.COLUMN_NEWSCHANNEL_SOURCE_ID + ") , " + " UNIQUE (" + NewsContract.ArticleEntry.COLUMN_ARTICLE_SOURCE_CHANNEL_ID +", "+ NewsContract.ArticleEntry.COLUMN_TITLE+ ") ON CONFLICT REPLACE);";









        final String SQL_CREATE_MESSAGE_TABLE = "CREATE TABLE " + NewsContract.MessageEntry.TABLE_NAME + " (" +
                NewsContract.MessageEntry._ID + " INTEGER PRIMARY KEY," +  NewsContract.MessageEntry.COLUMN_MESSAGE_ID+ " INTEGER UNIQUE NOT NULL, " +
                NewsContract.MessageEntry.COLUMN_MESSAGE_CONTENT + " TEXT NOT NULL, " +
                NewsContract.MessageEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                NewsContract.MessageEntry.COLUMN_MESSAGE_TYPE + " TEXT NOT NULL, " +  NewsContract.MessageEntry.COLUMN_MESSAGE_FROM + " TEXT NOT NULL, " +  NewsContract.MessageEntry.COLUMN_MESSAGE_RESULT_ISCLICK + " TEXT, " +
                " UNIQUE (" + NewsContract.MessageEntry.COLUMN_MESSAGE_ID + ") ON CONFLICT REPLACE);";





        final String SQL_CREATE_MESSAGE_CATEGORY_SELECTION_TABLE = "CREATE TABLE " + NewsContract.MessageCategorySelectionEntry.TABLE_NAME + " (" +
                NewsContract.MessageCategorySelectionEntry._ID + " INTEGER PRIMARY KEY," +  NewsContract.MessageCategorySelectionEntry.COLUMN_MESSAGE_ID+ " INTEGER UNIQUE NOT NULL, " +
                NewsContract.MessageCategorySelectionEntry.COLUMN_MESSAGE_SELECTED_CATEGORY_TYPE + " TEXT NOT NULL, " +

                " FOREIGN KEY (" + NewsContract.MessageCategorySelectionEntry.COLUMN_MESSAGE_ID + ") REFERENCES " +
                NewsContract.NewsChannelEntry.TABLE_NAME + " (" + NewsContract.MessageEntry.COLUMN_MESSAGE_ID + "), " + " UNIQUE (" + NewsContract.MessageCategorySelectionEntry.COLUMN_MESSAGE_ID + ") ON CONFLICT REPLACE);";


        final String SQL_CREATE_TOTALMESSAGE_LENGTH_TABLE = "CREATE TABLE " + NewsContract.TotalMessageLengthEntry.TABLE_NAME + " (" +
                NewsContract.TotalMessageLengthEntry._ID + " INTEGER PRIMARY KEY," +  NewsContract.TotalMessageLengthEntry.COLUMN_MESSAGE_FROM+ " TEXT UNIQUE NOT NULL, " +
                NewsContract.TotalMessageLengthEntry.COLUMN_MESSAGE_TOTAL_LENGTH + " INTEGER NOT NULL, " +
                " UNIQUE (" + NewsContract.TotalMessageLengthEntry.COLUMN_MESSAGE_FROM + ") ON CONFLICT REPLACE);";





        sqLiteDatabase.execSQL(SQL_CREATE_NEWSCHANNEL_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CATEGORY_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ARTICLES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MESSAGE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MESSAGE_CATEGORY_SELECTION_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TOTALMESSAGE_LENGTH_TABLE);
        Log.v(TAG,sqLiteDatabase.getPath());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NewsContract.NewsChannelEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NewsContract.CategoryEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NewsContract.ArticleEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NewsContract.MessageEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}


