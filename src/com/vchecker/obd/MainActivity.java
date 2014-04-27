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
 *	�����������Զ���TabHost
 */
public class MainActivity extends FragmentActivity{	
	//����FragmentTabHost����
	private FragmentTabHost mTabHost;
	
	//����һ������
	private LayoutInflater layoutInflater;
		
	//�������������Fragment����
	private Class fragmentArray[] = {FragmentPage_idle.class,FragmentPage_tour.class,FragmentPage_race.class,FragmentPage_detail.class,FragmentPage_setup.class};
		
	//Tabѡ�������
	private String mTextviewArray[] = {"����", "Ѳ��", "����", "��ϸ", "����"};
	
	//��ʱˢ�½�������
	private Handler handlerUpdate = new Handler();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//���ر���
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);//����ȫ��

        setContentView(R.layout.activity_main);
        
        // ��ʼ������
        initView();
        // ��ʼ����ʾ����
        initDemoData();
        // ��ʱˢ�½�������
        handlerUpdate.removeCallbacks(runnable);
        handlerUpdate.postDelayed(runnable,1000); 
    }
	
	/**
	 * ��ʼ����ʱ����
	 */
	private void initDemoData(){
		ObdDemoData demoData = new ObdDemoData(this);
		demoData.fInitDemoData();
	}	
	 
	/**
	 * ��ʼ�����
	 */
	private void initView(){
		//ʵ�������ֶ���
		layoutInflater = LayoutInflater.from(this);
				
		//ʵ����TabHost���󣬵õ�TabHost
		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);	

		//�õ�fragment�ĸ���
		int count = fragmentArray.length;	
				
		for(int i = 0; i < count; i++){	
			//Ϊÿһ��Tab��ť����ͼ�ꡢ���ֺ�����
			TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
			//��Tab��ť��ӽ�Tabѡ���
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
			//����Tab��ť�ı���
			mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
			
		}
	}
				
	/**
	 * ��Tab��ť����ͼ�������
	 */
	private View getTabItemView(int index){
		View view = layoutInflater.inflate(R.layout.tab_item_view, null);
			
		// ���ð�ť�ı�
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
