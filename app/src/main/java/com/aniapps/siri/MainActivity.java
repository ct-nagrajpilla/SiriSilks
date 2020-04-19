package com.aniapps.siri;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.aniapps.adapters.MainAdapter;
import com.aniapps.callbackclient.APIResponse;
import com.aniapps.callbackclient.RetrofitClient;
import com.aniapps.models.MyCategories;
import com.aniapps.models.OtherMenu;
import com.aniapps.models.SubCategory;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    ExpandableListView expandableListView;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Siri Silks");
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view2);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        showBadge(this, navigation, R.id.navigation_wishlist, "1");
        // navigation.getOrCreateBadge(R.id.navigation_wishlist).setNumber(2);
        expandableListView = findViewById(R.id.expandable_menu);
        apiCall();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
//https://stackoverflow.com/questions/42682855/display-badge-on-top-of-bottom-navigation-bars-icon
    public void showBadge(Context context, BottomNavigationView
            bottomNavigationView, @IdRes int itemId, String value) {
        removeBadge(bottomNavigationView, itemId);
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        View badge = LayoutInflater.from(context).inflate(R.layout.badge, bottomNavigationView, false);

        TextView text = badge.findViewById(R.id.badge_text_view);
        text.setText(value);
        itemView.addView(badge);
    }

    public void removeBadge(BottomNavigationView bottomNavigationView, @IdRes int itemId) {
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        if (itemView.getChildCount() == 3) {
            itemView.removeViewAt(2);
        }
    }


    SearchView search;
    public static int REQUEST_CODE = 999;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final Handler mHandler = new Handler();
        getMenuInflater().inflate(R.menu.main, menu);


/*
        final MenuItem item_share = menu.findItem(R.id.action_cart);
        item_share.setVisible(true);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        ImageView voiceIcon = (ImageView) search.findViewById(androidx.appcompat.R.id.search_voice_btn);
        voiceIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                try {
                    startActivityForResult(intent, REQUEST_CODE);
                    //  Log.e("###", "intentn");
                } catch (ActivityNotFoundException a) {

                }
            }
        });

        search.setQueryHint("Enter Product Name");
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
              */
/*  if (null != mainAdapter) {
                    mainAdapter.filterData(query);
              *//*
      search.clearFocus();
                  */
/*  expand_flag = true;
                    expandAll();
                }*//*

                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                mHandler.removeCallbacksAndMessages(null);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                      */
/*  if (null != mainAdapter)
                            mainAdapter.filterData(newText);
                        if (newText.equals("")) {
                            collapseAll();
                            expand_flag = false;
                            item_add.setVisible(true);
                        } else {
                            expand_flag = true;
                            expandAll();
                            if (null != mainAdapter && mainAdapter.getGroupCount() > 0) {
                                item_add.setVisible(false);
                            } else {
                                item_add.setVisible(true);
                            }
                        }*//*

                    }
                }, 100);

                return true;
            }


        });

        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    item_share.setVisible(false);
                   */
/* cityPicker.setVisibility(View.GONE);
                    bottom_border.setVisibility(View.GONE);
                    category_name.setVisibility(View.GONE);*//*

                } else {
                    */
/*cityPicker.setVisibility(View.VISIBLE);
                    bottom_border.setVisibility(View.VISIBLE);
                    category_name.setVisibility(View.VISIBLE);*//*

                    item_share.setVisible(true);
                }
            }
        });



        item_share.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(getApplicationContext(),"My Cart",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
*/


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setTitle("Home");
                    fragment = new Fragment_Home(MainActivity.this);
                    break;
                case R.id.navigation_wishlist:
                    toolbar.setTitle("Wish List");
                    fragment = new Fragment_WishList();
                    break;
                case R.id.navigation_account:
                    toolbar.setTitle("My Profile");
                    fragment = new Fragment_Profile();
                    break;
            }


            return loadFragment(fragment);
        }
    };

    /*   private void loadFragment(Fragment fragment) {
           // load fragment
           FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
           transaction.replace(R.id.nav_host_fragment, fragment);
           transaction.addToBackStack(null);
           transaction.commit();
       }
   */
//https://github.com/aurelhubert/ahbottomnavigation
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_cart:
                // do stuff, like showing settings fragment
                return true;

            case R.id.action_search:
                // do stuff, like showing settings fragment
                return true;
        }

        return super.onOptionsItemSelected(item); // important line
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();

        // apiCall();

    }

    Gson gson = new Gson();
    MyCategories mainCategories;

    public void apiCall() {
        Map<String, String> params = new HashMap<>();
        params.put("action", getResources().getString(R.string.menu));
        params.put("device_id", getResources().getString(R.string.menu));
        params.put("mobile", "9441349123");
        Log.e("####", "JSON OBJ" + params);
        RetrofitClient.getInstance().doBackProcess(MainActivity.this, params, "", new APIResponse() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onSuccess(String Response) {
                try {
                    int status;
                    JSONObject object = new JSONObject(Response);
                    status = object.getInt("status");
                    if (status == 1) {
                       /* Pref.getIn().setMainCount(object.getInt("livecnt"));
                        Pref.getIn().setShort_link(object.getString("short_link"));*/
                        expandableListView.setAdapter((BaseExpandableListAdapter) null);
                        JSONObject mylist = object.getJSONObject("list");
                        JSONArray resultArray = mylist.getJSONArray("categories");
                        JSONArray otherArray = mylist.getJSONArray("quicklinks");
                        Log.e("####", "JSON OBJ" + resultArray);
                        ArrayList<OtherMenu> otherMenus = new ArrayList<>();
                        for (int k = 0; k < otherArray.length(); k++) {
                            otherMenus.add(new OtherMenu(otherArray.get(k).toString()));
                        }
                       /* OtherMenuAdapter adaper = new OtherMenuAdapter(otherMenus);
                        other_menu.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        other_menu.setItemAnimator(new DefaultItemAnimator());
                        other_menu.setAdapter(adaper);
*/
                        ArrayList<MyCategories> mainCategory_list = new ArrayList<>();
                        for (int i = 0; i < resultArray.length(); i++) {
                            MyCategories categories = gson.fromJson(resultArray.getJSONObject(i).toString(), MyCategories.class);
                            JSONArray resultArray2 = resultArray.getJSONObject(i).getJSONArray("subcategories");
                            ArrayList<SubCategory> subCategory_list = new ArrayList<>();
                            for (int j = 0; j < resultArray2.length(); j++) {
                                SubCategory subCategory = gson.fromJson(resultArray2.getJSONObject(j).toString(), SubCategory.class);
                                subCategory_list.add(subCategory);
                            }
                            mainCategories = new MyCategories(categories.getName(), categories.getId(), subCategory_list);
                            mainCategory_list.add(mainCategories);


//                            Log.e("####", "JSON OBJ" + new JSONObject((Map) mainCategory_list));
                        }

                        for (int k = 0; k < otherArray.length(); k++) {
                            mainCategory_list.add(new MyCategories(otherArray.get(k).toString()));
                        }

                        MainAdapter mainAdapter = new MainAdapter(MainActivity.this, mainCategory_list);
                        expandableListView.setAdapter(mainAdapter);

                        loadFragment(new Fragment_Home(MainActivity.this));
                    } else {
                        apiStatusRes(MainActivity.this, status, object);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String res) {
                apiFailure(MainActivity.this, res);
            }
        });
    }


    public void apiStatusRes(Context ctx, int status, JSONObject job) {
        if (status == 3) {
            String exceptionTitle = "", exceptionMessage = "";
            try {
                exceptionTitle = job.getString("details");
                JSONObject details = job
                        .getJSONObject("validationErrors");
                JSONArray verrors = details
                        .getJSONArray("validationerror");
                for (int i = 0; i < verrors.length(); i++) {
                    JSONObject myvalue = verrors.getJSONObject(i);
                    exceptionMessage = myvalue.getString("description");
                }
                alertMessage(ctx, exceptionTitle, exceptionMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                alertMessage(ctx, "Alert..!", job.getString("details"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void apiFailure(Context ctx, String res) {
        if (res.equals("")) {
            try {
                alertMessage(ctx, "Oops..!", "Connection failed, please try again");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                alertMessage(ctx, "Oops..!", res);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void alertMessage(Context context, String title, String msg) {
        final Dialog myDialog = new Dialog(context, R.style.ThemeDialogCustom);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.dialog_alert);
        AppCompatTextView alert_title = myDialog.findViewById(R.id.alert_title);
        AppCompatTextView alert_message = myDialog.findViewById(R.id.alert_text);
        alert_title.setText(title);
        alert_message.setText(msg);
        myDialog.findViewById(R.id.btn_alert_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

}
