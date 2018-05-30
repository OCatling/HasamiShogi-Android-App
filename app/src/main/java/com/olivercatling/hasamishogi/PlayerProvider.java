package com.olivercatling.hasamishogi;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.olivercatling.hasamishogi.Database.HasamiShogiDBHelper;
import com.olivercatling.hasamishogi.Database.Player;
import com.olivercatling.hasamishogi.Database.PlayerContract;

/**
 * Created by Oliver on 23/04/2018.
 */

public class PlayerProvider extends ContentProvider {
    public static final String AUTHORITY = "com.olivercatling.hasamishogi.PlayerProvider";
    public static final String BASE_PATH = PlayerContract.TABLE_NAME_PLAYERS;
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    private static final int PLAYERS = 0;
    private static final int PLAYERS_ID = 1;

    private HasamiShogiDBHelper helper;
    private UriMatcher urimatcher;

    public PlayerProvider(){}

    @Override
    public boolean onCreate() {
        helper = new HasamiShogiDBHelper(getContext());
        urimatcher = new UriMatcher(UriMatcher.NO_MATCH);
        urimatcher.addURI(AUTHORITY, BASE_PATH, PLAYERS);
        urimatcher.addURI(AUTHORITY, BASE_PATH + "/", PLAYERS_ID);
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values){
        SQLiteDatabase db = helper.getWritableDatabase();
        int uriType = urimatcher.match(uri);
        Uri resultUri = null;
        if(uriType == PLAYERS){
            long rowID = db.insert(PlayerContract.TABLE_NAME_PLAYERS, null, values);
            resultUri = ContentUris.withAppendedId(uri, rowID);
            getContext().getContentResolver().notifyChange(resultUri, null);
        }
        else throw new IllegalArgumentException("Unknown URI: " + uri);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs){
        SQLiteDatabase db = helper.getWritableDatabase();
        int rowsDeleted = 0;
        int uriType = urimatcher.match(uri);
        switch(uriType){
            case PLAYERS:
                rowsDeleted = db.delete(PlayerContract.TABLE_NAME_PLAYERS, selection, selectionArgs);
                break;
            case PLAYERS_ID:
                String newSelection = appendToSelection(uri, selection);
                rowsDeleted = db.delete(PlayerContract.TABLE_NAME_PLAYERS, newSelection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unrecognised uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(PlayerContract.TABLE_NAME_PLAYERS);
        int uriType = urimatcher.match(uri);
        switch(uriType){
            case PLAYERS: break;
            case PLAYERS_ID:
                builder.appendWhere(PlayerContract._ID + " = " + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unrecognised URI");
        }
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        SQLiteDatabase db = helper.getWritableDatabase();
        int rowsUpdated = 0;
        int uriType = urimatcher.match(uri);
        switch(uriType){
            case PLAYERS:
                rowsUpdated = db.update(PlayerContract.TABLE_NAME_PLAYERS, values, selection, selectionArgs);
                break;
            case PLAYERS_ID:
                String newSelection = appendToSelection(uri, selection);
                rowsUpdated = db.update(PlayerContract.TABLE_NAME_PLAYERS, values, newSelection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unrecognised uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    @Override
    public String getType(Uri uri){
        switch(urimatcher.match(uri)){
            case PLAYERS: return "vnd.android.cursor.dir/vnd.olivercatling.hasamishogi.PlayerProvider";
            case PLAYERS_ID: return "vnd.android.cursor.item/vnd.olivercatling.hasamishogi.PlayerProvides";
            default: throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    private String appendToSelection(Uri uri, String selection) {
        String id = uri.getLastPathSegment();
        StringBuilder newSelection = new StringBuilder(PlayerContract._ID + "=" + id);

        if(selection != null && !selection.isEmpty())
            newSelection.append(" AND " + selection);
        return newSelection.toString();
    }
}
