package com.telekurye.data;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.table.DatabaseTable;
import com.telekurye.data.typetoken.SyncRequest;
import com.telekurye.database.DatabaseHelper;
import com.telekurye.tools.Tools;

@DatabaseTable(tableName = "basarshapeid")
public class BasarShapeId {

	@DatabaseField(id = true) private int	BasarShapeId;
	@DatabaseField private int				DistrictId;

	public void Insert() {
		try {
			Dao<BasarShapeId, Integer> insert = (DatabaseHelper.getDbHelper()).getBasarShapeIdDataHelper();
			BasarShapeId existenceCheck = insert.queryForId(this.BasarShapeId);

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

	public void Update() {
		try {
			Dao<BasarShapeId, Integer> insert = (DatabaseHelper.getDbHelper()).getBasarShapeIdDataHelper();
			insert.update(this);
		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public List<BasarShapeId> GetAllData() {

		List<BasarShapeId> data = new ArrayList<BasarShapeId>();

		try {
			Dao<BasarShapeId, Integer> dao = DatabaseHelper.getDbHelper().getBasarShapeIdDataHelper();
			data = dao.queryForAll();
		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}

		return data;
	}

	public SyncRequest<List<BasarShapeId>> GetAllDataForSync() {

		SyncRequest<List<BasarShapeId>> sr = new SyncRequest<List<BasarShapeId>>();

		try {

			String startDateString = "1989-10-03 11:26:36";
			sr.setLastSyncDate(startDateString); // bi önceki senkroniazyon saati
			sr.setEndSyncDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // þuanki saat

			Dao<BasarShapeId, Integer> dao = DatabaseHelper.getDbHelper().getBasarShapeIdDataHelper();

			List<BasarShapeId> data = dao.queryForAll();

			sr.setTypedObjects(data);

		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}

		return sr;
	}

	public int GetRowCount() {
		int count = 0;

		try {
			Dao<BasarShapeId, Integer> dao = DatabaseHelper.getDbHelper().getBasarShapeIdDataHelper();
			count = (int) dao.countOf();
		}
		catch (Exception e) {
			Tools.saveErrors(e);
		}

		return count;
	}

	public void DeleteRow(int deleteId) {
		try {

			Dao<BasarShapeId, Integer> dao = DatabaseHelper.getDbHelper().getBasarShapeIdDataHelper();
			DeleteBuilder<BasarShapeId, Integer> deleteBuilder = dao.deleteBuilder();
			deleteBuilder.where().eq("Id", deleteId);
			deleteBuilder.delete();
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}
	}

	public List<BasarShapeId> getColumn(String ColumnName) throws SQLException {
		Dao<BasarShapeId, Integer> dao = DatabaseHelper.getDbHelper().getBasarShapeIdDataHelper();
		List<BasarShapeId> results = dao.queryBuilder().distinct().selectColumns(ColumnName).query();
		return results;
	}

	public BasarShapeId getRow(int id) {

		BasarShapeId dmfb = null;

		try {
			Dao<BasarShapeId, Integer> dao = DatabaseHelper.getDbHelper().getBasarShapeIdDataHelper();
			dmfb = dao.queryForAll().get(id);
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}

		return dmfb;
	}

	public int getDistrictId() {
		return DistrictId;
	}

	public void setDistrictId(int districtId) {
		DistrictId = districtId;
	}

	public int getBasarShapeId() {
		return BasarShapeId;
	}

	public void setBasarShapeId(int basarShapeId) {
		BasarShapeId = basarShapeId;
	}
}
