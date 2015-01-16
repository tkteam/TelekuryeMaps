package com.telekurye.data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;
import com.telekurye.database.DatabaseHelper;
import com.telekurye.tools.Tools;

@DatabaseTable(tableName = "synctime")
public class SyncTime {

	@DatabaseField(id = true) private int	Id;
	@DatabaseField private String			LastSyncDate;
	@DatabaseField private int				UserId;

	public void Insert() {
		try {
			Dao<SyncTime, Integer> BuildingTypesinsert = (DatabaseHelper.getDbHelper()).getSyncTimeDataHelper();
			SyncTime existenceCheck = BuildingTypesinsert.queryForId(this.Id);

			if (existenceCheck != null) {
				BuildingTypesinsert.update(this);
			} else {
				BuildingTypesinsert.create(this);
			}

		} catch (SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public void Update() {
		try {
			Dao<SyncTime, Integer> BuildingTypesinsert = (DatabaseHelper.getDbHelper()).getSyncTimeDataHelper();

			BuildingTypesinsert.update(this);
		} catch (SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public List<SyncTime> GetAllData() {

		List<SyncTime> data = new ArrayList<SyncTime>();

		try {
			Dao<SyncTime, Integer> dao = DatabaseHelper.getDbHelper().getSyncTimeDataHelper();
			data = dao.queryForAll();
		} catch (SQLException e) {
			Tools.saveErrors(e);

		}

		return data;
	}

	public List<SyncTime> GetDataForUser(int userId) {

		List<SyncTime> data = new ArrayList<SyncTime>();

		try {
			Dao<SyncTime, Integer> dao = DatabaseHelper.getDbHelper().getSyncTimeDataHelper();
			// data = dao.queryForAll();

			QueryBuilder<SyncTime, Integer> qBuilder = dao.queryBuilder();
			qBuilder.where().eq("UserId", userId);
			PreparedQuery<SyncTime> pQuery = qBuilder.prepare();
			data = dao.query(pQuery);

		} catch (SQLException e) {
			Tools.saveErrors(e);

		}

		return data;
	}

	public int GetRowCount() {
		int count = 0;

		try {
			Dao<SyncTime, Integer> dao = DatabaseHelper.getDbHelper().getSyncTimeDataHelper();
			count = (int) dao.countOf();
		} catch (Exception e) {
			Tools.saveErrors(e);

		}

		return count;
	}

	public void DeleteRow(int deleteId) {
		try {

			Dao<SyncTime, Integer> dao = DatabaseHelper.getDbHelper().getSyncTimeDataHelper();
			DeleteBuilder<SyncTime, Integer> deleteBuilder = dao.deleteBuilder();
			deleteBuilder.where().eq("Id", deleteId);
			deleteBuilder.delete();
		} catch (Exception e) {
			Tools.saveErrors(e);

		}
	}

	public List<SyncTime> getColumn(String ColumnName) throws SQLException {
		Dao<SyncTime, Integer> dao = DatabaseHelper.getDbHelper().getSyncTimeDataHelper();
		List<SyncTime> results = dao.queryBuilder().distinct().selectColumns(ColumnName).query();
		return results;
	}

	public SyncTime getRow(int id) {

		SyncTime dmfb = null;

		try {
			Dao<SyncTime, Integer> dao = DatabaseHelper.getDbHelper().getSyncTimeDataHelper();
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

	public String getLastSyncDate() {
		return LastSyncDate;
	}

	public void setLastSyncDate(String lastSyncDate) {
		LastSyncDate = lastSyncDate;
	}

	public int getUserId() {
		return UserId;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}
}
