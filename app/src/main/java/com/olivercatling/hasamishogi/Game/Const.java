package com.olivercatling.hasamishogi.Game;

import com.olivercatling.hasamishogi.Database.Player;
import com.olivercatling.hasamishogi.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Oliver on 16/04/2018.
 */

public class Const {
    public static final int BLANK = 0;
    public static final int WHITE = 1;
    public static final int BLACK = 2;

    public static final int size9 = 0;
    public static final int size18 = 1;

    public static final String SETTINGS_KEY_REF = "SettingsKey";
    public static final int SETTINGS_REQUEST = 1;

    public static final String GAME_KEY_REF = "GameKey";
    public static final int GAME_KEY_REQUEST = 2;

    public static final Player GUEST =
            new Player("GUEST", "", 0, 0 , 0);

    public static final int[] STANDARD_GRID_ICON_DIRECTORIES = { R.drawable.board_blank_24dp,
            R.drawable.board_white_24dp, R.drawable.board_black_24dp};

}
