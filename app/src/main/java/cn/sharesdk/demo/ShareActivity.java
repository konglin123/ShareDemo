package cn.sharesdk.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.sharesdk.framework.Platform;

public class ShareActivity extends AppCompatActivity implements View.OnClickListener, SharePlatformAdapter.OnClickItemListener{

    private ImageView callBack;
    private TextView shareTitle;
    private ShareListItemInEntity entity;
    private RecyclerView recyclerView;
    private SharePlatformAdapter adapter;
    private List<Integer> lists;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        initData();

        recyclerView = (RecyclerView) this.findViewById(R.id.mSharePlatform);
        callBack = (ImageView) this.findViewById(R.id.mReback);
        callBack.setOnClickListener(this);
        shareTitle = (TextView) this.findViewById(R.id.mTitle);
        recyclerView = (RecyclerView) this.findViewById(R.id.mSharePlatform);
        if (entity != null) {
            shareTitle.setText(getString(R.string.share_platform_name) + entity.getName());
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new SharePlatformAdapter(lists, this);
        adapter.setOnClickItemListener(this);
        recyclerView.setAdapter(adapter);

    }

    public void initData() {
        if (lists == null) {
            lists = new ArrayList<>();
        }
        Intent intent = getIntent();
        if (intent != null) {
            entity = (ShareListItemInEntity) intent.getSerializableExtra("shareEntity");
            name = intent.getStringExtra("name");
        }
        if (entity != null) {
            lists.clear();
            if (!TextUtils.isEmpty(name)) {
                Integer[] plats = PlatformShareConstant.byNamePlatforms(name);
                List<Integer> ls = Arrays.asList(plats);
                lists.addAll(ls);
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.mReback) {
            finish();
        }
    }

    @Override
    public void onItemClick(int platformCode) {
        Platform platform = App.getInstance().getPlatformList().get(0);
        if (platform != null) {
            ShareTypeManager shareManager = new ShareTypeManager(this, platform);
            shareManager.shareShow(platformCode);
        }
    }


}
