package com.olivercatling.hasamishogi.CustomViews;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.olivercatling.hasamishogi.Database.Player;
import com.olivercatling.hasamishogi.R;

import java.util.List;

/**
 * Created by Oliver on 24/04/2018.
 */

public class CustomListLeaderboard extends ArrayAdapter<Player> {
    private final Activity context;
    private final List<Player> players;

    public CustomListLeaderboard(Activity context, List<Player> players){
        super(context, R.layout.custom_list_leaderboard, players);
        this.context = context;
        this.players = players;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.custom_list_leaderboard, null, true);
        Player player = players.get(position);

        TextView username = (TextView) rowView.findViewById(R.id.custom_list_player_username);
        TextView won = (TextView) rowView.findViewById(R.id.custom_list_player_games_won);
        TextView lost = (TextView) rowView.findViewById(R.id.custom_list_player_games_lost);
        TextView points = (TextView) rowView.findViewById(R.id.custom_list_player_points);

        username.setText(player.getUsername());
        won.setText(String.valueOf(player.getGamesWon()));
        lost.setText(String.valueOf(player.getGamesPlayed() - player.getGamesPlayed()));
        points.setText(String.valueOf(player.getGamesWon() * 1));

        return rowView;
    }
}
