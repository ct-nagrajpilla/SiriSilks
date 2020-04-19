package com.aniapps.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;

import com.aniapps.models.MyCategories;
import com.aniapps.siri.R;
import com.aniapps.models.SubCategory;

import java.util.ArrayList;
import java.util.List;


public class MainAdapter extends BaseExpandableListAdapter {


    private Activity context;
    private ArrayList<MyCategories> auctionsList;
    private ArrayList<MyCategories> originalList;
    private LayoutInflater inflater;

    public MainAdapter(Activity context, ArrayList<MyCategories> continentList) {
        this.context = context;
        this.auctionsList = new ArrayList<MyCategories>();
        this.auctionsList.addAll(continentList);
        this.originalList = new ArrayList<MyCategories>();
        this.originalList.addAll(continentList);
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
        return auctionsList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        int childCount = 0;
        if (auctionsList.get(i).getSubCategory().size() > 0) {
            List<SubCategory> countryList = auctionsList.get(i).getSubCategory();
            childCount = countryList.size();
        }
        return childCount;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.auctionsList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        List<SubCategory> childList = auctionsList.get(groupPosition).getSubCategory();
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
    public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {
        final ViewHolderParent viewHolderParent;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_item, null);
            viewHolderParent = new ViewHolderParent();
            viewHolderParent.tvMainCategoryName = convertView.findViewById(R.id.tv_category);
            convertView.setTag(viewHolderParent);
        } else {
            viewHolderParent = (ViewHolderParent) convertView.getTag();
        }
        viewHolderParent.tvMainCategoryName.setText(auctionsList.get(i).getName());

        if (auctionsList.get(i).getSubCategory().size() > 0) {
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

            convertView.setTag(viewHolderChild);
        } else {
            viewHolderChild = (ViewHolderChild) convertView.getTag();
        }
        viewHolderChild.tv_subCategory.setText(childItems.getName());


        return convertView;
    }


    private class ViewHolderChild {
        AppCompatTextView tv_subCategory, tv_quantity, tv_price, tv_unit, tv_notes;
        AppCompatCheckBox cb_subCategory;
        AppCompatImageView img_subCategory, iv_healthinfo;
        LinearLayout lay_child;
    }

    public class ViewHolderParent {
        CardView cardView;
        AppCompatTextView tvMainCategoryName;
    }


}
