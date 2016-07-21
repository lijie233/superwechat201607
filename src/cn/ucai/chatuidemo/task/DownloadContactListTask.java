package cn.ucai.chatuidemo.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;
import java.util.Map;

import cn.ucai.I;
import cn.ucai.bean.Result;
import cn.ucai.bean.UserAvatar;
import cn.ucai.chatuidemo.SuperWeChatApplication;
import cn.ucai.chatuidemo.utils.Utils;
import cn.ucai.data.OkHttpUtils2;

/**
 * Created by Administrator on 2016/7/20.
 */
public class DownloadContactListTask {
    private static final String TAG = DownloadContactListTask.class.getSimpleName();
    Context context;
    String username;

    public DownloadContactListTask(Context context, String username) {
        this.context = context;
        this.username = username;
    }

    public void excute() {
        final OkHttpUtils2<String> utils = new OkHttpUtils2<String>();
        utils.setRequestUrl(I.REQUEST_DOWNLOAD_CONTACT_ALL_LIST)
                .addParam(I.Contact.USER_NAME,username)
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Result result = Utils.getResultFromJson(s, UserAvatar.class);
                        List<UserAvatar> list = (List<UserAvatar>) result.getRetData();
                        if (list != null && list.size() > 0) {
                            SuperWeChatApplication.getInstance().setUserList(list);
                            Log.e("main", list.toString());
                            context.sendStickyBroadcast(new Intent("update_contact_list"));
                            Map<String, UserAvatar> userMap = SuperWeChatApplication.getInstance().getUserMap();
                            for (UserAvatar u : list) {
                                userMap.put(u.getMUserName(), u);
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "error=" + error);
                    }
                });
    }
}
