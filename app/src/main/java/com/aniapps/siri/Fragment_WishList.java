package com.aniapps.siri;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aniapps.models.MyProduct;
import com.aniapps.utils.Pref;
import com.aniapps.utils.quatity.NumberPicker;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Fragment_WishList extends Fragment {

    RecyclerView rc_wishlist;
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        rc_wishlist = view.findViewById(R.id.rc_wishlist);
        tv_place_order = view.findViewById(R.id.tv_placeorder);
        rc_wishlist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rc_wishlist.setItemAnimator(new DefaultItemAnimator());
        rc_wishlist.setNestedScrollingEnabled(false);

        for (int i = 0; i < 10; i++) {
            MyProduct products = new MyProduct();
            products.setProduct_name("Light blue wovern banarasi sareen with blouse");
            products.setProduct_company("Siri Sarees");
            products.setWishlist("y");
            products.setProduct_id("" + i + 1000);
            products.setProduct_image("https://5.imimg.com/data5/BK/GE/MY-4197884/rajtex-art-silk-saree-32001-32012-500x500.jpg");
            products.setProduct_img_count("0");
            products.setProduct_msg("IN STOCK! HURRY");
            products.setProduct_price("23500");
            products.setProduct_quantity("10");
            myProducts.add(products);
        }
        wishListAdapter = new WishListAdapter(myProducts);
        rc_wishlist.setAdapter(wishListAdapter);
        return view;
    }


    class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {
        ArrayList<MyProduct> myProducts = new ArrayList<>();

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

        public WishListAdapter(ArrayList<MyProduct> myProducts) {
            this.myProducts = myProducts;
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

    public void SetData(WishListAdapter.ViewHolder holder, MyProduct products) {

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
        if (Pref.getIn().getCart_count() > 0) {
            ((AppCompatTextView) getActivity().findViewById(R.id.filter_count)).setVisibility(View.VISIBLE);
            ((AppCompatTextView) getActivity().findViewById(R.id.filter_count)).setText("" + Pref.getIn().getCart_count());
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
               // Pref.getIn().setWish_count(Pref.getIn().getWish_count() + 1);
               // ((AppCompatTextView) getActivity().findViewById(R.id.filter_count)).setText("" + Pref.getIn().getWish_count());
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
