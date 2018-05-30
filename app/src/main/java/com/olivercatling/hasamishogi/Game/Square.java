package com.olivercatling.hasamishogi.Game;


/**
 * Created by Oliver on 16/04/2018.
 */

public class Square {
    private int piece;
    private final int row;
    private final int col;

    public Square(int row, int col){
        this.piece = Const.BLANK;
        this.row = row;
        this.col = col;
    }

    public int getPiece(){
        return this.piece;
    }

    public void setPiece(int piece){
        this.piece = piece;
    }

    public int getRow(){ return this.row; }

    public int getCol(){ return this.col; }
}
