package com.olivercatling.hasamishogi.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.olivercatling.hasamishogi.Database.*;
import com.olivercatling.hasamishogi.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityAddPlayer extends AppCompatActivity {
    private HasamiShogiDAO dao;

    /*-----------------------------------> Visual Component <-------------------------------------*/
    private EditText usernameInput;
    private EditText bioInput;

    /*------------------------------------------> Data <------------------------------------------*/
    private List<Player> players = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dao = new HasamiShogiDAO(this);
        dao.open();

        usernameInput = (EditText) findViewById(R.id.input_username);
        bioInput = (EditText) findViewById(R.id.input_bio);
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
            /*
            ContentValues values = new ContentValues();
            String username = usernameInput.getText().toString();
            String bio = bioInput.getText().toString();
            int gamesPlayed = 0; int gamesWon = 0;

            values.put(PlayerContract.COLUMN_NAME_USERNAME, username);
            values.put(PlayerContract.COLUMN_NAME_BIO, bio);
            values.put(PlayerContract.COLUMN_NAME_PLAYED, gamesPlayed);
            values.put(PlayerContract.COLUMN_NAME_WON, gamesWon);

            Uri uri = getContentResolver().insert(PlayerProvider.CONTENT_URI, values);

             */
            dao.addPlayer(new Player(usernameInput.getText().toString(), bioInput.getText().toString()));
            finish();
        } else if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
