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
	/**��PULL��������������
	 * @param inputStream --- XML�ļ���
	 * @return  ��������Ϣ
	 * @throws Exception
	 */
	public static List<TroubleCodeItem> getTroubleCode(InputStream inputStream) throws Exception{  
		List<TroubleCodeItem> listTroubleCode = null;  
		TroubleCodeItem troubleCodeItem = null;  
        XmlPullParser parser = Xml.newPullParser();  
        parser.setInput(inputStream, "UTF-8");  
        
        Log.i("DataItem", "��ȡ���������ʾ����");
        
        int event = parser.getEventType();//������һ���¼�  
        while(event!=XmlPullParser.END_DOCUMENT){  
            switch(event){  
            case XmlPullParser.START_DOCUMENT:{//�жϵ�ǰ�¼��Ƿ����ĵ���ʼ�¼�  
                	Log.i("TroubleCode_List", "��ʼ�������뼯��");
                	listTroubleCode = new ArrayList<TroubleCodeItem>();//��ʼ������            		
            	}
                break;  
            case XmlPullParser.START_TAG:{//�жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؿ�ʼ�¼�  
	                if("TroubleCodeItem".equals(parser.getName())){//�жϿ�ʼ��ǩԪ���Ƿ���
	                	troubleCodeItem = new TroubleCodeItem();  
	                	troubleCodeItem.setiCodeID(Integer.parseInt(parser.getAttributeValue(0)));//
	                	troubleCodeItem.setsCodeText(parser.getAttributeValue(1));
	                } 
            	}
                break;  
            case XmlPullParser.END_TAG://�жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؽ����¼�  
                if("TroubleCodeItem".equals(parser.getName())){//�жϽ�����ǩԪ���Ƿ� 

                	listTroubleCode.add(troubleCodeItem);//
                	troubleCodeItem = null;  
                }  
                break;  
            }  
            event = parser.next();//������һ��Ԫ�ز�������Ӧ�¼�  
        }//end while  
        return listTroubleCode;  
    }  
	
	/**
	 * @param inputStream --- XML�ļ���
	 * @param strDsIDs --- ������ID�б�
	 * @return  ����������
	 * @throws Exception
	 */
	public static List<DataStreamItem> getDatastream(InputStream inputStream,String[] strDsIDs) throws Exception{  
		List<DataStreamItem> listDataStream = null;  
		DataStreamItem dataStreamItem = null;  
        XmlPullParser parser = Xml.newPullParser();  
        parser.setInput(inputStream, "UTF-8");  
        
        Log.i("DataItem", "��ȡ����������ʾ����");
        
        int event = parser.getEventType();//������һ���¼�  
        while(event!=XmlPullParser.END_DOCUMENT){  
            switch(event){  
            case XmlPullParser.START_DOCUMENT:{//�жϵ�ǰ�¼��Ƿ����ĵ���ʼ�¼�  
                	Log.i("DataStream_List", "��ʼ������������");
                	listDataStream = new ArrayList<DataStreamItem>();//��ʼ������            		
            	}
                break;  
            case XmlPullParser.START_TAG:{//�жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؿ�ʼ�¼�  
	                if("DataItem".equals(parser.getName())){//�жϿ�ʼ��ǩԪ���Ƿ���
	                	dataStreamItem = new DataStreamItem();  
	                	for(int i=0;i<strDsIDs.length;i++){
		                	dataStreamItem.setDataItem(strDsIDs[i],Float.parseFloat(parser.getAttributeValue(i)));//
	                	}
	                } 
            	}
                break;  
            case XmlPullParser.END_TAG://�жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؽ����¼�  
                if("DataItem".equals(parser.getName())){//�жϽ�����ǩԪ���Ƿ� 

                	listDataStream.add(dataStreamItem);//
                	dataStreamItem = null;  
                }  
                break;  
            }  
            event = parser.next();//������һ��Ԫ�ز�������Ӧ�¼�  
        }//end while  
        return listDataStream;  
    }  
}
