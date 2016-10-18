package com.liang530.utils.address.provice;

import java.util.List;

import com.liang530.views.wheelview.model.WheelData;

public class ProvinceModel implements WheelData{
	private String name;
	private List<CityModel> cityList;
	
	public ProvinceModel() {
		super();
	}

	public ProvinceModel(String name, List<CityModel> cityList) {
		super();
		this.name = name;
		this.cityList = cityList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CityModel> getCityList() {
		return cityList;
	}

	public void setCityList(List<CityModel> cityList) {
		this.cityList = cityList;
	}

	@Override
	public String toString() {
		return "ProvinceModel [name=" + name + ", cityList=" + cityList + "]";
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
