package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.FieldPosition;
import java.util.ArrayList;
import java.util.List;

import cn.ucai.bean.NewGoodBean;
import cn.ucai.fulicenter.R;

/**
 * Created by Administrator on 2016/8/1.
 */
public class GoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    List<NewGoodBean> mGoodList;

    public GoodsAdapter(Context context, List<NewGoodBean> list) {
        this.mContext = context;
        mGoodList = new ArrayList<NewGoodBean>();
        mGoodList.addAll(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder=null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        holder = new GoodViewHolder(inflater.inflate(R.layout.item_good, null, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GoodViewHolder) {
            NewGoodBean good = mGoodList.get(position);
            ((GoodViewHolder) holder).tvGoodName.setText(good.getGoodsName());
            ((GoodViewHolder) holder).tvGoodPrice.setText(good.getCurrencyPrice());
        }
    }

    @Override
    public int getItemCount() {
        return mGoodList.size();
    }

    class GoodViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        ImageView ivGoodThumb;
        TextView tvGoodName,tvGoodPrice;
        public GoodViewHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.layout_good);
            ivGoodThumb = (ImageView) itemView.findViewById(R.id.iv_good_thumb);
            tvGoodName = (TextView) itemView.findViewById(R.id.tv_good_name);
            tvGoodPrice = (TextView) itemView.findViewById(R.id.tv_good_price);
        }
    }

}
