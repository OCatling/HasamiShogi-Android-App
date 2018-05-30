package com.olivercatling.hasamishogi.CustomViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.olivercatling.hasamishogi.Game.Board;
import com.olivercatling.hasamishogi.Game.Const;
import com.olivercatling.hasamishogi.Game.Square;
import com.olivercatling.hasamishogi.R;

import java.util.List;

/**
 * Created by Oliver on 27/04/2018.
 */

public class CustomGridBoard extends BaseAdapter{
    private Context context;
    private final List<Square> squares;

    private int[] imageID;

    public CustomGridBoard(Context context, List<Square> squares, int[] imageID){
        this.context = context;
        this.squares = squares;
        this.imageID = imageID;
    }


    @Override
    public int getCount() {
        return squares.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView squareImageView;

        if(view == null){
            grid = new View(context);
            grid = inflater.inflate(R.layout.custom_grid_board, null);

            squareImageView = (ImageView) grid.findViewById(R.id.square_image);
            squareImageView.setLayoutParams(new GridView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            squareImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            squareImageView.setPadding(10, 20, 10, 20);


        } else squareImageView = (ImageView) view;

        switch(squares.get(position).getPiece()){
            case Const.BLANK: squareImageView.setImageResource(imageID[Const.BLANK]); break;
            case Const.WHITE: squareImageView.setImageResource(imageID[Const.WHITE]); break;
            case Const.BLACK: squareImageView.setImageResource(imageID[Const.BLACK]); break;
            default: squareImageView.setImageResource(imageID[Const.BLANK]); break;
        }

        return squareImageView;
    }
}
