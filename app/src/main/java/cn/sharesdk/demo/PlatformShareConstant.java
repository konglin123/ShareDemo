package cn.sharesdk.demo;

/**
 * Created by zhangcirui on 2018/8/7.
 */

import java.lang.reflect.Field;

import cn.sharesdk.framework.Platform;

/**
 * 各个平台具体可以实现的分享类型的定义类。
 */
public class PlatformShareConstant {
    private static PlatformShareConstant instance = null;
    private Platform platform;
    public static Integer[] sinaWeibo;
    public static Integer[] qzone;
    public static Integer[] wechat;
    public static Integer[] wechatMoments;
    public static Integer[] wechatFavorite;
    public static Integer[] qq;
    public static Integer[] email;

    private PlatformShareConstant(){
        sinaWeibo = new Integer[]{Platform.SHARE_IMAGE, Platform.SHARE_TEXT, Platform.SHARE_VIDEO};
        qzone = new Integer[]{Platform.SHARE_IMAGE, Platform.SHARE_TEXT,Platform.SHARE_WEBPAGE, Platform.SHARE_VIDEO};
        wechat = new Integer[]{Platform.SHARE_IMAGE, Platform.SHARE_TEXT, Platform.SHARE_FILE, Platform.SHARE_WEBPAGE, Platform.SHARE_MUSIC,
                Platform.SHARE_VIDEO, Platform.SHARE_EMOJI, Platform.SHARE_WXMINIPROGRAM};
        wechatMoments = new Integer[]{Platform.SHARE_IMAGE, Platform.SHARE_TEXT, Platform.SHARE_WEBPAGE, Platform.SHARE_MUSIC, Platform.SHARE_VIDEO};
        wechatFavorite = new Integer[]{Platform.SHARE_IMAGE, Platform.SHARE_TEXT, Platform.SHARE_FILE, Platform.SHARE_WEBPAGE,
                Platform.SHARE_MUSIC, Platform.SHARE_VIDEO};
        qq = new Integer[]{Platform.SHARE_IMAGE, Platform.SHARE_WEBPAGE, Platform.SHARE_MUSIC};
        email = new Integer[]{Platform.SHARE_IMAGE, Platform.SHARE_TEXT, Platform.SHARE_VIDEO};
    }
    public synchronized static PlatformShareConstant getInstance(){
        if(instance == null){
            instance = new PlatformShareConstant();
        }
        return instance;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public static Integer[] byNamePlatforms(String name) {
        PlatformShareConstant platformShare = new PlatformShareConstant();
        Class cls = platformShare.getClass();
        Field[] fields = cls.getFields();
        try {
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getName().toUpperCase().equals(name.toUpperCase())) {
                    Object obj = fields[i].get(platformShare);
                    if (obj != null && obj instanceof Integer[]) {
                        Integer[] lists = (Integer[]) obj;
                        return lists;
                    }
                }
            }
        } catch (Exception e) {

        }
        return new Integer[]{};
    }

}
