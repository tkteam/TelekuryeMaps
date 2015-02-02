package com.telekurye.data;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.table.DatabaseTable;
import com.telekurye.data.typetoken.SyncRequest;
import com.telekurye.database.DatabaseHelper;
import com.telekurye.tools.Tools;

@DatabaseTable(tableName = "locations")
public class Locations implements Comparator<Locations> {

	@DatabaseField(generatedId = true) private int				Id;
	@DatabaseField private int									UserId;
	@DatabaseField private Float								Latitude;
	@DatabaseField private Float								Longitude;
	@DatabaseField private int									UserId_Create;
	@DatabaseField private int									UserId_Modify;
	@DatabaseField(format = "yyyy-MM-dd HH:mm:ss") private Date	CreateDate;
	@DatabaseField(format = "yyyy-MM-dd HH:mm:ss") private Date	ModifiedDate;
	@DatabaseField private Float								Accuracy;
	@DatabaseField private Float								Speed;
	@DatabaseField private Long									LocationTime;

	public void Insert() {
		try {
			Dao<Locations, Integer> locationinsert = DatabaseHelper.getDbHelper().getLocationsDataHelper();

			Locations existenceCheck = locationinsert.queryForId(this.Id);

			if (existenceCheck != null) {
				locationinsert.update(this);
			} else {
				locationinsert.create(this);
			}
		} catch (SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public SyncRequest<List<Locations>> GetAllDataForSync() {

		SyncRequest<List<Locations>> sr = new SyncRequest<List<Locations>>();

		try {

			String startDateString = "1989-10-03 11:26:36";
			sr.setLastSyncDate(startDateString); // bi önceki senkroniazyon saati
			sr.setEndSyncDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // þuanki saat

			Dao<Locations, Integer> dao = DatabaseHelper.getDbHelper().getLocationsDataHelper();

			List<Locations> data = dao.queryForAll();

			sr.setTypedObjects(data);

		} catch (SQLException e) {
			Tools.saveErrors(e);

		}

		return sr;
	}

	public void Update() {
		try {
			Dao<Locations, Integer> pickupOrderinsert = (DatabaseHelper.getDbHelper()).getLocationsDataHelper();

			pickupOrderinsert.update(this);
		} catch (SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public int GetRowCount() {
		int count = 0;

		try {
			Dao<Locations, Integer> dao = DatabaseHelper.getDbHelper().getLocationsDataHelper();
			count = (int) dao.countOf();
		} catch (Exception e) {
			Tools.saveErrors(e);

		}

		return count;
	}

	public void DeleteRow(int deleteId) {
		try {

			Dao<Locations, Integer> dao = DatabaseHelper.getDbHelper().getLocationsDataHelper();
			DeleteBuilder<Locations, Integer> deleteBuilder = dao.deleteBuilder();
			deleteBuilder.where().eq("Id", deleteId);
			deleteBuilder.delete();
		} catch (Exception e) {
			Tools.saveErrors(e);

		}
	}

	public List<Locations> getColumn(String ColumnName) throws SQLException {
		Dao<Locations, Integer> dao = DatabaseHelper.getDbHelper().getLocationsDataHelper();
		List<Locations> results = dao.queryBuilder().distinct().selectColumns(ColumnName).query();
		return results;
	}

	public Locations getRow(int id) {

		Locations dmfb = null;

		try {
			Dao<Locations, Integer> dao = DatabaseHelper.getDbHelper().getLocationsDataHelper();
			dmfb = dao.queryForAll().get(id);
		} catch (Exception e) {
			Tools.saveErrors(e);

		}

		return dmfb;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public int getUserId() {
		return UserId;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}

	public Float getLatitude() {
		return Latitude;
	}

	public void setLatitude(Float latitude) {
		Latitude = latitude;
	}

	public Float getLongitude() {
		return Longitude;
	}

	public void setLongitude(Float longitude) {
		Longitude = longitude;
	}

	public int getUserId_Create() {
		return UserId_Create;
	}

	public void setUserId_Create(int userId_Create) {
		UserId_Create = userId_Create;
	}

	public int getUserId_Modify() {
		return UserId_Modify;
	}

	public void setUserId_Modify(int userId_Modify) {
		UserId_Modify = userId_Modify;
	}

	public Date getCreateDate() {
		return CreateDate;
	}

	public void setCreateDate(Date createDate) {
		CreateDate = createDate;
	}

	public Date getModifiedDate() {
		return ModifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		ModifiedDate = modifiedDate;
	}

	public Float getAccuracy() {
		return Accuracy;
	}

	public void setAccuracy(Float accuracy) {
		Accuracy = accuracy;
	}

	public Float getSpeed() {
		return Speed;
	}

	public void setSpeed(Float speed) {
		Speed = speed;
	}

	public Long getLocationTime() {
		return LocationTime;
	}

	public void setLocationTime(Long locationTime) {
		LocationTime = locationTime;
	}

	@Override
	public int compare(Locations lhs, Locations rhs) {
		float change1 = lhs.getAccuracy();
		float change2 = rhs.getAccuracy();

		if (change1 < change2)
			return -1;
		if (change1 > change2)
			return 1;

		return 0;
	}

}
