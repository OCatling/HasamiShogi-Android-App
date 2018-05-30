package com.olivercatling.hasamishogi.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.olivercatling.hasamishogi.CustomViews.CustomGridBoard;
import com.olivercatling.hasamishogi.Database.HasamiShogiDAO;
import com.olivercatling.hasamishogi.Database.Player;
import com.olivercatling.hasamishogi.Database.Settings;
import com.olivercatling.hasamishogi.Game.Board;
import com.olivercatling.hasamishogi.Game.Const;
import com.olivercatling.hasamishogi.Game.Game;
import com.olivercatling.hasamishogi.Game.Square;
import com.olivercatling.hasamishogi.R;

public class ActivityGame extends AppCompatActivity {
    private HasamiShogiDAO dao;

    private Game game;

    private GridView gameGrid;

    private CustomGridBoard gameGridAdapter;

    private Square selection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        dao = new HasamiShogiDAO(this);
        dao.open();
        Intent intent = getIntent();
        game = dao.getGameFromRef(intent.getIntExtra(Const.GAME_KEY_REF, 0));
        game.getPlayer1().incrementGamesPlayed();
        game.getPlayer2().incrementGamesPlayed();
        createGrid();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home ){
            dao.updateGame(game);
            dao.updateBoard(game.getBoard());
            Intent intent = new Intent(getApplicationContext(), ActivityGameControl.class);
            startActivity(intent);
            finish();

        } else if(id == R.id.menu_item_settings){
            dao.updateGame(game);
            Intent intent = new Intent(getApplicationContext(), ActivityGameSettings.class);
            intent.putExtra(Const.SETTINGS_KEY_REF, game.getSettings().getReference());
            startActivityForResult(intent, Const.SETTINGS_REQUEST);
            finish();

        } else if(id == R.id.menu_item_refresh){
            this.refresh();
        }

        return super.onOptionsItemSelected(item);
    }

    private void refresh(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("ARE YOU SURE YOU WANT TO RESTART?");
        builder.setCancelable(true);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Board board;
                if(game.getSettings().isDaiHasamiShogi())
                    board = new Board(Const.size18);
                else
                    board = new Board(Const.size9);
                game.setBoard(board);
                game.setMoveNumber(1);
                game.setWhiteScore(0);
                game.setBlackScore(0);
                dao.updateGame(game);
                createGrid();
            }
        });
        builder.setNegativeButton("NAH", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void createGrid(){
        gameGrid = (GridView) findViewById(R.id.grid_board);
        gameGridAdapter = new CustomGridBoard(this,game.getBoard().getFlatBoard(), Const.STANDARD_GRID_ICON_DIRECTORIES);
        gameGrid.setAdapter(gameGridAdapter);
        gameGrid.setOnItemClickListener(setGridClickListener());
        gameGridAdapter.notifyDataSetChanged();
    }

    /*-------------------- Game Control -------------------*/
    private AdapterView.OnItemClickListener setGridClickListener() {

        return new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                int colPos = position % 9;
                int rowPos = position / 9;
                int move = game.getMoveNumber();
                Square currentSelection = game.getBoard().getSquare(rowPos, colPos);
                // If 1: If The Player Selects Own Piece During Their Move
                // If 2: If The Player Has Selected Their Own Piece, Its Their Move And The Target
                // Square is Blank
                // Else If Selection Is Not Their Piece: Error Message
                if (    (move % 2 == 1
                        && currentSelection.getPiece() == Const.WHITE) ||
                        (move % 2 == 0
                        && currentSelection.getPiece() == Const.BLACK)){

                    selection = game.getBoard().getSquare(rowPos, colPos);

                } else if(selection != null && ((move % 2 == 1 && selection.getPiece() == Const.WHITE) ||
                        (move % 2 == 0 && selection.getPiece() == Const.BLACK)) &&
                        (currentSelection.getPiece() == Const.BLANK)){

                    movePiece(currentSelection);
                } else if((move % 2 == 1
                        && currentSelection.getPiece() != Const.WHITE) ||
                        (move % 2 == 0
                        && currentSelection.getPiece() != Const.BLACK))
                    if(move % 2 == 1) errorMessage("IT IS WHITE'S MOVE");
                    else errorMessage("IT IS BLACK'S MOVE");

            }
        };
    }

    private void movePiece(Square currentSelection) {
        // If 1: Check If Movement Is Possible
        // Else Error Message
        if (game.isMovementPossible(selection, currentSelection)){
            game.getBoard().movePiece(selection, currentSelection);
            checkCapture(currentSelection);
            checkWin(currentSelection);
            game.incrementMoveCount();
        }else
            errorMessage("MOVE NOT VALID");
    }

    private void checkCapture(Square currentSelection){
        int capturedPieces = game.getBoard().capture(currentSelection);
        if (currentSelection.getPiece() == Const.WHITE) {
            game.increaseWhiteScore(capturedPieces);
        } else if(currentSelection.getPiece() == Const.BLACK) {
            game.increaseBlackScore(capturedPieces);
        }
        this.gameGridAdapter.notifyDataSetChanged();
    }

    private void checkWin(Square currentSelection){
        if(game.isWhiteWin() ||
                (game.isDaiWin(currentSelection) &&
                        currentSelection.getPiece() == Const.WHITE) ){
            game.getPlayer1().incrementGamesWon();
            winMessage(game.getPlayer1());
        } else if(game.isBlackWin() ||
                (game.isDaiWin(currentSelection) &&
                        currentSelection.getPiece() == Const.BLACK) ){
            game.getPlayer2().incrementGamesWon();
            winMessage(game.getPlayer2());
        }
    }

    private void errorMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void winMessage(final Player winner){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("CONGRATULATIONS " + winner.getUsername().toUpperCase()
                + "!\nWOULD YOU LIKE TO PLAY AGAIN?");
        builder.setCancelable(true);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Settings s = dao.getSettingsFromRef(0);
                Game g;
                Board board;

                if(s.isDaiHasamiShogi()) board = new Board(Const.size18);
                else board = new Board(Const.size9);

                long boardRowId = dao.addBoard(board);
                board.setBoardID(dao.getBoardFromRowID(boardRowId).getBoardID());

                g = new Game(board, s, game.getPlayer1(), game.getPlayer2());
                long rowID = dao.addGame(g);
                int ref = dao.getGameFromRowID(rowID).getReference();

                dao.removeGame(game);

                Intent intent = new Intent(getApplicationContext(), ActivityGame.class);
                intent.putExtra(Const.GAME_KEY_REF, ref);
                startActivityForResult(intent, Const.GAME_KEY_REQUEST);

                finish();

            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dao.removeGame(game);
                Intent intent = new Intent(getApplicationContext(), ActivityGameControl.class);
                startActivity(intent);
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
