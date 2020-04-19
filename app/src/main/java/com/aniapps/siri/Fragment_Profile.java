package com.aniapps.siri;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.aniapps.adapters.PagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class Fragment_Profile extends Fragment {

    RecyclerView rcv_newTasks;
    TextView tv_nolisting;
    ViewPager mypager;
    TabLayout mytabs;
    ProgressBar progressBar;
    String TITLES[]={"My Account","My Orders","Settings"};
    public Fragment_Profile() {
        // Required empty public constructor
    }

    public static Fragment_Profile newInstance(String param1, String param2) {
        Fragment_Profile fragment = new Fragment_Profile();
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
        View view_newtasks = inflater.inflate(R.layout.fragment_profile, container, false);
        mypager=view_newtasks.findViewById(R.id.viewpager);
        mytabs=view_newtasks.findViewById(R.id.sliding_tabs);
        configureTabLayout();

        return view_newtasks;
    }

    private void configureTabLayout() {
        mytabs.setTabMode(TabLayout.MODE_FIXED);
        for(int i=0;i<TITLES.length;i++){
            mytabs.addTab(mytabs.newTab().setText(TITLES[i]));
        }

        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager(), mytabs.getTabCount());
        mypager.setAdapter(adapter);

        mypager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mytabs));
        mytabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mypager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        /*if (from.equals("pending"))
            this.mViewPager!!.setCurrentItem(0)
        if (from.equals("current"))
            this.mViewPager!!.setCurrentItem(1)
        if (from.equals("end"))
            this.mViewPager!!.setCurrentItem(2)
        else
        this.mViewPager!!.setCurrentItem(0)*/

    }
}
