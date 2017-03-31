package com.wolff.wshablon.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wolff.wshablon.R;

/**
 * Created by wolff on 07.03.2017.
 */

public class Fragment_logo extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logo, container, false);
        LinearLayout lin_layout_logo = (LinearLayout)view.findViewById(R.id.linLayout_logo);
        lin_layout_logo.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        return view;
    }
}
