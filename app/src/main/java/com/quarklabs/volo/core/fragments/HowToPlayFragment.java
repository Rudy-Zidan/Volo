package com.quarklabs.volo.core.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quarklabs.volo.R;

/**
 * Created by rudy on 6/16/17.
 */

public class HowToPlayFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.how_to_play_fragment, container, false);

        return rootView;
    }
}
