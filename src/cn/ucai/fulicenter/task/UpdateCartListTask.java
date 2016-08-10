package cn.ucai.fulicenter.task;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.FuliCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.GoodDetailsBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;

/**
 * Created by Administrator on 2016/7/20.
 */
public class UpdateCartListTask {
    private static final String TAG = UpdateCartListTask.class.getSimpleName();
    CartBean mCart;
    Context mContext;
    public UpdateCartListTask(Context context, CartBean cart) {
        mContext = context;
        this.mCart = cart;
    }
    public void execute(){
        final List<CartBean> cartList = FuliCenterApplication.getInstance().getCartList();
        if (cartList.contains(mCart)) {
            if (mCart.getCount() > 0) {
                updaeCart(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        cartList.set(cartList.indexOf(mCart),mCart);
                        mContext.sendStickyBroadcast(new Intent("update_cart_list"));
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            } else {
                //删除购物车数据
            }
        } else {
            //新增购物车数据
        }
    }

    private void updaeCart(OkHttpUtils2.OnCompleteListener<MessageBean> listener) {
        OkHttpUtils2<MessageBean> utils = new OkHttpUtils2<MessageBean>();
        utils.setRequestUrl(I.REQUEST_UPDATE_CART)
                .addParam(I.Cart.ID,String.valueOf(mCart.getId()))
                .addParam(I.Cart.COUNT,String.valueOf(mCart.getCount()))
                .addParam(I.Cart.IS_CHECKED,String.valueOf(mCart.isChecked()))
                .targetClass(MessageBean.class)
                .execute(listener);
    }
}
