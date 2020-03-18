package cn.sharesdk.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView listView;
    private ShareRecylerViewAdapter adapter;
    private List<ShareListItemInEntity> lists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        initView();

    }

    public void initView() {
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
    
   public void initData(){

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
       },5000);
   }

    @Override
    public void onClick(View v) {

    }

    public static ShareListItemInEntity createInLand(Context mContext){
        ShareListItemInEntity entity = new ShareListItemInEntity();
        entity.setName(mContext.getString(R.string.item_title_txt_catagory_inland));
        entity.setType(SharePlatformType.TITLE_SHARE_PLAT);
        return entity;
    }

    public static ShareListItemInEntity createSystem(Context mContext){
        ShareListItemInEntity entity = new ShareListItemInEntity();
        entity.setName(mContext.getString(R.string.item_title_txt_catagory_system));
        entity.setType(SharePlatformType.TITLE_SHARE_PLAT);
        return entity;
    }


}
