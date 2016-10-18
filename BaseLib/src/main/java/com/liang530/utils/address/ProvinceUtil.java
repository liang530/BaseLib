package com.liang530.utils.address;

import android.content.res.AssetManager;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.liang530.utils.address.provice.CityModel;
import com.liang530.utils.address.provice.DistrictModel;
import com.liang530.utils.address.provice.ProvinceModel;

public class ProvinceUtil {
	
	/**
	 * ����ʡ
	 */
	protected List<ProvinceModel> mProvinceDatas;
	/**
	 * key - ʡ value - ��
	 */
	protected Map<String, List<CityModel>> mCitisDatasMap = new HashMap<String,  List<CityModel>>();
	/**
	 * key - �� values - ��
	 */
	protected Map<String,  List<DistrictModel>> mDistrictDatasMap = new HashMap<String,  List<DistrictModel>>();
	
	/**
	 * key - �� values - �ʱ�
	 */
	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>(); 

	/**
	 * ��ǰʡ������
	 */
	protected String mCurrentProviceName;
	/**
	 * ��ǰ�е�����
	 */
	protected String mCurrentCityName;
	/**
	 * ��ǰ��������
	 */
	protected String mCurrentDistrictName ="";
	
	/**
	 * ��ǰ������������
	 */
	protected String mCurrentZipCode ="";
	
	/**
	 * ����ʡ������XML����
	 */
    public void initProvinceDatas(AssetManager asset)
	{
        try {
            InputStream input = asset.open("my_province_data.xml");
            // ����һ������xml�Ĺ�������
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser parser = spf.newSAXParser();
			XmlParserHandler handler = new XmlParserHandler();
			parser.parse(input, handler);
			input.close();
			mProvinceDatas = handler.getDataList();
			if (mProvinceDatas!= null && !mProvinceDatas.isEmpty()) {
				mCurrentProviceName = mProvinceDatas.get(0).getName();
				List<CityModel> cityList = mProvinceDatas.get(0).getCityList();
				if (cityList!= null && !cityList.isEmpty()) {
					mCurrentCityName = cityList.get(0).getName();
					List<DistrictModel> districtList = cityList.get(0).getDistrictList();
					mCurrentDistrictName = districtList.get(0).getName();
					mCurrentZipCode = districtList.get(0).getZipcode();
				}
			}
        	for (int i=0; i< mProvinceDatas.size(); i++) {
        		List<CityModel> cityList = mProvinceDatas.get(i).getCityList();
        		for (int j=0; j< cityList.size(); j++) {
        			List<DistrictModel> districtList = cityList.get(j).getDistrictList();
        			mDistrictDatasMap.put(cityList.get(j).getName(), districtList);
        		}
        		mCitisDatasMap.put(mProvinceDatas.get(i).getName(), cityList);
        	}
        } catch (Throwable e) {  
            e.printStackTrace();  
        } finally {
        	
        } 
	}

	public Map<String, String> getmZipcodeDatasMap() {
		return mZipcodeDatasMap;
	}

	public List<ProvinceModel> getmProvinceDatas() {
		return mProvinceDatas;
	}

	public Map<String, List<CityModel>> getmCitisDatasMap() {
		return mCitisDatasMap;
	}

	public Map<String, List<DistrictModel>> getmDistrictDatasMap() {
		return mDistrictDatasMap;
	}
}
