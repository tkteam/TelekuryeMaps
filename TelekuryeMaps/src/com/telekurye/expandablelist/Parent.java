package com.telekurye.expandablelist;

import java.util.ArrayList;
import java.util.Date;

public class Parent {

	private String	name;
	private String	text1;
	private String	text2;
	private String	Counter;
	private int		StreetId;
	private Date	LastOperationDate;
	private int		TypeId;
	private int     BuildingCount;

	public Date getLastOperationDate() {
		return LastOperationDate;
	}

	public Date setLastOperationDate(Date lastOperationDate) {
		return LastOperationDate = lastOperationDate;
	}

	public int getStreetId() {
		return StreetId;
	}

	public void setStreetId(int streetId) {
		StreetId = streetId;
	}

	private ArrayList<Child>	children;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText1() {
		return text1;
	}

	public void setText1(String text1) {
		this.text1 = text1;
	}

	public String getText2() {
		return text2;
	}

	public void setText2(String text2) {
		this.text2 = text2;
	}

	public String getCounter() {
		return Counter;
	}

	public void setCounter(String counter) {
		Counter = counter;
	}

	public ArrayList<Child> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Child> children) {
		this.children = children;
	}

	public int getTypeId() {
		return TypeId;
	}

	public void setTypeId(int typeId) {
		TypeId = typeId;
	}
	
	public int getBuildingCount() {
        return BuildingCount;
  }

  public void setBuildingCount(int buildingCount) {
        BuildingCount = buildingCount;
  }

}
