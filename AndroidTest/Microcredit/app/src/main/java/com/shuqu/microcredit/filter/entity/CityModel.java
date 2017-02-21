package com.shuqu.microcredit.filter.entity;

public class CityModel {
	private String id;
	private String areaName;
	private String beginStr;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getBeginStr() {
		return beginStr;
	}

	public void setBeginStr(String beginStr) {
		this.beginStr = beginStr;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if(obj instanceof CityModel) {
			CityModel temp = (CityModel)obj;
			return temp.getId().equals(temp.getId()) && temp.getAreaName().equals(this.getAreaName());
		}

		return false;
	}

	@Override
	public String toString() {
		return "[id=" + this.id + "areaName=" + this.areaName + "beginStr=" + this.beginStr + "]";
	}
}
