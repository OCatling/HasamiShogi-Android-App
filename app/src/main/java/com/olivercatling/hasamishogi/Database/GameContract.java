package com.olivercatling.hasamishogi.Database;

/**
 * Created by Oliver on 26/04/2018.
 */

public class GameContract {
    public static final String TABLE_NAME_GAME = "GAME";
    public static final String COLUMN_NAME_GAME_ID = "GAME_ID";
    public static final String COLUMN_NAME_BOARD_ID = "BOARD_ID";
    public static final String COLUMN_NAME_SETTINGS_ID = "SETTINGS_ID";
    public static final String COLUMN_NAME_PLAYER_WHITE = "WHITE";
    public static final String COLUMN_NAME_PLAYER_BLACK = "BLACK";
    public static final String COLUMN_NAME_MOVE = "MOVE_NO";
    public static final String COLUMN_NAME_WHITE_SCORE = "WHITE_SCORE";
    public static final String COLUMN_NAME_BLACK_SCORE = "BLACK_SCORE";

    public static final int COLUMN_INDEX_GAME_ID = 0;
    public static final int COLUMN_INDEX_BOARD_ID = 1;
    public static final int COLUMN_INDEX_SETTINGS_ID = 2;
    public static final int COLUMN_INDEX_PLAYER1 = 3;
    public static final int COLUMN_INDEX_PLAYER2 = 4;
    public static final int COLUMN_INDEX_MOVE = 5;
    public static final int COLUMN_INDEX_WHITE_SCORE = 6;
    public static final int COLUMN_INDEX_BLACK_SCORE = 7;
}
