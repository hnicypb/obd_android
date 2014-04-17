package com.vchecker.obd.communication.service;
import java.io.InputStream;  
import java.util.ArrayList;  
import java.util.List;    
import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;    

import android.util.Log;
import android.util.Xml;  
import android.widget.Toast;

import com.vchecker.obd.communication.entity.*;

public class PullDemoDataParseService {
	/**用PULL解析故障码数据
	 * @param inputStream --- XML文件流
	 * @return  故障码信息
	 * @throws Exception
	 */
	public static List<TroubleCodeItem> getTroubleCode(InputStream inputStream) throws Exception{  
		List<TroubleCodeItem> listTroubleCode = null;  
		TroubleCodeItem troubleCodeItem = null;  
        XmlPullParser parser = Xml.newPullParser();  
        parser.setInput(inputStream, "UTF-8");  
        
        Log.i("DataItem", "获取故障码的演示数据");
        
        int event = parser.getEventType();//产生第一个事件  
        while(event!=XmlPullParser.END_DOCUMENT){  
            switch(event){  
            case XmlPullParser.START_DOCUMENT:{//判断当前事件是否是文档开始事件  
                	Log.i("TroubleCode_List", "初始化故障码集合");
                	listTroubleCode = new ArrayList<TroubleCodeItem>();//初始化集合            		
            	}
                break;  
            case XmlPullParser.START_TAG:{//判断当前事件是否是标签元素开始事件  
	                if("TroubleCodeItem".equals(parser.getName())){//判断开始标签元素是否是
	                	troubleCodeItem = new TroubleCodeItem();  
	                	troubleCodeItem.setiCodeID(Integer.parseInt(parser.getAttributeValue(0)));//
	                	troubleCodeItem.setsCodeText(parser.getAttributeValue(1));
	                } 
            	}
                break;  
            case XmlPullParser.END_TAG://判断当前事件是否是标签元素结束事件  
                if("TroubleCodeItem".equals(parser.getName())){//判断结束标签元素是否 

                	listTroubleCode.add(troubleCodeItem);//
                	troubleCodeItem = null;  
                }  
                break;  
            }  
            event = parser.next();//进入下一个元素并触发相应事件  
        }//end while  
        return listTroubleCode;  
    }  
	
	/**
	 * @param inputStream --- XML文件流
	 * @param strDsIDs --- 数据流ID列表
	 * @return  数据流数据
	 * @throws Exception
	 */
	public static List<DataStreamItem> getDatastream(InputStream inputStream,String[] strDsIDs) throws Exception{  
		List<DataStreamItem> listDataStream = null;  
		DataStreamItem dataStreamItem = null;  
        XmlPullParser parser = Xml.newPullParser();  
        parser.setInput(inputStream, "UTF-8");  
        
        Log.i("DataItem", "获取数据流的演示数据");
        
        int event = parser.getEventType();//产生第一个事件  
        while(event!=XmlPullParser.END_DOCUMENT){  
            switch(event){  
            case XmlPullParser.START_DOCUMENT:{//判断当前事件是否是文档开始事件  
                	Log.i("DataStream_List", "初始化数据流集合");
                	listDataStream = new ArrayList<DataStreamItem>();//初始化集合            		
            	}
                break;  
            case XmlPullParser.START_TAG:{//判断当前事件是否是标签元素开始事件  
	                if("DataItem".equals(parser.getName())){//判断开始标签元素是否是
	                	dataStreamItem = new DataStreamItem();  
	                	for(int i=0;i<strDsIDs.length;i++){
		                	dataStreamItem.setDataItem(strDsIDs[i],Float.parseFloat(parser.getAttributeValue(i)));//
	                	}
	                } 
            	}
                break;  
            case XmlPullParser.END_TAG://判断当前事件是否是标签元素结束事件  
                if("DataItem".equals(parser.getName())){//判断结束标签元素是否 

                	listDataStream.add(dataStreamItem);//
                	dataStreamItem = null;  
                }  
                break;  
            }  
            event = parser.next();//进入下一个元素并触发相应事件  
        }//end while  
        return listDataStream;  
    }  
}
