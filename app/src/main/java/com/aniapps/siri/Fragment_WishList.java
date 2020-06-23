package com.aniapps.siri;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import java.util.Map;

import static com.aniapps.siri.AppConstants.apiFailure;
import static com.aniapps.siri.AppConstants.apiStatusRes;
import static com.aniapps.siri.MainActivity.bottomNavigationView;
import static com.aniapps.siri.MainActivity.refreshMenu;
import static com.aniapps.siri.MainActivity.showBadge;

public class Fragment_WishList extends Fragment {

    RecyclerView rc_wishlist;
    AppCompatTextView tv_nowishlist;
    AppCompatTextView tv_place_order;
    WishListAdapter wishListAdapter;
    ArrayList<MyProduct> myProducts = new ArrayList<>();
    View menu_cart;

    public Fragment_WishList(View menu_cart) {
        this.menu_cart = menu_cart;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        rc_wishlist = view.findViewById(R.id.rc_wishlist);
        tv_nowishlist = view.findViewById(R.id.tv_nowishlist);
        tv_place_order = view.findViewById(R.id.tv_placeorder);
        rc_wishlist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rc_wishlist.setItemAnimator(new DefaultItemAnimator());
        rc_wishlist.setNestedScrollingEnabled(false);
        rc_wishlist.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        getWishList();
        return view;
    }


    class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {

        ArrayList<MyProduct> newArrivals;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public AppCompatTextView name, price_offer, tv_addcart;

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

        WishListAdapter(ArrayList<MyProduct> newArrivals) {
            this.newArrivals = newArrivals;
        }

        @NonNull
        @Override
        public WishListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.home_products, parent, false);

            return new WishListAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull WishListAdapter.ViewHolder holder, int position) {
            MyProduct products = newArrivals.get(position);
            SetData(holder, products, position);

        }

        @Override
        public int getItemCount() {
            return newArrivals.size();
        }


    }
    int imageItem_height_calculation = 0;
    float imageheight = 0;
    public void SetData(final WishListAdapter.ViewHolder holder, final MyProduct products, final int pos) {
        holder.name.setText(products.getProduct_name());
        holder.price_offer.setText(rupeeFormat(products.getProduct_price()));
        if (imageItem_height_calculation == 0) {
            imageItem_height_calculation = 1;
            holder.thumbnail.getLayoutParams().height = (int) ((float) ((getActivity()
                    .getResources().getDisplayMetrics().widthPixels - AppConstants
                    .dpToPx(10, getActivity())) / 2) / 0.75);
            imageheight = (int) ((float) ((getActivity()
                    .getResources().getDisplayMetrics().widthPixels - AppConstants
                    .dpToPx(10, getActivity())) / 2) / 0.75);
        } else {
            holder.thumbnail.getLayoutParams().height = (int) imageheight;
        }
        try {
            Glide.with(getActivity())
                    .load(products.getProduct_image())
                    .placeholder(R.drawable.no_image_gallery)
                    .fitCenter()
                    .into(holder.thumbnail);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.product_fav.setBackgroundDrawable(getResources().getDrawable(R.mipmap.fav_done2));
        holder.tv_addcart.setText("ADD TO CART");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), DetailsPage.class);
                startActivity(i);
            }
        });
        holder.product_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Pref.getIn().getUser_id().equals("")) {
                    wishList(products, pos);
                } else {
                    Intent in_login = new Intent(getActivity(), LoginPage.class);
                    startActivity(in_login);
                }
            }
        });
        holder.tv_addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Pref.getIn().getUser_id().equals("")) {
                    addCart(products, pos);
                } else {
                    Intent in_login = new Intent(getActivity(), LoginPage.class);
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

    public void addCart(final MyProduct products, final int pos) {
        Map<String, String> params = new HashMap<>();
        params.put("action", getResources().getString(R.string.addtocart));
        params.put("product_id", products.getProduct_id());
        params.put("cart", "y");
        RetrofitClient.getInstance().doBackProcess(getActivity(), params, "", new APIResponse() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onSuccess(String Response) {
                try {
                    int status;
                    JSONObject object = new JSONObject(Response);
                    status = object.getInt("status");
                    if (status == 1) {
                        Toast.makeText(getActivity(), "Successfully added to cart", Toast.LENGTH_SHORT).show();
                        myProducts.remove(pos);
                        wishListAdapter.notifyDataSetChanged();
                        refreshMenu(object.getInt("cart_count"));
                        Pref.getIn().setCart_count(object.getInt("cart_count"));
                    } else {
                        AppConstants.apiStatusRes(getActivity(), status, object);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String res) {
                AppConstants.apiFailure(getActivity(), res);
            }
        });
    }

    public void getWishList() {
        final HashMap<String, String> params = new HashMap<>();
        params.put("action", getResources().getString(R.string.loadallwishlist));
        RetrofitClient.getInstance().doBackProcess(getActivity(), params, "", new APIResponse() {
            @Override
            public void onSuccess(String res) {
                try {
                    JSONObject jsonobject = new JSONObject(res);
                    int status = jsonobject.getInt("status");
                    if (status == 1) {
                        JSONObject object = jsonobject.getJSONObject("list");
                        JSONArray productList = object.getJSONArray("products");
                        if (productList.length() != 0) {
                            for (int i = 0; i < productList.length(); i++) {
                                MyProduct myObject = new Gson().fromJson(productList
                                        .getJSONObject(0).toString(), MyProduct.class);
                                myProducts.add(myObject);
                            }
                            rc_wishlist.setVisibility(View.VISIBLE);
                            tv_nowishlist.setVisibility(View.GONE);
                            wishListAdapter = new WishListAdapter(myProducts);
                            rc_wishlist.setAdapter(wishListAdapter);
                        } else {
                            tv_nowishlist.setVisibility(View.VISIBLE);
                            rc_wishlist.setVisibility(View.GONE);
                        }
                    } else {
                        apiStatusRes(getActivity(), status, jsonobject);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String res) {
                Log.e("####", "In Failuer");
                apiFailure(getActivity(), res);
            }
        });
    }

    public void wishList(final MyProduct products, final int pos) {
        Map<String, String> params = new HashMap<>();
        params.put("action", getResources().getString(R.string.wishlist_api));
        params.put("product_id", products.getProduct_id());
        params.put("wishlist", "n");
        RetrofitClient.getInstance().doBackProcess(getActivity(), params, "", new APIResponse() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onSuccess(String Response) {
                try {
                    int status;
                    JSONObject object = new JSONObject(Response);
                    status = object.getInt("status");
                    if (status == 1) {
                        showBadge(getActivity(), bottomNavigationView, R.id.navigation_wishlist, object.getInt("wishlist_count"));
                        Toast.makeText(getActivity(), "Successfully removed from wish list", Toast.LENGTH_SHORT).show();
                        myProducts.remove(pos);
                        wishListAdapter.notifyDataSetChanged();
                    } else {
                        AppConstants.apiStatusRes(getActivity(), status, object);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String res) {
                apiFailure(getActivity(), res);
            }
        });
    }


}
