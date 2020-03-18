package cn.sharesdk.demo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.mob.tools.utils.ResHelper;
import com.mob.tools.utils.UIHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.sharesdk.framework.CustomPlatform;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by zhangcirui on 2018/8/7.
 */

public class PlatformMananger {

    private List<ShareListItemInEntity> chinaList = new ArrayList<>();
    private List<ShareListItemInEntity> systemList = new ArrayList<>();

    private static PlatformMananger instance;
    private Context context = null;
    public static final String SDK_SINAWEIBO_UID = "3189087725";
    public static final String SDK_TENCENTWEIBO_UID = "shareSDK";
    public static String[] china = {"SinaWeibo",  "QZone", "Wechat", "WechatMoments", "WechatFavorite", "QQ"};
    public static String[] system = {"Email"};

    private PlatformMananger(final Context context) {
        this.context = context;
        Platform[] list = ShareSDK.getPlatformList();
        if (list != null) {
            Message msg = new Message();
            msg.obj = list;
            UIHandler.sendMessage(msg, new Handler.Callback() {
                public boolean handleMessage(Message msg) {
                    Toast.makeText(context,"length="+((Platform[]) msg.obj).length, Toast.LENGTH_SHORT).show();
                    afterPlatformsGot((Platform[]) msg.obj);
                    return false;
                }
            });
        }
    }

    private void afterPlatformsGot(Platform[] platforms) {
        ShareListItemInEntity entity = null;
        //PlatformEntity normalEntity = null;
        for (Platform platform : platforms) {
            String name = platform.getName();
            //客户端分享的情况
//			if (DemoUtils.isUseClientToShare(name)) {
//				continue;
//			}
            if (platform instanceof CustomPlatform) {
                continue;
            }
            //#if def{lang} == cn
            // 处理左边按钮和右边按钮
            //#elif def{lang} == en
            // initiate buttons
            //#endif
            entity = new ShareListItemInEntity();
            entity.setPlatform(platform);
            //normalEntity = new PlatformEntity();
            //normalEntity.setmPlatform(platform);
            entity.setType(SharePlatformType.FOREIGN_SHARE_PLAT);
            int platNameRes = ResHelper.getStringRes(context, "ssdk_" + name.toLowerCase());
            String resName = "ssdk_oks_classic_" + name;
            int resId = ResHelper.getBitmapRes(context, resName.toLowerCase());
            if (resId > 0) {
                entity.setIcon(resId);
                //normalEntity.setmIcon(resId);
            }
            if (platNameRes > 0) {
                String platName = context.getString(platNameRes);
                entity.setName(platName);
                //normalEntity.setName(platName);
                String text = context.getString(R.string.share_to_format, platName);
            }
            if (Arrays.asList(china).contains(name) ) {
                if(!name.equals("Cmcc")){
                    chinaList.add(entity);
                }
            } else {
                if (Arrays.asList(system).contains(name)) {
                    systemList.add(entity);

                }
            }
        }
    }

    public static PlatformMananger getInstance(Context context) {
        synchronized (PlatformMananger.class) {
            if (instance == null) {
                synchronized (PlatformMananger.class) {
                    if (instance == null) {
                        instance = new PlatformMananger(context);
                    }
                }
            }
        }
        return instance;
    }

    public List<ShareListItemInEntity> getChinaList() {
        return chinaList;
    }

    public void setChinaList(List<ShareListItemInEntity> chinaList) {
        this.chinaList = chinaList;
    }

    public List<ShareListItemInEntity> getSystemList() {
        return systemList;
    }

    public void setSystemList(List<ShareListItemInEntity> systemList) {
        this.systemList = systemList;
    }


}
