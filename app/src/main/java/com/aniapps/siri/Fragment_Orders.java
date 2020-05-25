package com.aniapps.siri;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class Fragment_Orders extends Fragment {

    public Fragment_Orders() {
        // Required empty public constructor
    }

    public static Fragment_Orders newInstance(String param1, String param2) {
        Fragment_Orders fragment = new Fragment_Orders();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view_newtasks = inflater.inflate(R.layout.fragment_wishlist, container, false);


        return view_newtasks;
    }

}
