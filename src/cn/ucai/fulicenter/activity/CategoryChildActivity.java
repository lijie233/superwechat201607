package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.D;
import cn.ucai.I;
import cn.ucai.bean.NewGoodBean;
import cn.ucai.data.OkHttpUtils2;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.CategoryAdapter;
import cn.ucai.fulicenter.adapter.GoodsAdapter;
import cn.ucai.fulicenter.utils.Utils;
import cn.ucai.fulicenter.view.DisplayUtils;

/**
 * Created by Administrator on 2016/8/1.
 */
public class CategoryChildActivity extends BaseActivity{
    private static final String TAG = CategoryChildActivity.class.getSimpleName();
    CategoryChildActivity mContext;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    GridLayoutManager mGridLayoutManager;
    GoodsAdapter mAdapter;
    List<NewGoodBean> mGoodList;
    int pageId = 0;
    TextView tvHint;
    int action = I.ACTION_DOWNLOAD;
    int catId = 0;

    Button btnSortPrice;
    Button btnSorAddTime;
    Boolean mSortPriceAsc;
    Boolean mSortAddTimeAsc;
    int sortBy;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.acitvity_category_child);
        mContext = this;
        mGoodList = new ArrayList<NewGoodBean>();
        sortBy=I.SORT_BY_ADDTIME_DESC;
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        setPullDownRefreshListener();
        setPullUpRefreshListener();
        SortStatusChangedListener listener = new SortStatusChangedListener();
        btnSortPrice.setOnClickListener(listener);
        btnSorAddTime.setOnClickListener(listener);
    }

    private void setPullUpRefreshListener() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastItemPosition;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int a = RecyclerView.SCROLL_STATE_DRAGGING;//1
                int b = RecyclerView.SCROLL_STATE_IDLE;//0
                int c = RecyclerView.SCROLL_STATE_SETTLING;//2
                Log.e(TAG,"newState="+newState);
                if (newState==RecyclerView.SCROLL_STATE_IDLE
                        && lastItemPosition==mAdapter.getItemCount()-1){
                    if (mAdapter.isMore()) {
                        action = I.ACTION_PULL_UP;
                        pageId += I.PAGE_SIZE_DEFAULT;
                        initData();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int f = mGridLayoutManager.findFirstVisibleItemPosition();
                int l = mGridLayoutManager.findLastVisibleItemPosition();
                Log.e(TAG,"f="+f+",l="+l);
                lastItemPosition = mGridLayoutManager.findLastVisibleItemPosition();
                mSwipeRefreshLayout.setEnabled(mGridLayoutManager.findFirstVisibleItemPosition()==0);
                if (f==-1||l==-1){
                    lastItemPosition = mAdapter.getItemCount()-1;
                }
            }
        });
    }

    private void setPullDownRefreshListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                action = I.ACTION_PULL_DOWN;
                pageId = 0;
                mSwipeRefreshLayout.setRefreshing(true);
                tvHint.setVisibility(View.VISIBLE);
                initData();
            }
        });
    }

    private void initData() {
        catId = getIntent().getIntExtra(I.NewAndBoutiqueGood.CAT_ID,0);
        Log.e(TAG,"catId="+catId);
        if(catId<0)finish();
        findNewGoodList(new OkHttpUtils2.OnCompleteListener<NewGoodBean[]>() {
            @Override
            public void onSuccess(NewGoodBean[] result) {
                mSwipeRefreshLayout.setRefreshing(false);
                tvHint.setVisibility(View.GONE);
                mAdapter.setMore(true);
                mAdapter.setFooterString(getResources().getString(R.string.load_more));
                Log.e(TAG,"result="+result);
                if (result!=null){
                    Log.e(TAG,"result.length="+result.length);
                    ArrayList<NewGoodBean> goodBeanArrayList = Utils.array2List(result);
                    if (action==I.ACTION_DOWNLOAD||action==I.ACTION_PULL_DOWN) {
                        mAdapter.initItem(goodBeanArrayList);
                    }else{
                        mAdapter.addItem(goodBeanArrayList);
                    }
                    if (goodBeanArrayList.size()<I.PAGE_SIZE_DEFAULT){
                        mAdapter.setMore(false);
                        mAdapter.setFooterString(getResources().getString(R.string.no_more));
                    }
                }else{
                    mAdapter.setMore(false);
                    mAdapter.setFooterString(getResources().getString(R.string.no_more));
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
    private void findNewGoodList(OkHttpUtils2.OnCompleteListener<NewGoodBean[]> listener){
        OkHttpUtils2<NewGoodBean[]> utils = new OkHttpUtils2<NewGoodBean[]>();
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGood.CAT_ID,String.valueOf(catId))
                .addParam(I.PAGE_ID,String.valueOf(pageId))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodBean[].class)
                .execute(listener);
    }

    private void initView() {
//        String name = getIntent().getStringExtra(D.Boutique.KEY_NAME);
        DisplayUtils.initBack(mContext);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_category_child);
        mSwipeRefreshLayout.setColorSchemeColors(
                R.color.google_blue,
                R.color.google_yellow,
                R.color.google_red,
                R.color.google_green
        );
        mGridLayoutManager = new GridLayoutManager(mContext, I.COLUM_NUM);
        mGridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView)findViewById(R.id.rv_category_child);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mAdapter = new GoodsAdapter(mContext,mGoodList);
        mRecyclerView.setAdapter(mAdapter);
        tvHint = (TextView) findViewById(R.id.tv_refresh_hint);
        btnSortPrice = (Button) findViewById(R.id.btn_sort_price);
        btnSorAddTime = (Button) findViewById(R.id.btn_sort_addtime);
    }

    class SortStatusChangedListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_sort_price:
                    if (mSortPriceAsc) {
                        sortBy = I.SORT_BY_PRICE_ASC;
                    } else {
                        sortBy = I.SORT_BY_PRICE_DESC;
                    }
                    mSortPriceAsc = !mSortPriceAsc;
                    break;
                case R.id.btn_sort_addtime:
                    if (mSortAddTimeAsc) {
                        sortBy = I.SORT_BY_ADDTIME_ASC;
                    } else {
                        sortBy = I.SORT_BY_ADDTIME_DESC;
                    }
                    mSortAddTimeAsc = !mSortAddTimeAsc;
                    break;
            }
            mAdapter.setSortBy(sortBy);
        }
    }

}
