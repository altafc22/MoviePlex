package gq.altafchaudhari.www.movieplex.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gq.altafchaudhari.www.movieplex.R;

public class TheaterListFragment extends Fragment {
    public static TheaterListFragment newInstance() {
        TheaterListFragment fragment = new TheaterListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_theater_list, container, false);
    }
}