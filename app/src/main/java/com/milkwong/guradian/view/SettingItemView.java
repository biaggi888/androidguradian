package com.milkwong.guradian.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.milkwong.guradian.R;

/**
 * Created by Leo on 29/02/16.
 */

public class SettingItemView extends RelativeLayout
{
  TextView mTvTitle, MtvDescription;
  CheckBox mCbStatus;
  public SettingItemView(Context context) {
    super(context);
    initView();
  }

  public SettingItemView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initView();
  }

  public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initView();
  }

  public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    initView();
  }
  private void initView() {
    View v = View.inflate(getContext(), R.layout.view_setting_item,this);
    mTvTitle = (TextView)v.findViewById(R.id.view_setting_item_tvTitle);
    MtvDescription = (TextView)v.findViewById(R.id.view_setting_item_tvDesc);
    mCbStatus = (CheckBox)v.findViewById(R.id.view_setting_item_cbStatus);
  }
  public void setTitle(String title){
    this.mTvTitle.setText(title);
  }
  public void setDescriptuon(String description){
    this.mTvTitle.setText(description);
  }
  public boolean isChecked() {
    return mCbStatus.isChecked();
  }
  public void setStatus(boolean flag) {
    mCbStatus.setChecked(flag);
  }
}
