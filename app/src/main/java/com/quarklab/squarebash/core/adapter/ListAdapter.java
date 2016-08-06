package com.quarklab.squarebash.core.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.quarklab.squarebash.R;


/**
 * Created by rudy on 8/6/16.
 */
public class ListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] players;
    private final String[] scores;
    private final String[] userId;
    public ListAdapter(Activity context,
                      String[] players, String[] userId, String[] scores) {
        super(context, R.layout.list_item, players);
        this.context = context;
        this.players = players;
        this.userId = userId;
        this.scores = scores;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_item, null, true);
        TextView nameTxt = (TextView) rowView.findViewById(R.id.name);
        TextView scoreTxt = (TextView) rowView.findViewById(R.id.score);
        ProfilePictureView imageView =  (ProfilePictureView) rowView.findViewById(R.id.image);
        ImageView trophy = (ImageView) rowView.findViewById(R.id.trophy);

        nameTxt.setText(this.players[position]);
        scoreTxt.setText(this.scores[position]);
        imageView.setProfileId(this.userId[position]);
        if(position == 0)
            trophy.setImageResource(R.drawable.trophy);
        return rowView;
    }
}
