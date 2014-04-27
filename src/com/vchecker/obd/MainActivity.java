package com.vchecker.obd;

import java.util.ArrayList;

import com.vchecker.obd.R;
import com.vchecker.obd.main.*;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.vchecker.obd.communication.ObdDemoData;

/**
 * @author yangyu
 *	功能描述：自定义TabHost
 */
public class MainActivity extends FragmentActivity{	
	//定义FragmentTabHost对象
	private FragmentTabHost mTabHost;
	
	//定义一个布局
	private LayoutInflater layoutInflater;
		
	//定义数组来存放Fragment界面
	private Class fragmentArray[] = {FragmentPage_idle.class,FragmentPage_tour.class,FragmentPage_race.class,FragmentPage_detail.class,FragmentPage_setup.class};
		
	//Tab选项卡的文字
	private String mTextviewArray[] = {"怠速", "巡航", "竞技", "明细", "设置"};
	
	//定时刷新界面数据
	private Handler handlerUpdate = new Handler();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏

        setContentView(R.layout.activity_main);
        
        // 初始化界面
        initView();
        // 初始化演示数据
        initDemoData();
        // 定时刷新界面数据
        handlerUpdate.removeCallbacks(runnable);
        handlerUpdate.postDelayed(runnable,1000); 
    }
	
	/**
	 * 初始化延时数据
	 */
	private void initDemoData(){
		ObdDemoData demoData = new ObdDemoData(this);
		demoData.fInitDemoData();
	}	
	 
	/**
	 * 初始化组件
	 */
	private void initView(){
		//实例化布局对象
		layoutInflater = LayoutInflater.from(this);
				
		//实例化TabHost对象，得到TabHost
		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);	

		//得到fragment的个数
		int count = fragmentArray.length;	
				
		for(int i = 0; i < count; i++){	
			//为每一个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
			//将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
			//设置Tab按钮的背景
			mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
			
		}
	}
				
	/**
	 * 给Tab按钮设置图标和文字
	 */
	private View getTabItemView(int index){
		View view = layoutInflater.inflate(R.layout.tab_item_view, null);
			
		// 设置按钮文本
		TextView textView = (TextView) view.findViewById(R.id.textview);		
		textView.setText(mTextviewArray[index]);
	
		return view;
	}
	
	private void update(){
		
	}
	
    private Runnable runnable = new Runnable() {
         public void run () {
             update();
             handlerUpdate.postDelayed(this,1000); 
      }
    };
    

}
