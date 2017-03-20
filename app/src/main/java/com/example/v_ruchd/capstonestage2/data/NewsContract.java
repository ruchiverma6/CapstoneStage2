package com.example.v_ruchd.capstonestage2.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ruchi on 19/3/17.
 */

public class NewsContract {

    public static final String CONTENT_AUTHORITY = "com.example.v_ruchd.capstonestage2";
    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_NEWSCHANNEL = "newschannel";
    public static final String PATH_CATEGORY = "category";

    public static final String PATH_NEWSARTICLES = "newsarticles";


    public static final class NewsChannelEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_NEWSCHANNEL).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWSCHANNEL;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWSCHANNEL;
        // Table name
        public static final String TABLE_NAME = "newschannel";

        public static final String COLUMN_NEWSCHANNEL_ID = "newschannelid";

        public static final String COLUMN_NEWSCHANNEL_SOURCE_ID = "newschannelsourceid";

        public static final String COLUMN_NEWSCHANNEL_NAME = "newschannelname";

        public static final String COLUMN_NEWSCHANNEL_DESCRIPTION = "newschanneldescription";

        public static final String COLUMN_NEWSCHANNEL_URL = "newschannelurl";
        public static final String COLUMN_NEWSCHANNEL_URL_TO_LOGOS = "urlsToLogos";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        /*
            Student: This is the buildWeatherLocation function you filled in.
         */
        public static Uri buildNewsChannelWithCategory(String categoryType) {
            return CONTENT_URI.buildUpon().appendPath(categoryType).build();
        }

        public static Uri buildMovieWithSortByAndId(String sortBy,String movieID) {
            return CONTENT_URI.buildUpon().appendPath(sortBy).appendPath(movieID).build();
        }

        public static Uri buildMovieWithMovieId(String movieID) {
            return CONTENT_URI.buildUpon().appendPath(movieID).build();
        }
    }


    public static final class CategoryEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORY).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORY;
        // Table name
        public static final String TABLE_NAME = "newscategory";
        public static final String COLUMN_CATEGORY_TYPE = "categorytype";
        public static final String COLUMN_NEWSCHANNEL_KEY="newschannelid";

        public static String selection=COLUMN_NEWSCHANNEL_KEY + " =? AND " + COLUMN_CATEGORY_TYPE + " =? ";

        public static Uri buildSortByUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);

        }

        public static Uri buildCategoryTypeUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


    public static final class ArticleEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_NEWSARTICLES).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWSARTICLES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWSARTICLES;
        // Table name
        public static final String TABLE_NAME = "newsarticles";
        public static final String COLUMN_ARTICLE_SOURCE_CHANNEL_ID="id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_URL_TO_IMAGE = "urlToImage";
        public static final String COLUMN_PUBLISHEDAT = "publishedAt";
        public static final String COLUMN_NEWSCHANNEL_ID="newschannelid";

        public static String selection=COLUMN_NEWSCHANNEL_ID + " =? AND " + COLUMN_ARTICLE_SOURCE_CHANNEL_ID + " =? ";

        public static Uri buildNewsArticleWithChannel(String newsChannel) {
            return CONTENT_URI.buildUpon().appendPath(newsChannel).build();
        }



        public static Uri buildArticleUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
