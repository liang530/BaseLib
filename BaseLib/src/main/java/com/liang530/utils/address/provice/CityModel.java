package com.liang530.utils.address.provice;

import java.util.List;

import com.liang530.views.wheelview.model.WheelData;

public class CityModel implements WheelData{
	private String name;
	private List<DistrictModel> districtList;
	
	public CityModel() {
		super();
	}

	public CityModel(String name, List<DistrictModel> districtList) {
		super();
		this.name = name;
		this.districtList = districtList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DistrictModel> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<DistrictModel> districtList) {
		this.districtList = districtList;
	}

	@Override
	public String toString() {
		return "CityModel [name=" + name + ", districtList=" + districtList
				+ "]";
	}

	@Override
	public String getWheelKey() {
		return name;
	}

	@Override
	public String getWheelText() {
		return name;
	}

	@Override
	public String getWheelValue() {
		return name;
	}
}
