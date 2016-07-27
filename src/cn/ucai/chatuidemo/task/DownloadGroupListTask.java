package cn.ucai.chatuidemo.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;
import java.util.Map;

import cn.ucai.I;
import cn.ucai.bean.GroupAvatar;
import cn.ucai.bean.Result;
import cn.ucai.bean.UserAvatar;
import cn.ucai.chatuidemo.SuperWeChatApplication;
import cn.ucai.chatuidemo.activity.GroupDetailsActivity;
import cn.ucai.chatuidemo.utils.Utils;
import cn.ucai.data.OkHttpUtils2;

/**
 * Created by Administrator on 2016/7/20.
 */
public class DownloadGroupListTask {
    private static final String TAG = DownloadGroupListTask.class.getSimpleName();
    String username;
    Context mContext;
    public DownloadGroupListTask(Context context, String username) {
        mContext = context;
        this.username = username;
    }
    public void execute(){
        final OkHttpUtils2<String> utils = new OkHttpUtils2<String>();
        utils.setRequestUrl(I.REQUEST_FIND_GROUP_BY_USER_NAME)
                .addParam(I.User.USER_NAME,username)
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG,"s="+s);
                        Result result = Utils.getListResultFromJson(s, GroupAvatar.class);
                        Log.e(TAG,"result="+result);
                        List<GroupAvatar> list = (List<GroupAvatar>) result.getRetData();
                        Log.e(TAG,"list="+list);
                        if (list!=null && list.size()>0){
                            Log.e(TAG,"list.size="+list.size());
                            SuperWeChatApplication.getInstance().setGroupList(list);
                            for (GroupAvatar g : list) {
                                SuperWeChatApplication.getInstance().getGroupMap().put(g.getMGroupHxid(),g);
                            }
                            mContext.sendStickyBroadcast(new Intent("update_group_list"));
                        }
                    }
                    @Override
                    public void onError(String error) {
                        Log.e(TAG,"error="+error);
                    }
                });
    }
}
