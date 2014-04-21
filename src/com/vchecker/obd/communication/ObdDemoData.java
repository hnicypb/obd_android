package com.vchecker.obd.communication;

import java.io.InputStream;  
import java.io.StringWriter;  
import java.util.ArrayList;  
import java.util.HashMap;
import java.util.List;  
  
import java.util.Map;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;  
import org.xmlpull.v1.XmlSerializer;  

import android.content.Context;
import android.util.Log;
import android.util.Xml;  
import android.widget.Toast;

import com.vchecker.obd.communication.entity.*;
import com.vchecker.obd.communication.service.*;

public class ObdDemoData {

	private Context m_Context;	
	// 所有Demo数据
	private List<DataStreamItem> m_listDemoDataList;		
	// 故障码数据
	private List<TroubleCodeItem> m_listTroubleCode;
	// 数据流数据
    private String m_strDsIDs[] = {"xFF010001","xFF010002","xFF010005","xFF010006","xFF010007","xFF010008","xFF010009",
    		"xFF01000B","xFF01000E","x00000400","x00000500","x00000B00","x00000C00","x00000D00","x00000E00",
    		"x00000F00","x00001100",};
	public ObdDemoData(Context context){
		m_Context = context;
	}
	
	private int miCurrDemoDataIndex=0;
	
	//当前油价
	private float mfFuelPrice;
	//即时油耗系数
	private float mfNowFCC;
	//平局油耗系数
	private float mfAvgFCC;

	/** 初始化Demo数据 使用pull方式解析xml
	 * @return 成功返回true，失败返回false
	 */
	public boolean fInitDemoData(){
		try{
            InputStream in = m_Context.getResources().getAssets().open("DemoData.xml");
            
            PullDemoDataParseService pullDemoService = new PullDemoDataParseService();   
            in.mark(0);
            m_listTroubleCode = pullDemoService.getTroubleCode(in);            
            in.reset();
            m_listDemoDataList = pullDemoService.getDatastream(in,m_strDsIDs);
            
            for(int i=0;i<m_listDemoDataList.size();i++){
            	DataStreamItem dsi = (DataStreamItem)m_listDemoDataList.get(i);
//            	String strText = "";
//            	for(int j=0;j<m_strDsIDs.length;j++){
//            		strText += dsi.getDataItem(m_strDsIDs[j].toString()) + ";";
//            	}
//            	Log.i("DataItem", strText);
            	//本次行驶油费
            	dsi.setDataItemF("x00020001", 0.0);
            	dsi.setDataItemS("x00020001", "-----");
            	//本次每公里花费
            	dsi.setDataItemF("x00020002", 0.0);
            	dsi.setDataItemS("x00020002", "-----");
            	//总行车油费
            	dsi.setDataItemF("x00020003", 0.0);
            	dsi.setDataItemS("x00020003", "-----");
            	//累计每公里花费
            	dsi.setDataItemF("x00020004", 0.0);
            	dsi.setDataItemS("x00020004", "-----");
            	//累计耗油量
            	dsi.setDataItemF("xFF01000F", 0.0);
            	dsi.setDataItemS("xFF01000F", "-----");
            	//累计里程
            	dsi.setDataItemF("xFF01000A", 0.0);
            	dsi.setDataItemS("xFF01000A", "-----");
            }
//            
//            for(int i=0;i<m_listTroubleCode.size();i++){
//            	TroubleCodeItem tci = (TroubleCodeItem)m_listTroubleCode.get(i);
//            	Log.i("DataItem", tci.getsCodeText());
//            }
            

        	
            
            mfFuelPrice = (float) 7.9;
            mfAvgFCC = 116;
            mfNowFCC = 331;
             
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
		
		return true;
	}
	
	public DataStreamItem fGetNextDataStream(){
		DataStreamItem dsi = m_listDemoDataList.get(miCurrDemoDataIndex);
		
		Map<String, Float> m = dsi.getM_mapDataItemS();
		for(String key : m.keySet()){
			Log.i("DataItem", key + " = " + String.valueOf(m.get(key)));
		}
		
		//计算值
		//本次行驶里程
		float fValue = (float) (dsi.getDataItemF("xFF010008"));
		dsi.setDataItemS("xFF010008", String.valueOf(fValue));
		Log.i("DataItem", "本次行驶里程xFF010008="+dsi.getDataItemS("xFF010008"));
		//累计行驶里程
		fValue = (float) (dsi.getDataItemF("xFF010008") + dsi.getDataItemF("xFF01000A"));
		dsi.setDataItemF("xFF01000A", fValue);
		dsi.setDataItemS("xFF01000A", String.valueOf(fValue));
		Log.i("DataItem", "累计行驶里程xFF01000A="+dsi.getDataItemS("xFF01000A"));
		
		//本次耗油
		fValue = (float) (dsi.getDataItemF("xFF01000E")/116.0*mfAvgFCC);
		dsi.setDataItemF("xFF01000E", fValue);
		dsi.setDataItemS("xFF01000E", String.valueOf(fValue));
		Log.i("DataItem", "本次耗油xFF01000E="+dsi.getDataItemS("xFF01000E"));
		//累计耗油
		fValue = (float) (dsi.getDataItemF("xFF01000E") + mfFuelPrice*dsi.getDataItemF("xFF01000F"));
		dsi.setDataItemF("xFF01000F", fValue);
		dsi.setDataItemS("xFF01000F", String.valueOf(fValue));
		Log.i("DataItem", "累计耗油xFF01000F="+dsi.getDataItemS("xFF01000F"));
		
		//本次行驶油费(本次耗油量L*油价)
		fValue = mfFuelPrice*dsi.getDataItemF("xFF01000E");
		dsi.setDataItemF("x00020001", fValue);
		dsi.setDataItemS("x00020001",String.valueOf(fValue));		
		Log.i("DataItem", "本次行驶油费x00020001="+dsi.getDataItemS("x00020001"));
		// 总行车油费(总行车油费L*油价)
		fValue = (float)mfFuelPrice*dsi.getDataItemF("x00020003");
		dsi.setDataItemF("x00020003", fValue);
		dsi.setDataItemS("x00020003", String.valueOf(fValue));		
		Log.i("DataItem", "总行车油费x00020003="+dsi.getDataItemS("x00020003"));

		//本次每公里花费(本次耗油量L*油价)/本次里程
		fValue = (float)mfFuelPrice*dsi.getDataItemF("xFF01000E")/dsi.getDataItemF("xFF010008");
		dsi.setDataItemF("x00020002", fValue);
		dsi.setDataItemS("x00020002", String.valueOf(fValue));		
		Log.i("DataItem", "本次每公里花费x00020002="+dsi.getDataItemS("x00020002"));
		//累计每公里花费(总行车油费L*油价)/累计里程
		fValue = (float)mfFuelPrice*dsi.getDataItemF("xFF01000F")/dsi.getDataItemF("xFF01000A");
		dsi.setDataItemF("x00020004", fValue);
		dsi.setDataItemS("x00020004", String.valueOf(fValue));
		Log.i("DataItem", "累计每公里花费x00020004="+dsi.getDataItemS("x00020004"));

//		//本次行驶时间
		int iMinuter = (int) (dsi.getDataItemF("xFF010007")/60);
		dsi.setDataItemS("xFF010007", String.format("%02d",iMinuter/60)	+ ":" + String.format("%02d",iMinuter%60));
		
		if(miCurrDemoDataIndex++>=(m_listDemoDataList.size()-1))
			miCurrDemoDataIndex=0;
		
		return dsi;
	}
	
}
