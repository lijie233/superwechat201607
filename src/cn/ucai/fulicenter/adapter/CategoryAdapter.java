package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.bean.CategoryChildBean;
import cn.ucai.bean.CategoryGroupBean;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.ImageUtils;

/**
 * Created by Administrator on 2016/8/4.
 */
public class CategoryAdapter extends BaseExpandableListAdapter{
    Context mContext;
    List<CategoryGroupBean> mGroupList;
    List<ArrayList<CategoryChildBean>> mChildList;

    public CategoryAdapter(List<ArrayList<CategoryChildBean>> mChildList, Context mContext, List<CategoryGroupBean> mGroupList) {
        this.mContext = mContext;
        this.mChildList = new ArrayList<ArrayList<CategoryChildBean>>();
        mChildList.addAll(mChildList);
        this.mGroupList = new ArrayList<CategoryGroupBean>();
        mGroupList.addAll(mGroupList);
    }

    @Override
    public int getGroupCount() {
        return mGroupList != null ? mGroupList.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildList.get(groupPosition).size();
    }

    @Override
    public CategoryGroupBean getGroup(int groupPosition) {
        if (mGroupList != null) {
            return mGroupList.get(groupPosition);
        } else {
            return null;
        }
    }

    @Override
    public CategoryChildBean getChild(int groupPosition, int childPosition) {
        if (mChildList.get(groupPosition) != null && mChildList.get(groupPosition).get(childPosition) != null) {
            return mChildList.get(groupPosition).get(childPosition);
        } else {
            return null;
        }
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder=null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_category_group, null);
            holder = new GroupViewHolder();
            holder.ivGroupThumb = (ImageView) convertView.findViewById(R.id.iv_group_thumb);
            holder.tvGroupName = (TextView) convertView.findViewById(R.id.tv_group_name);
            holder.ivindicator = (ImageView) convertView.findViewById(R.id.iv_indicator);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
            CategoryGroupBean group = getGroup(groupPosition);
            ImageUtils.setGroupCategoryImage(mContext, holder.ivGroupThumb, group.getImageUrl());
            holder.tvGroupName.setText(group.getName());
        if (isExpanded) {
            holder.ivindicator.setImageResource(R.drawable.expand_off);
        } else {
            holder.ivindicator.setImageResource(R.drawable.expand_on);
        }
            convertView.setTag(holder);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ChildViewHolder holder=null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_category_child, null);
            holder = new ChildViewHolder();
            holder.ivCategoryChildThumb = (ImageView) convertView.findViewById(R.id.iv_category_child_thumb);
            holder.tvCategoryChildName = (TextView) convertView.findViewById(R.id.tv_category_child_name);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        CategoryChildBean child = getChild(groupPosition, childPosition);
        if (child != null) {
            ImageUtils.setChildCategoryImage(mContext, holder.ivCategoryChildThumb, child.getImageUrl());
            holder.tvCategoryChildName.setText(child.getName());
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();

        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    class GroupViewHolder {
        ImageView ivGroupThumb;
        TextView tvGroupName;
        ImageView ivindicator;
    }

    class ChildViewHolder {
        RelativeLayout layoutCategoryChild;
        ImageView ivCategoryChildThumb;
        TextView tvCategoryChildName;
    }
}
