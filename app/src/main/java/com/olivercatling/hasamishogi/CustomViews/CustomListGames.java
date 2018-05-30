package com.olivercatling.hasamishogi.CustomViews;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.olivercatling.hasamishogi.Game.Game;
import com.olivercatling.hasamishogi.R;

import java.util.List;

/**
 * Created by Oliver on 29/04/2018.
 */

public class CustomListGames extends ArrayAdapter<Game>{
    private final Activity context;
    private final List<Game> games;

    public CustomListGames(Activity context, List<Game> games){
        super(context, R.layout.custom_game_layout, games);
        this.context = context;
        this.games = games;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.custom_game_layout, null, true);
        Game game = games.get(position);

        TextView p1 = (TextView) rowView.findViewById(R.id.custom_list_player1);
        TextView p2 = (TextView) rowView.findViewById(R.id.custom_list_player2);

        p1.setText(game.getPlayer1().getUsername());
        p2.setText(game.getPlayer2().getUsername());

        return rowView;
    }
}
