package com.olivercatling.hasamishogi.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Oliver on 23/04/2018.
 */

public class HasamiShogiDBHelper extends SQLiteOpenHelper{

    /* ------------------------------------> SQL KEY VALUES <------------------------------------ */
    private static final String TEXT_TYPE = " TEXT ";
    private static final String INTEGER_TYPE = " INTEGER ";
    private static final String PRIMARY_KEY = " PRIMARY KEY ";
    private static final String COMMA_SEP = ", ";
    private static final String AUTOINCREMENT = " AUTOINCREMENT ";
    private static final String FOREIGN_KEY = " FOREIGN KEY ";
    private static final String REFERENCES = " REFERENCES ";

    /* --------------------------------> DELETE ENTRIES VALUES <--------------------------------- */
    private static final String DELETE_PLAYERS_ENTRIES = "DROP TABLE IF EXISTS " + PlayerContract.TABLE_NAME_PLAYERS;
    private static final String DELETE_SETTINGS_ENTRIES = "DROP TABLE IF EXISTS " + SettingsContract.TABLE_NAME_SETTINGS;
    private static final String DELETE_BOARD_ENTRIES = "DROP TABLE IF EXISTS " + BoardContract.TABLE_NAME_BOARD;
    private static final String DELETE_GAME_ENTRIES = "DROP TABLE IF EXISTS " + GameContract.TABLE_NAME_GAME;

    /* --------------------------------> CREATE ENTRIES VALUES <--------------------------------- */
    private static final String CREATE_PLAYERS_ENTRIES = "CREATE TABLE " + PlayerContract.TABLE_NAME_PLAYERS + " ( "
            + PlayerContract._ID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP
            + PlayerContract.COLUMN_NAME_USERNAME + TEXT_TYPE + " UNIQUE " + COMMA_SEP
            + PlayerContract.COLUMN_NAME_BIO + TEXT_TYPE + COMMA_SEP
            + PlayerContract.COLUMN_NAME_PLAYED + INTEGER_TYPE + COMMA_SEP
            + PlayerContract.COLUMN_NAME_WON + INTEGER_TYPE + ")";

    private static final String CREATE_SETTINGS_TABLE = "CREATE TABLE " + SettingsContract.TABLE_NAME_SETTINGS + " ( "
            + SettingsContract.COLUMN_NAME_SETTINGS_ID + INTEGER_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_SEP
            + SettingsContract.COLUMN_NAME_SIZE +  INTEGER_TYPE + COMMA_SEP
            + SettingsContract.COLUMN_NAME_DAI_WIN_RULE + INTEGER_TYPE + COMMA_SEP
            + SettingsContract.COLUMN_NAME_AMOUNT + INTEGER_TYPE + ")";

    private static final String CREATE_BOARD_TABLE = "CREATE TABLE " + BoardContract.TABLE_NAME_BOARD + " ( "
            + BoardContract.COLUMN_NAME_BOARD_ID + INTEGER_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_SEP
            + BoardContract.COLUMN_NAME_R1 + TEXT_TYPE + COMMA_SEP
            + BoardContract.COLUMN_NAME_R2 + TEXT_TYPE + COMMA_SEP
            + BoardContract.COLUMN_NAME_R3 + TEXT_TYPE + COMMA_SEP
            + BoardContract.COLUMN_NAME_R4 + TEXT_TYPE + COMMA_SEP
            + BoardContract.COLUMN_NAME_R5 + TEXT_TYPE + COMMA_SEP
            + BoardContract.COLUMN_NAME_R6 + TEXT_TYPE + COMMA_SEP
            + BoardContract.COLUMN_NAME_R7 + TEXT_TYPE + COMMA_SEP
            + BoardContract.COLUMN_NAME_R8 + TEXT_TYPE + COMMA_SEP
            + BoardContract.COLUMN_NAME_R9 + TEXT_TYPE + " )";

    private static final String CREATE_GAME_TABLE = "CREATE TABLE " + GameContract.TABLE_NAME_GAME + " ( "
            + GameContract.COLUMN_NAME_GAME_ID + INTEGER_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_SEP
            + GameContract.COLUMN_NAME_BOARD_ID + INTEGER_TYPE + COMMA_SEP
            + GameContract.COLUMN_NAME_SETTINGS_ID + INTEGER_TYPE + COMMA_SEP
            + GameContract.COLUMN_NAME_PLAYER_WHITE + TEXT_TYPE + COMMA_SEP
            + GameContract.COLUMN_NAME_PLAYER_BLACK + TEXT_TYPE + COMMA_SEP
            + GameContract.COLUMN_NAME_MOVE + INTEGER_TYPE + COMMA_SEP
            + GameContract.COLUMN_NAME_WHITE_SCORE + INTEGER_TYPE + COMMA_SEP
            + GameContract.COLUMN_NAME_BLACK_SCORE + INTEGER_TYPE + COMMA_SEP
            + FOREIGN_KEY + "(" + GameContract.COLUMN_NAME_BOARD_ID + ")" + REFERENCES +
            BoardContract.TABLE_NAME_BOARD + "(" + BoardContract.COLUMN_NAME_BOARD_ID + ")" + COMMA_SEP
            + FOREIGN_KEY + "(" + GameContract.COLUMN_NAME_SETTINGS_ID + ")" + REFERENCES +
            SettingsContract.TABLE_NAME_SETTINGS + "(" + SettingsContract.COLUMN_NAME_SETTINGS_ID + ")" + COMMA_SEP
            + FOREIGN_KEY + "(" + GameContract.COLUMN_NAME_PLAYER_WHITE + ")" + REFERENCES +
            PlayerContract.TABLE_NAME_PLAYERS + "(" + PlayerContract._ID + ")" + COMMA_SEP
            + FOREIGN_KEY + "(" + GameContract.COLUMN_NAME_PLAYER_BLACK + ")" + REFERENCES +
            PlayerContract.TABLE_NAME_PLAYERS + "(" + PlayerContract._ID + "))";


    /* -----------------------------------> DATABASE VALUES <------------------------------------ */
    public static final String DATABASE_NAME = "HashamiShogi.db";
    public static final int DATABASE_VERSION = 5;

    /* -------------------------------------> CONSTRUCTOR <-------------------------------------- */
    public HasamiShogiDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_PLAYERS_ENTRIES);
        sqLiteDatabase.execSQL(CREATE_SETTINGS_TABLE);
        sqLiteDatabase.execSQL(CREATE_BOARD_TABLE);
        sqLiteDatabase.execSQL(CREATE_GAME_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DELETE_PLAYERS_ENTRIES);
        sqLiteDatabase.execSQL(DELETE_SETTINGS_ENTRIES);
        sqLiteDatabase.execSQL(DELETE_BOARD_ENTRIES);
        sqLiteDatabase.execSQL(DELETE_GAME_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int i, int i1){
        onUpgrade(sqLiteDatabase, i, i1);
    }
}
