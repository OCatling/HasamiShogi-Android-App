package com.olivercatling.hasamishogi.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Oliver on 16/04/2018.
 */

public class Board {
    private Square[][] gameboard;
    private int boardID;

    /* [01] CONSTRUCTORS -------------------------------------------------------------------------*/
    // Initial Constructor Specifying amount of pieces
    public Board(int boardSize){
        this.gameboard = new Square[9][9];

        initBoard();
        init9Board();
        if(boardSize == Const.size18) init18Board();
    }

    // Constructor for when board has already been built with unknown number of pieces left
    public Board(){
        this.gameboard = new Square[9][9];
        initBoard();
    }

    /* [02] BOARD CREATION FUNCTIONS ------------------------------------------------------------*/
    private void initBoard(){
        Thread t1, t2, t3, t4, t5, t6, t7, t8, t9;
        t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                initByRow(0);
            }
        });
        t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                initByRow(1);
            }
        });
        t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                initByRow(2);
            }
        });
        t4 = new Thread(new Runnable() {
            @Override
            public void run() {
                initByRow(3);
            }
        });
        t5 = new Thread(new Runnable() {
            @Override
            public void run() {
                initByRow(4);
            }
        });
        t6 = new Thread(new Runnable() {
            @Override
            public void run() {
                initByRow(5);
            }
        });
        t7 = new Thread(new Runnable() {
            @Override
            public void run() {
                initByRow(6);
            }
        });
        t8 = new Thread(new Runnable() {
            @Override
            public void run(){
                initByRow(7);
            }
        });
        t9 = new Thread(new Runnable() {
            @Override
            public void run() {
                initByRow(8);
            }
        });
        Thread[] threads = {t1, t2, t3, t4, t5, t6, t7, t8, t9};
        for(Thread t : threads) t.start();
        for(int i = 0; i < threads.length; i++)
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

    }

    private void initByRow(int row){
        for(int col = 0; col < gameboard[row].length; col++)
            gameboard[row][col] = new Square(row, col);
    }


    /* Set Pieces */
    private void init9Board(){
        setRow(0, Const.WHITE);
        setRow(8, Const.BLACK);
    }

    private void init18Board(){
        setRow(1, Const.WHITE);
        setRow(7, Const.BLACK);

    }

    private void setRow(int row, int colour){
        for(Square square : gameboard[row]) square.setPiece(colour);
    }

    /* [03] ACCESSOR METHODS ---------------------------------------------------------------------*/
    public int getBoardID(){
        return this.boardID;
    }

    public Square[][] getGameboard(){
        return this.gameboard;
    }

    public String getRowDetails(int row) {
        StringBuilder sb = new StringBuilder();
        for(Square square : gameboard[row]) sb.append(square.getPiece());
        return sb.toString();
    }

    public Square getSquare(int row, int square){
        return gameboard[row][square];
    }
    public List<Square> getFlatBoard(){
        List<Square> flatBoard = new ArrayList<>();
        for(Square[] row : gameboard)
            flatBoard.addAll(Arrays.asList(row));
        return flatBoard;
    }

    /* [04] MUTATOR METHODS ----------------------------------------------------------------------*/
    public void setBoardID(int boardID){
        this.boardID = boardID;
    }

    /*-----> Parsing String To Row Data <-----*/
    public void setRowFromString(String rowInfo, int row){
        for(int col = 0; col < gameboard[row].length; col++)
            gameboard[row][col].setPiece(Character.getNumericValue(rowInfo.charAt(col)));
    }


    /*-----> For Testing <-----*/
    public void viewBoard(){
        for(int row = 0; row < gameboard.length; row++)
            System.out.println(getRowDetails(row));
    }

    /* [05] PIECE MOVEMENT -----------------------------------------------------------------------*/
    public void movePiece(Square origin, Square destination){
        gameboard[destination.getRow()][destination.getCol()].setPiece(origin.getPiece());
        gameboard[origin.getRow()][origin.getCol()].setPiece(Const.BLANK);
    }

    /* [06] MOVEMENT CHECKING --------------------------------------------------------------------*/
    // These Functions Are To Check Movement Around The Board Is A Valid Movement In Terms Of Both
    // Direction And Not Intercepting Any Pieces

    /* Movement Check Correct Direction Functions */
    public boolean isHorizontalMovement(Square origin, Square destination){
        return origin.getRow() == destination.getRow() && origin.getCol() != destination.getCol();
    }

    public boolean isVerticalMovement(Square origin, Square destination){
        return origin.getRow() != destination.getRow() && origin.getCol() == destination.getCol();
    }

    /* Movement Piece Intercept Functions */
    public boolean isHorizontalMovePossible(Square origin, Square destination){
        // First Make Sure They're On The Same Row, Otherwise Not A Horizontal Movement
        assert(origin.getRow() == destination.getRow()) : "Not Same Row";

        boolean check = true;
        int startCol, finishCol;

        if(origin.getCol() < destination.getCol()) {            // Movement Is From Right To Left
            startCol = origin.getCol() + 1;
            finishCol = destination.getCol();
        } else {                                                // Movement Is From Left To Right
            startCol = destination.getCol() + 1;
            finishCol = origin.getCol();
        }

        while(check && startCol < finishCol){
            if(gameboard[origin.getRow()][startCol].getPiece() != Const.BLANK)
                check = false;
            startCol++;
        }
        return check;
    }

    public boolean isVerticalMovePossible(Square origin, Square destination){
        // First Make Sure They're On The Same Column, Otherwise Not A Vertical Movement
        assert(origin.getCol() == destination.getCol()) : "Not Same Column";
        boolean check = true;
        int startRow, finishRow;

        if(origin.getRow() < destination.getRow()){
            startRow = origin.getRow() + 1;
            finishRow = destination.getRow();
        } else {
            startRow = destination.getRow() + 1;
            finishRow = origin.getRow();
        }

        while(check && startRow < finishRow){
            if(gameboard[startRow][origin.getCol()].getPiece() != Const.BLANK)
                check = false;
            startRow++;
        }
        return check;
    }

    /* [07] PIECE CAPTURE CHECKING FUNCTIONS -----------------------------------------------------*/
    public int capture(Square landingSquare){
        return getCapturedPieces(landingSquare);
    }

    private int getCapturedPieces(final Square landingSquare){
        final int[] values = new int[4];
        Thread[] threads = new Thread[4];
        threads[0] = new Thread(new Runnable() {
            @Override
            public void run() {
                values[0] = getCaughtPiecesToLeft(landingSquare);
            }
        });
        threads[1] = new Thread(new Runnable() {
            @Override
            public void run() {
                values[1] = getCaughtPiecesToTop(landingSquare);
            }
        });
        threads[2] = new Thread(new Runnable() {
            @Override
            public void run() {
                values[2] = getCaughtPiecesToRight(landingSquare);
            }
        });
        threads[3] = new Thread(new Runnable() {
            @Override
            public void run() {
                values[3] = getCaughtPiecesToBottom(landingSquare);
            }
        });
        for(Thread t : threads) t.start();
        for(int i = 0; i < threads.length; i++)
            try{
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        return values[0] + values[1] + values[2] + values[3];
    }

    private int getCaughtPiecesToLeft(Square landingSquare){
        int col = landingSquare.getCol() - 1;
        List<Square> squaresToRemove = new ArrayList<>();
        boolean loopCheck = true;
        Square s = landingSquare; // Init S
        while(col >= 0 && loopCheck){
            s = gameboard[landingSquare.getRow()][col];
            // IF Piece Is Opponent Piece, Add Square To Squares To Be Removed
            // Else Break Loop
            if(s.getPiece() != Const.BLANK && s.getPiece() != landingSquare.getPiece()){
                squaresToRemove.add(s);
                col--;
            }
            else loopCheck = false;
        }
        // Check Square Has Moved Beyond Its Original Location and Check The Finishing Piece Is Of
        // The Same Type
        if(s == landingSquare || s.getPiece() != landingSquare.getPiece() ) squaresToRemove.clear();
        else for(Square square : squaresToRemove)
            gameboard[square.getRow()][square.getCol()].setPiece(Const.BLANK);
        return squaresToRemove.size();
    }

    private int getCaughtPiecesToTop(Square landingSquare){
        boolean loopCheck = true;
        List<Square> squaresToRemove = new ArrayList<>();
        Square s = landingSquare; // Init S
        int row = landingSquare.getRow() - 1;


        while(row >= 0 && loopCheck){
            s = gameboard[row][landingSquare.getCol()];
            // IF Piece Is Opponent Piece, Add Square To Squares To Be Removed
            // Else Break Loop
            if(s.getPiece() != Const.BLANK && s.getPiece() != landingSquare.getPiece()){
                squaresToRemove.add(s);
                row--;
            } else loopCheck = false;
        }
        // Check Square Has Moved Beyond Its Original Location and Check The Finishing Piece Is Of
        // The Same Type
        if(s == landingSquare || s.getPiece() != landingSquare.getPiece() ) squaresToRemove.clear();
        else for(Square square : squaresToRemove)
            gameboard[square.getRow()][square.getCol()].setPiece(Const.BLANK);
        return squaresToRemove.size();
    }

    private int getCaughtPiecesToRight(Square landingSquare){
        int col = landingSquare.getCol() + 1;
        boolean loopCheck = true;
        List<Square> squaresToRemove = new ArrayList<>();
        Square s = landingSquare; // Init S

        while(col < gameboard[landingSquare.getRow()].length && loopCheck){
            s = gameboard[landingSquare.getRow()][col];
            // IF Piece Is Opponent Piece, Add Square To Squares To Be Removed
            // Else Break Loop
            if(s.getPiece() != Const.BLANK && s.getPiece() != landingSquare.getPiece()){
                squaresToRemove.add(s);
                col++;
            }
            else loopCheck = false;
        }
        // Check Square Has Moved Beyond Its Original Location and Check The Finishing Piece Is Of
        // The Same Type
        if(s == landingSquare || s.getPiece() != landingSquare.getPiece() ) squaresToRemove.clear();
        else for(Square square : squaresToRemove)
            gameboard[square.getRow()][square.getCol()].setPiece(Const.BLANK);
        return squaresToRemove.size();
    }

    private int getCaughtPiecesToBottom(Square landingSquare){
        int row = landingSquare.getRow() + 1;
        boolean loopCheck = true;
        List<Square> squaresToRemove = new ArrayList<>();
        Square s = landingSquare; // Init s

        while(row < gameboard.length && loopCheck){
            s = gameboard[row][landingSquare.getCol()];
            // IF Piece Is Opponent Piece, Add Square To Squares To Be Removed
            // Else Break Loop
            if(s.getPiece() != Const.BLANK && s.getPiece() != landingSquare.getPiece()){
                squaresToRemove.add(s);
                row++;
            } else loopCheck = false;
        }
        if(s == landingSquare || s.getPiece() != landingSquare.getPiece() ) squaresToRemove.clear();
        else for(Square square : squaresToRemove)
            gameboard[square.getRow()][square.getCol()].setPiece(Const.BLANK);
        return squaresToRemove.size();
    }


    /* [08] DAI HASAMI SHOGI 5 PIECE WIN RULE CHECK ----------------------------------------------*/
    public boolean daiHasamiShogiWinCheck(final Square landingSquare){
        /*Thread[] threads = new Thread[4];
        final boolean[] check = new boolean[4];
        threads[0]= new Thread(new Runnable() {
            @Override
            public void run() {
                check[0] = daiCheckHorizontal(landingSquare);
            }
        });
        threads[1] = new Thread(new Runnable() {
            @Override
            public void run() {
                check[1] = daiCheckVertical(landingSquare);
            }
        });
        threads[2] = new Thread(new Runnable() {
            @Override
            public void run() {
                check[2] = daiCheckAscendingDiagonal(landingSquare);
            }
        });
        threads[3] = new Thread(new Runnable() {
            @Override
            public void run() {
                check[3] = daiCheckDescendingDiagonal(landingSquare);
            }
        });
        for(Thread thread : threads) thread.start();
        for(int i = 0; i < threads.length; i++)
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        return check[0] && check[1] && check[2] && check[3]; */
        return false;
    }


    private boolean daiCheckHorizontal(Square start){
        int pieces = 1;
        pieces += getLeftCount(start);
        pieces += getRightCount(start);
        return pieces >= 5;
    }
    private boolean daiCheckVertical(Square start){
        int pieces = 1;
        pieces += getUpCount(start);
        pieces += getDownCount(start);
        return pieces >= 5;
    }
    private boolean daiCheckAscendingDiagonal(Square start){
        int pieces = 1;
        pieces += getAscendingRightCount(start);
        pieces += getDescendingLeftCount(start);
        return pieces >= 5;
    }
    private boolean daiCheckDescendingDiagonal(Square start){
        int pieces = 1;
        pieces += getAscendingLeftCount(start);
        pieces += getDescendingRightCount(start);
        return pieces >= 5;
    }

    private int getLeftCount(Square start){
        int pieces = 0;
        int col = start.getCol() - 1;
        boolean loopControl = true;
        while(loopControl && col >= 0){
            if(gameboard[start.getRow()][col].getPiece() == start.getPiece()) pieces++;
            else loopControl = false;
            col--;
        }
        return pieces;
    }
    private int getRightCount(Square start){
        int pieces = 0;
        int col = start.getCol() + 1;
        boolean loopControl = true;
        while(loopControl && col < 9){
            if(gameboard[start.getRow()][col].getPiece() == start.getPiece()) pieces++;
            else loopControl = false;
            col++;
        }
        return pieces;
    }
    private int getUpCount(Square start){
        int pieces = 0;
        int row = start.getRow() - 1;
        boolean loopControl = true;
        while(loopControl && row <= 2){
            if(gameboard[row][start.getCol()].getPiece() == start.getPiece()) pieces++;
            else loopControl = false;
            row--;
        }
        return pieces;
    }
    private int getDownCount(Square start){
        int pieces = 0;
        int row = start.getRow() + 1;
        boolean loopControl = true;
        while(loopControl && row <= 6){
            if(gameboard[row][start.getCol()].getPiece() == start.getPiece()) pieces++;
            else loopControl = false;
            row++;
        }
        return pieces;
    }
    private int getAscendingRightCount(Square start){
        int pieces = 0;
        int row = start.getRow() - 1;
        int col = start.getCol() + 1;
        boolean loopControl = true;
        while(loopControl && row >= 2 && col <= 8){
            if(gameboard[row][col].getPiece() == start.getPiece()) pieces++;
            else loopControl = false;
            row--; col++;
        }
        return pieces;
    }
    private int getDescendingLeftCount(Square start){
        int pieces = 0;
        int row = start.getRow() + 1;
        int col = start.getCol() - 1;
        boolean loopControl = true;
        while(loopControl && row <= 6 && col >= 0){
            if(gameboard[row][col].getPiece() == start.getPiece()) pieces++;
            else loopControl = false;
            row++; col--;
        }
        return pieces;
    }
    private int getAscendingLeftCount(Square start){
        int pieces = 0;
        int row = start.getRow() - 1;
        int col = start.getCol() - 1;
        boolean loopControl = true;
        while(loopControl && row >= 2 && col >= 0){
            if(gameboard[row][col].getPiece() == start.getPiece()) pieces++;
            else loopControl = false;
            row--; col--;
        }
        return pieces;
    }
    private int getDescendingRightCount(Square start){
        int pieces = 0;
        int row = start.getRow() + 1;
        int col = start.getCol() + 1;
        boolean loopControl = true;
        while(loopControl && row <= 6 && col <= 8){
            if(gameboard[row][col].getPiece() == start.getPiece()) pieces++;
            else loopControl = false;
            row++; col++;
        }
        return pieces;
    }
}
