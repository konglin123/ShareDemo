package cn.sharesdk.demo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PlatformActionListener {
    private RecyclerView listView;
    private ShareRecylerViewAdapter adapter;
    private List<ShareListItemInEntity> lists;
    private TextView oneKeyShare;
    private TextView wxThirdLogin;
    private TextView wbThirdLogin;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        initView();

        initListener();

    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hideProgressDialog();
            switch (msg.arg1){
                case 1:
                    Platform platform= (Platform) msg.obj;
                    String userId = platform.getDb().getUserId();//获取用户账号
                    String userName = platform.getDb().getUserName();//获取用户名字
                    String userIcon = platform.getDb().getUserIcon();//获取用户头像
                    String userGender = platform.getDb().getUserGender(); //获取用户性别，m = 男, f = 女，如果微信没有设置性别,默认返回null
                    Toast.makeText(MainActivity.this, "用户信息为--用户名：" + userName + "  性别：" + userGender+"   头像："+userIcon+"   用户账号："+userId, Toast.LENGTH_SHORT).show();

                    //下面就可以利用获取的用户信息登录自己的服务器或者做自己想做的事啦!
                    //。。。
                    break;
                case 2:
                    Throwable t= (Throwable) msg.obj;
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
//                    Toast.makeText(MainActivity.this, "授权登陆失败", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(MainActivity.this, "授权登陆取消", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void initListener() {
        oneKeyShare.setOnClickListener(this);
        wxThirdLogin.setOnClickListener(this);
        wbThirdLogin.setOnClickListener(this);
    }

    public void initView() {
        oneKeyShare = findViewById(R.id.onekey_share);
        wxThirdLogin = findViewById(R.id.WX_third_login);
        wbThirdLogin = findViewById(R.id.WB_third_login);
        listView = (RecyclerView) findViewById(R.id.mListView);
        listView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        listView.setLayoutManager(linearLayoutManager);

        adapter = new ShareRecylerViewAdapter(this, lists);
        adapter.setOnItemListener(new ShareRecylerViewAdapter.ListOnItemListener() {
            @Override
            public void onClick(View view, int position) {
                ShareListItemInEntity inEntity = lists.get(position);
                if (view.getId() == R.id.onMainLayout) {
                    Intent intent = new Intent(getApplicationContext(), ShareActivity.class);
                    PlatformShareConstant.getInstance().setPlatform(lists.get(position).getPlatform());
                    App.getInstance().setPlatformList(lists.get(position).getPlatform());
                    intent.putExtra("name", lists.get(position).getPlatName());
                    inEntity.setPlatform(null);
                    intent.putExtra("shareEntity", inEntity);
                    startActivity(intent);
                }
                lists.get(position).setPlatform(App.getInstance().getPlatformList().get(0));
            }
        });

        listView.setAdapter(adapter);
        listView.setItemAnimator(new DefaultItemAnimator());
    }

    public void initData() {

        if (lists == null) {
            lists = new ArrayList<>();
        }

        lists.add(createInLand(this));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lists.addAll(PlatformMananger.getInstance(MainActivity.this).getChinaList());

                lists.add(createSystem(MainActivity.this));
                lists.addAll(PlatformMananger.getInstance(MainActivity.this).getSystemList());

                adapter.notifyDataSetChanged();
            }
        }, 5000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.onekey_share:
                oneKeyShare();
                break;
            case R.id.WX_third_login:
                Platform wx = ShareSDK.getPlatform(Wechat.NAME);
                wx.setPlatformActionListener(this);
                wx.SSOSetting(false);
                authorize(wx, 1);
                break;

            case R.id.WB_third_login:
                Platform wb = ShareSDK.getPlatform(SinaWeibo.NAME);
                wb.setPlatformActionListener(this);
                wb.SSOSetting(false);
                authorize(wb, 2);
                break;
        }
    }

    private void authorize(Platform platform, int type) {
        switch (type) {
            case 1:
                showProgressDialog(getString(R.string.opening_wechat));
                break;

            case 2:
                showProgressDialog(getString(R.string.opening_weibo));
                break;
        }

        //如果授权就删除授权资料
        if (platform.isAuthValid()) {
            platform.removeAccount(true);
        }
        //授权并获取用户信息
        platform.showUser(null);
    }


    //一键分享，都不用你写布局
    private void oneKeyShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle("测试分享");
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网使用
        oks.setComment("我是测试评论文本");
        // 启动分享GUI
        oks.show(this);
    }

    public static ShareListItemInEntity createInLand(Context mContext) {
        ShareListItemInEntity entity = new ShareListItemInEntity();
        entity.setName(mContext.getString(R.string.item_title_txt_catagory_inland));
        entity.setType(SharePlatformType.TITLE_SHARE_PLAT);
        return entity;
    }

    public static ShareListItemInEntity createSystem(Context mContext) {
        ShareListItemInEntity entity = new ShareListItemInEntity();
        entity.setName(mContext.getString(R.string.item_title_txt_catagory_system));
        entity.setType(SharePlatformType.TITLE_SHARE_PLAT);
        return entity;
    }

    //登陆授权成功的回调
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Message msg = new Message();
        msg.arg1=1;
        msg.obj=platform;
        handler.sendMessage(msg);
    }

    //登陆授权错误的回调
    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Message msg = new Message();
        msg.arg1=2;
        msg.obj=throwable;
        handler.sendMessage(msg);
    }

    //登陆授权取消的回调
    @Override
    public void onCancel(Platform platform, int i) {
        Message msg = new Message();
        msg.arg1=3;
        msg.obj=platform;
        handler.sendMessage(msg);
    }


    //显示dialog
    private void showProgressDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(true);
        }
        progressDialog.show();
    }

    //隐藏dialog
    private void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
