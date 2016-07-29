package cn.ucai.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ucai.I;
import cn.ucai.bean.MemberUserAvatar;
import cn.ucai.bean.Result;
import cn.ucai.fulicenter.SuperWeChatApplication;
import cn.ucai.fulicenter.utils.Utils;
import cn.ucai.data.OkHttpUtils2;

/**
 * Created by Administrator on 2016/7/20.
 */
public class DownloadMemberMapListTask {
    private static final String TAG = DownloadMemberMapListTask.class.getSimpleName();
    String hxid;
    Context mContext;
    public DownloadMemberMapListTask(Context context, String hxid) {
        mContext = context;
        this.hxid = hxid;
    }
    public void execute(){
        final OkHttpUtils2<String> utils = new OkHttpUtils2<String>();
        utils.setRequestUrl(I.REQUEST_DOWNLOAD_GROUP_MEMBERS_BY_HXID)
                .addParam(I.Member.GROUP_HX_ID,hxid)
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG,"s="+s);
                        Result result = Utils.getListResultFromJson(s, MemberUserAvatar.class);
                        Log.e(TAG,"result="+result);
                        List<MemberUserAvatar> list = (List<MemberUserAvatar>) result.getRetData();
                        Log.e(TAG,"list="+list);
                        if (list!=null && list.size()>0){
                            Log.e(TAG,"list.size="+list.size());
                            Map<String, HashMap<String, MemberUserAvatar>> memberMap = SuperWeChatApplication.getInstance().getMemberMap();
                            if (!memberMap.containsKey(hxid)) {
                                memberMap.put(hxid, new HashMap<String, MemberUserAvatar>());
                            }
                            HashMap<String, MemberUserAvatar> hxidMembers = memberMap.get(hxid);
                            for(MemberUserAvatar u:list){
                                hxidMembers.put(u.getMUserName(),u);
                            }
                            mContext.sendStickyBroadcast(new Intent("update_member_list"));
                        }
                    }
                    @Override
                    public void onError(String error) {
                        Log.e(TAG,"error="+error);
                    }
                });
    }
}
