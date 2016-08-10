package cn.ucai.fulicenter.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.FuliCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.CartAdapter;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.GoodDetailsBean;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;

/**
 * Created by Administrator on 2016/8/1.
 */
public class CartFragment extends Fragment{
    private static final String TAG = CartFragment.class.getSimpleName();
    FuliCenterMainActivity mContext;
    List<CartBean> mCartList;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLinearLayoutManager;
    CartAdapter mAdapter;

    UpdateCartReceiver mReceiver;
    TextView tvHint;
    TextView tvSumPrice;
    TextView tvSavePrice;
    TextView tvBuy;
    int pageId=1;
    int action=I.ACTION_DOWNLOAD;
    public CartFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mContext = (FuliCenterMainActivity) getContext();
        View layout = View.inflate(mContext, R.layout.fragment_cart, null);
        mCartList = new ArrayList<CartBean>();
        initView(layout);
        initData();
        setListener();
        return layout;
    }

    private void setListener() {
        setPullDownRefreshListener();
        setPullUpRefrshListener();
        setUpdateCartListener();
    }
    private void setPullUpRefrshListener() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastItemPosition;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int f = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                int l = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                lastItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                mSwipeRefreshLayout.setEnabled(mLinearLayoutManager.findFirstCompletelyVisibleItemPosition()==0);
                if (f == -1 || l == -1) {
                    lastItemPosition=mAdapter.getItemCount()-1;
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastItemPosition == mAdapter.getItemCount() - 1) {
                    int action=I.ACTION_PULL_UP;
                    if (mAdapter.isMore()) {
                        pageId += I.PAGE_SIZE_DEFAULT;
                        initData();
                    }
                }
            }
        });
    }

    private void setPullDownRefreshListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                int action=I.ACTION_PULL_DOWN;
                tvHint.setVisibility(View.VISIBLE);
                pageId=0;
                initData();
            }
        });
    }
    private void initData() {
        List<CartBean> cartList = FuliCenterApplication.getInstance().getCartList();
        mCartList.clear();
        mCartList.addAll(cartList);
        tvHint.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.setMore(true);
        if (mCartList != null&&mCartList.size()>0) {
            if (action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN) {
                mAdapter.initData(cartList);
            } else {
                mAdapter.addItem(cartList);
            }
            if (cartList.size() < I.PAGE_SIZE_DEFAULT) {
                mAdapter.setMore(false);
            }
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        } else {
                    mAdapter.setMore(false);
            mSwipeRefreshLayout.setVisibility(View.GONE);
        }
        sumPrice();
    }

    private void initView(View layout) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.srl_cart);
        mSwipeRefreshLayout.setColorSchemeColors(
                R.color.google_blue,
                R.color.google_yellow,
                R.color.google_red,
                R.color.google_green
        );
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.rv_cart);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new CartAdapter(mContext,mCartList);
        mRecyclerView.setAdapter(mAdapter);
        tvHint = (TextView) layout.findViewById(R.id.tv_refresh_hint);
        tvSavePrice = (TextView) layout.findViewById(R.id.tv_cart_save_price);
        tvSumPrice = (TextView) layout.findViewById(R.id.tv_cart_sum_price);
        tvBuy = (TextView)layout.findViewById(R.id.tv_cart_buy);
    }

    private void sumPrice() {
        if (mCartList != null && mCartList.size() > 0) {
            int sumPrice = 0;
            int rankPrice = 0;
            for (CartBean cart : mCartList) {
                GoodDetailsBean good = cart.getGoods();
                if (good != null &&cart.isChecked()) {
                    sumPrice += convertPrice(good.getCurrencyPrice())*cart.getCount();
                    rankPrice += convertPrice(good.getRankPrice())*cart.getCount();
                }
            }
            tvSumPrice.setText("合计: ¥"+sumPrice);
            tvSavePrice.setText("节省: ¥"+(sumPrice-rankPrice));
        } else {
            tvSumPrice.setText("合计: ¥00.00");
            tvSavePrice.setText("节省: ¥00.00");

        }
    }

    private int convertPrice(String price) {
        price = price.substring(price.indexOf("¥") + 1);
        return Integer.valueOf(price);
    }

    class UpdateCartReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            initData();
        }
    }

    private void setUpdateCartListener() {
        mReceiver = new UpdateCartReceiver();
        IntentFilter filter = new IntentFilter("update_cart_list");
        filter.addAction("update_user");
        mContext.registerReceiver(mReceiver, filter);
    }
}
