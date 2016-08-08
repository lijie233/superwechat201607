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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.GoodDetailsActivity;
import cn.ucai.fulicenter.bean.CollectBean;
import cn.ucai.fulicenter.bean.NewGoodBean;
import cn.ucai.fulicenter.utils.ImageUtils;

/**
 * Created by Administrator on 2016/8/1.
 */
public class CollectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    List<CollectBean> mCollectList;
    CollectViewHolder mCollectViewHolder;
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

    public CollectAdapter(Context context, List<CollectBean> list) {
        mContext = context;
         mCollectList = new ArrayList<CollectBean>();
         mCollectList.addAll(list);
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
                holder = new CollectViewHolder(inflater.inflate(R.layout.item_good,null,false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CollectViewHolder){
            mCollectViewHolder = (CollectViewHolder) holder;
            final CollectBean collect =  mCollectList.get(position);
            ImageUtils.setGoodThumb(mContext,mCollectViewHolder.ivGoodThumb,collect.getGoodsThumb());
            // mGoodViewHolder.ivGoodThumb.setImageURI(good.getGoodsThumb());
            mCollectViewHolder.tvGoodName.setText(collect.getGoodsName());
            mCollectViewHolder.layoutGood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(mContext, GoodDetailsActivity.class).putExtra(D.GoodDetails.KEY_GOODS_ID, collect.getGoodsId()));

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
        return  mCollectList!=null? mCollectList.size()+1:1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        } else {
            return I.TYPE_ITEM;
        }
    }



    public void initData(ArrayList<CollectBean> list) {
        if ( mCollectList != null) {
             mCollectList.clear();
        }
         mCollectList.addAll(list);
        notifyDataSetChanged();
    }

    public boolean isMore() {
        return isMore;
    }

    public void addItem(ArrayList<CollectBean> collectBeanArrayList) {
         mCollectList.addAll(collectBeanArrayList);
        notifyDataSetChanged();
    }

    public void initItem(ArrayList<CollectBean> list) {
        if ( mCollectList!=null){
             mCollectList.clear();
        }
         mCollectList.addAll(list);
        notifyDataSetChanged();
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView tvFooter;
        public FooterViewHolder(View itemView) {
            super(itemView);
            tvFooter = (TextView) itemView.findViewById(R.id.tv_footer);
        }
    }
    class CollectViewHolder extends RecyclerView.ViewHolder{
        LinearLayout layoutGood;
        ImageView ivGoodThumb;
        TextView tvGoodName;
        ImageView ivDelete;
        public CollectViewHolder(View itemView) {
            super(itemView);
            layoutGood = (LinearLayout) itemView.findViewById(R.id.layout_collect);
            ivGoodThumb = (ImageView) itemView.findViewById(R.id.iv_collect_thumb);
            tvGoodName = (TextView) itemView.findViewById(R.id.tv_collect_name);
            ivDelete = (ImageView) itemView.findViewById(R.id.iv_collect_delete);
        }
    }

}
