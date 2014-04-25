package com.vchecker.obd;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import com.vchecker.obd.R;
import com.vchecker.obd.MainPager.TabsAdapter;
import com.vchecker.obd.MainPager.TabsAdapter.DummyTabFactory;
import com.vchecker.obd.MainPager.TabsAdapter.TabInfo;
import com.vchecker.obd.communication.ObdDemoData;
import com.vchecker.obd.communication.entity.DataStreamItem;
import com.vchecker.obd.main.*;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;
import android.os.Build;
import android.preference.PreferenceManager;

public class MainPager extends FragmentActivity {

	private FragmentTabHost mTabHost;
	private RadioGroup mTabRg;
	private ViewPager mViewPage;
	TabsAdapter mTabsAdapter;
	private final Class[] fragments = { FragmentPage_idle.class, FragmentPage_tour.class,
			FragmentPage_race.class, FragmentPage_detail.class,FragmentPage_setup.class };
	
	public static final int WEEKDAYS = 7;	  
	
	Context mThis;

	//定时刷新界面数据
	private Handler handlerUpdate = new Handler();
	ObdDemoData mDemoData;
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
        // 定时刷新界面数据
        handlerUpdate.removeCallbacks(runnable);
        handlerUpdate.postDelayed(runnable,1000); 
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
        handlerUpdate.removeCallbacks(runnable);
	}

    public static final int SET = Menu.FIRST;  
    public static final int EXIT = Menu.FIRST+1;  
	 //创建Menu菜单  
    @Override  
    public boolean onCreateOptionsMenu(Menu menu) {  
        menu.add(0,SET,0,"设置").setIcon(android.R.drawable.ic_menu_preferences);  
        return super.onCreateOptionsMenu(menu);  
    }  
    
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) { 
        // TODO Auto-generated method stub  
        super.onOptionsItemSelected(item);  
        Intent intent = new Intent(this, PreferenceSetup.class);  
        startActivity(intent);  
        
		return false;
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        
		setContentView(R.layout.activity_main_pager);
		
		mThis = this;
		// 初始化界面
		initView();
		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));			
		}
		
        // 初始化演示数据
        initDemoData();
	}

	/**
	 * 初始化演示数据
	 */
	private void initDemoData(){
		mDemoData = new ObdDemoData(this);
		mDemoData.fInitDemoData();
	}	

	private void update(){		
		DataStreamItem ds = mDemoData.fGetNextDataStream();
		TextView tv;
		switch(mTabHost.getCurrentTab()){
		case 0:{//怠速 {"x00020001","xFF010005","xFF010002","x00000C00","xFF01000B","x00000F00"};			
			tv = (TextView)findViewById(R.id.tvIdleLeftUpValue);
			tv.setText(ds.getDataItemS("x00020001"));
			tv = (TextView)findViewById(R.id.tvIdleLeftCenterValue);
			tv.setText(ds.getDataItemS("xFF010005"));//			
			tv = (TextView)findViewById(R.id.tvIdleLeftDownValue);
			tv.setText(ds.getDataItemS("xFF010002"));			
			tv = (TextView)findViewById(R.id.tvIdleRightUpValue);
			tv.setText(ds.getDataItemS("x00000C00"));			
			tv = (TextView)findViewById(R.id.tvIdleRightCenterValue);
			tv.setText(ds.getDataItemS("xFF01000B"));			
			tv = (TextView)findViewById(R.id.tvIdleRightDownValue);
			tv.setText(ds.getDataItemS("x00000F00"));
			
			tv = (TextView)findViewById(R.id.tvWaterValue);
			tv.setText(ds.getDataItemS("x00000500"));
			
			long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();  
		    SimpleDateFormat formatDate=new SimpleDateFormat("yyyy-MM-dd");  
		    SimpleDateFormat formatTime=new SimpleDateFormat("HH:mm:ss"); 
		    Date date=new Date(time);  
		    String sDate=formatDate.format(date);
		    String sTime=formatTime.format(date);
		        
			tv = (TextView)findViewById(R.id.tvDate);
			tv.setText(sDate);

			tv = (TextView)findViewById(R.id.tvTime);
			tv.setText(sTime);
				
			Calendar calendar = Calendar.getInstance();  
		    calendar.setTime(date);  
		    String mWay=getResources().getStringArray(R.array.Week)[calendar.get(Calendar.DAY_OF_WEEK)-1];
	        tv = (TextView)findViewById(R.id.tvWeek);
			tv.setText(mWay);
		}
		break;
		case 1:{//巡航{"0xFF010007","0xFF010009","0x00000500","0xFF010008","0xFF010006","0xFF010001"};
			tv = (TextView)findViewById(R.id.tvTourLeftUpValue);
			tv.setText(ds.getDataItemS("xFF010007"));
			tv = (TextView)findViewById(R.id.tvTourLeftCenterValue);
			tv.setText(ds.getDataItemS("xFF010009"));//			
			tv = (TextView)findViewById(R.id.tvTourLeftDownValue);
			tv.setText(ds.getDataItemS("x00000500"));			
			tv = (TextView)findViewById(R.id.tvTourRightUpValue);
			tv.setText(ds.getDataItemS("xFF010008"));			
			tv = (TextView)findViewById(R.id.tvTourRightCenterValue);
			tv.setText(ds.getDataItemS("xFF010006"));			
			tv = (TextView)findViewById(R.id.tvTourRightDownValue);
			tv.setText(ds.getDataItemS("xFF010001"));			

			tv = (TextView)findViewById(R.id.tvSpeedvalue);
			tv.setText(ds.getDataItemS("x00000D00"));
			
		}
		break;
		case 2:{//竞技{"0x00000D00","0x00000F00","0x00001100","0x00000400","0x00000400","0x00000E00","0x00000C00","0x00000B00"};
			tv = (TextView)findViewById(R.id.tvRaceLeftUpValue);
			tv.setText(ds.getDataItemS("x00000D00"));	
			tv = (TextView)findViewById(R.id.tvRaceLeftDownValue);
			tv.setText(ds.getDataItemS("x00000F00"));		
			tv = (TextView)findViewById(R.id.tvRaceCenterUpValue);
			tv.setText(ds.getDataItemS("x00001100"));			
			tv = (TextView)findViewById(R.id.tvRaceCenterCenterValue);
			tv.setText(ds.getDataItemS("x00000400"));			
			tv = (TextView)findViewById(R.id.tvRaceCenterDownValue);
			tv.setText(ds.getDataItemS("x00000E00"));	
			tv = (TextView)findViewById(R.id.tvRaceRightUpValue);
			tv.setText(ds.getDataItemS("x00000C00"));			
			tv = (TextView)findViewById(R.id.tvRaceRightDownValue);
			tv.setText(ds.getDataItemS("x00000B00"));
			
		}
		break;
		case 3:{	//明细{"0xFF010008","0xFF010006","0xFF01000E","0x00020001","0x00020002","0xFF01000A","0xFF010005","0xFF01000F","0x00020003","0x00020004"}
			tv = (TextView)findViewById(R.id.tvDetailLeft1Value);
			tv.setText(ds.getDataItemS("xFF010008"));	
			tv = (TextView)findViewById(R.id.tvDetailLeft2Value);
			tv.setText(ds.getDataItemS("xFF010006"));		
			tv = (TextView)findViewById(R.id.tvDetailLeft3Value);
			tv.setText(ds.getDataItemS("xFF01000E"));			
			tv = (TextView)findViewById(R.id.tvDetailLeft4Value);
			tv.setText(ds.getDataItemS("x00020001"));			
			tv = (TextView)findViewById(R.id.tvDetailLeft5Value);
			tv.setText(ds.getDataItemS("x00020002"));	
			
			tv = (TextView)findViewById(R.id.tvDetailRight1Value);
			tv.setText(ds.getDataItemS("xFF01000A"));	
			tv = (TextView)findViewById(R.id.tvDetailRight2Value);
			tv.setText(ds.getDataItemS("xFF010005"));		
			tv = (TextView)findViewById(R.id.tvDetailRight3Value);
			tv.setText(ds.getDataItemS("xFF01000F"));			
			tv = (TextView)findViewById(R.id.tvDetailRight4Value);
			tv.setText(ds.getDataItemS("x00020003"));			
			tv = (TextView)findViewById(R.id.tvDetailRight5Value);
			tv.setText(ds.getDataItemS("x00020004"));	
		}
		break;
		case 4:{	//设置
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mThis); 
		    //根据preferences 的key获取entity value，并给出缺省值。由于应用第一次运行时，没有保存的preference文件，如果不使用缺省值，则返回null。
			//在这里，我们直接指定缺省值为“1”，这只是为了例子简单的便捷方式。实际上，我们应该在res/values/下设置我们的缺省值，
			//除了可在preference的xml中引用，还可以直接在此设定缺省值。同一个值不要在多处进行赋值是编程的基本原则之一。  
		    String option = prefs.getString("AlarmWaterTemp", "100"); 
		    //通过entity value获取entity的内容  
//		    String[] optionText = getResources().getStringArray(R.array.flight_sort_options); 
//		    showInfo("option = " + option + ",select : " + optionText[Integer.parseInt(option)]);
		}
		break;		
			
		}
	}
	
    private Runnable runnable = new Runnable() {
         public void run () {
             update();
             handlerUpdate.postDelayed(this,1000); 
      }
    };
    

	private void initView() {
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager());
		mViewPage = (ViewPager) findViewById(R.id.pager);
		mTabRg = (RadioGroup) findViewById(R.id.tab_rg_menu);
		mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPage, mTabRg);
		int count = fragments.length;
		for (int i = 0; i < count; i++) {
			TabSpec tabSpec = mTabHost.newTabSpec(i + "").setIndicator(i + "");
			mTabHost.addTab(tabSpec, fragments[i], null);
			mTabsAdapter.addTab(mTabHost.newTabSpec(i + "")
					.setIndicator(i + ""), fragments[i], null);
		}

		mTabRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.tab_rb_1:
					mTabHost.setCurrentTab(0);
					break;
				case R.id.tab_rb_2:
					mTabHost.setCurrentTab(1);
					break;
				case R.id.tab_rb_3:
					mTabHost.setCurrentTab(2);
					break;
				case R.id.tab_rb_4:
					mTabHost.setCurrentTab(3);
					break;
				case R.id.tab_rb_5:{
			        Intent intent = new Intent(mThis, PreferenceSetup.class);  
			        startActivity(intent);  
				}
				break;
				default:
					break;
				}
				
				update();
			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tab", mTabHost.getCurrentTabTag());
	}
	/**
	 * This is a helper class that implements the management of tabs and all
	 * details of connecting a ViewPager with associated TabHost. It relies on a
	 * trick. Normally a tab host has a simple API for supplying a View or
	 * Intent that each tab will show. This is not sufficient for switching
	 * between pages. So instead we make the content part of the tab host 0dp
	 * high (it is not shown) and the TabsAdapter supplies its own dummy view to
	 * show as the tab content. It listens to changes in tabs, and takes care of
	 * switch to the correct paged in the ViewPager whenever the selected tab
	 * changes.
	 */
	public static class TabsAdapter extends FragmentPagerAdapter implements
			TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
		private final Context mContext;
		private final TabHost mTabHost;
		private final ViewPager mViewPager;
		private final RadioGroup mTabRg;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		static final class TabInfo {
			private final String tag;
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(String _tag, Class<?> _class, Bundle _args) {
				tag = _tag;
				clss = _class;
				args = _args;
			}
		}

		static class DummyTabFactory implements TabHost.TabContentFactory {
			private final Context mContext;

			public DummyTabFactory(Context context) {
				mContext = context;
			}

			@Override
			public View createTabContent(String tag) {
				View v = new View(mContext);
				v.setMinimumWidth(0);
				v.setMinimumHeight(0);
				return v;
			}
		}

		public TabsAdapter(FragmentActivity activity, TabHost tabHost,
				ViewPager pager, RadioGroup tabRg) {
			super(activity.getSupportFragmentManager());
			mContext = activity;
			mTabHost = tabHost;
			mViewPager = pager;
			mTabRg = tabRg;
			mTabHost.setOnTabChangedListener(this);
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
			tabSpec.setContent(new DummyTabFactory(mContext));
			String tag = tabSpec.getTag();

			TabInfo info = new TabInfo(tag, clss, args);
			mTabs.add(info);
			mTabHost.addTab(tabSpec);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}

		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);
			return Fragment.instantiate(mContext, info.clss.getName(),
					info.args);
		}

		@Override
		public void onTabChanged(String tabId) {
			int position = mTabHost.getCurrentTab();
			mViewPager.setCurrentItem(position);
			((RadioButton) mTabRg.getChildAt(position)).setChecked(true);
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			// Unfortunately when TabHost changes the current tab, it kindly
			// also takes care of putting focus on it when not in touch mode.
			// The jerk.
			// This hack tries to prevent this from pulling focus out of our
			// ViewPager.
			TabWidget widget = mTabHost.getTabWidget();
			int oldFocusability = widget.getDescendantFocusability();
			widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
			mTabHost.setCurrentTab(position);
			widget.setDescendantFocusability(oldFocusability);
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}
	}
}
