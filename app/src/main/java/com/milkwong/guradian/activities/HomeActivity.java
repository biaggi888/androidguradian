package com.milkwong.guradian.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.milkwong.guradian.R;

public class HomeActivity extends AppCompatActivity
{
  //private String[] mItems = new String[] { "1","2","3","4","5","6","7","8","9"};

  private String[] mItems = new String[] { "手机防盗", "通讯卫士", "软件管理", "进程管理",
                                             "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };

  private int[] mPics = new int[] { R.drawable.home_safe,
                                      R.drawable.home_callmsgsafe, R.drawable.home_apps,
                                      R.drawable.home_taskmanager, R.drawable.home_netmanager,
                                      R.drawable.home_trojan, R.drawable.home_sysoptimize,
                                      R.drawable.home_tools, R.drawable.home_settings };

  private GridView mGvMainFun;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    mGvMainFun = (GridView)findViewById(R.id.gvNainFun);
    mGvMainFun.setAdapter(new HomeAdapter());
  }

  class HomeAdapter extends BaseAdapter
  {
    @Override
    public int getCount() {
      return mItems.length;
    }

    @Override
    public Object getItem(int position) {
      return mItems[position];
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
      if (view == null) {
        view = View.inflate(getApplicationContext(),
                                    R.layout.home_item_list, null);
      }
      ImageView ivItem = (ImageView) view.findViewById(R.id.home_list_item_iv);
      TextView tvItem = (TextView) view.findViewById(R.id.home_list_item_tv);

      tvItem.setText(mItems[position]);
      ivItem.setImageResource(mPics[position]);
      return view;
    }


  }

}
