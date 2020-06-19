package com.aniapps.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;

import com.aniapps.models.MyCategories;
import com.aniapps.models.SubCategory;
import com.aniapps.siri.ListingPage;
import com.aniapps.siri.R;

import java.util.ArrayList;
import java.util.List;

import static com.aniapps.siri.MainActivity.drawer;


public class MenuAdapter extends BaseExpandableListAdapter {


    private Activity context;
    private ArrayList<MyCategories> productList;
    private ArrayList<MyCategories> originalList;
    private ExpandableListView expandableListView;

    public MenuAdapter(Activity context, ArrayList<MyCategories> continentList, ExpandableListView expandableListView) {
        this.context = context;
        this.productList = new ArrayList<MyCategories>();
        this.productList.addAll(continentList);
        this.originalList = new ArrayList<MyCategories>();
        this.originalList.addAll(continentList);
        this.expandableListView = expandableListView;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    @Override
    public int getGroupCount() {
        return productList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        int childCount = 0;
        if (productList.get(i).getSubCategory().size() > 0) {
            List<SubCategory> countryList = productList.get(i).getSubCategory();
            childCount = countryList.size();
        }
        return childCount;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.productList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        List<SubCategory> childList = productList.get(groupPosition).getSubCategory();
        return childList.get(childPosititon);

    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public View getGroupView(final int i, final boolean b, View convertView, final ViewGroup viewGroup) {
        final ViewHolderParent viewHolderParent;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_item, null);
            viewHolderParent = new ViewHolderParent();
            viewHolderParent.tvMainCategoryName = convertView.findViewById(R.id.tv_category);
            viewHolderParent.cardView = convertView.findViewById(R.id.group_card);
            convertView.setTag(viewHolderParent);
        } else {
            viewHolderParent = (ViewHolderParent) convertView.getTag();
        }
        viewHolderParent.tvMainCategoryName.setText(productList.get(i).getName());
        if (productList.get(i).getSubCategory().size() > 0) {
            if (b) {
                viewHolderParent.tvMainCategoryName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_minus, 0);
                viewHolderParent.tvMainCategoryName.setCompoundDrawablePadding(5);
            } else {
                viewHolderParent.tvMainCategoryName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_plus, 0);
                viewHolderParent.tvMainCategoryName.setCompoundDrawablePadding(5);
            }
        } else {
            viewHolderParent.tvMainCategoryName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);


        }

        viewHolderParent.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (productList.get(i).getName()) {
                    case "Cart":
                        drawer.closeDrawer(GravityCompat.START);
                        Toast.makeText(context, "Clickec on " + productList.get(i).getName(), Toast.LENGTH_SHORT).show();
                        break;
                    case "Privacy Policy":
                        drawer.closeDrawer(GravityCompat.START);
                        Toast.makeText(context, "Clickec on " + productList.get(i).getName(), Toast.LENGTH_SHORT).show();
                        break;
                    case "Terms & Conditions":
                        drawer.closeDrawer(GravityCompat.START);
                        Toast.makeText(context, "Clickec on " + productList.get(i).getName(), Toast.LENGTH_SHORT).show();
                        break;
                    case "About Us":
                        drawer.closeDrawer(GravityCompat.START);
                        Toast.makeText(context, "Clickec on " + productList.get(i).getName(), Toast.LENGTH_SHORT).show();
                        break;
                    case "Contact Us":
                        drawer.closeDrawer(GravityCompat.START);
                        Toast.makeText(context, "Clickec on " + productList.get(i).getName(), Toast.LENGTH_SHORT).show();
                        break;
                    case "Subscribe":
                        drawer.closeDrawer(GravityCompat.START);
                        Toast.makeText(context, "Clickec on " + productList.get(i).getName(), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        if (productList.get(i).getSubCategory().size() <= 0) {
                            drawer.closeDrawer(GravityCompat.START);
                            Intent in = new Intent(context, ListingPage.class);
                            in.putExtra("area_id", "" + productList.get(i).getId());
                            in.putExtra("area_name", productList.get(i).getName());
                            in.putExtra("category_id", "");
                            in.putExtra("category_name", "");
                            context.startActivity(in);
                        } else {
                            if (expandableListView.isGroupExpanded(i)) {
                                expandableListView.collapseGroup(i);
                            } else {
                                expandableListView.expandGroup(i);
                            }
                        }
                        break;

                }
            }
        });

        return convertView;
    }


    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final SubCategory childItems = (SubCategory) getChild(groupPosition, childPosition);
        final ViewHolderChild viewHolderChild;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_item, null);
            viewHolderChild = new ViewHolderChild();
            viewHolderChild.tv_subCategory = convertView.findViewById(R.id.tv_subcat);
            viewHolderChild.child_cardView = convertView.findViewById(R.id.child_card);
            convertView.setTag(viewHolderChild);
        } else {
            viewHolderChild = (ViewHolderChild) convertView.getTag();
        }
        viewHolderChild.tv_subCategory.setText(childItems.getName());
        viewHolderChild.child_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                Intent in = new Intent(context, ListingPage.class);
                in.putExtra("area_id", "" + productList.get(groupPosition).getId());
                in.putExtra("area_name", productList.get(groupPosition).getName());
                in.putExtra("category_id", "" + childItems.getId());
                in.putExtra("category_name", childItems.getName());
                context.startActivity(in);
            }
        });

        return convertView;
    }


    private class ViewHolderChild {
        AppCompatTextView tv_subCategory;
        CardView child_cardView;
    }

    public class ViewHolderParent {
        CardView cardView;
        AppCompatTextView tvMainCategoryName;
    }


}
