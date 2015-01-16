package com.telekurye.data_send;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;
import com.telekurye.data.typetoken.SyncRequest;
import com.telekurye.database.DatabaseHelper;
import com.telekurye.tools.Tools;

@DatabaseTable(tableName = "missionfeedback")
public class MissionFeedBack {

	@DatabaseField(id = true) private int						UserDailyMissionId;
	@DatabaseField private Boolean								IsCompleted;
	@DatabaseField private double								GPSLat;
	@DatabaseField private double								GPSLng;
	@DatabaseField private double								GPSAltitude;
	@DatabaseField private double								GPSBearing;
	@DatabaseField private double								GPSSpeed;
	@DatabaseField(format = "yyyy-MM-dd HH:mm:ss") private Date	GPSTime;
	@DatabaseField private double								GPSAccuracy;
	@DatabaseField private double								SignedLat;
	@DatabaseField private double								SignedLng;
	@DatabaseField private int									TypeId;
	@DatabaseField private int									OutDoorPositionStatu;
	@DatabaseField(format = "yyyy-MM-dd HH:mm:ss") private Date	OperationDate;
	// yeni bina görevi için eklendi
	@DatabaseField private String								BuildingNumber;
	@DatabaseField private String								BuildingName;
	@DatabaseField private int									StreetId;

	@DatabaseField private int									IndependentSectionCount;
	@DatabaseField private int									FloorCount;
	@DatabaseField private int									BasarShapeId;
	@DatabaseField private int									StreetTypeId;
	@DatabaseField private int									IndependentSectionTypeId;

	public MissionFeedBack() {
		IsCompleted = false;
	}

	public void Insert() {
		try {
			Dao<MissionFeedBack, Integer> MissionFeedBackinsert = (DatabaseHelper.getDbHelper()).getMissionFeedBackDataHelper();
			MissionFeedBack existenceCheck = MissionFeedBackinsert.queryForId(this.UserDailyMissionId);

			if (existenceCheck != null) {
				MissionFeedBackinsert.update(this);
				// throw new Exception("UPDATED! UDMID: " + this.UserDailyMissionId + " STREETID: " + this.StreetId);
			}
			else {
				MissionFeedBackinsert.create(this);
			}
		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}
		catch (Exception e) {
			Tools.saveErrors(e);
		}
	}

	public void Update() {
		try {
			Dao<MissionFeedBack, Integer> MissionFeedBackinsert = (DatabaseHelper.getDbHelper()).getMissionFeedBackDataHelper();

			MissionFeedBackinsert.update(this);
		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public SyncRequest<List<MissionFeedBack>> GetAllDataForSync2() {

		SyncRequest<List<MissionFeedBack>> sr = new SyncRequest<List<MissionFeedBack>>();

		try {

			String startDateString = "1989-10-03 11:26:36";
			sr.setLastSyncDate(startDateString); // bi önceki senkroniazyon saati
			sr.setEndSyncDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // þuanki saat

			Dao<MissionFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getMissionFeedBackDataHelper();

			List<MissionFeedBack> data = dao.queryForAll();

			sr.setTypedObjects(data);

		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}

		return sr;
	}

	public SyncRequest<List<MissionFeedBack>> GetAllDataForSync() {

		SyncRequest<List<MissionFeedBack>> sr = new SyncRequest<List<MissionFeedBack>>();

		try {

			String startDateString = "1989-10-03 11:26:36";
			sr.setLastSyncDate(startDateString); // bi önceki senkroniazyon saati
			sr.setEndSyncDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // þuanki saat

			Dao<MissionFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getMissionFeedBackDataHelper();
			QueryBuilder<MissionFeedBack, Integer> qBuilder = dao.queryBuilder();
			qBuilder.where().not().eq("IsCompleted", true);
			PreparedQuery<MissionFeedBack> pQuery = qBuilder.prepare();
			List<MissionFeedBack> data = dao.query(pQuery);

			sr.setTypedObjects(data);

		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}

		return sr;
	}

	public List<MissionFeedBack> GetAllData() {

		List<MissionFeedBack> data = new ArrayList<MissionFeedBack>();

		try {
			Dao<MissionFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getMissionFeedBackDataHelper();
			data = dao.queryForAll();
		}
		catch (SQLException e) {
			Tools.saveErrors(e);
		}

		return data;
	}

	public List<MissionFeedBack> GetAllSyncData() {

		List<MissionFeedBack> data = new ArrayList<MissionFeedBack>();

		try {
			Dao<MissionFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getMissionFeedBackDataHelper();
			QueryBuilder<MissionFeedBack, Integer> qBuilder = dao.queryBuilder();
			qBuilder.where().eq("IsCompleted", false);
			PreparedQuery<MissionFeedBack> pQuery = qBuilder.prepare();
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
			Dao<MissionFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getMissionFeedBackDataHelper();
			count = (int) dao.countOf();
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}

		return count;
	}

	public void DeleteRow(int userDailyMissionId) {
		try {

			Dao<MissionFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getMissionFeedBackDataHelper();
			DeleteBuilder<MissionFeedBack, Integer> deleteBuilder = dao.deleteBuilder();
			deleteBuilder.where().eq("UserDailyMissionId", userDailyMissionId);
			deleteBuilder.delete();
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}
	}

	public List<MissionFeedBack> getColumn(String ColumnName) throws SQLException {
		Dao<MissionFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getMissionFeedBackDataHelper();
		List<MissionFeedBack> results = dao.queryBuilder().distinct().selectColumns(ColumnName).query();
		return results;
	}

	public MissionFeedBack getRow(int id) {

		MissionFeedBack dmfb = null;

		try {
			Dao<MissionFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getMissionFeedBackDataHelper();
			dmfb = dao.queryForAll().get(id);
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}

		return dmfb;
	}

	public SyncRequest<List<MissionFeedBack>> getSyncReqForFBack(MissionFeedBack fb) {

		SyncRequest<List<MissionFeedBack>> sr = new SyncRequest<List<MissionFeedBack>>();
		List<MissionFeedBack> mf = new ArrayList<MissionFeedBack>();

		mf.add(fb);

		try {
			String startDateString = "1989-10-03 11:26:36";
			sr.setLastSyncDate(startDateString); // bi önceki senkroniazyon saati
			sr.setEndSyncDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // þuanki saat
			sr.setTypedObjects(mf);
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}

		return sr;
	}

	public SyncRequest<List<MissionFeedBack>> getFirstRowForSync() {

		SyncRequest<List<MissionFeedBack>> sr = new SyncRequest<List<MissionFeedBack>>();
		List<MissionFeedBack> mf = new ArrayList<MissionFeedBack>();
		MissionFeedBack dmfb = null;

		try {
			Dao<MissionFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getMissionFeedBackDataHelper();
			dmfb = dao.queryForAll().get(0);

			mf.add(dmfb);

			String startDateString = "1989-10-03 11:26:36";
			sr.setLastSyncDate(startDateString); // bi önceki senkroniazyon saati
			sr.setEndSyncDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // þuanki saat
			sr.setTypedObjects(mf);
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}

		return sr;
	}

	public int getUserDailyMissionId() {
		return UserDailyMissionId;
	}

	public void setUserDailyMissionId(int userDailyMissionId) {
		UserDailyMissionId = userDailyMissionId;
	}

	public Boolean getIsCompleted() {
		return IsCompleted;
	}

	public void setIsCompleted(Boolean isCompleted) {
		IsCompleted = isCompleted;
	}

	public double getGPSLat() {
		return GPSLat;
	}

	public void setGPSLat(double gPSLat) {
		GPSLat = gPSLat;
	}

	public double getGPSLng() {
		return GPSLng;
	}

	public void setGPSLng(double gPSLng) {
		GPSLng = gPSLng;
	}

	public double getGPSAltitude() {
		return GPSAltitude;
	}

	public void setGPSAltitude(double gPSAltitude) {
		GPSAltitude = gPSAltitude;
	}

	public double getGPSBearing() {
		return GPSBearing;
	}

	public void setGPSBearing(double gPSBearing) {
		GPSBearing = gPSBearing;
	}

	public double getGPSSpeed() {
		return GPSSpeed;
	}

	public void setGPSSpeed(double gPSSpeed) {
		GPSSpeed = gPSSpeed;
	}

	public Date getGPSTime() {
		return GPSTime;
	}

	public void setGPSTime(Date gPSTime) {
		GPSTime = gPSTime;
	}

	public double getGPSAccuracy() {
		return GPSAccuracy;
	}

	public void setGPSAccuracy(double gPSAccuracy) {
		GPSAccuracy = gPSAccuracy;
	}

	public double getSignedLat() {
		return SignedLat;
	}

	public void setSignedLat(double signedLat) {
		SignedLat = signedLat;
	}

	public double getSignedLng() {
		return SignedLng;
	}

	public void setSignedLng(double signedLng) {
		SignedLng = signedLng;
	}

	public int getTypeId() {
		return TypeId;
	}

	public void setTypeId(int typeId) {
		TypeId = typeId;
	}

	public int getOutDoorPositionStatu() {
		return OutDoorPositionStatu;
	}

	public void setOutDoorPositionStatu(int outDoorPositionStatu) {
		OutDoorPositionStatu = outDoorPositionStatu;
	}

	public Date getOperationDate() {
		return OperationDate;
	}

	public void setOperationDate(Date operationDate) {
		OperationDate = operationDate;
	}

	public String getBuildingName() {
		return BuildingName;
	}

	public void setBuildingName(String buildingName) {
		BuildingName = buildingName;
	}

	public String getBuildingNumber() {
		return BuildingNumber;
	}

	public void setBuildingNumber(String buildingNumber) {
		BuildingNumber = buildingNumber;
	}

	public int getStreetId() {
		return StreetId;
	}

	public void setStreetId(int streetId) {
		StreetId = streetId;
	}

	public int getIndependentSectionCount() {
		return IndependentSectionCount;
	}

	public void setIndependentSectionCount(int independentSectionCount) {
		IndependentSectionCount = independentSectionCount;
	}

	public int getFloorCount() {
		return FloorCount;
	}

	public void setFloorCount(int floorCount) {
		FloorCount = floorCount;
	}

	public int getBasarShapeId() {
		return BasarShapeId;
	}

	public void setBasarShapeId(int basarShapeId) {
		BasarShapeId = basarShapeId;
	}

	public int getStreetTypeId() {
		return StreetTypeId;
	}

	public void setStreetTypeId(int streetTypeId) {
		StreetTypeId = streetTypeId;
	}

	public int getIndependentSectionTypeId() {
		return IndependentSectionTypeId;
	}

	public void setIndependentSectionTypeId(int independentSectionTypeId) {
		IndependentSectionTypeId = independentSectionTypeId;
	}

}
