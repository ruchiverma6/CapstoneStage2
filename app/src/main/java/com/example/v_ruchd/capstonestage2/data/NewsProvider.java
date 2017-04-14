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
    static final int NEWSARTICLES_WITH_SOURCECHANNELS = 104;
    static final int MESSAGES = 105;
    static final int MESSAGESCATEGORYINPUTSELECTION = 106;
    static final int MESSAGESTOTALLENGTH = 107;
    static final int MESSAGESTOTALLENGTHPERUSER = 108;
    private static final int CATEGORYINPUTSELECTIONMESSAGEID = 109;
    private static final int MESSAGEWITHMESSAGEID = 109;
    private NewsDebHelper mOpenHelper;


    private static final String selectionNewsArticlesWithourceChannel = NewsContract.ArticleEntry.TABLE_NAME + "." + NewsContract.ArticleEntry.COLUMN_ARTICLE_SOURCE_CHANNEL_ID + " = ? ";
    private static final String selectionTotalLengthPerUser = NewsContract.TotalMessageLengthEntry.TABLE_NAME + "." + NewsContract.TotalMessageLengthEntry.COLUMN_MESSAGE_FROM + " = ? ";
    private static final String selectionCategoryForMessage = NewsContract.MessageCategorySelectionEntry.TABLE_NAME + "." + NewsContract.MessageCategorySelectionEntry.COLUMN_MESSAGE_ID + " = ? ";
    private static final String selectionCategoryForChannels = NewsContract.CategoryEntry.TABLE_NAME + "." + NewsContract.CategoryEntry.COLUMN_CATEGORY_TYPE + " = ? ";
    private static final String selectionIdForMessage = NewsContract.MessageEntry.TABLE_NAME + "." + NewsContract.MessageEntry.COLUMN_MESSAGE_ID + " = ? ";

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = NewsContract.CONTENT_AUTHORITY;


        matcher.addURI(authority, NewsContract.PATH_NEWSCHANNEL, NEWSCHANNELS);
        matcher.addURI(authority, NewsContract.PATH_CATEGORY, CATEGORY);
        matcher.addURI(authority, NewsContract.PATH_NEWSCHANNEL + "/*", NEWSCHANNELS_WITH_CATEGORY);


        matcher.addURI(authority, NewsContract.PATH_NEWSARTICLES, NEWSARTICLES);
        matcher.addURI(authority, NewsContract.PATH_NEWSARTICLES + "/*", NEWSARTICLES_WITH_SOURCECHANNELS);
        matcher.addURI(authority, NewsContract.PATH_MESSAGES, MESSAGES);
        matcher.addURI(authority, NewsContract.PATH_CATEGORY_SELECTION_TYPE, MESSAGESCATEGORYINPUTSELECTION);
        matcher.addURI(authority, NewsContract.PATH_MESSAGESLENGTH, MESSAGESTOTALLENGTH);
        matcher.addURI(authority, NewsContract.PATH_MESSAGESLENGTH + "/*", MESSAGESTOTALLENGTHPERUSER);
        matcher.addURI(authority, NewsContract.PATH_CATEGORY_SELECTION_TYPE + "/*", CATEGORYINPUTSELECTIONMESSAGEID);
        matcher.addURI(authority, NewsContract.PATH_MESSAGES + "/*", MESSAGEWITHMESSAGEID);


        return matcher;
    }


    private static final SQLiteQueryBuilder sNewsArticleForNewsChannelByQueryBuilder;
    private static final SQLiteQueryBuilder sNewsChannelsForNewsCategoryQueryBuilder;

    static {
        sNewsArticleForNewsChannelByQueryBuilder = new SQLiteQueryBuilder();
        sNewsChannelsForNewsCategoryQueryBuilder = new SQLiteQueryBuilder();


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

        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

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

            case CATEGORY: {

                retCursor = mOpenHelper.getReadableDatabase().query(true,
                        NewsContract.CategoryEntry.TABLE_NAME,
                        new String[]{NewsContract.CategoryEntry.COLUMN_CATEGORY_TYPE},
                        selection,
                        selectionArgs,
                        null,
                        null, null, null
                );
                break;
            }

            case NEWSCHANNELS_WITH_CATEGORY: {
                retCursor = getNewsChannelsForNewsCategory(uri, projection, sortOrder);

                break;
            }

            case NEWSARTICLES_WITH_SOURCECHANNELS: {

                String sortBy = uri.getPathSegments().get(1);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        NewsContract.ArticleEntry.TABLE_NAME,
                        projection,
                        selectionNewsArticlesWithourceChannel,
                        new String[]{sortBy},
                        null,
                        null,
                        sortOrder
                );


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


            case MESSAGESCATEGORYINPUTSELECTION: {
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


            case MESSAGESTOTALLENGTHPERUSER: {
                String sortBy = uri.getPathSegments().get(1);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        NewsContract.TotalMessageLengthEntry.TABLE_NAME,
                        projection,
                        selectionTotalLengthPerUser,
                        new String[]{sortBy},
                        null,
                        null,
                        sortOrder
                );
                break;
            }


            case CATEGORYINPUTSELECTIONMESSAGEID: {
                String sortBy = uri.getPathSegments().get(1);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        NewsContract.MessageCategorySelectionEntry.TABLE_NAME,
                        new String[]{NewsContract.MessageCategorySelectionEntry.COLUMN_MESSAGE_SELECTED_CATEGORY_TYPE},
                        selectionCategoryForMessage,
                        new String[]{sortBy},
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
                    returnUri = NewsContract.NewsChannelEntry.buildNewsChannelUri(_id);
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

            case MESSAGESCATEGORYINPUTSELECTION: {
                long _id = db.insert(NewsContract.MessageCategorySelectionEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = NewsContract.MessageEntry.buildMessageUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }


            case MESSAGESTOTALLENGTH: {
                long _id = db.insert(NewsContract.TotalMessageLengthEntry.TABLE_NAME, null, values);
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
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;
        switch (match) {
            case MESSAGEWITHMESSAGEID:
                String messageId = uri.getPathSegments().get(1);
                long _id = db.update(NewsContract.MessageEntry.TABLE_NAME, values,selectionIdForMessage,new String[]{messageId});
                if (_id > 0) {
                    returnCount++;
                }
                else
                    throw new android.database.SQLException("Failed to update row into " + uri);
                 break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
    }



    private Cursor getNewsChannelsForNewsCategory(Uri uri, String[] projection, String sortOrder) {
        String sortBy = uri.getPathSegments().get(1);

        return sNewsChannelsForNewsCategoryQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selectionCategoryForChannels,
                new String[]{sortBy},
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

            case MESSAGESCATEGORYINPUTSELECTION:
                db.beginTransaction();
                int countMessageCategorySelection = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(NewsContract.MessageCategorySelectionEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            countMessageCategorySelection++;
                        }
                    }


                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return countMessageCategorySelection;


            case MESSAGESTOTALLENGTH:
                db.beginTransaction();
                int totalMessageLength = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(NewsContract.TotalMessageLengthEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            totalMessageLength++;
                        }
                    }


                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return totalMessageLength;


            default:
                return super.bulkInsert(uri, values);
        }
    }


}
