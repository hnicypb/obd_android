package com.vchecker.obd;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SplashActivity extends Activity {
    private TextView tv_version;
    private LinearLayout ll;

	//定时刷新界面数据
	private Handler handlerLoadMain = new Handler();
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        //设置不要显示标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        
        //设置全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        tv_version = (TextView) findViewById(R.id.tv_splash_version);
        tv_version.setText("版本号  " + getVersion());
        
        ll = (LinearLayout) findViewById(R.id.ll_splash_main);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(2000);
        ll.startAnimation(alphaAnimation);

        handlerLoadMain.postDelayed(runnable,3000); 
    }

    private Runnable runnable = new Runnable() {
         public void run () {
             handlerLoadMain.removeCallbacks(runnable);
             LoadMain();
      }
    };
    
    private void LoadMain(){
        Intent intent = new Intent(this, MainPager.class);
        startActivity(intent);
        finish(); 
    }
    
    private String getVersion()
    {
        try
        {
                PackageManager packageManager = getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
                
                return packageInfo.versionName;
        }
        catch (NameNotFoundException e)
        {
                e.printStackTrace();
                return "版本号未知";
        }
    }
}

