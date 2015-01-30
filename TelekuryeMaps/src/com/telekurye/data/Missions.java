package com.telekurye.data;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;
import com.telekurye.data.typetoken.SyncRequest;
import com.telekurye.database.DatabaseHelper;
import com.telekurye.tools.Tools;

@DatabaseTable(tableName = "missions")
public class Missions implements Parcelable, Comparator<Missions> {

	@DatabaseField(id = true) private int						UserDailyMissionId;
	@DatabaseField private int									StreetId;
	@DatabaseField private String								Name;
	@DatabaseField private String								BuildingNumber;
	@DatabaseField private int									UserDailyMissionTypeId;
	@DatabaseField private Boolean								BuildingNumber_IsOdd;
	@DatabaseField private String								AddressText;
	@DatabaseField private int									OrderIndex;
	@DatabaseField private int									StreetTypeId;
	@DatabaseField private int									IndependentSectionCount;
	@DatabaseField(format = "yyyy-MM-dd HH:mm:ss") private Date	ModifiedDate;
	@DatabaseField private Boolean								IsDeleted;
	@DatabaseField private Boolean								IsCompleted;
	@DatabaseField private String								PersonNameSurname;
	@DatabaseField(format = "yyyy-MM-dd HH:mm:ss") private Date	LastOperationDate;
	@DatabaseField private int									UserId;
	@DatabaseField private String								IndependentSectionType;
	@DatabaseField private int									DistrictId;
	@DatabaseField private Boolean								IsForcedUrbanStreet;

	public void Insert() {
		try {
			Dao<Missions, Integer> Missionsinsert = (DatabaseHelper.getDbHelper()).getMissionsDataHelper();
			Missions existenceCheck = Missionsinsert.queryForId(this.UserDailyMissionId);
			if (IsCompleted == null) {
				IsCompleted = false;
			}
			if (existenceCheck != null) {
				Missionsinsert.update(this);
			}
			else {
				Missionsinsert.create(this);
			}

		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public void Update() {
		try {
			Dao<Missions, Integer> Missionsinsert = (DatabaseHelper.getDbHelper()).getMissionsDataHelper();
			Missionsinsert.update(this);
		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public SyncRequest<List<Missions>> GetAllDataForSync() {

		SyncRequest<List<Missions>> sr = new SyncRequest<List<Missions>>();

		try {

			String startDateString = "1989-10-03 11:26:36";
			sr.setLastSyncDate(startDateString); // bi önceki senkroniazyon saati
			sr.setEndSyncDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // þuanki saat

			Dao<Missions, Integer> dao = DatabaseHelper.getDbHelper().getMissionsDataHelper();

			List<Missions> data = dao.queryForAll();

			sr.setTypedObjects(data);

		}
		catch (SQLException e) {
			Tools.saveErrors(e);
		}

		return sr;
	}

	public List<Missions> GetAllData() {

		List<Missions> data = new ArrayList<Missions>();

		try {

			Dao<Missions, Integer> dao = DatabaseHelper.getDbHelper().getMissionsDataHelper();
			QueryBuilder<Missions, Integer> qBuilder = dao.queryBuilder();
			qBuilder.where().eq("IsDeleted", false).and().not().eq("IsCompleted", true);
			PreparedQuery<Missions> pQuery = qBuilder.prepare();
			data = dao.query(pQuery);

		}
		catch (SQLException e) {
			Tools.saveErrors(e);
		}

		return data;
	}

	public List<Missions> GetBuildingsByStreetId(int streetId) {

		List<Missions> data = new ArrayList<Missions>();

		try {

			Dao<Missions, Integer> dao = DatabaseHelper.getDbHelper().getMissionsDataHelper();
			QueryBuilder<Missions, Integer> qBuilder = dao.queryBuilder();
			qBuilder.where().eq("IsDeleted", false).and().eq("IsCompleted", false).and().eq("StreetId", streetId);
			PreparedQuery<Missions> pQuery = qBuilder.prepare();
			data = dao.query(pQuery);

		}
		catch (SQLException e) {
			Tools.saveErrors(e);
		}

		return data;
	}

	public int GetRowCount() {
		int count = 0;

		try {
			Dao<Missions, Integer> dao = DatabaseHelper.getDbHelper().getMissionsDataHelper();
			count = (int) dao.countOf();
		}
		catch (Exception e) {
			Tools.saveErrors(e);
		}

		return count;
	}

	public void DeleteRow(int deleteId) {
		try {

			Dao<Missions, Integer> dao = DatabaseHelper.getDbHelper().getMissionsDataHelper();
			DeleteBuilder<Missions, Integer> deleteBuilder = dao.deleteBuilder();
			deleteBuilder.where().eq("UserDailyMissionId", deleteId);
			deleteBuilder.delete();
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}
	}

	public List<Missions> getColumn(String ColumnName) throws SQLException {
		Dao<Missions, Integer> dao = DatabaseHelper.getDbHelper().getMissionsDataHelper();
		List<Missions> results = dao.queryBuilder().distinct().selectColumns(ColumnName).query();
		return results;
	}

	public Missions getRow(int id) {

		Missions dmfb = null;

		try {
			Dao<Missions, Integer> dao = DatabaseHelper.getDbHelper().getMissionsDataHelper();
			dmfb = dao.queryForAll().get(id);
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}

		return dmfb;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

	}

	@Override
	public int compare(Missions lhs, Missions rhs) {
		if (lhs.getOrderIndex() - rhs.getOrderIndex() != 0) {
			return lhs.getOrderIndex() - rhs.getOrderIndex();
		}
		else {
			return lhs.getBuildingNumber().compareTo(rhs.getBuildingNumber());
		}
	}

	public int getUserDailyMissionId() {
		return UserDailyMissionId;
	}

	public void setUserDailyMissionId(int userDailyMissionId) {
		UserDailyMissionId = userDailyMissionId;
	}

	public int getStreetId() {
		return StreetId;
	}

	public void setStreetId(int streetId) {
		StreetId = streetId;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getBuildingNumber() {
		return BuildingNumber;
	}

	public void setBuildingNumber(String buildingNumber) {
		BuildingNumber = buildingNumber;
	}

	public int getUserDailyMissionTypeId() {
		return UserDailyMissionTypeId;
	}

	public void setUserDailyMissionTypeId(int userDailyMissionTypeId) {
		UserDailyMissionTypeId = userDailyMissionTypeId;
	}

	public Boolean getBuildingNumber_IsOdd() {
		return BuildingNumber_IsOdd;
	}

	public void setBuildingNumber_IsOdd(Boolean buildingNumber_IsOdd) {
		BuildingNumber_IsOdd = buildingNumber_IsOdd;
	}

	public String getAddressText() {
		return AddressText;
	}

	public void setAddressText(String addressText) {
		AddressText = addressText;
	}

	public int getOrderIndex() {
		return OrderIndex;
	}

	public void setOrderIndex(int orderIndex) {
		OrderIndex = orderIndex;
	}

	public int getStreetTypeId() {
		return StreetTypeId;
	}

	public void setStreetTypeId(int streetTypeId) {
		StreetTypeId = streetTypeId;
	}

	public int getIndependentSectionCount() {
		return IndependentSectionCount;
	}

	public void setIndependentSectionCount(int independentSectionCount) {
		IndependentSectionCount = independentSectionCount;
	}

	public Date getModifiedDate() {
		return ModifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		ModifiedDate = modifiedDate;
	}

	public Boolean getIsDeleted() {
		return IsDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		IsDeleted = isDeleted;
	}

	public Boolean getIsCompleted() {
		return IsCompleted;
	}

	public void setIsCompleted(Boolean isCompleted) {
		IsCompleted = isCompleted;
	}

	public String getPersonNameSurname() {
		return PersonNameSurname;
	}

	public void setPersonNameSurname(String personNameSurname) {
		PersonNameSurname = personNameSurname;
	}

	public Date getLastOperationDate() {
		return LastOperationDate;
	}

	public void setLastOperationDate(Date lastOperationDate) {
		LastOperationDate = lastOperationDate;
	}

	public int getUserId() {
		return UserId;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}

	public String getIndependentSectionType() {
		return IndependentSectionType;
	}

	public void setIndependentSectionType(String independentSectionType) {
		IndependentSectionType = independentSectionType;
	}

	public int getDistrictId() {
		return DistrictId;
	}

	public void setDistrictId(int districtId) {
		DistrictId = districtId;
	}

	public Boolean getIsForcedUrbanStreet() {
		return IsForcedUrbanStreet;
	}

	public void setIsForcedUrbanStreet(Boolean isForcedUrbanStreet) {
		IsForcedUrbanStreet = isForcedUrbanStreet;
	}

}
