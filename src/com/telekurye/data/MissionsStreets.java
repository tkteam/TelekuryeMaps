package com.telekurye.data;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.DatabaseTable;
import com.telekurye.data.typetoken.SyncRequest;
import com.telekurye.data_send.DistrictIdFeedBack;
import com.telekurye.database.DatabaseHelper;
import com.telekurye.tools.Info;
import com.telekurye.tools.Tools;
import com.telekurye.utils.JSONHelper;
import com.telekurye.utils.SendFeedback;

@DatabaseTable(tableName = "missionsstreets")
public class MissionsStreets implements Parcelable, Comparator<MissionsStreets>, IMission {

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
	@DatabaseField private int									UserId;
	@DatabaseField private String								PersonNameSurname;
	@DatabaseField(format = "yyyy-MM-dd HH:mm:ss") private Date	LastOperationDate;
	@DatabaseField private String								IndependentSectionType;
	@DatabaseField private int									DistrictId;
	@DatabaseField private Boolean								IsForcedUrbanStreet;

	@Override
	public void setMissionsList(IMission m) {
		this.UserDailyMissionId = m.getUserDailyMissionId();
		this.StreetId = m.getStreetId();
		this.Name = m.getName();
		this.BuildingNumber = m.getBuildingNumber();
		this.UserDailyMissionTypeId = m.getUserDailyMissionTypeId();
		this.BuildingNumber_IsOdd = m.getBuildingNumber_IsOdd();
		this.AddressText = m.getAddressText();
		this.OrderIndex = m.getOrderIndex();
		this.StreetTypeId = m.getStreetTypeId();
		this.IndependentSectionCount = m.getIndependentSectionCount();
		this.ModifiedDate = m.getModifiedDate();
		this.IsDeleted = m.getIsDeleted();
		this.IsCompleted = m.getIsCompleted();
		this.PersonNameSurname = m.getPersonNameSurname();
		this.LastOperationDate = m.getLastOperationDate();
		this.UserId = m.getUserId();
		this.IndependentSectionType = m.getIndependentSectionType();
		this.DistrictId = m.getDistrictId();
		this.IsForcedUrbanStreet = m.getIsForcedUrbanStreet();

	}

	public void Insert() {
		try {
			Dao<MissionsStreets, Integer> Missionsinsert = (DatabaseHelper.getDbHelper()).getMissionsStreetsDataHelper();
			MissionsStreets existenceCheck = Missionsinsert.queryForId(this.UserDailyMissionId);
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
			Dao<MissionsStreets, Integer> Missionsinsert = (DatabaseHelper.getDbHelper()).getMissionsStreetsDataHelper();

			Missionsinsert.update(this);
		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public SyncRequest<List<MissionsStreets>> GetAllDataForSync() {

		SyncRequest<List<MissionsStreets>> sr = new SyncRequest<List<MissionsStreets>>();

		try {

			String startDateString = "1989-10-03 11:26:36";
			sr.setLastSyncDate(startDateString); // bi önceki senkroniazyon saati
			sr.setEndSyncDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // þuanki saat

			Dao<MissionsStreets, Integer> dao = DatabaseHelper.getDbHelper().getMissionsStreetsDataHelper();

			List<MissionsStreets> data = dao.queryForAll();

			sr.setTypedObjects(data);

		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}

		return sr;
	}

	public List<MissionsStreets> GetAllData() {

		List<MissionsStreets> data = new ArrayList<MissionsStreets>();

		try {

			Dao<MissionsStreets, Integer> dao = DatabaseHelper.getDbHelper().getMissionsStreetsDataHelper();
			QueryBuilder<MissionsStreets, Integer> qBuilder = dao.queryBuilder();
			qBuilder.where().eq("IsDeleted", false).and().eq("IsCompleted", false);

			PreparedQuery<MissionsStreets> pQuery = qBuilder.prepare();
			data = dao.query(pQuery);

		}
		catch (SQLException e) {
			Tools.saveErrors(e);
		}

		return data;
	}

	public static List<MissionsStreets> GetAllDataForShape() {
		List<MissionsStreets> data = new ArrayList<MissionsStreets>();

		try {

			Dao<MissionsStreets, Integer> dao = DatabaseHelper.getDbHelper().getMissionsStreetsDataHelper();
			QueryBuilder<MissionsStreets, Integer> qBuilder = dao.queryBuilder();
			Where<MissionsStreets, Integer> w = qBuilder.where();// .eq("IsDeleted", false);
			w.and(w.eq("IsDeleted", false), w.eq("BuildingNumber_IsOdd", true));

			PreparedQuery<MissionsStreets> pQuery = qBuilder.prepare();
			data = dao.query(pQuery);

		}
		catch (SQLException e) {
			Tools.saveErrors(e);
		}

		return data;
	}

	public List<MissionsStreets> GetStreetsByStreetId(int streetId) {
		List<MissionsStreets> data = new ArrayList<MissionsStreets>();

		try {

			Dao<MissionsStreets, Integer> dao = DatabaseHelper.getDbHelper().getMissionsStreetsDataHelper();
			QueryBuilder<MissionsStreets, Integer> qBuilder = dao.queryBuilder();
			qBuilder.where().eq("IsDeleted", false).and().eq("IsCompleted", false).and().eq("StreetId", streetId);
			PreparedQuery<MissionsStreets> pQuery = qBuilder.prepare();
			data = dao.query(pQuery);

		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}

		return data;
	}

	public String GetDistrictIdListJsonForSync() {

		List<DistrictIdFeedBack> data = new ArrayList<DistrictIdFeedBack>();

		List<MissionsStreets> results = null;
		HashSet<Integer> districtIdlist = new HashSet<Integer>();

		try {
			Dao<MissionsStreets, Integer> dao = DatabaseHelper.getDbHelper().getMissionsStreetsDataHelper();
			results = dao.queryBuilder().where().eq("IsDeleted", false).and().eq("IsCompleted", false).query();

			for (MissionsStreets ms : results) {
				districtIdlist.add(ms.getDistrictId());
			}

			// results = dao.queryBuilder().distinct().selectColumns("DistrictId").query();
		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}

		for (Integer dilist : districtIdlist) {
			DistrictIdFeedBack difb = new DistrictIdFeedBack();
			difb.setDistrictId(dilist);
			data.add(difb);
		}

		SyncRequest<List<DistrictIdFeedBack>> sr = new SyncRequest<List<DistrictIdFeedBack>>();

		String startDateString = "1989-10-03 11:26:36";
		sr.setLastSyncDate(startDateString); // bi önceki senkroniazyon saati
		sr.setEndSyncDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // þuanki saat

		sr.setTypedObjects(data);

		Type listType = new TypeToken<SyncRequest<List<DistrictIdFeedBack>>>() {
		}.getType();

		String json = JSONHelper.ToJson(sr, listType);

		String str = new SendFeedback<DistrictIdFeedBack>().SendDistricts(Info.tagBasarShapeId, json);

		return str;
	}

	public int GetRowCount() {
		int count = 0;

		try {
			Dao<MissionsStreets, Integer> dao = DatabaseHelper.getDbHelper().getMissionsStreetsDataHelper();
			count = (int) dao.countOf();
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}

		return count;
	}

	public void DeleteRow(int deleteId) {
		try {

			Dao<MissionsStreets, Integer> dao = DatabaseHelper.getDbHelper().getMissionsStreetsDataHelper();
			DeleteBuilder<MissionsStreets, Integer> deleteBuilder = dao.deleteBuilder();
			deleteBuilder.where().eq("UserDailyMissionId", deleteId);
			deleteBuilder.delete();
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}
	}

	public List<MissionsStreets> getColumn(String ColumnName) throws SQLException {
		Dao<MissionsStreets, Integer> dao = DatabaseHelper.getDbHelper().getMissionsStreetsDataHelper();
		List<MissionsStreets> results = dao.queryBuilder().distinct().selectColumns(ColumnName).query();
		return results;
	}

	public MissionsStreets getRow(int id) {

		MissionsStreets dmfb = null;

		try {
			Dao<MissionsStreets, Integer> dao = DatabaseHelper.getDbHelper().getMissionsStreetsDataHelper();
			dmfb = dao.queryForAll().get(id);
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}

		return dmfb;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

	}

	@Override
	public int compare(MissionsStreets lhs, MissionsStreets rhs) {
		return lhs.getOrderIndex() - rhs.getOrderIndex();
	}

	@Override
	public int getUserDailyMissionId() {
		return UserDailyMissionId;
	}

	@Override
	public int getStreetId() {
		return StreetId;
	}

	@Override
	public String getName() {
		return Name;
	}

	@Override
	public String getBuildingNumber() {
		return BuildingNumber;
	}

	@Override
	public int getUserDailyMissionTypeId() {
		return UserDailyMissionTypeId;
	}

	@Override
	public Boolean getBuildingNumber_IsOdd() {
		return BuildingNumber_IsOdd;
	}

	@Override
	public String getAddressText() {
		return AddressText;
	}

	@Override
	public int getOrderIndex() {
		return OrderIndex;
	}

	@Override
	public int getStreetTypeId() {
		return StreetTypeId;
	}

	@Override
	public int getIndependentSectionCount() {
		return IndependentSectionCount;
	}

	@Override
	public Date getModifiedDate() {
		return ModifiedDate;
	}

	@Override
	public Boolean getIsDeleted() {
		return IsDeleted;
	}

	@Override
	public Boolean getIsCompleted() {
		return IsCompleted;
	}

	@Override
	public int getUserId() {
		return UserId;
	}

	@Override
	public String getPersonNameSurname() {
		return PersonNameSurname;
	}

	@Override
	public Date getLastOperationDate() {
		return LastOperationDate;
	}

	@Override
	public String getIndependentSectionType() {
		return IndependentSectionType;
	}

	@Override
	public int getDistrictId() {
		return DistrictId;
	}

	@Override
	public void setUserDailyMissionId(int userDailyMissionId) {
		UserDailyMissionId = userDailyMissionId;
	}

	@Override
	public void setStreetId(int streetId) {
		StreetId = streetId;
	}

	@Override
	public void setName(String name) {
		Name = name;
	}

	@Override
	public void setBuildingNumber(String buildingNumber) {
		BuildingNumber = buildingNumber;
	}

	@Override
	public void setUserDailyMissionTypeId(int userDailyMissionTypeId) {
		UserDailyMissionTypeId = userDailyMissionTypeId;
	}

	@Override
	public void setBuildingNumber_IsOdd(Boolean buildingNumber_IsOdd) {
		BuildingNumber_IsOdd = buildingNumber_IsOdd;
	}

	@Override
	public void setAddressText(String addressText) {
		AddressText = addressText;
	}

	@Override
	public void setOrderIndex(int orderIndex) {
		OrderIndex = orderIndex;
	}

	@Override
	public void setStreetTypeId(int streetTypeId) {
		StreetTypeId = streetTypeId;
	}

	@Override
	public void setIndependentSectionCount(int independentSectionCount) {
		IndependentSectionCount = independentSectionCount;
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		ModifiedDate = modifiedDate;
	}

	@Override
	public void setIsDeleted(Boolean isDeleted) {
		IsDeleted = isDeleted;
	}

	@Override
	public void setIsCompleted(Boolean isCompleted) {
		IsCompleted = isCompleted;
	}

	@Override
	public void setUserId(int userId) {
		UserId = userId;
	}

	@Override
	public void setPersonNameSurname(String personNameSurname) {
		PersonNameSurname = personNameSurname;
	}

	@Override
	public void setLastOperationDate(Date lastOperationDate) {
		LastOperationDate = lastOperationDate;
	}

	@Override
	public void setIndependentSectionType(String independentSectionType) {
		IndependentSectionType = independentSectionType;
	}

	@Override
	public void setDistrictId(int districtId) {
		DistrictId = districtId;
	}

	@Override
	public Boolean getIsForcedUrbanStreet() {
		return IsForcedUrbanStreet;
	}

	@Override
	public void setIsForcedUrbanStreet(Boolean IsForcedUrbanStreet) {
		this.IsForcedUrbanStreet = IsForcedUrbanStreet;
	}

}
