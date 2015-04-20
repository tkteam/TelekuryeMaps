package com.telekurye.data_send;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;
import com.telekurye.data.typetoken.SyncRequest;
import com.telekurye.database.DatabaseHelper;
import com.telekurye.tools.Tools;

@DatabaseTable(tableName = "visitfeedback")
public class VisitFeedBack {

	@DatabaseField(id = true) private int						UserDailyMissionId;
	@DatabaseField private int									CourierId;
	@DatabaseField private int									DeviceId;
	@DatabaseField private double								GPSLat;
	@DatabaseField private double								GPSLng;
	@DatabaseField private double								GPSAltitude;
	@DatabaseField private double								GPSAccuricy;
	@DatabaseField private Boolean								IsCompleted;
	@DatabaseField(format = "yyyy-MM-dd HH:mm:ss") private Date	CreateDate;
	@DatabaseField private double								GPSBearing;
	@DatabaseField private double								GPSSpeed;
	@DatabaseField(format = "yyyy-MM-dd HH:mm:ss") private Date	GPSTime;
	@DatabaseField private String								SimCardNo;

	public VisitFeedBack() {
		IsCompleted = false;
	}

	public void Insert() {
		try {
			Dao<VisitFeedBack, Integer> VisitFeedBackinsert = (DatabaseHelper.getDbHelper()).getVisitFeedBackDataHelper();
			VisitFeedBack existenceCheck = VisitFeedBackinsert.queryForId(this.UserDailyMissionId);

			if (existenceCheck != null) {
				VisitFeedBackinsert.update(this);
			}
			else {
				VisitFeedBackinsert.create(this);
			}

		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public void Update() {
		try {
			Dao<VisitFeedBack, Integer> VisitFeedBackinsert = (DatabaseHelper.getDbHelper()).getVisitFeedBackDataHelper();

			VisitFeedBackinsert.update(this);
		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public SyncRequest<List<VisitFeedBack>> GetAllDataForSync() {

		SyncRequest<List<VisitFeedBack>> sr = new SyncRequest<List<VisitFeedBack>>();

		try {

			String startDateString = "1989-10-03 11:26:36";
			sr.setLastSyncDate(startDateString); // bi önceki senkroniazyon saati
			sr.setEndSyncDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // þuanki saat

			Dao<VisitFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getVisitFeedBackDataHelper();

			QueryBuilder<VisitFeedBack, Integer> qBuilder = dao.queryBuilder();
			qBuilder.where().not().eq("IsCompleted", true);
			PreparedQuery<VisitFeedBack> pQuery = qBuilder.prepare();

			List<VisitFeedBack> data = dao.query(pQuery);

			sr.setTypedObjects(data);

		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}

		return sr;
	}

	public SyncRequest<List<VisitFeedBack>> GetAllDataForSync2() {

		SyncRequest<List<VisitFeedBack>> sr = new SyncRequest<List<VisitFeedBack>>();

		try {

			String startDateString = "1989-10-03 11:26:36";
			sr.setLastSyncDate(startDateString); // bi önceki senkroniazyon saati
			sr.setEndSyncDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // þuanki saat

			Dao<VisitFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getVisitFeedBackDataHelper();

			List<VisitFeedBack> data = dao.queryForAll();

			sr.setTypedObjects(data);

		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}

		return sr;
	}

	public List<VisitFeedBack> GetAllData() {

		List<VisitFeedBack> data = new ArrayList<VisitFeedBack>();

		try {
			Dao<VisitFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getVisitFeedBackDataHelper();
			QueryBuilder<VisitFeedBack, Integer> qBuilder = dao.queryBuilder();
			qBuilder.where().eq("IsCompleted", true);
			PreparedQuery<VisitFeedBack> pQuery = qBuilder.prepare();
			data = dao.query(pQuery);

		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}

		return data;
	}

	public SyncRequest<List<VisitFeedBack>> getFirstRowForSync() {

		SyncRequest<List<VisitFeedBack>> sr = new SyncRequest<List<VisitFeedBack>>();
		List<VisitFeedBack> mf = new ArrayList<VisitFeedBack>();
		VisitFeedBack dmfb = null;

		try {
			Dao<VisitFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getVisitFeedBackDataHelper();
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

	public SyncRequest<List<VisitFeedBack>> getFirstRowForSync2() {

		SyncRequest<List<VisitFeedBack>> sr = new SyncRequest<List<VisitFeedBack>>();
		List<VisitFeedBack> mf = new ArrayList<VisitFeedBack>();
		VisitFeedBack dmfb = null;

		try {
			Dao<VisitFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getVisitFeedBackDataHelper();
			QueryBuilder<VisitFeedBack, Integer> qBuilder = dao.queryBuilder();
			qBuilder.where().eq("IsCompleted", true);
			PreparedQuery<VisitFeedBack> pQuery = qBuilder.prepare();
			List<VisitFeedBack> results = dao.query(pQuery);
			dmfb = results.get(0);

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

	public int GetRowCount() {
		int count = 0;

		try {
			Dao<VisitFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getVisitFeedBackDataHelper();
			count = (int) dao.countOf();
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}

		return count;
	}

	/*
	 * public void DeleteRow(int deleteId) { try {
	 * 
	 * Dao<VisitFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getVisitFeedBackDataHelper(); DeleteBuilder<VisitFeedBack, Integer> deleteBuilder = dao.deleteBuilder();
	 * deleteBuilder.where().eq("UserDailyMissionId", deleteId); deleteBuilder.delete(); } catch (Exception e) { Tools.customSendError(e);
	 * 
	 * } }
	 */

	public List<VisitFeedBack> getColumn(String ColumnName) throws SQLException {
		Dao<VisitFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getVisitFeedBackDataHelper();
		QueryBuilder<VisitFeedBack, Integer> qBuilder = dao.queryBuilder();
		qBuilder.where().eq("IsCompleted", true);
		PreparedQuery<VisitFeedBack> pQuery = qBuilder.prepare();
		List<VisitFeedBack> results = dao.query(pQuery);
		return results;
	}

	public VisitFeedBack getRow(int id) {

		VisitFeedBack dmfb = null;

		try {
			Dao<VisitFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getVisitFeedBackDataHelper();
			dmfb = dao.queryForAll().get(id);
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}

		return dmfb;
	}

	public int getUserDailyMissionId() {
		return UserDailyMissionId;
	}

	public void setUserDailyMissionId(int userDailyMissionId) {
		UserDailyMissionId = userDailyMissionId;
	}

	public int getCourierId() {
		return CourierId;
	}

	public void setCourierId(int courierId) {
		CourierId = courierId;
	}

	public int getDeviceId() {
		return DeviceId;
	}

	public void setDeviceId(int deviceId) {
		DeviceId = deviceId;
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

	public double getGPSAccuricy() {
		return GPSAccuricy;
	}

	public void setGPSAccuricy(double gPSAccuricy) {
		GPSAccuricy = gPSAccuricy;
	}

	public Date getCreateDate() {
		return CreateDate;
	}

	public void setCreateDate(Date createDate) {
		CreateDate = createDate;
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

	public String getSimCardNo() {
		return SimCardNo;
	}

	public void setSimCardNo(String simCardNo) {
		SimCardNo = simCardNo;
	}

	public Boolean getIsCompleted() {
		return IsCompleted;
	}

	public void setIsCompleted(Boolean isCompleted) {
		IsCompleted = isCompleted;
	}
}
