package com.example.v_ruchd.capstonestage2.helper;

import android.database.Cursor;
import android.net.Uri;

/**
 * Created by v-ruchd on 4/11/2017.
 */

public interface AsyncQueryHandlerListener {
    public void onInsertComplete(int token, Object cookie, Uri uri);
    public void onDeleteComplete(int token, Object cookie, int result);
    public void onQueryComplete(int token, Object cookie, Cursor cursor);
    public void onUpdateComplete(int token, Object cookie, int result);
}
