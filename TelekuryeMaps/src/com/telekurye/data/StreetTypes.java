package com.telekurye.data;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
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

@DatabaseTable(tableName = "streettypes")
public class StreetTypes {

	@DatabaseField(id = true) private int	Id;
	@DatabaseField private String			Name;

	public void Insert() {
		try {
			Dao<StreetTypes, Integer> StreetTypesinsert = (DatabaseHelper.getDbHelper()).getStreetTypesDataHelper();
			StreetTypes existenceCheck = StreetTypesinsert.queryForId(this.Id);

			if (existenceCheck != null) {
				StreetTypesinsert.update(this);
			} else {
				StreetTypesinsert.create(this);
			}

		} catch (SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public void Update() {
		try {
			Dao<StreetTypes, Integer> StreetTypesinsert = (DatabaseHelper.getDbHelper()).getStreetTypesDataHelper();

			StreetTypesinsert.update(this);
		} catch (SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public List<StreetTypes> GetAllData() {

		List<StreetTypes> data = new ArrayList<StreetTypes>();

		try {
			Dao<StreetTypes, Integer> dao = DatabaseHelper.getDbHelper().getStreetTypesDataHelper();
			data = dao.queryForAll();
		} catch (SQLException e) {
			Tools.saveErrors(e);

		}

		return data;
	}

	public SyncRequest<List<StreetTypes>> GetAllDataForSync() {

		SyncRequest<List<StreetTypes>> sr = new SyncRequest<List<StreetTypes>>();

		try {

			String startDateString = "1989-10-03 11:26:36";
			sr.setLastSyncDate(startDateString); // bi önceki senkroniazyon saati
			sr.setEndSyncDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // þuanki saat

			Dao<StreetTypes, Integer> dao = DatabaseHelper.getDbHelper().getStreetTypesDataHelper();

			List<StreetTypes> data = dao.queryForAll();

			sr.setTypedObjects(data);

		} catch (SQLException e) {
			Tools.saveErrors(e);

		}

		return sr;
	}

	public int GetRowCount() {
		int count = 0;

		try {
			Dao<StreetTypes, Integer> dao = DatabaseHelper.getDbHelper().getStreetTypesDataHelper();
			count = (int) dao.countOf();
		} catch (Exception e) {
			Tools.saveErrors(e);

		}

		return count;
	}

	public void DeleteRow(int deleteId) {
		try {

			Dao<StreetTypes, Integer> dao = DatabaseHelper.getDbHelper().getStreetTypesDataHelper();
			DeleteBuilder<StreetTypes, Integer> deleteBuilder = dao.deleteBuilder();
			deleteBuilder.where().eq("Id", deleteId);
			deleteBuilder.delete();
		} catch (Exception e) {
			Tools.saveErrors(e);

		}
	}

	public List<StreetTypes> getColumn(String ColumnName) throws SQLException {
		Dao<StreetTypes, Integer> dao = DatabaseHelper.getDbHelper().getStreetTypesDataHelper();
		List<StreetTypes> results = dao.queryBuilder().distinct().selectColumns(ColumnName).query();
		return results;
	}

	public StreetTypes getRow(int id) {

		StreetTypes dmfb = null;

		try {
			Dao<StreetTypes, Integer> dao = DatabaseHelper.getDbHelper().getStreetTypesDataHelper();
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

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

}
