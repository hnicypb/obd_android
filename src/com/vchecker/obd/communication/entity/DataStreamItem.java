package com.vchecker.obd.communication.entity;

import java.util.HashMap;
import java.util.Map;

public class DataStreamItem {
	private Map<String,Float> m_mapDataItem = new HashMap<String,Float>();

	public Map<String, Float> getM_mapDataItem() {
		return m_mapDataItem;
	}

	public void setM_mapDataItem(Map<String, Float> m_mapDataItem) {
		this.m_mapDataItem = m_mapDataItem;
	}
	
	public void setDataItem(String strID,Float fValue){
		m_mapDataItem.put(strID, fValue);
	}
	
	public Float getDataItem(String strID){
		return m_mapDataItem.get(strID);
	}
}
