package com.vchecker.obd.communication.entity;

import java.util.HashMap;

import java.util.Map;

/** 
* @ClassName: DataStreamItem 
* @Description: TODO(数据流数据类) 
* @author Yipb
* @date 2014年6月21日 上午10:43:43 
*  
*/
public class DataStreamItem {
	private Map<String,Float> m_mapDataItemF = new HashMap<String,Float>();
	private Map<String,String> m_mapDataItemS = new HashMap<String,String>();

	public Map<String, Float> getM_mapDataItemF() {
		return m_mapDataItemF;
	}

	public void setM_mapDataItem(Map<String, Float> mapDataItemF) {
		this.m_mapDataItemF = mapDataItemF;
	}
	
	public void setDataItemF(String strID,double d){
		m_mapDataItemF.put(strID, (float) d);
	}
	
	public Float getDataItemF(String strID){
		return m_mapDataItemF.get(strID);
	}
	
	public Map<String, Float> getM_mapDataItemS() {
		return m_mapDataItemF;
	}

	public void setM_mapDataItemS(Map<String, String> mapDataItemS) {
		this.m_mapDataItemS = mapDataItemS;
	}
	
	public void setDataItemS(String strID,String sValue){
		m_mapDataItemS.put(strID, sValue);
	}
	
	public String getDataItemS(String strID){
		String sValue = "-----";
		try {
			sValue = m_mapDataItemS.get(strID);
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(sValue=="")
			sValue = "-----";		
		return sValue;
	}
}
