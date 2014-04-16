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
	
	public static List<DataStreamItem> getDatastream(InputStream inputStream) throws Exception{  
		List<DataStreamItem> listDataStream = null;  
		DataStreamItem dataStreamItem = null;  
        XmlPullParser parser = Xml.newPullParser();  
        parser.setInput(inputStream, "UTF-8");  
        
        Log.i("DataItem", "获取数据流的演示数据");
        String strDsID[] = {"xFF010001","xFF010002","xFF010005","xFF010006","xFF010007","xFF010008","xFF010009",
        		"xFF01000B","xFF01000E","x00000400","x00000500","x00000B00","x00000C00","x00000D00","x00000E00",
        		"x00000F00","x00001100",};
        
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
	                	for(int i=0;i<17;i++){
		                	dataStreamItem.setDataItem(strDsID[i],Float.parseFloat(parser.getAttributeValue(i)));//
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
