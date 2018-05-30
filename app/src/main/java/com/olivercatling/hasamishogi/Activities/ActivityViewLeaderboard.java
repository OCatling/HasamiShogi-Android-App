package com.olivercatling.hasamishogi.Activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


import com.olivercatling.hasamishogi.Database.Player;
import com.olivercatling.hasamishogi.Database.PlayerContract;
import com.olivercatling.hasamishogi.PlayerProvider;
import com.olivercatling.hasamishogi.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityViewLeaderboard extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private Player[] players;

    private ListView leaderboard;
    private SimpleCursorAdapter adapter;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_leaderboard);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] from = {PlayerContract.COLUMN_NAME_USERNAME, PlayerContract.COLUMN_NAME_PLAYED,
                PlayerContract.COLUMN_NAME_WON};

        int[] to = {R.id.custom_list_player_username, R.id.custom_list_player_games_won,
                R.id.custom_list_player_points};

        adapter = new SimpleCursorAdapter(this, R.layout.custom_list_leaderboard,
                cursor, from, to, 0);

        leaderboard = (ListView) findViewById(R.id.list_leaderboard_full);
        getLoaderManager().initLoader(0,null, this);
        leaderboard.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(getApplicationContext(), ActivityGameControl.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this, PlayerProvider.CONTENT_URI,
                null, null, null, null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
