package cn.sharesdk.demo;

import com.mob.MobApplication;
import com.mob.MobSDK;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.Platform;

/**
 * Created by zhangcirui on 2018/8/7.
 */

public class App extends MobApplication{
        //private BaseActivity currentActivity;
        public List<Platform> platformList = new ArrayList<>();
        public  static App app = null;

        @Override
        public void onCreate() {
            super.onCreate();
            this.app = this;
            ResourcesManager.getInstace(MobSDK.getContext());
        }

    public static App getInstance(){
        return app;
    }

    public List<Platform> getPlatformList() {
        return platformList;
    }

    public void setPlatformList(Platform platform) {
        if(platformList.size() > 0){
            platformList.clear();
        }
        if(platform != null){
            platformList.add(platform);
        }
    }


    /*public void setCurrentActivity(BaseActivity activity){
        this.currentActivity = activity;
    }*/



}
