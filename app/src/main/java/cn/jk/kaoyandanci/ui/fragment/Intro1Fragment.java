package cn.jk.kaoyandanci.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.jk.kaoyandanci.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Intro1Fragment extends Fragment {


    public Intro1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro1, container, false);
    }

}
