package com.olivercatling.hasamishogi.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.olivercatling.hasamishogi.CustomViews.CustomListGames;
import com.olivercatling.hasamishogi.CustomViews.CustomListLeaderboard;
import com.olivercatling.hasamishogi.Database.HasamiShogiDAO;
import com.olivercatling.hasamishogi.Database.Player;
import com.olivercatling.hasamishogi.Database.Settings;
import com.olivercatling.hasamishogi.Game.Const;
import com.olivercatling.hasamishogi.Game.Game;
import com.olivercatling.hasamishogi.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityGameControl extends AppCompatActivity {
    private HasamiShogiDAO dao;
    private SharedPreferences prefs;

    /*-----------------------------------> Visual Component <-------------------------------------*/
    private ListView leaderboardListView;
    private ListView gamesListView;

    /*----------------------------------------> Adapter <-----------------------------------------*/
    private CustomListLeaderboard leaderboardAdapter;
    private ArrayAdapter<Game> gameAdapter;

    /*------------------------------------------> Data <------------------------------------------*/
    private List<Player> players = new ArrayList<>();
    private List<Game> games = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_control);

        prefs = getSharedPreferences("com.olivercatling.hasamishogi", MODE_PRIVATE);

        dao = new HasamiShogiDAO(this);
        dao.open();
    }

    @Override
    protected void onResume(){
        super.onResume();
        players.clear();
        dao.getPlayers(players);
        dao.getGames(games);
        players.remove(0); // Always Guest

        if(prefs.getBoolean("firstrun", true)) {
            dao.addSettings(new Settings(false, false, 8, 0));
            dao.addPlayer(Const.GUEST);
            prefs.edit().putBoolean("firstrun", false).commit();
        }

        leaderboardListView = (ListView) findViewById(R.id.list_leaderboard);
        leaderboardAdapter = new CustomListLeaderboard(this, players);
        leaderboardListView.setAdapter(leaderboardAdapter);
        leaderboardListView.setOnItemClickListener(leaderboardClick());
        leaderboardAdapter.notifyDataSetChanged();

        gamesListView = (ListView) findViewById(R.id.listview_games);
        gameAdapter = new CustomListGames(this, games);
        gamesListView.setAdapter(gameAdapter);
        gamesListView.setOnItemClickListener(gameClick());
        gameAdapter.notifyDataSetChanged();
    }

    private AdapterView.OnItemClickListener leaderboardClick(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Player player = players.get(pos);
                Toast.makeText(getApplicationContext(), "USERNAME: " + player.getUsername() +
                        "\nBIO: " + player.getBio() + "\nPLAYED: " + player.getGamesWon()
                        +"\nWON: " + player.getGamesWon(), Toast.LENGTH_LONG ).show();
            }
        };
    }

    private AdapterView.OnItemClickListener gameClick(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ActivityGame.class);
                intent.putExtra(Const.GAME_KEY_REF, games.get(i).getReference());
                startActivityForResult(intent, Const.GAME_KEY_REQUEST);
            }
        };
    }

    public void registerPlayerClick(View view){
        Intent intent = new Intent(getApplicationContext(), ActivityAddPlayer.class);
        startActivity(intent);
    }

    public void startNewGame(View view){
        Intent intent = new Intent(getApplicationContext(), ActivitySelectPlayer.class);
        startActivity(intent);
    }

    public void viewLeaderboard(View view){
        Intent intent = new Intent(getApplicationContext(), ActivityViewLeaderboard.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.menu_item_settings){
            Intent intent = new Intent(getApplicationContext(), ActivityGameSettings.class);
            intent.putExtra(Const.SETTINGS_KEY_REF, 0);
            startActivityForResult(intent, Const.SETTINGS_REQUEST);
        }
        return super.onOptionsItemSelected(item);
    }

}
