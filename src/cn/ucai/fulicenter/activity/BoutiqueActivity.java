package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.bean.NewGoodBean;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.GoodsAdapter;
import cn.ucai.fulicenter.utils.Utils;
import cn.ucai.fulicenter.view.DisplayUtils;

/**
 * Created by Administrator on 2016/8/1.
 */
public class BoutiqueActivity extends BaseActivity{
    private static final String TAG = BoutiqueActivity.class.getSimpleName();
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    GridLayoutManager mGridLayoutManager;
    GoodsAdapter mAdapter;
    List<NewGoodBean> mGoodList;
    int pageId = 1;
    TextView tvHint;
    int action=I.ACTION_DOWNLOAD;
    int catId=0;
    public BoutiqueActivity() {

    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.acitvity_boutique_child);
        mGoodList = new ArrayList<NewGoodBean>();
        initView();
        initData();
        setListener();
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
                int f = mGridLayoutManager.findFirstCompletelyVisibleItemPosition();
                int l = mGridLayoutManager.findLastCompletelyVisibleItemPosition();
                lastItemPosition = mGridLayoutManager.findLastVisibleItemPosition();
                mSwipeRefreshLayout.setEnabled(mGridLayoutManager.findFirstCompletelyVisibleItemPosition()==0);
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
        findBoutiqueChildList(new OkHttpUtils2.OnCompleteListener<NewGoodBean[]>() {
            @Override
            public void onSuccess(NewGoodBean[] result) {
                Log.e(TAG,"result="+result);
                tvHint.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter.setMore(true);
                mAdapter.setFooterString(getResources().getString(R.string.load_more));
                if (result != null) {
                    Log.e(TAG, "result.length=" + result.length);
                    ArrayList<NewGoodBean> boutiqueList = Utils.array2List(result);
                    if (action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN) {
                        mAdapter.initData(boutiqueList);
                    } else {
                        mAdapter.addItem(boutiqueList);
                    }
                    if (boutiqueList.size() < I.PAGE_SIZE_DEFAULT) {
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
    private void findBoutiqueChildList(OkHttpUtils2.OnCompleteListener<NewGoodBean[]> listener){
        OkHttpUtils2<NewGoodBean[]> utils = new OkHttpUtils2<NewGoodBean[]>();
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGood.CAT_ID,String.valueOf(catId))
                .addParam(I.PAGE_ID,String.valueOf(pageId))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodBean[].class)
                .execute(listener);
    }

    private void initView() {
        catId = getIntent().getIntExtra(D.Boutique.KEY_GOODS_ID, catId);
        if(catId<0)finish();
        String name = getIntent().getStringExtra(D.Boutique.KEY_NAME);
        DisplayUtils.initBackWithTitle(BoutiqueActivity.this,name);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_boutique_child);
        mSwipeRefreshLayout.setColorSchemeColors(
                R.color.google_blue,
                R.color.google_yellow,
                R.color.google_red,
                R.color.google_green
        );
        mRecyclerView = (RecyclerView) findViewById(R.id.boutique_child);
        mGridLayoutManager = new GridLayoutManager(BoutiqueActivity.this, I.COLUM_NUM);
        mGridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mAdapter = new GoodsAdapter(BoutiqueActivity.this,mGoodList);
        mRecyclerView.setAdapter(mAdapter);
        tvHint = (TextView)findViewById(R.id.tv_refresh_hint);
    }


}
