package com.olivercatling.hasamishogi.Game;

import com.olivercatling.hasamishogi.Database.Player;
import com.olivercatling.hasamishogi.Database.Settings;

/**
 * Created by Oliver on 16/04/2018.
 */

public class Game {
    private Board board;
    private Settings settings;
    private Player player1;
    private Player player2;
    private int moveNumber, whiteScore, blackScore, reference;

    public Game(Board board, Settings settings, Player player1, Player player2){
        this.board = board;
        this.settings = settings;
        this.player1 = player1;
        this.player2 = player2;
        this.moveNumber = 1;
        this.whiteScore = 0;
        this.blackScore = 0;
        this.reference = 0;
    }

    public Game(Board board, Settings settings, Player player1, Player player2, int moveNumber,
                int whiteScore, int blackScore, int reference){
        this.board = board;
        this.settings = settings;
        this.player1 = player1;
        this.player2 = player2;
        this.moveNumber = moveNumber;
        this.whiteScore = whiteScore;
        this.blackScore = blackScore;
        this.reference = reference;
    }

    public Board getBoard(){ return this.board; }

    public void setBoard(Board board) { this.board = board;}

    public Settings getSettings(){ return this.settings; }

    public Player getPlayer1(){ return this.player1; }

    public Player getPlayer2(){ return this.player2; }

    public int getMoveNumber(){ return this.moveNumber; }

    public void setMoveNumber(int moveNumber){ this.moveNumber = moveNumber; }

    public int getWhiteScore(){ return this.whiteScore; }

    public void setWhiteScore(int whiteScore){ this.whiteScore = whiteScore; }

    public int getBlackScore(){ return this.blackScore; }

    public void setBlackScore(int blackScore){ this.whiteScore = blackScore; }

    public int getReference(){ return this.reference; }

    public void increaseWhiteScore(int pieces) { this.whiteScore += pieces; }

    public void increaseBlackScore(int pieces) { this.blackScore += pieces; }

    public void incrementMoveCount() { this.moveNumber++; }

    public boolean isWhiteWin(){
        System.out.println(this.whiteScore >= settings.getWinNumber());
        return this.whiteScore >= settings.getWinNumber(); }

    public boolean isBlackWin(){ return this.blackScore >= settings.getWinNumber(); }

    public boolean isDaiWin(Square start){
        return settings.isDaiHasamiShogi() && board.daiHasamiShogiWinCheck(start);
    }

    public boolean isMovementPossible(Square origin, Square destination) {
        boolean value = (board.isHorizontalMovement(origin, destination) && board.isHorizontalMovePossible(origin, destination) ||
                (board.isVerticalMovement(origin, destination) && board.isVerticalMovePossible(origin, destination)));
        System.out.println("isMovementPossible: " + value);
        return value;
    }
}
