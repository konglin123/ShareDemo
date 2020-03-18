package cn.sharesdk.demo;

import cn.sharesdk.framework.Platform;

/**
 * Created by zhangcirui on 2018/8/7.
 */

public class PlatformUITypeManager {

    public static int getPlatformIcon(int share_type) {
        switch (share_type) {
            case Platform.SHARE_VIDEO: {
                return R.mipmap.base_video;
            }
            case Platform.SHARE_TEXT: {
                return R.mipmap.share_text;
            }
            case Platform.SHARE_IMAGE: {
                return R.mipmap.share_multimages;
            }
            case Platform.SHARE_APPS: {
                return R.mipmap.share_app;
            }
            case Platform.SHARE_FILE: {
                return R.mipmap.base_file;
            }

            case Platform.SHARE_MUSIC: {
                return R.mipmap.share_url_music;
            }

            default: {
                return R.mipmap.share_webpage;
            }
        }
    }

    public static int getPlatformName(int share_type) {
        switch (share_type) {
            case Platform.SHARE_VIDEO: {
                return R.string.platform_share_video;
            }
            case Platform.SHARE_TEXT: {
                return R.string.platform_share_text;
            }
            case Platform.SHARE_IMAGE: {
                return R.string.platform_share_image;
            }
            case Platform.SHARE_APPS: {
                return R.string.platform_share_app;
            }
            case Platform.SHARE_FILE: {
                return R.string.platform_share_file;
            }
            case Platform.SHARE_EMOJI: {
                return R.string.platform_share_emoji;
            }
            case Platform.SHARE_WXMINIPROGRAM: {
                return R.string.platform_share_mini_app;
            }
            case Platform.SHARE_WEBPAGE: {
                return R.string.platform_share_webpage;
            }
            case Platform.SHARE_MUSIC: {
                return R.string.platform_share_music;
            }
            default: {
                return R.string.platform_share_webpage;
            }
        }
    }

}
