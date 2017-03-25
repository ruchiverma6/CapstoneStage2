package com.example.v_ruchd.capstonestage2.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by ruchi on 19/3/17.
 */

public class NewsProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static final int NEWSCHANNELS = 100;
    static final int CATEGORY = 101;
    static final int NEWSCHANNELS_WITH_CATEGORY = 102;

    static final int NEWSARTICLES = 103;
    static final int NEWSARTICLES_WITH_SOURCECHANNELS=104;
    static final int MESSAGES = 105;
    private NewsDebHelper mOpenHelper;

    /*static final int MOVIE_WITH_SORT_BY_ID = 102;
    static final int TRAILER_WITH_MOVIE_ID = 104;
    static final int REVIEW_WITH_MOVIE_ID = 105;
    static final int REVIEWS = 106;
    // static final int WEATHER_WITH_LOCATION_AND_DATE = 102;
    static final int SORTBY = 300;*/

    private static final String selectionMovieWithSortBy = NewsContract.ArticleEntry.TABLE_NAME + "." + NewsContract.ArticleEntry.COLUMN_ARTICLE_SOURCE_CHANNEL_ID + " = ? ";

    static UriMatcher buildUriMatcher() {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = NewsContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, NewsContract.PATH_NEWSCHANNEL, NEWSCHANNELS);
        matcher.addURI(authority, NewsContract.PATH_CATEGORY, CATEGORY);
        matcher.addURI(authority, NewsContract.PATH_NEWSCHANNEL + "/*", NEWSCHANNELS_WITH_CATEGORY);
        //  matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*/#", WEATHER_WITH_LOCATION_AND_DATE);

        matcher.addURI(authority, NewsContract.PATH_NEWSARTICLES,NEWSARTICLES);
        matcher.addURI(authority, NewsContract.PATH_NEWSARTICLES+ "/*",NEWSARTICLES_WITH_SOURCECHANNELS);
        matcher.addURI(authority, NewsContract.PATH_MESSAGES, MESSAGES);
//        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*", MOVIE_WITH_SORT_BY);
//        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*/*", MOVIE_WITH_SORT_BY_ID);
//        matcher.addURI(authority, MovieContract.PATH_TRAILERS, TRAILERS);
//        matcher.addURI(authority, MovieContract.PATH_TRAILERS + "/*", TRAILER_WITH_MOVIE_ID);
//        matcher.addURI(authority, MovieContract.PATH_REVIEWS, REVIEWS);
//        matcher.addURI(authority, MovieContract.PATH_REVIEWS + "/*", REVIEW_WITH_MOVIE_ID);
        return matcher;
    }


    private static final SQLiteQueryBuilder sNewsArticleForNewsChannelByQueryBuilder;
    private static final SQLiteQueryBuilder sNewsChannelsForNewsCategoryQueryBuilder;
    static {
        sNewsArticleForNewsChannelByQueryBuilder = new SQLiteQueryBuilder();
        sNewsChannelsForNewsCategoryQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sNewsArticleForNewsChannelByQueryBuilder.setTables(
                NewsContract.NewsChannelEntry.TABLE_NAME + " INNER JOIN " +
                        NewsContract.ArticleEntry.TABLE_NAME +
                        " ON " + NewsContract.NewsChannelEntry.TABLE_NAME +
                        "." + NewsContract.NewsChannelEntry.COLUMN_NEWSCHANNEL_SOURCE_ID +
                        " = " + NewsContract.ArticleEntry.TABLE_NAME +
                        "." + NewsContract.ArticleEntry.COLUMN_ARTICLE_SOURCE_CHANNEL_ID);



        sNewsChannelsForNewsCategoryQueryBuilder.setTables(
                NewsContract.NewsChannelEntry.TABLE_NAME + " INNER JOIN " +
                        NewsContract.CategoryEntry.TABLE_NAME +
                        " ON " + NewsContract.NewsChannelEntry.TABLE_NAME +
                        "." + NewsContract.NewsChannelEntry.COLUMN_NEWSCHANNEL_SOURCE_ID +
                        " = " + NewsContract.CategoryEntry.TABLE_NAME +
                        "." + NewsContract.CategoryEntry.COLUMN_NEWSCHANNEL_KEY);
    }





    @Override
    public boolean onCreate() {
        mOpenHelper = new NewsDebHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

            // "movie"
            case NEWSCHANNELS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        NewsContract.NewsChannelEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "sortby"
            case NEWSCHANNELS_WITH_CATEGORY: {
                retCursor = getNewsChannelsForNewsCategory(uri, projection, sortOrder);

                break;
            }

            case NEWSARTICLES_WITH_SOURCECHANNELS: {
                retCursor = getArticledForNewsChannel(uri, projection, sortOrder);
                break;
            }

            case MESSAGES: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        NewsContract.MessageEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case NEWSCHANNELS: {

                long _id = db.insert(NewsContract.NewsChannelEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = NewsContract.NewsChannelEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;


            }
            case CATEGORY: {
                long _id = db.insert(NewsContract.CategoryEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = NewsContract.CategoryEntry.buildCategoryTypeUri(_id);
                } else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case NEWSARTICLES: {
                long _id = db.insert(NewsContract.ArticleEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = NewsContract.ArticleEntry.buildArticleUri(_id);
                } else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case MESSAGES: {

                long _id = db.insert(NewsContract.MessageEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = NewsContract.MessageEntry.buildMessageUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;


            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }


    private Cursor getArticledForNewsChannel(Uri uri, String[] projection, String sortOrder) {
        String sortBy = uri.getPathSegments().get(1);

        return sNewsArticleForNewsChannelByQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selectionMovieWithSortBy,
               new String[]{ sortBy},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getNewsChannelsForNewsCategory(Uri uri, String[] projection, String sortOrder) {
        String sortBy = uri.getPathSegments().get(1);

        return sNewsChannelsForNewsCategoryQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
    }


    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NEWSCHANNELS:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(NewsContract.NewsChannelEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }



                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            case CATEGORY: {
                db.beginTransaction();
                int Count = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(NewsContract.CategoryEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            Count++;
                        }
                    }
                    db.setTransactionSuccessful();


                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return Count;
            }
            case NEWSARTICLES: {
                db.beginTransaction();
                int Count = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(NewsContract.ArticleEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            Count++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return Count;
            }


            case MESSAGES:
                db.beginTransaction();
                int count = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(NewsContract.MessageEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            count++;
                        }
                    }



                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return count;


            default:
                return super.bulkInsert(uri, values);
        }
    }








}
