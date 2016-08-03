package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.I;
import cn.ucai.bean.BoutiqueBean;
import cn.ucai.bean.NewGoodBean;
import cn.ucai.data.OkHttpUtils2;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.BoutiqueAdapter;
import cn.ucai.fulicenter.adapter.GoodsAdapter;
import cn.ucai.fulicenter.utils.Utils;

/**
 * Created by Administrator on 2016/8/1.
 */
public class BoutiqueFragment extends Fragment{
    private static final String TAG = BoutiqueFragment.class.getSimpleName();
    FuliCenterMainActivity mContext;
    List<BoutiqueBean> mBoutiqueList;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLinearLayoutManager;
    BoutiqueAdapter mAdapter;
    TextView tvHint;
    int pageId=1;
    int action=I.ACTION_DOWNLOAD;
    public BoutiqueFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mContext = (FuliCenterMainActivity) getContext();
        View layout = View.inflate(mContext, R.layout.fragment_boutique, null);
        mBoutiqueList = new ArrayList<BoutiqueBean>();
        initView(layout);
        initData();
        setListener();
        return layout;
    }

    private void setListener() {
        setPullDownRefreshListener();
        setPullUpRefrshListener();
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
        findBoutiquList(new OkHttpUtils2.OnCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(BoutiqueBean[] result) {
                Log.e(TAG,"result="+result);
                tvHint.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter.setMore(true);
                mAdapter.setFooterString(getResources().getString(R.string.load_more));
                if (result != null) {
                    Log.e(TAG, "result.length=" + result.length);
                    ArrayList<BoutiqueBean> boutiqueBeanArrayList = Utils.array2List(result);
                    if (action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN) {
                        mAdapter.initData(boutiqueBeanArrayList);
                    } else {
                        mAdapter.addItem(boutiqueBeanArrayList);
                    }
                    if (boutiqueBeanArrayList.size() < I.PAGE_SIZE_DEFAULT) {
                        mAdapter.setMore(false);
                        mAdapter.setFooterString(getResources().getString(R.string.no_more));
                    }
                } else {
                    mAdapter.setMore(false);
                    mAdapter.setFooterString(getResources().getString(R.string.no_more));
                }
            }

            @Override
            public void onError(String error) {

                tvHint.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    private void findBoutiquList(OkHttpUtils2.OnCompleteListener<BoutiqueBean[]> listener){
        OkHttpUtils2<BoutiqueBean[]> utils = new OkHttpUtils2<BoutiqueBean[]>();
        utils.setRequestUrl(I.REQUEST_FIND_BOUTIQUES)
//                .addParam(I.NewAndBoutiqueGood.CAT_ID,String.valueOf(I.CAT_ID))
//                .addParam(I.PAGE_ID,String.valueOf(pageId))
//                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(BoutiqueBean[].class)
                .execute(listener);
    }

    private void initView(View layout) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.srl_boutique);
        mSwipeRefreshLayout.setColorSchemeColors(
                R.color.google_blue,
                R.color.google_yellow,
                R.color.google_red,
                R.color.google_green
        );
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.rv_boutique);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new BoutiqueAdapter(mContext,mBoutiqueList);
        mRecyclerView.setAdapter(mAdapter);
        tvHint = (TextView) layout.findViewById(R.id.tv_refresh_hint);
    }

}
