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

@DatabaseTable(tableName = "MissionFeedBackPhoto")
public class MissionFeedBackPhoto {

	@DatabaseField(generatedId = true) private int				Id;
	@DatabaseField private String								Photo;
	@DatabaseField(format = "yyyy-MM-dd HH:mm:ss") private Date	CreateDate;
	@DatabaseField private Boolean								IsCompleted;
	@DatabaseField private int									UserDailyMissionId;

	public MissionFeedBackPhoto() {
		IsCompleted = false;
	}

	public void Insert() {
		
		try {
			IsCompleted = false;
			Dao<MissionFeedBackPhoto, Integer> MissionFeedBackPhotoinsert = (DatabaseHelper.getDbHelper()).getMissionFeedBackPhotoDataHelper();
			MissionFeedBackPhoto existenceCheck = MissionFeedBackPhotoinsert.queryForId(this.Id);
			int idx;
			if (existenceCheck != null) {
				MissionFeedBackPhotoinsert.update(this);
			}
			else {
				idx = MissionFeedBackPhotoinsert.create(this);
			}

		}
		catch (SQLException e) {
			Tools.saveErrors(e);
		}
	}

	public void Update() {
		try {
			Dao<MissionFeedBackPhoto, Integer> MissionFeedBackPhotoinsert = (DatabaseHelper.getDbHelper()).getMissionFeedBackPhotoDataHelper();

			MissionFeedBackPhotoinsert.update(this);
		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public SyncRequest<List<MissionFeedBackPhoto>> GetAllDataForSync() {

		SyncRequest<List<MissionFeedBackPhoto>> sr = new SyncRequest<List<MissionFeedBackPhoto>>();

		try {

			String startDateString = "1987-03-03 11:26:36";
			sr.setLastSyncDate(startDateString); // bi önceki senkroniazyon saati
			sr.setEndSyncDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // þuanki saat

			Dao<MissionFeedBackPhoto, Integer> dao = DatabaseHelper.getDbHelper().getMissionFeedBackPhotoDataHelper();

			QueryBuilder<MissionFeedBackPhoto, Integer> qBuilder = dao.queryBuilder();
			qBuilder.where().not().eq("IsCompleted", true);
			PreparedQuery<MissionFeedBackPhoto> pQuery = qBuilder.prepare();

			List<MissionFeedBackPhoto> data = dao.query(pQuery);

			sr.setTypedObjects(data);

		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}

		return sr;
	}

	public SyncRequest<List<MissionFeedBackPhoto>> GetAllDataForSync2() {

		SyncRequest<List<MissionFeedBackPhoto>> sr = new SyncRequest<List<MissionFeedBackPhoto>>();

		try {

			String startDateString = "1989-10-03 11:26:36";
			sr.setLastSyncDate(startDateString); // bi önceki senkroniazyon saati
			sr.setEndSyncDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // þuanki saat

			Dao<MissionFeedBackPhoto, Integer> dao = DatabaseHelper.getDbHelper().getMissionFeedBackPhotoDataHelper();

			List<MissionFeedBackPhoto> data = dao.queryForAll();

			sr.setTypedObjects(data);

		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}

		return sr;
	}

	public List<MissionFeedBackPhoto> GetAllData() {

		List<MissionFeedBackPhoto> data = new ArrayList<MissionFeedBackPhoto>();

		try {

			Dao<MissionFeedBackPhoto, Integer> dao = DatabaseHelper.getDbHelper().getMissionFeedBackPhotoDataHelper();

			QueryBuilder<MissionFeedBackPhoto, Integer> qBuilder = dao.queryBuilder();
			qBuilder.where().not().eq("IsCompleted", true);
			PreparedQuery<MissionFeedBackPhoto> pQuery = qBuilder.prepare();

			data = dao.query(pQuery);

		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}

		return data;
	}

	public List<MissionFeedBackPhoto> GetByUserDailyMissionId(int udmid) {

		List<MissionFeedBackPhoto> data = new ArrayList<MissionFeedBackPhoto>();

		try {

			Dao<MissionFeedBackPhoto, Integer> dao = DatabaseHelper.getDbHelper().getMissionFeedBackPhotoDataHelper();

			QueryBuilder<MissionFeedBackPhoto, Integer> qBuilder = dao.queryBuilder();
			qBuilder.where().not().eq("IsCompleted", true).and().eq("UserDailyMissionId", udmid);

			PreparedQuery<MissionFeedBackPhoto> pQuery = qBuilder.prepare();

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
			Dao<MissionFeedBackPhoto, Integer> dao = DatabaseHelper.getDbHelper().getMissionFeedBackPhotoDataHelper();
			count = (int) dao.countOf();
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}

		return count;
	}

	public void DeleteRow(int Id) {
		try {

			Dao<MissionFeedBackPhoto, Integer> dao = DatabaseHelper.getDbHelper().getMissionFeedBackPhotoDataHelper();
			DeleteBuilder<MissionFeedBackPhoto, Integer> deleteBuilder = dao.deleteBuilder();
			deleteBuilder.where().eq("Id", Id);
			deleteBuilder.delete();
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}
	}

	public List<MissionFeedBackPhoto> getColumn(String ColumnName) throws SQLException {
		Dao<MissionFeedBackPhoto, Integer> dao = DatabaseHelper.getDbHelper().getMissionFeedBackPhotoDataHelper();
		List<MissionFeedBackPhoto> results = dao.queryBuilder().distinct().selectColumns(ColumnName).query();
		return results;
	}

	public MissionFeedBackPhoto getRow(int id) {

		MissionFeedBackPhoto dmfb = null;

		try {
			Dao<MissionFeedBackPhoto, Integer> dao = DatabaseHelper.getDbHelper().getMissionFeedBackPhotoDataHelper();
			dmfb = dao.queryForAll().get(id);
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}

		return dmfb;
	}

	public SyncRequest<List<MissionFeedBackPhoto>> getFirstRowForSync() {

		SyncRequest<List<MissionFeedBackPhoto>> sr = new SyncRequest<List<MissionFeedBackPhoto>>();
		List<MissionFeedBackPhoto> mf = new ArrayList<MissionFeedBackPhoto>();
		MissionFeedBackPhoto dmfb = null;

		try {
			Dao<MissionFeedBackPhoto, Integer> dao = DatabaseHelper.getDbHelper().getMissionFeedBackPhotoDataHelper();
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

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getPhoto() {
		return Photo;
	}

	public void setPhoto(String photo) {
		Photo = photo.replace("file://", "");
	}

	public Date getCreateDate() {
		return CreateDate;
	}

	public void setCreateDate(Date createDate) {
		CreateDate = createDate;
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

	public void setIsCompleted(Boolean IsCompleted) {
		this.IsCompleted = IsCompleted;
	}
}
