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

@DatabaseTable(tableName = "finishedshapehistory")
public class FinishedShapeHistory {

	@DatabaseField(id = true) private int	UserDailyMissionId;
	@DatabaseField private int				UserId;
	@DatabaseField private int				ShapeId;

	public void Insert() {
		try {
			Dao<FinishedShapeHistory, Integer> insert = (DatabaseHelper.getDbHelper()).getFinishedShapeHistoryDataHelper();
			FinishedShapeHistory existenceCheck = insert.queryForId(this.UserDailyMissionId);

			if (existenceCheck != null) {
				insert.update(this);
			}
			else {
				insert.create(this);
			}

		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public List<Integer> GetShapeIdList(int userId) {
		List<Integer> data = new ArrayList<Integer>();

		List<FinishedShapeHistory> results = null;
		try {
			Dao<FinishedShapeHistory, Integer> dao = DatabaseHelper.getDbHelper().getFinishedShapeHistoryDataHelper();

			QueryBuilder<FinishedShapeHistory, Integer> qBuilder = dao.queryBuilder();
			qBuilder.where().eq("UserId", userId);
			qBuilder.distinct().selectColumns("ShapeId").query();
			PreparedQuery<FinishedShapeHistory> pQuery = qBuilder.prepare();
			results = dao.query(pQuery);
		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}

		for (FinishedShapeHistory fsh : results) {
			data.add(fsh.getShapeId());
		}

		return data;
	}

	public List<FinishedShapeHistory> GetDataForUser(int userId) {

		List<FinishedShapeHistory> data = new ArrayList<FinishedShapeHistory>();

		try {
			Dao<FinishedShapeHistory, Integer> dao = DatabaseHelper.getDbHelper().getFinishedShapeHistoryDataHelper();
			// data = dao.queryForAll();

			QueryBuilder<FinishedShapeHistory, Integer> qBuilder = dao.queryBuilder();
			qBuilder.where().eq("UserId", userId);
			PreparedQuery<FinishedShapeHistory> pQuery = qBuilder.prepare();
			data = dao.query(pQuery);

		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}

		return data;
	}

	public void Update() {
		try {
			Dao<FinishedShapeHistory, Integer> insert = (DatabaseHelper.getDbHelper()).getFinishedShapeHistoryDataHelper();

			insert.update(this);
		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public List<FinishedShapeHistory> GetAllData() {

		List<FinishedShapeHistory> data = new ArrayList<FinishedShapeHistory>();

		try {
			Dao<FinishedShapeHistory, Integer> dao = DatabaseHelper.getDbHelper().getFinishedShapeHistoryDataHelper();
			data = dao.queryForAll();
		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}

		return data;
	}

	public int GetRowCount() {
		int count = 0;

		try {
			Dao<FinishedShapeHistory, Integer> dao = DatabaseHelper.getDbHelper().getFinishedShapeHistoryDataHelper();
			count = (int) dao.countOf();
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}

		return count;
	}

	public void DeleteRow(int deleteId) {
		try {

			Dao<FinishedShapeHistory, Integer> dao = DatabaseHelper.getDbHelper().getFinishedShapeHistoryDataHelper();
			DeleteBuilder<FinishedShapeHistory, Integer> deleteBuilder = dao.deleteBuilder();
			deleteBuilder.where().eq("Id", deleteId);
			deleteBuilder.delete();
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}
	}

	public List<FinishedShapeHistory> getColumn(String ColumnName) throws SQLException {
		Dao<FinishedShapeHistory, Integer> dao = DatabaseHelper.getDbHelper().getFinishedShapeHistoryDataHelper();
		List<FinishedShapeHistory> results = dao.queryBuilder().distinct().selectColumns(ColumnName).query();
		return results;
	}

	public FinishedShapeHistory getRow(int id) {

		FinishedShapeHistory dmfb = null;

		try {
			Dao<FinishedShapeHistory, Integer> dao = DatabaseHelper.getDbHelper().getFinishedShapeHistoryDataHelper();
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

	public int getUserId() {
		return UserId;
	}

	public int getShapeId() {
		return ShapeId;
	}

	public void setUserDailyMissionId(int userDailyMissionId) {
		UserDailyMissionId = userDailyMissionId;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}

	public void setShapeId(int shapeId) {
		ShapeId = shapeId;
	}

}
