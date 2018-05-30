package com.olivercatling.hasamishogi.Database;

import android.provider.BaseColumns;

/**
 * Created by Oliver on 23/04/2018.
 */

public class PlayerContract implements BaseColumns{
    public static final String TABLE_NAME_PLAYERS = "PLAYERS";
    public static final String COLUMN_NAME_USERNAME = "USERNAME";
    public static final String COLUMN_NAME_BIO = "BIO";
    public static final String COLUMN_NAME_PLAYED = "PLAYED";
    public static final String COLUMN_NAME_WON = "WON";
    public static final String COLUMN_NAME_REF = "PLAYER_REF";


    public static final int COLUMN_INDEX_ID = 0;
    public static final int COLUMN_INDEX_USERNAME = 1;
    public static final int COLUMN_INDEX_BIO = 2;
    public static final int COLUMN_INDEX_PLAYED = 3;
    public static final int COLUMN_INDEX_WON = 4;
    public static final int COOLUMN_INDEX_REF = 5;
}
