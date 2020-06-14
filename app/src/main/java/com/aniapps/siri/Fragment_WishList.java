package com.aniapps.siri;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aniapps.models.Products;
import com.aniapps.utils.Pref;
import com.aniapps.utils.quatity.NumberPicker;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Fragment_WishList extends Fragment {

    RecyclerView rc_wishlist;
    AppCompatTextView tv_place_order;
    WishListAdapter wishListAdapter;
    ArrayList<Products> myProducts = new ArrayList<>();
    AppCompatImageView heart;
    View menu_cart;

    public Fragment_WishList(View menu_cart, AppCompatImageView heart) {
        this.menu_cart = menu_cart;
        this.heart=heart;
        // Required empty public constructor
    }
/*
    public static Fragment_WishList newInstance(String param1, String param2) {
        Fragment_WishList fragment = new Fragment_WishList(menu_cart);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        rc_wishlist = view.findViewById(R.id.rc_wishlist);
        tv_place_order = view.findViewById(R.id.tv_placeorder);
        rc_wishlist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rc_wishlist.setItemAnimator(new DefaultItemAnimator());
        rc_wishlist.setNestedScrollingEnabled(false);

        for (int i = 0; i < 10; i++) {
            Products products = new Products();
            products.setProduct_name("Light blue wovern banarasi sareen with blouse");
            products.setProduct_company("Siri Sarees");
            products.setProduct_fav("y");
            products.setProduct_id("" + i + 1000);
            products.setProduct_image("https://5.imimg.com/data5/BK/GE/MY-4197884/rajtex-art-silk-saree-32001-32012-500x500.jpg");
            products.setProduct_img_count("0");
            products.setProduct_msg("IN STOCK! HURRY");
            products.setProduct_price("23500");
            products.setProduct_quantity("10");
            myProducts.add(products);
        }
        wishListAdapter = new WishListAdapter(myProducts, heart);
        rc_wishlist.setAdapter(wishListAdapter);
        return view;
    }


    class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {
        ArrayList<Products> myProducts = new ArrayList<>();
        AppCompatImageView heart;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public AppCompatTextView product_title, product_price, product_company, product_message, product_addcart;
            public CardView product_card;
            AppCompatImageView product_image, product_fav;
            NumberPicker product_quantity;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                product_title = itemView.findViewById(R.id.tv_product_title);
                product_image = itemView.findViewById(R.id.product_image);
                product_fav = itemView.findViewById(R.id.product_fav);
                product_price = itemView.findViewById(R.id.tv_product_price);
                product_company = itemView.findViewById(R.id.product_company);
                product_quantity = itemView.findViewById(R.id.product_quantity);
                product_message = itemView.findViewById(R.id.product_message);
                product_addcart = itemView.findViewById(R.id.product_addcart);
                product_card = itemView.findViewById(R.id.product_card);
            }
        }

        public WishListAdapter(ArrayList<Products> myProducts, AppCompatImageView heart) {
            this.myProducts = myProducts;
            this.heart = heart;
        }


        @NonNull
        @Override
        public WishListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_item, parent, false);

            return new WishListAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull WishListAdapter.ViewHolder holder, int position) {
            SetData(holder, myProducts.get(position));

        }

        @Override
        public int getItemCount() {
            return myProducts.size();
        }


    }

    public void SetData(WishListAdapter.ViewHolder holder, Products products) {

        holder.product_title.setText(products.getProduct_name());
        try {
            Glide.with(getActivity())
                    .load(products.getProduct_image())
                    .placeholder(R.drawable.no_image_gallery)
                    .fitCenter()
                    .into(holder.product_image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Pref.getIn().getSelected_count() > 0) {
            ((AppCompatTextView) getActivity().findViewById(R.id.filter_count)).setVisibility(View.VISIBLE);
            ((AppCompatTextView) getActivity().findViewById(R.id.filter_count)).setText("" + Pref.getIn().getSelected_count());
        } else {
            ((AppCompatTextView) getActivity().findViewById(R.id.filter_count)).setVisibility(View.GONE);
        }


        holder.product_price.setText(rupeeFormat(products.getProduct_price()));
        holder.product_message.setText(products.getProduct_msg());
        holder.product_quantity.setMax(Integer.parseInt(products.getProduct_quantity()));
        holder.product_addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Clicked Add Cart", Toast.LENGTH_SHORT).show();
            }
        });
        holder.product_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), DetailsPage.class);
                startActivity(i);
            }
        });

        holder.product_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  int width = 0;
                int[] locations = new int[2];
                v.getLocationOnScreen(locations);
                CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(locations[0],
                        locations[1] - dpToPx(30, getActivity()), 0, 0);
                width = dpToPx(7, getActivity());
                heart.setLayoutParams(layoutParams);
                heart.setVisibility(View.VISIBLE);
                scaleAnimation(v, width);*/
               Pref.getIn().setSelected_count(Pref.getIn().getSelected_count() + 1);

             //   Pref.getIn().setSelected_count(Pref.getIn().getSelected_count() - 1);
              ((AppCompatTextView) getActivity().findViewById(R.id.filter_count)).setText("" + Pref.getIn().getSelected_count());
            }
        });

    }

    public int dpToPx(int dp, Activity context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (dp * scale + 0.5f);
        return pixels;
    }

    private void scaleAnimation(final View heart2, int width) {
        int startViewLocation[] = new int[2];
        heart2.getLocationInWindow(startViewLocation);
        int finishViewLocation[] = new int[2];
        menu_cart.getLocationInWindow(finishViewLocation);
        int startX = startViewLocation[0] + heart2.getWidth() / 2;
        int startY = startViewLocation[1] + heart2.getHeight() / 2;
        int endX = finishViewLocation[0] + menu_cart.getWidth() / 2;
        int endY = finishViewLocation[1] + menu_cart.getHeight() / 2;
        TranslateAnimation animation = new TranslateAnimation(0,
                finishViewLocation[0] - startViewLocation[0] + width, 0,
                finishViewLocation[1] - startViewLocation[1] + (width / 2));
        animation.setDuration(1000);
        // animation.setStartOffset(50);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                heart.setVisibility(View.GONE);
                Animation fadeIn = new AlphaAnimation(0, 1);
                fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
                fadeIn.setDuration(1000);
                menu_cart.startAnimation(fadeIn);
            }
        });
        heart.startAnimation(animation);
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
