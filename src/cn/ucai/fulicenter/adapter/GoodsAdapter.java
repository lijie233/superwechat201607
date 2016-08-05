package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.FieldPosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.ucai.D;
import cn.ucai.I;
import cn.ucai.bean.NewGoodBean;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.GoodDetailsActivity;
import cn.ucai.fulicenter.utils.ImageUtils;

/**
 * Created by Administrator on 2016/8/1.
 */
public class GoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    List<NewGoodBean> mGoodList;
    GoodViewHolder mGoodViewHolder;
    FooterViewHolder mFooterViewHolder;
    boolean isMore;
    String footerString;

    public void setFooterString(String footerString) {
        this.footerString = footerString;
        notifyDataSetChanged();
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public GoodsAdapter(Context context, List<NewGoodBean> list) {
        mContext = context;
        mGoodList = new ArrayList<NewGoodBean>();
        mGoodList.addAll(list);
        soryByAddTime();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        switch (viewType) {
            case I.TYPE_FOOTER:
                holder = new FooterViewHolder(inflater.inflate(R.layout.item_footer, parent, false));
                break;
            case I.TYPE_ITEM:
                holder = new GoodViewHolder(inflater.inflate(R.layout.item_good,null,false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GoodViewHolder){
            mGoodViewHolder = (GoodViewHolder) holder;
            final NewGoodBean good = mGoodList.get(position);
            ImageUtils.setGoodThumb(mContext,mGoodViewHolder.ivGoodThumb,good.getGoodsThumb());
            // mGoodViewHolder.ivGoodThumb.setImageURI(good.getGoodsThumb());
            mGoodViewHolder.tvGoodName.setText(good.getGoodsName());
            mGoodViewHolder.tvGoodPrice.setText(good.getCurrencyPrice());
            mGoodViewHolder.layoutGood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(mContext, GoodDetailsActivity.class).putExtra(D.GoodDetails.KEY_GOODS_ID, good.getGoodsId()));

                }
            });
        }
        if (holder instanceof FooterViewHolder) {
            mFooterViewHolder= (FooterViewHolder) holder;
            mFooterViewHolder.tvFooter.setText(footerString);
        }
    }

    @Override
    public int getItemCount() {
        return mGoodList!=null?mGoodList.size()+1:1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {
            return I.TYPE_ITEM;
        }
    }



    public void initData(ArrayList<NewGoodBean> list) {
        if (mGoodList != null) {
            mGoodList.clear();
        }
        mGoodList.addAll(list);
        notifyDataSetChanged();
    }

    public boolean isMore() {
        return isMore;
    }

    public void addItem(ArrayList<NewGoodBean> goodBeanArrayList) {
        mGoodList.addAll(goodBeanArrayList);
        soryByAddTime();
        notifyDataSetChanged();
    }

    public void initItem(ArrayList<NewGoodBean> list) {
        if (mGoodList!=null){
            mGoodList.clear();
        }
        mGoodList.addAll(list);
        sortByAddTime();
        notifyDataSetChanged();
    }

    private void sortByAddTime(){
        Collections.sort(mGoodList, new Comparator<NewGoodBean>() {
            @Override
            public int compare(NewGoodBean goodLeft, NewGoodBean goodRight) {
                return (int)(Long.valueOf(goodRight.getAddTime())-Long.valueOf(goodLeft.getAddTime()));
            }
        });
    }



    class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView tvFooter;
        public FooterViewHolder(View itemView) {
            super(itemView);
            tvFooter = (TextView) itemView.findViewById(R.id.tv_footer);
        }
    }
    class GoodViewHolder extends RecyclerView.ViewHolder{
        LinearLayout layoutGood;
        ImageView ivGoodThumb;
        TextView tvGoodName;
        TextView tvGoodPrice;
        public GoodViewHolder(View itemView) {
            super(itemView);
            layoutGood = (LinearLayout) itemView.findViewById(R.id.layout_good);
            ivGoodThumb = (ImageView) itemView.findViewById(R.id.iv_good_thumb);
            tvGoodName = (TextView) itemView.findViewById(R.id.tv_good_name);
            tvGoodPrice = (TextView) itemView.findViewById(R.id.tv_good_price);
        }
    }
    private void soryByAddTime() {
        Collections.sort(mGoodList, new Comparator<NewGoodBean>() {
            @Override
            public int compare(NewGoodBean goodLeft, NewGoodBean goodRight) {
                return (int)(Long.valueOf(goodRight.getAddTime())-Long.valueOf(goodLeft.getAddTime()));
            }
        });
    }
}
