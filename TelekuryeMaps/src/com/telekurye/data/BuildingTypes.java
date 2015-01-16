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

@DatabaseTable(tableName = "buildingtypes")
public class BuildingTypes {

	@DatabaseField(id = true) private int	Id;
	@DatabaseField private String			Name;

	public void Insert() {
		try {
			Dao<BuildingTypes, Integer> BuildingTypesinsert = (DatabaseHelper.getDbHelper()).getBuildingTypesDataHelper();
			BuildingTypes existenceCheck = BuildingTypesinsert.queryForId(this.Id);

			if (existenceCheck != null) {
				BuildingTypesinsert.update(this);
			}
			else {
				BuildingTypesinsert.create(this);
			}
		}
		catch (SQLException e) {
			Tools.saveErrors(e);
		}
	}

	public void Update() {
		try {
			Dao<BuildingTypes, Integer> BuildingTypesinsert = (DatabaseHelper.getDbHelper()).getBuildingTypesDataHelper();
			BuildingTypesinsert.update(this);
		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public SyncRequest<List<BuildingTypes>> GetAllDataForSync() {

		SyncRequest<List<BuildingTypes>> sr = new SyncRequest<List<BuildingTypes>>();

		try {

			String startDateString = "1989-10-03 11:26:36";
			sr.setLastSyncDate(startDateString); // bi önceki senkroniazyon saati
			sr.setEndSyncDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // þuanki saat

			Dao<BuildingTypes, Integer> dao = DatabaseHelper.getDbHelper().getBuildingTypesDataHelper();

			List<BuildingTypes> data = dao.queryForAll();

			sr.setTypedObjects(data);

		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}

		return sr;
	}

	public List<BuildingTypes> GetAllData() {

		List<BuildingTypes> data = new ArrayList<BuildingTypes>();

		try {
			Dao<BuildingTypes, Integer> dao = DatabaseHelper.getDbHelper().getBuildingTypesDataHelper();
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
			Dao<BuildingTypes, Integer> dao = DatabaseHelper.getDbHelper().getBuildingTypesDataHelper();
			count = (int) dao.countOf();
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}

		return count;
	}

	public void DeleteRow(int deleteId) {
		try {

			Dao<BuildingTypes, Integer> dao = DatabaseHelper.getDbHelper().getBuildingTypesDataHelper();
			DeleteBuilder<BuildingTypes, Integer> deleteBuilder = dao.deleteBuilder();
			deleteBuilder.where().eq("Id", deleteId);
			deleteBuilder.delete();
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}
	}

	public List<BuildingTypes> getColumn(String ColumnName) throws SQLException {
		Dao<BuildingTypes, Integer> dao = DatabaseHelper.getDbHelper().getBuildingTypesDataHelper();
		List<BuildingTypes> results = dao.queryBuilder().distinct().selectColumns(ColumnName).query();
		return results;
	}

	public BuildingTypes getRow(int id) {

		BuildingTypes dmfb = null;

		try {
			Dao<BuildingTypes, Integer> dao = DatabaseHelper.getDbHelper().getBuildingTypesDataHelper();
			dmfb = dao.queryForAll().get(id);
		}
		catch (Exception e) {
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
