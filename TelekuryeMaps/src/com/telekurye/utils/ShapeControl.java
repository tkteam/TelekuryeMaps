package com.telekurye.utils;

import java.util.List;

public class ShapeControl {

	private List<Integer>	shapeIdList;

	public ShapeControl(List<Integer> ShapeIdList) {
		this.shapeIdList = ShapeIdList;
	}

	public Boolean isListEmpty() {
		if (shapeIdList.size() > 0) {
			return false;
		}
		else {
			return true;
		}
	}

	public Boolean weHaveThisShape(com.telekurye.kml.Polygon pol) {

		Boolean status = false;

		for (int i = 0; i < shapeIdList.size(); i++) {
			if (pol.polygonid.compareTo((long) shapeIdList.get(i)) == 0) {
				status = true;
			}
		}

		return status;
	}

	public Boolean CompareShapes(com.telekurye.kml.Polygon pol) {

		Boolean status = false;

		for (int i = 0; i < shapeIdList.size(); i++) {
			if (pol.polygonid.compareTo((long) shapeIdList.get(i)) == 0) {
				status = true;
				shapeIdList.remove(i);
			}
		}

		return status;
	}

	public int getListSize() {
		return shapeIdList.size();
	}

	public List<Integer> getShapeIdList() {
		return shapeIdList;
	}
}
