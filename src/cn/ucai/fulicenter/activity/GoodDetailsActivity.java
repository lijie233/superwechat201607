package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.ucai.D;
import cn.ucai.I;
import cn.ucai.bean.AlbumBean;
import cn.ucai.bean.GoodDetailsBean;
import cn.ucai.data.OkHttpUtils2;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.view.DisplayUtils;
import cn.ucai.fulicenter.view.FlowIndicator;
import cn.ucai.fulicenter.view.SlideAutoLoopView;

/**
 * Created by Administrator on 2016/8/3.
 */
public class GoodDetailsActivity extends BaseActivity{
    ImageView ivShare,ivCollect,ivCart;
    TextView tvCartCount,tvGoodEnglishName,tvGoodName,tvGoodPriceCurrent,tvGoodPriceShop;
    SlideAutoLoopView mSlideAutoLoopView;
    FlowIndicator mFlowIndicator;
    WebView wvGoodBrief;
    int mGoodId;
    GoodDetailsBean mGoodDetail;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_good_details);
        initView();
        initData();
    }

    private void initData() {
        mGoodId=getIntent().getIntExtra(D.GoodDetails.KEY_GOODS_ID, 0);
        if (mGoodId > 0) {
            getGoodDetailsByGoodId(new OkHttpUtils2.OnCompleteListener<GoodDetailsBean>() {
                @Override
                public void onSuccess(GoodDetailsBean result) {
                    if (result != null) {
                        mGoodDetail=result;
                        showGoodDetails(result);
                    }
                }

                @Override
                public void onError(String error) {
                    finish();
                    Toast.makeText(GoodDetailsActivity.this,"获取商品详情失败!",Toast.LENGTH_LONG).show();
                }
            });
        } else {
            finish();
            Toast.makeText(GoodDetailsActivity.this,"获取商品详情失败!",Toast.LENGTH_LONG).show();
        }
    }

    private void showGoodDetails(GoodDetailsBean details) {
        tvGoodEnglishName.setText(details.getGoodsEnglishName());
        tvGoodName.setText(details.getGoodsName());
        tvGoodPriceCurrent.setText(details.getCurrencyPrice());
        tvGoodPriceShop.setText(details.getShopPrice());
        mSlideAutoLoopView.startPlayLoop(mFlowIndicator,getAlbumImageUrl(),getAlbumImageSize());
        wvGoodBrief.loadDataWithBaseURL(null, mGoodDetail.getGoodsBrief(), D.TEXT_HTML, D.UTF_8, null);
    }

    private String[] getAlbumImageUrl() {
        String[] albumImageUrl=new String[]{};
        if (mGoodDetail.getProperties() != null && mGoodDetail.getProperties().length > 0) {
            AlbumBean[] albums=mGoodDetail.getProperties()[0].getAlbums();
            albumImageUrl = new String[albums.length];
            for(int i=0;i<albumImageUrl.length;i++) {
                albumImageUrl[i] = albums[i].getImgUrl();
            }
        }
        return albumImageUrl;
    }

    private int getAlbumImageSize() {
        if (mGoodDetail.getProperties() != null && mGoodDetail.getProperties().length > 0) {
            return  mGoodDetail.getProperties()[0].getAlbums().length;
        }
        return 0;
    }

    private void getGoodDetailsByGoodId(OkHttpUtils2.OnCompleteListener<GoodDetailsBean> listener) {
        OkHttpUtils2<GoodDetailsBean> utils = new OkHttpUtils2<GoodDetailsBean>();
        utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(D.GoodDetails.KEY_GOODS_ID,String.valueOf(mGoodId))
                .targetClass(GoodDetailsBean.class)
                .execute(listener);
    }

    private void initView() {
        DisplayUtils.initBack(GoodDetailsActivity.this);
        ivShare = (ImageView) findViewById(R.id.iv_good_share);
        ivCollect = (ImageView) findViewById(R.id.iv_good_collect);
        ivCart = (ImageView) findViewById(R.id.iv_good_cart);
        tvCartCount = (TextView) findViewById(R.id.tv_cart_count);
        tvGoodEnglishName = (TextView) findViewById(R.id.tv_good_name_english);
        tvGoodName = (TextView) findViewById(R.id.tv_good_name);
        tvGoodPriceCurrent = (TextView) findViewById(R.id.tv_good_price_current);
        tvGoodPriceShop = (TextView) findViewById(R.id.tv_good_price_shop);
        mSlideAutoLoopView = (SlideAutoLoopView) findViewById(R.id.salv);
        mFlowIndicator = (FlowIndicator) findViewById(R.id.indicator);
        wvGoodBrief = (WebView) findViewById(R.id.wv_good_brief);
        WebSettings settings = wvGoodBrief.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setBuiltInZoomControls(true);
    }
}
