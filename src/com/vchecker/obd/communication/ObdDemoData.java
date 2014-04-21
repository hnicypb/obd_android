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
	// ����Demo����
	private List<DataStreamItem> m_listDemoDataList;		
	// ����������
	private List<TroubleCodeItem> m_listTroubleCode;
	// ����������
    private String m_strDsIDs[] = {"xFF010001","xFF010002","xFF010005","xFF010006","xFF010007","xFF010008","xFF010009",
    		"xFF01000B","xFF01000E","x00000400","x00000500","x00000B00","x00000C00","x00000D00","x00000E00",
    		"x00000F00","x00001100",};
	public ObdDemoData(Context context){
		m_Context = context;
	}
	
	private int miCurrDemoDataIndex=0;
	
	//��ǰ�ͼ�
	private float mfFuelPrice;
	//��ʱ�ͺ�ϵ��
	private float mfNowFCC;
	//ƽ���ͺ�ϵ��
	private float mfAvgFCC;

	/** ��ʼ��Demo���� ʹ��pull��ʽ����xml
	 * @return �ɹ�����true��ʧ�ܷ���false
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
            	//������ʻ�ͷ�
            	dsi.setDataItemF("x00020001", 0.0);
            	dsi.setDataItemS("x00020001", "-----");
            	//����ÿ���ﻨ��
            	dsi.setDataItemF("x00020002", 0.0);
            	dsi.setDataItemS("x00020002", "-----");
            	//���г��ͷ�
            	dsi.setDataItemF("x00020003", 0.0);
            	dsi.setDataItemS("x00020003", "-----");
            	//�ۼ�ÿ���ﻨ��
            	dsi.setDataItemF("x00020004", 0.0);
            	dsi.setDataItemS("x00020004", "-----");
            	//�ۼƺ�����
            	dsi.setDataItemF("xFF01000F", 0.0);
            	dsi.setDataItemS("xFF01000F", "-----");
            	//�ۼ����
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
		
		//����ֵ
		//������ʻ���
		float fValue = (float) (dsi.getDataItemF("xFF010008"));
		dsi.setDataItemS("xFF010008", String.valueOf(fValue));
		Log.i("DataItem", "������ʻ���xFF010008="+dsi.getDataItemS("xFF010008"));
		//�ۼ���ʻ���
		fValue = (float) (dsi.getDataItemF("xFF010008") + dsi.getDataItemF("xFF01000A"));
		dsi.setDataItemF("xFF01000A", fValue);
		dsi.setDataItemS("xFF01000A", String.valueOf(fValue));
		Log.i("DataItem", "�ۼ���ʻ���xFF01000A="+dsi.getDataItemS("xFF01000A"));
		
		//���κ���
		fValue = (float) (dsi.getDataItemF("xFF01000E")/116.0*mfAvgFCC);
		dsi.setDataItemF("xFF01000E", fValue);
		dsi.setDataItemS("xFF01000E", String.valueOf(fValue));
		Log.i("DataItem", "���κ���xFF01000E="+dsi.getDataItemS("xFF01000E"));
		//�ۼƺ���
		fValue = (float) (dsi.getDataItemF("xFF01000E") + mfFuelPrice*dsi.getDataItemF("xFF01000F"));
		dsi.setDataItemF("xFF01000F", fValue);
		dsi.setDataItemS("xFF01000F", String.valueOf(fValue));
		Log.i("DataItem", "�ۼƺ���xFF01000F="+dsi.getDataItemS("xFF01000F"));
		
		//������ʻ�ͷ�(���κ�����L*�ͼ�)
		fValue = mfFuelPrice*dsi.getDataItemF("xFF01000E");
		dsi.setDataItemF("x00020001", fValue);
		dsi.setDataItemS("x00020001",String.valueOf(fValue));		
		Log.i("DataItem", "������ʻ�ͷ�x00020001="+dsi.getDataItemS("x00020001"));
		// ���г��ͷ�(���г��ͷ�L*�ͼ�)
		fValue = (float)mfFuelPrice*dsi.getDataItemF("x00020003");
		dsi.setDataItemF("x00020003", fValue);
		dsi.setDataItemS("x00020003", String.valueOf(fValue));		
		Log.i("DataItem", "���г��ͷ�x00020003="+dsi.getDataItemS("x00020003"));

		//����ÿ���ﻨ��(���κ�����L*�ͼ�)/�������
		fValue = (float)mfFuelPrice*dsi.getDataItemF("xFF01000E")/dsi.getDataItemF("xFF010008");
		dsi.setDataItemF("x00020002", fValue);
		dsi.setDataItemS("x00020002", String.valueOf(fValue));		
		Log.i("DataItem", "����ÿ���ﻨ��x00020002="+dsi.getDataItemS("x00020002"));
		//�ۼ�ÿ���ﻨ��(���г��ͷ�L*�ͼ�)/�ۼ����
		fValue = (float)mfFuelPrice*dsi.getDataItemF("xFF01000F")/dsi.getDataItemF("xFF01000A");
		dsi.setDataItemF("x00020004", fValue);
		dsi.setDataItemS("x00020004", String.valueOf(fValue));
		Log.i("DataItem", "�ۼ�ÿ���ﻨ��x00020004="+dsi.getDataItemS("x00020004"));

//		//������ʻʱ��
		int iMinuter = (int) (dsi.getDataItemF("xFF010007")/60);
		dsi.setDataItemS("xFF010007", String.format("%02d",iMinuter/60)	+ ":" + String.format("%02d",iMinuter%60));
		
		if(miCurrDemoDataIndex++>=(m_listDemoDataList.size()-1))
			miCurrDemoDataIndex=0;
		
		return dsi;
	}
	
}
