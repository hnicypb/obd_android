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
	
	public ObdDemoData(Context context){
		m_Context = context;
	}
	
	

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
            m_listDemoDataList = pullDemoService.getDatastream(in);
            
            for(int i=0;i<m_listDemoDataList.size();i++){
            	DataStreamItem dsi = (DataStreamItem)m_listDemoDataList.get(i);
            	Log.i("DataItem", dsi.getDataItem("xFF010001").toString());
            }
            
            for(int i=0;i<m_listTroubleCode.size();i++){
            	TroubleCodeItem tci = (TroubleCodeItem)m_listTroubleCode.get(i);
            	Log.i("DataItem", tci.getsCodeText());
            }
             
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
		
		return true;
	}
	
	
}
