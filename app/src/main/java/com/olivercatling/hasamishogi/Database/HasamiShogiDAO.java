package com.olivercatling.hasamishogi.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.olivercatling.hasamishogi.Game.Board;
import com.olivercatling.hasamishogi.Game.Const;
import com.olivercatling.hasamishogi.Game.Game;

import java.util.List;

/**
 * Created by Oliver on 23/04/2018.
 */

public class HasamiShogiDAO {
    private HasamiShogiDBHelper dbHelper;
    private SQLiteDatabase db;

    public HasamiShogiDAO(Context context){
        dbHelper = new HasamiShogiDBHelper(context);
    }

    public void open(){
        db = dbHelper.getWritableDatabase();
    }

    public long addPlayer(Player player){
        ContentValues values = new ContentValues();
        values.put(PlayerContract.COLUMN_NAME_USERNAME, player.getUsername());
        values.put(PlayerContract.COLUMN_NAME_BIO, player.getBio());
        values.put(PlayerContract.COLUMN_NAME_PLAYED, player.getGamesPlayed());
        values.put(PlayerContract.COLUMN_NAME_WON, player.getGamesWon());

        long rowID = db.insert(PlayerContract.TABLE_NAME_PLAYERS, null, values);
        return rowID;
    }

    public long addBoard(Board board){
        ContentValues values = new ContentValues();
        values.put(BoardContract.COLUMN_NAME_R1, board.getRowDetails(0));
        values.put(BoardContract.COLUMN_NAME_R2, board.getRowDetails(1));
        values.put(BoardContract.COLUMN_NAME_R3, board.getRowDetails(2));
        values.put(BoardContract.COLUMN_NAME_R4, board.getRowDetails(3));
        values.put(BoardContract.COLUMN_NAME_R5, board.getRowDetails(4));
        values.put(BoardContract.COLUMN_NAME_R6, board.getRowDetails(5));
        values.put(BoardContract.COLUMN_NAME_R7, board.getRowDetails(6));
        values.put(BoardContract.COLUMN_NAME_R8, board.getRowDetails(7));
        values.put(BoardContract.COLUMN_NAME_R9, board.getRowDetails(8));

        long rowID = db.insert(BoardContract.TABLE_NAME_BOARD, null, values);
        return rowID;
    }

    public long addSettings(Settings settings){
        ContentValues values = new ContentValues();
        values.put(SettingsContract.COLUMN_NAME_SETTINGS_ID, settings.getReference());
        values.put(SettingsContract.COLUMN_NAME_SIZE, settings.isDaiHasamiShogi());
        values.put(SettingsContract.COLUMN_NAME_DAI_WIN_RULE, settings.isDaiHasamiShogiFiveWinRule());
        values.put(SettingsContract.COLUMN_NAME_AMOUNT, settings.getWinNumber());

        long rowID = db.insert(SettingsContract.TABLE_NAME_SETTINGS, null, values);
        return rowID;
    }

    public long addGame(Game game){
        ContentValues values = new ContentValues();

        values.put(GameContract.COLUMN_NAME_BOARD_ID, game.getBoard().getBoardID());
        values.put(GameContract.COLUMN_NAME_SETTINGS_ID, game.getSettings().getReference());
        values.put(GameContract.COLUMN_NAME_PLAYER_WHITE, game.getPlayer1().getPlayer_reference());
        values.put(GameContract.COLUMN_NAME_PLAYER_BLACK, game.getPlayer2().getPlayer_reference());
        values.put(GameContract.COLUMN_NAME_MOVE, game.getMoveNumber());
        values.put(GameContract.COLUMN_NAME_WHITE_SCORE, game.getWhiteScore());
        values.put(GameContract.COLUMN_NAME_BLACK_SCORE, game.getBlackScore());

        long rowID = db.insert(GameContract.TABLE_NAME_GAME, null, values);
        return rowID;
    }

    public List<Player> getPlayers(List<Player> players){
        Cursor cursor = db.query(PlayerContract.TABLE_NAME_PLAYERS,
                null,
                null,
                null,
                null,
                null,
                null );

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String username = cursor.getString(PlayerContract.COLUMN_INDEX_USERNAME);
            String bio = cursor.getString(PlayerContract.COLUMN_INDEX_BIO);
            int played = cursor.getInt(PlayerContract.COLUMN_INDEX_PLAYED);
            int won = cursor.getInt(PlayerContract.COLUMN_INDEX_WON);
            int ref = cursor.getInt(PlayerContract.COLUMN_INDEX_ID);

            players.add(new Player(username, bio, played, won, ref));
            cursor.moveToNext();
        }
        cursor.close();
        return players;
    }

    public List<Board> getBoards(List<Board> boards){
        Cursor cursor = db.query(BoardContract.TABLE_NAME_BOARD,
                null,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Board board = new Board();
            board.setRowFromString(cursor.getString(BoardContract.COLUMN_INDEX_R1), 1);
            board.setRowFromString(cursor.getString(BoardContract.COLUMN_INDEX_R2), 2);
            board.setRowFromString(cursor.getString(BoardContract.COLUMN_INDEX_R3), 3);
            board.setRowFromString(cursor.getString(BoardContract.COLUMN_INDEX_R4), 4);
            board.setRowFromString(cursor.getString(BoardContract.COLUMN_INDEX_R5), 5);
            board.setRowFromString(cursor.getString(BoardContract.COLUMN_INDEX_R6), 6);
            board.setRowFromString(cursor.getString(BoardContract.COLUMN_INDEX_R7), 7);
            board.setRowFromString(cursor.getString(BoardContract.COLUMN_INDEX_R8), 8);
            board.setRowFromString(cursor.getString(BoardContract.COLUMN_INDEX_R9), 9);
            boards.add(board);
            cursor.moveToNext();
        }
        cursor.close();
        return boards;
    }

    public List<Settings> getSettings(List<Settings> settings){
        Cursor cursor = db.query(SettingsContract.TABLE_NAME_SETTINGS,
                null,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            boolean type, win;
            int amount, ref;
            if (cursor.getInt(SettingsContract.COLUMN_INDEX_SIZE) == 0) type = false;
            else type = true;

            if (cursor.getInt(SettingsContract.COLUMN_INDEX_DAI_WIN_RULE) == 0) win = false;
            else win = true;

            amount = cursor.getInt(SettingsContract.COLUMN_INDEX_AMOUNT);
            ref = cursor.getInt(SettingsContract.COLUMN_INDEX_SETTINGS_ID);

            settings.add(new Settings(type, win, amount, ref));
        }
        cursor.close();
        return settings;
    }

    public List<Game> getGames(List<Game> games){
        Cursor cursor = db.query(GameContract.TABLE_NAME_GAME,
                null,
                null,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            games.add(getGameFromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return games;
    }

    public Player getPlayerFromRef(int ref){
        Cursor cursor = db.query(PlayerContract.TABLE_NAME_PLAYERS,
                null,
                PlayerContract._ID + " = " + String.valueOf(ref),
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        Player p = null;
        while(!cursor.isAfterLast()){
            String username, bio;
            int played, won, id;
            username = cursor.getString(PlayerContract.COLUMN_INDEX_USERNAME);
            bio = cursor.getString(PlayerContract.COLUMN_INDEX_BIO);
            won = cursor.getInt(PlayerContract.COLUMN_INDEX_WON);
            played = cursor.getInt(PlayerContract.COLUMN_INDEX_PLAYED);
            p = new Player(username, bio, played, won, ref);
            cursor.moveToNext();
        }
        cursor.close();
        return p;
    }

    public Board getBoardFromRef(int ref){

        Cursor cursor = db.query(BoardContract.TABLE_NAME_BOARD,
                null,
                BoardContract.COLUMN_NAME_BOARD_ID + " = " + String.valueOf(ref),
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        Board b = new Board();
        while(!cursor.isAfterLast()){
            System.out.println("-----> RUNNING BOI");
            b.setBoardID(cursor.getInt(BoardContract.COLUMN_INDEX_BOARD_ID));
            b.setRowFromString(cursor.getString(BoardContract.COLUMN_INDEX_R1), 0);
            b.setRowFromString(cursor.getString(BoardContract.COLUMN_INDEX_R2), 1);
            b.setRowFromString(cursor.getString(BoardContract.COLUMN_INDEX_R3), 2);
            b.setRowFromString(cursor.getString(BoardContract.COLUMN_INDEX_R4), 3);
            b.setRowFromString(cursor.getString(BoardContract.COLUMN_INDEX_R5), 4);
            b.setRowFromString(cursor.getString(BoardContract.COLUMN_INDEX_R6), 5);
            b.setRowFromString(cursor.getString(BoardContract.COLUMN_INDEX_R7), 6);
            b.setRowFromString(cursor.getString(BoardContract.COLUMN_INDEX_R8), 7);
            b.setRowFromString(cursor.getString(BoardContract.COLUMN_INDEX_R9), 8);
            cursor.moveToNext();
        }
        cursor.close();
        return b;
    }

    public Settings getSettingsFromRef(int ref) {
            Cursor cursor = db.query(SettingsContract.TABLE_NAME_SETTINGS,
                    null,
                    SettingsContract.COLUMN_NAME_SETTINGS_ID + " = " + String.valueOf(ref),
                    null,
                    null,
                    null,
                    null);

            cursor.moveToFirst();
            Settings s = null;
            while (!cursor.isAfterLast()) {
                boolean type, win;
                int amount;
                if (cursor.getInt(SettingsContract.COLUMN_INDEX_SIZE) == 0) type = false;
                else type = true;

                if (cursor.getInt(SettingsContract.COLUMN_INDEX_DAI_WIN_RULE) == 0) win = false;
                else win = true;

                amount = cursor.getInt(SettingsContract.COLUMN_INDEX_AMOUNT);

                s = new Settings(type, win, amount, ref);
                cursor.moveToNext();
            }
            cursor.close();
        return s;
    }

    public Game getGameFromRef(int ref){
        Cursor cursor = db.query(GameContract.TABLE_NAME_GAME,
                null,
                GameContract.COLUMN_NAME_GAME_ID + " = " + String.valueOf(ref),
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        Game game = null;
        while(!cursor.isAfterLast()){
            game = getGameFromCursor(cursor);
            cursor.moveToNext();
        }
        cursor.close();
        return game;
    }

    public Board getBoardFromRowID(long rowID){
        final Cursor cursor = db.query(BoardContract.TABLE_NAME_BOARD,
                null,
                "ROWID = " + rowID,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        Board b = new Board();
        while(!cursor.isAfterLast()){
            System.out.println("-----> RUNNING BOI");
            b.setBoardID(cursor.getInt(BoardContract.COLUMN_INDEX_BOARD_ID));
            b.setRowFromString(cursor.getString(BoardContract.COLUMN_INDEX_R1), 0);
            b.setRowFromString(cursor.getString(BoardContract.COLUMN_INDEX_R2), 1);
            b.setRowFromString(cursor.getString(BoardContract.COLUMN_INDEX_R3), 2);
            b.setRowFromString(cursor.getString(BoardContract.COLUMN_INDEX_R4), 3);
            b.setRowFromString(cursor.getString(BoardContract.COLUMN_INDEX_R5), 4);
            b.setRowFromString(cursor.getString(BoardContract.COLUMN_INDEX_R6), 5);
            b.setRowFromString(cursor.getString(BoardContract.COLUMN_INDEX_R7), 6);
            b.setRowFromString(cursor.getString(BoardContract.COLUMN_INDEX_R8), 7);
            b.setRowFromString(cursor.getString(BoardContract.COLUMN_INDEX_R9), 8);
            cursor.moveToNext();
        }
        cursor.close();
        return b;
    }

    public Game getGameFromRowID(long rowID){
        final Cursor cursor = db.query(GameContract.TABLE_NAME_GAME,
                null,
                "ROWID = " + rowID,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();
        Game game = null;
        while(!cursor.isAfterLast()){
            game = getGameFromCursor(cursor);
            cursor.moveToNext();
        }
        cursor.close();
        System.out.println("THIS HAS FINISHED RUNNING");
        return game;
    }

    private Game getGameFromCursor(final Cursor cursor){
        final Board[] b = new Board[1];
        final Settings[] s = new Settings[1];
        final Player[] p = new Player[2];
        final int[] move = new int[2];
        final int[] scores = new int[2];
        Thread[] threads = new Thread[6];

        threads[0] = new Thread(new Runnable() {
            @Override
            public void run() {
                b[0] = getBoardFromRef(cursor.getInt(GameContract.COLUMN_INDEX_BOARD_ID));
            }
        });
        threads[1] = new Thread(new Runnable() {
            @Override
            public void run() {
                s[0] = getSettingsFromRef(cursor.getInt(GameContract.COLUMN_INDEX_SETTINGS_ID));
            }
        });
        threads[2] = new Thread(new Runnable() {
            @Override
            public void run() {
                p[0] = getPlayerFromRef(cursor.getInt(GameContract.COLUMN_INDEX_PLAYER1));
            }
        });
        threads[3] = new Thread(new Runnable() {
            @Override
            public void run() {
                p[1] = getPlayerFromRef(cursor.getInt(GameContract.COLUMN_INDEX_PLAYER2));
            }
        });
        threads[4] = new Thread(new Runnable() {
            @Override
            public void run() {
                move[0] = cursor.getInt(GameContract.COLUMN_INDEX_MOVE);
                move[1] = cursor.getInt(GameContract.COLUMN_INDEX_GAME_ID);

            }
        });
        threads[5] = new Thread(new Runnable() {
            @Override
            public void run() {
                scores[0] = cursor.getInt(GameContract.COLUMN_INDEX_WHITE_SCORE);
                scores[1] = cursor.getInt(GameContract.COLUMN_INDEX_BLACK_SCORE);
            }
        });


        // Start Threads
        for(Thread thread: threads) thread.start();

        for(int i = 0; i < threads.length; i++)
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        return new Game(b[0], s[0], p[0], p[1], move[0], scores[0], scores[1], move[1]);
    }

    public void updateBoard(Board board){
        ContentValues values = new ContentValues();
        values.put(BoardContract.COLUMN_NAME_R1, board.getRowDetails(0));
        values.put(BoardContract.COLUMN_NAME_R2, board.getRowDetails(1));
        values.put(BoardContract.COLUMN_NAME_R3, board.getRowDetails(2));
        values.put(BoardContract.COLUMN_NAME_R4, board.getRowDetails(3));
        values.put(BoardContract.COLUMN_NAME_R5, board.getRowDetails(4));
        values.put(BoardContract.COLUMN_NAME_R6, board.getRowDetails(5));
        values.put(BoardContract.COLUMN_NAME_R7, board.getRowDetails(6));
        values.put(BoardContract.COLUMN_NAME_R8, board.getRowDetails(7));
        values.put(BoardContract.COLUMN_NAME_R9, board.getRowDetails(8));

        db.update(BoardContract.TABLE_NAME_BOARD, values,
                BoardContract.COLUMN_NAME_BOARD_ID + " = " + String.valueOf(board.getBoardID()),
                null);
    }

    public void updateSettings(Settings settings){
        ContentValues values = new ContentValues();
        values.put(SettingsContract.COLUMN_NAME_SIZE, settings.isDaiHasamiShogi());
        values.put(SettingsContract.COLUMN_NAME_DAI_WIN_RULE, settings.isDaiHasamiShogiFiveWinRule());
        values.put(SettingsContract.COLUMN_NAME_AMOUNT, settings.getWinNumber());

        db.update(SettingsContract.TABLE_NAME_SETTINGS,
                values,
                SettingsContract.COLUMN_NAME_SETTINGS_ID + " = " + String.valueOf(settings.getReference()) ,
                null);
    }

    public void updateGame(Game game){
        ContentValues values = new ContentValues();
        values.put(GameContract.COLUMN_NAME_BOARD_ID, game.getBoard().getBoardID());
        values.put(GameContract.COLUMN_NAME_SETTINGS_ID, game.getSettings().getReference());
        values.put(GameContract.COLUMN_NAME_PLAYER_WHITE, game.getPlayer1().getPlayer_reference());
        values.put(GameContract.COLUMN_NAME_PLAYER_BLACK, game.getPlayer2().getPlayer_reference());
        values.put(GameContract.COLUMN_NAME_MOVE, game.getMoveNumber());
        values.put(GameContract.COLUMN_NAME_WHITE_SCORE, game.getWhiteScore());
        values.put(GameContract.COLUMN_NAME_BLACK_SCORE, game.getBlackScore());

        db.update(GameContract.TABLE_NAME_GAME,
                values,
                GameContract.COLUMN_NAME_GAME_ID + " = " + String.valueOf(game.getReference()),
                null);
    }

    public void removeGame(Game game){
        db.delete(GameContract.TABLE_NAME_GAME,
                GameContract.COLUMN_NAME_GAME_ID + " = " + String.valueOf(game.getReference()),
                null);
    }

}
