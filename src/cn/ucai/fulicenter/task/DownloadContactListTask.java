package cn.ucai.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;
import java.util.Map;

import cn.ucai.I;
import cn.ucai.bean.Result;
import cn.ucai.bean.UserAvatar;
import cn.ucai.fulicenter.SuperWeChatApplication;
import cn.ucai.fulicenter.utils.Utils;
import cn.ucai.data.OkHttpUtils2;

/**
 * Created by Administrator on 2016/7/20.
 */
public class DownloadContactListTask {
    private static final String TAG = DownloadContactListTask.class.getSimpleName();
    String username;
    Context mContext;
    public DownloadContactListTask(Context context,String username) {
        mContext = context;
        this.username = username;
    }
    public void execute(){
        final OkHttpUtils2<String> utils = new OkHttpUtils2<String>();
        utils.setRequestUrl(I.REQUEST_DOWNLOAD_CONTACT_ALL_LIST)
                .addParam(I.Contact.USER_NAME,username)
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG,"s="+s);
                        Result result = Utils.getListResultFromJson(s, UserAvatar.class);
                        Log.e(TAG,"result="+result);
                        List<UserAvatar> list = (List<UserAvatar>) result.getRetData();
                        Log.e(TAG,"list="+list);
                        if (list!=null && list.size()>0){
                            Log.e(TAG,"list.size="+list.size());
                            SuperWeChatApplication.getInstance().setUserList(list);
                            mContext.sendStickyBroadcast(new Intent("update_contact_list"));
                            Map<String,UserAvatar> userMap = SuperWeChatApplication.getInstance().getUserMap();
                            for(UserAvatar u:list){
                                userMap.put(u.getMUserName(),u);
                            }
                        }
                    }
                    @Override
                    public void onError(String error) {
                        Log.e(TAG,"error="+error);
                    }
                });
    }
}
