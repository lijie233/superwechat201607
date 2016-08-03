package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.ucai.D;
import cn.ucai.I;
import cn.ucai.bean.BoutiqueBean;
import cn.ucai.bean.NewGoodBean;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.GoodDetailsActivity;
import cn.ucai.fulicenter.utils.ImageUtils;

/**
 * Created by Administrator on 2016/8/1.
 */
public class BoutiqueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    List<BoutiqueBean> mBoutiqueBean;
    BoutiqueHolder mBoutiqueHolder;
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

    public BoutiqueAdapter(Context context, List<BoutiqueBean> list) {
        mContext = context;
        mBoutiqueBean = new ArrayList<BoutiqueBean>();
        mBoutiqueBean.addAll(list);
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
                holder = new BoutiqueHolder(inflater.inflate(R.layout.item_boutique,null,false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BoutiqueHolder){
            mBoutiqueHolder = (BoutiqueHolder) holder;
            final BoutiqueBean good = mBoutiqueBean.get(position);
            ImageUtils.setGoodThumb(mContext,mBoutiqueHolder.ivBoutiqueImg,good.getImageurl());
            mBoutiqueHolder.tvTitle.setText(good.getTitle());
            mBoutiqueHolder.tvName.setText(good.getName());
            mBoutiqueHolder.tvDesc.setText(good.getDescription());
//            mBoutiqueHolder.layoutBoutique.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mContext.startActivity(new Intent(mContext, BoutiqueActivity.class).putExtra(D.GoodDetails.KEY_GOODS_ID, good.getGoodsId()));
//
//                }
//            });
        }
        if (holder instanceof FooterViewHolder) {
            mFooterViewHolder= (FooterViewHolder) holder;
            mFooterViewHolder.tvFooter.setText(footerString);
        }
    }

    @Override
    public int getItemCount() {
        return mBoutiqueBean!=null?mBoutiqueBean.size()+1:1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {
            return I.TYPE_ITEM;
        }
    }



    public void initData(ArrayList<BoutiqueBean> list) {
        if (mBoutiqueBean != null) {
            mBoutiqueBean.clear();
        }
        mBoutiqueBean.addAll(list);
        notifyDataSetChanged();
    }

    public boolean isMore() {
        return isMore;
    }

    public void addItem(ArrayList<BoutiqueBean> goodBeanArrayList) {
        mBoutiqueBean.addAll(goodBeanArrayList);
        notifyDataSetChanged();
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView tvFooter;
        public FooterViewHolder(View itemView) {
            super(itemView);
            tvFooter = (TextView) itemView.findViewById(R.id.tv_footer);
        }
    }
    class BoutiqueHolder extends RecyclerView.ViewHolder{
        RelativeLayout layoutBoutique;
        ImageView ivBoutiqueImg;
        TextView tvTitle;
        TextView tvName;
        TextView tvDesc;
        public BoutiqueHolder(View itemView) {
            super(itemView);
            layoutBoutique = (RelativeLayout) itemView.findViewById(R.id.layout_boutique_item);
            ivBoutiqueImg = (ImageView) itemView.findViewById(R.id.ivBoutiqueImg);
            tvTitle = (TextView) itemView.findViewById(R.id.tvBoutiqueTitle);
            tvName = (TextView) itemView.findViewById(R.id.tvBoutiqueName);
            tvDesc = (TextView) itemView.findViewById(R.id.tvBoutiqueDescription);
        }
    }

}
