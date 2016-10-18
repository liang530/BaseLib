package com.liang530.utils.address.provice;

import com.liang530.views.wheelview.model.WheelData;

public class DistrictModel implements WheelData{
	private String name;
	private String zipcode;
	
	public DistrictModel() {
		super();
	}

	public DistrictModel(String name, String zipcode) {
		super();
		this.name = name;
		this.zipcode = zipcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	@Override
	public String toString() {
		return "DistrictModel [name=" + name + ", zipcode=" + zipcode + "]";
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
