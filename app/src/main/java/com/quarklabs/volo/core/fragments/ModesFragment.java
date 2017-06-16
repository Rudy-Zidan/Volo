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

public class ModesFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.modes_fragment_layout, container, false);

        return rootView;
    }
}
