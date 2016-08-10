package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.BoutiqueActivity;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.utils.ImageUtils;

/**
 * Created by Administrator on 2016/8/1.
 */
public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    List<CartBean> mCartBean;
    CartHolder mCartHolder;
    boolean isMore;

    public void setMore(boolean more) {
        isMore = more;
    }

    public CartAdapter(Context context, List<CartBean> list) {
        mContext = context;
        mCartBean = new ArrayList<CartBean>();
        mCartBean.addAll(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        holder = new FooterViewHolder(inflater.inflate(R.layout.item_cart, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CartHolder) {
            mCartHolder = (CartHolder) holder;
            final CartBean cart = mCartBean.get(position);
            mCartHolder.cbCurtselected.setChecked(cart.isChecked());
            ImageUtils.setGoodThumb(mContext, mCartHolder.ivCartThumb, cart.getGoods().getGoodsThumb());
            mCartHolder.tvCartGoodName.setText(cart.getGoods().getGoodsName());
            mCartHolder.tvCartCount.setText("(" + cart.getCount() + ")");
            mCartHolder.tvCartPrice.setText(cart.getGoods().getCurrencyPrice());
        }
    }

    @Override
    public int getItemCount() {
        return mCartBean!=null?mCartBean.size():0;
    }

    public void initData(List<CartBean> list) {
        if (mCartBean != null) {
            mCartBean.clear();
        }
        mCartBean.addAll(list);
        notifyDataSetChanged();
    }

    public boolean isMore() {
        return isMore;
    }

    public void addItem(List<CartBean> goodBeanArrayList) {
        mCartBean.addAll(goodBeanArrayList);
        notifyDataSetChanged();
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView tvFooter;
        public FooterViewHolder(View itemView) {
            super(itemView);
            tvFooter = (TextView) itemView.findViewById(R.id.tv_footer);
        }
    }
    class CartHolder extends RecyclerView.ViewHolder{
        CheckBox cbCurtselected;
        ImageView ivCartThumb;
        TextView tvCartGoodName;
        ImageView ivCartAdd;
        TextView tvCartCount;
        ImageView ivCartDel;
        TextView tvCartPrice;
        public CartHolder(View itemView) {
            super(itemView);
            cbCurtselected = (CheckBox) itemView.findViewById(R.id.cb_cart_selected);
            ivCartThumb = (ImageView) itemView.findViewById(R.id.iv_cart_thumb);
            tvCartGoodName = (TextView) itemView.findViewById(R.id.tv_cart_good_name);
            ivCartAdd = (ImageView) itemView.findViewById(R.id.iv_cart_add);
            tvCartCount = (TextView) itemView.findViewById(R.id.tv_cart_count);
            ivCartDel = (ImageView) itemView.findViewById(R.id.iv_cart_del);
            tvCartPrice = (TextView) itemView.findViewById(R.id.tv_cart_price);
        }
    }

}
