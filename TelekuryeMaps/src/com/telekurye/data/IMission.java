package com.telekurye.data;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;

public interface IMission {

	int getUserDailyMissionId();

	int getStreetId();

	String getName();

	String getBuildingNumber();

	int getUserDailyMissionTypeId();

	Boolean getBuildingNumber_IsOdd();

	String getAddressText();

	int getOrderIndex();

	int getStreetTypeId();

	int getIndependentSectionCount();

	Date getModifiedDate();

	Boolean getIsDeleted();

	Boolean getIsCompleted();

	int getUserId();

	String getPersonNameSurname();

	Date getLastOperationDate();

	String getIndependentSectionType();

	int getDistrictId();

	// setters

	void setMissionsList(IMission mb);

	void setUserDailyMissionId(int userDailyMissionId);

	void setStreetId(int streetId);

	void setName(String name);

	void setBuildingNumber(String buildingNumber);

	void setUserDailyMissionTypeId(int userDailyMissionTypeId);

	void setBuildingNumber_IsOdd(Boolean buildingNumber_IsOdd);

	void setAddressText(String addressText);

	void setOrderIndex(int orderIndex);

	void setStreetTypeId(int streetTypeId);

	void setIndependentSectionCount(int independentSectionCount);

	void setModifiedDate(Date modifiedDate);

	void setIsDeleted(Boolean isDeleted);

	void setIsCompleted(Boolean isCompleted);

	void setUserId(int userId);

	void setPersonNameSurname(String personNameSurname);

	void setLastOperationDate(Date lastOperationDate);

	void setIndependentSectionType(String independentSectionType);

	void setDistrictId(int districtId);

}
