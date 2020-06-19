package com.aniapps.siri;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aniapps.callbackclient.APIResponse;
import com.aniapps.callbackclient.RetrofitClient;
import com.aniapps.models.MyProduct;
import com.aniapps.utils.Pref;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListingPage extends AppConstants {
    AppCompatTextView tv_main_header, tv_sub_header;
    AppCompatImageView ic_close;
    LinearLayout cl_sort;
    RecyclerView rc_main;
    String area_id = "", category_id = "";
    MainAdapter mainAdapter;
    ArrayList<MyProduct> listingProducts = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listingpage);
        area_id = getIntent().getStringExtra("area_id");
        category_id = getIntent().getStringExtra("category_id");
        tv_main_header = findViewById(R.id.tv_main_header);
        tv_sub_header = findViewById(R.id.tv_sub_header);
        ic_close = findViewById(R.id.close_back);
        cl_sort = findViewById(R.id.cl_sort);
        rc_main = findViewById(R.id.rc_main);
        rc_main.setLayoutManager(new GridLayoutManager(this, 2));
        loadallListing();
        tv_main_header.setText(getIntent().getStringExtra("area_name"));
        if (!getIntent().getStringExtra("category_name").equals("")) {
            tv_sub_header.setVisibility(View.VISIBLE);
            tv_sub_header.setText(getIntent().getStringExtra("category_name"));
        }
        ic_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (Pref.getIn().getCart_count() > 0) {
            ((AppCompatTextView) findViewById(R.id.filter_count)).setVisibility(View.VISIBLE);
            ((AppCompatTextView) findViewById(R.id.filter_count)).setText("" + Pref.getIn().getCart_count());
        } else {
            ((AppCompatTextView) findViewById(R.id.filter_count)).setVisibility(View.GONE);
        }


    }

    public void loadallListing() {
        final HashMap<String, String> params = new HashMap<>();
        params.put("action", getResources().getString(R.string.loadallproducts));
        params.put("product_id", "");
        params.put("area_id", "" + area_id);
        params.put("category_id", "" + category_id);
        Log.e("Post params", "" + params);
        RetrofitClient.getInstance().doBackProcess(ListingPage.this, params, "", new APIResponse() {
            @Override
            public void onSuccess(String res) {
                try {
                    JSONObject jsonobject = new JSONObject(res);
                    int status = jsonobject.getInt("status");
                    if (status == 1) {
                        JSONObject mainObj = jsonobject.getJSONObject("list");
                        JSONArray products_list = mainObj.getJSONArray("products");
                        for (int i = 0; i < products_list.length(); i++) {
                            MyProduct myObject = new Gson().fromJson(products_list
                                    .getJSONObject(i).toString(), MyProduct.class);
                            listingProducts.add(myObject);
                        }
                        mainAdapter = new MainAdapter(listingProducts);
                        rc_main.setAdapter(mainAdapter);
                    } else {
                        apiStatusRes(ListingPage.this, status, jsonobject);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String res) {
                apiFailure(ListingPage.this, res);
            }
        });
    }


    class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

        ArrayList<MyProduct> myproducts;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView name, price_offer, tv_addcart;

            public CardView cardView;
            AppCompatImageView thumbnail, product_fav;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.title);
                price_offer = itemView.findViewById(R.id.offer_price);
                product_fav = itemView.findViewById(R.id.product_fav);
                tv_addcart = itemView.findViewById(R.id.tv_addcart);
                cardView = itemView.findViewById(R.id.myCard);
                thumbnail = itemView.findViewById(R.id.thumbnail);

            }
        }

        MainAdapter(ArrayList<MyProduct> myproducts) {
            this.myproducts = myproducts;
        }

        @NonNull
        @Override
        public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.home_products, parent, false);

            return new MainAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
            MyProduct products = myproducts.get(position);
            SetData(holder, products);

        }

        @Override
        public int getItemCount() {
            return myproducts.size();
        }


    }

    public void SetData(final MainAdapter.ViewHolder holder, MyProduct products) {
        holder.name.setText(products.getProduct_name());
        holder.price_offer.setText(rupeeFormat(products.getProduct_price()));
        try {
            Glide.with(ListingPage.this)
                    .load(products.getProduct_image())
                    .placeholder(R.drawable.no_image_gallery)
                    .fitCenter()
                    .into(holder.thumbnail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListingPage.this, DetailsPage.class);
                startActivity(i);
            }
        });
        holder.product_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Pref.getIn().getUser_id().equals("")) {
                    Animation animBlink = AnimationUtils.loadAnimation(ListingPage.this,
                            R.anim.blink);
                    animBlink.setDuration(1000);
                    holder.product_fav.startAnimation(animBlink);
                    holder.product_fav.setBackgroundDrawable(getResources().getDrawable(R.mipmap.fav_done2));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.product_fav.clearAnimation();
                        }
                    }, 1000);
                } else {
                    Intent in_login = new Intent(ListingPage.this, LoginPage.class);
                    startActivity(in_login);
                }
            }
        });
        holder.tv_addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Pref.getIn().getUser_id().equals("")) {
                    Toast.makeText(ListingPage.this, "clicked on cart", Toast.LENGTH_SHORT).show();
                    holder.tv_addcart.setText("REMOVE CART");
                } else {
                    Intent in_login = new Intent(ListingPage.this, LoginPage.class);
                    startActivity(in_login);
                }
            }
        });
    }

    public static String rupeeFormat(String value) {
        value = value.replace(",", "").replace("₹ ", "").replace("₹", "");
        char lastDigit = value.charAt(value.length() - 1);
        String result = "";
        int len = value.length() - 1;
        int nDigits = 0;

        for (int i = len - 1; i >= 0; i--) {
            result = value.charAt(i) + result;
            nDigits++;
            if (((nDigits % 2) == 0) && (i > 0)) {
                result = "," + result;
            }
        }
        return ("₹ " + result + lastDigit);
    }


}
