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
            
//            for(int i=0;i<m_listDemoDataList.size();i++){
//            	DataStreamItem dsi = (DataStreamItem)m_listDemoDataList.get(i);
//            	String strText = "";
//            	for(int j=0;j<m_strDsIDs.length;j++){
//            		strText += dsi.getDataItem(m_strDsIDs[j].toString()) + ";";
//            	}
//            	Log.i("DataItem", strText);
//            }
//            
//            for(int i=0;i<m_listTroubleCode.size();i++){
//            	TroubleCodeItem tci = (TroubleCodeItem)m_listTroubleCode.get(i);
//            	Log.i("DataItem", tci.getsCodeText());
//            }
             
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
		
		return true;
	}
	
	
}
