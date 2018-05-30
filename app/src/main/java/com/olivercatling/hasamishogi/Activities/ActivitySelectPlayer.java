package com.olivercatling.hasamishogi.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.olivercatling.hasamishogi.Database.HasamiShogiDAO;
import com.olivercatling.hasamishogi.Database.Player;
import com.olivercatling.hasamishogi.Database.Settings;
import com.olivercatling.hasamishogi.Game.Board;
import com.olivercatling.hasamishogi.Game.Const;
import com.olivercatling.hasamishogi.Game.Game;
import com.olivercatling.hasamishogi.R;

import java.util.ArrayList;

public class ActivitySelectPlayer extends AppCompatActivity {
    private HasamiShogiDAO dao;

    private ArrayList<Player> players = new ArrayList<>();
    private Player player1 = Const.GUEST;
    private Player player2 = Const.GUEST;
    private static final int STANDARD_BACKGROUND_COLOUR =
            Integer.parseInt("#FAFAFA".replaceFirst("^#",""), 16);
    private static final int HIGHLIGHTED_BACKGROUND_COLOUR = Color.CYAN;

    private ListView player1ListView;
    private ListView player2ListView;
    private TextView player1Insert;
    private TextView player2Insert;

    private ArrayAdapter player1adapter;
    private ArrayAdapter player2adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_player);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dao = new HasamiShogiDAO(this);
        dao.open();
        dao.getPlayers(players);

        this.player1Insert = (TextView) findViewById(R.id.text_player1_insert);
        this.player2Insert = (TextView) findViewById(R.id.text_player2_insert);
        initPlayer1();
        initPLayer2();
    }

    private void initPlayer1(){
        this.player1ListView = (ListView) findViewById(R.id.list_select_player1);
        this.player1adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, players);
        this.player1ListView.setAdapter(player1adapter);
        this.player1ListView.setOnItemClickListener(this.player1OnClickListener());
    }

    private void initPLayer2(){
        this.player2ListView = (ListView) findViewById(R.id.list_select_player2);
        this.player2adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, players);
        this.player2ListView.setAdapter(player2adapter);
        this.player2ListView.setOnItemClickListener(this.player2OnClickListener());
    }

    private AdapterView.OnItemClickListener player1OnClickListener(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // IF PLAYER IS BEING DESELECTED WITHOUT A REPLACEMENT
                if(player1 == players.get(position)) {
                    player1 = Const.GUEST;
                    player1ListView.getChildAt(position).setBackgroundColor(STANDARD_BACKGROUND_COLOUR);
                    // OTHERWISE PLAYER 1 = THAT PLAYER
                } else {
                    if(player1 != Const.GUEST)
                        player1ListView.getChildAt(players.indexOf(player1)).setBackgroundColor(STANDARD_BACKGROUND_COLOUR);
                    player1 = players.get(position);
                    player1ListView.getChildAt(position).setBackgroundColor(HIGHLIGHTED_BACKGROUND_COLOUR);

                }

                // IF PLAYER IS BEING CHANGED FROM ONE TO ANOTHER
                if(player1 == player2) {
                    player2 = Const.GUEST;
                    player2ListView.getChildAt(position).setBackgroundColor(STANDARD_BACKGROUND_COLOUR);
                }
                player1Insert.setText(player1.toString());
            }
        };
    }

    private AdapterView.OnItemClickListener player2OnClickListener(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // IF PLAYER IS BEING DESELECTED WITHOUT A REPLACEMENT
                if(player2 == players.get(position)) {
                    player2 = Const.GUEST;
                    player2ListView.getChildAt(position).setBackgroundColor(STANDARD_BACKGROUND_COLOUR);
                // OTHERWISE PLAYER 2 = THAT PLAYER
                } else {
                    if(player2 != Const.GUEST)
                        player2ListView.getChildAt(players.indexOf(player2)).setBackgroundColor(STANDARD_BACKGROUND_COLOUR);
                    player2 = players.get(position);
                    player2ListView.getChildAt(position).setBackgroundColor(HIGHLIGHTED_BACKGROUND_COLOUR);

                }

                // IF PLAYER IS BEING CHANGED FROM ONE TO ANOTHER
                if(player1 == player2) {
                    player1 = Const.GUEST;
                    player1ListView.getChildAt(position).setBackgroundColor(STANDARD_BACKGROUND_COLOUR);
                }
                player2Insert.setText(player2.toString());
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();


        if(id == R.id.menu_item_save){
            Game game;
            Board board;
            Settings s = dao.getSettingsFromRef(0);

            if(s.isDaiHasamiShogi()) board = new Board(Const.size18);
            else board = new Board(Const.size9);

            long boardRowId = dao.addBoard(board);
            board.setBoardID(dao.getBoardFromRowID(boardRowId).getBoardID());

            game = new Game(board, s, player1, player2);
            long rowID = dao.addGame(game);
            int ref = dao.getGameFromRowID(rowID).getReference();


            Intent intent = new Intent(getApplicationContext(), ActivityGame.class);
            intent.putExtra(Const.GAME_KEY_REF, ref);
            startActivityForResult(intent, Const.GAME_KEY_REQUEST);
            /*Intent intent = new Intent(getApplicationContext(), ActivityGame.class);
            startActivity(intent);*/
            finish();

        } else if (id == android.R.id.home){
            Intent intent = new Intent(getApplicationContext(), ActivityGameControl.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
