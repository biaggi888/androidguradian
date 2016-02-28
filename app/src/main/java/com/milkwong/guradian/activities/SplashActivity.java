package com.milkwong.guradian.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.milkwong.guradian.R;
import com.milkwong.guradian.utils.StreamUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class SplashActivity extends AppCompatActivity
{
  private static final int CODE_NET_ERROR = 0xff01;
  private static final int CODE_JSON_ERROR = 0xff02;
  private static final int CODE_UPDATE_DIALOG = 0xff03;
  private static final int CODE_ENTER_HOME = 0xff04;
  private static final int CODE_NOT_EXTENAL_STORAGE_ERROR = 0xff05;

  private TextView mTvVersion;
  private TextView mTvProgress;
  private int mVersionCode;
  private String mVersionName;
  private PackageInfo mPackageInfo;
  private Handler mHander;
  private String mDowmloadUrl;
  private long currentTime;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

//    getSupportActionBar().hide();
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_splash);
    init();
    testInit();
  }
  @Override
  protected void onActivityResult(int reqCode, int resCode, Intent data) {
    enterHome();
    super.onActivityResult(reqCode, resCode, data);
  }

  private void testInit() {
    System.out.println(" mVersionCode: " + mVersionName
                           + " mVersionCode : " + mVersionCode);
  }

  private void init() {
    mTvProgress = (TextView) findViewById(R.id.tvProgress);
    mTvVersion = (TextView) findViewById(R.id.tvVersion);
    try {
      mPackageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    mVersionCode = getVerCode();
    mVersionName = getVerName();
    mTvVersion.setText("Version : " + mVersionName);
    mHander = new SplashHander();
    checkVersion();
  }

  /**
   * check version , update local
   * if version lower then server version
   */
  private void checkVersion() {
    new Thread()
    {
      @Override
      public void run() {
        HttpURLConnection conn = null;
        String result = null;
        Message msg = new Message();
        try {
          conn = (HttpURLConnection) new URL("http://x.33oy.com/v/version.json")
                                         .openConnection();
          result = StreamUtils.getJsonStream(conn.getInputStream());
          JSONObject jsObj = new JSONObject(result);
          int versionCode = jsObj.getInt("versionCode");
          mDowmloadUrl = jsObj.getString(("downloadUrl"));

          System.out.println(" mDowmloadUrl : " + mDowmloadUrl
                                 + " versionCode : " + versionCode);

          if (mVersionCode < versionCode) {
            msg.what = CODE_UPDATE_DIALOG;
          } else {
            currentTime = System.currentTimeMillis();
            msg.what = CODE_ENTER_HOME;
          }
        } catch (IOException e) {
          msg.what = CODE_NET_ERROR;
          e.printStackTrace();
        } catch (JSONException e) {
          msg.what = CODE_JSON_ERROR;
          e.printStackTrace();
        } finally {
          if (conn != null) {
            conn.disconnect();
            mHander.sendMessage(msg);
          }
        }
      }
    }.start();

    //JSONObject jsObj =
  }

  /**
   * Show
   *
   * @param dowmloadUrl
   */
  protected void upDateVersion(String dowmloadUrl) {
    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    dialog.setTitle("Could you update new version ~ ?");
    dialog.setPositiveButton("Update", new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        Toast.makeText(getApplicationContext(), "Start Download", Toast.LENGTH_LONG).show();
        // if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
        download();
        /*
        }else {
          Message msg = Message.obtain();
          msg.what = CODE_NOT_EXTENAL_STORAGE_ERROR;
          mHander.sendMessage(msg);
        }
        */
      }

    });
    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        enterHome();
      }
    });
    dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
    {
      @Override
      public void onCancel(DialogInterface dialog) {
        enterHome();
      }
    });
    dialog.show();
    //TODO
    //File f = StreamUtils.download(dowmloadUrl);
  }

  private void download() {
    String target = Environment.getExternalStorageDirectory()
                        + File.separator + "update.apk";
    //HttpUtils httpUtils = new HttpUtils();
    //RequestParams requestParams = new RequestParams(mDowmloadUrl);
    HttpUtils httpUtils = new HttpUtils(5000);
    httpUtils.download(mDowmloadUrl, target, new RequestCallBack<File>()
    {
      @Override
      public void onStart() {
        super.onStart();
        //Toast.makeText(getApplicationContext(), "Down in start", Toast.LENGTH_LONG).show();
      }

      @Override
      public void onSuccess(ResponseInfo<File> responseInfo) {
        //Toast.makeText(getApplicationContext(), "Down success\n"
         //                                           + responseInfo.result.getPath(), Toast.LENGTH_LONG).show();
        System.out.println(responseInfo.result.getPath());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(responseInfo.result),
                                 "application/vnd.android.package-archive");
        //startActivity(intent);
        startActivityForResult(intent,0);
      }

      @Override
      public void onLoading(long total, long current, boolean isUploading) {
        super.onLoading(total, current, isUploading);
        mTvProgress.setVisibility(TextView.VISIBLE);
        mTvProgress.setText(current + " / " + total);

      }

      @Override
      public void onFailure(HttpException error, String msg) {
        //System.out.println(error.toString() + " : " + msg);
        Toast.makeText(getApplicationContext(), error.toString() + " : " + msg, Toast.LENGTH_LONG).show();
        enterHome();
      }

    });
  }


  /**
   * get version name from manifests
   *
   * @return String version name
   */
  private String getVerName() {
    return mPackageInfo.versionName;
  }

  /**
   * get version code from manifests
   *
   * @return int version code
   */
  private int getVerCode() {
    return mPackageInfo.versionCode;
  }


  /**
   * Go to home activity
   */
  private void enterHome() {
    long sleepTime = System.currentTimeMillis() - currentTime;
    if (sleepTime < 3000) {
      try {
        Thread.sleep(3000 - sleepTime);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    Intent intent = new Intent(this, HomeActivity.class);
    startActivity(intent);
    finish();
  }

  /**

  /**
   *
   */
  class SplashHander extends Handler
  {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case CODE_UPDATE_DIALOG:
          upDateVersion(mDowmloadUrl);
          break;
        case CODE_NET_ERROR:
          enterHome();
          break;
        case CODE_JSON_ERROR:
          enterHome();
          break;
        case CODE_ENTER_HOME:
          enterHome();
          break;
        case CODE_NOT_EXTENAL_STORAGE_ERROR:
          Toast.makeText(SplashActivity.this, "Not SD card", Toast.LENGTH_LONG).show();
        default:
          break;
      }
    }
  }
}
