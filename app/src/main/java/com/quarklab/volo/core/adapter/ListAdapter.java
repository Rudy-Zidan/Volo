package com.quarklab.volo.core.adapter;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.quarklab.volo.R;

import java.util.Locale;


/**
 * Created by rudy on 8/6/16.
 */
public class ListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] players;
    private final String[] scores;
    private final String[] userId;
    private Typeface typeface;
    public ListAdapter(Activity context,
                      String[] players, String[] userId, String[] scores) {
        super(context, R.layout.list_item, players);
        this.context = context;
        this.players = players;
        this.userId = userId;
        this.scores = scores;
        this.setFont();
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
        nameTxt.setTypeface(this.typeface);
        scoreTxt.setText(this.scores[position]);
        scoreTxt.setTypeface(this.typeface);
        imageView.setProfileId(this.userId[position]);
        if(position == 0)
            trophy.setImageResource(R.drawable.trophy);
        return rowView;
    }

    private void setFont(){
        AssetManager am = this.context.getApplicationContext().getAssets();
        this.typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "KBZipaDeeDooDah.ttf"));
    }
}
