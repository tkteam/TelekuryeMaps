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

@DatabaseTable(tableName = "unsortedlist")
public class UnsortedList {

	@DatabaseField(id = true) private int	Id;
	@DatabaseField private String			Number;
	@DatabaseField private String			Name;

	public void Insert() {
		try {
			Dao<UnsortedList, Integer> insert = (DatabaseHelper.getDbHelper()).getUnsortedListDataHelper();
			UnsortedList existenceCheck = insert.queryForId(this.Id);

			if (existenceCheck != null) {
				insert.update(this);
			} else {
				insert.create(this);
			}

		} catch (SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public void Update() {
		try {
			Dao<UnsortedList, Integer> UnsortedListinsert = (DatabaseHelper.getDbHelper()).getUnsortedListDataHelper();

			UnsortedListinsert.update(this);
		} catch (SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public List<UnsortedList> GetAllData() {

		List<UnsortedList> data = new ArrayList<UnsortedList>();

		try {
			Dao<UnsortedList, Integer> dao = DatabaseHelper.getDbHelper().getUnsortedListDataHelper();
			data = dao.queryForAll();
		} catch (SQLException e) {
			Tools.saveErrors(e);

		}

		return data;
	}

	public SyncRequest<List<UnsortedList>> GetAllDataForSync() {

		SyncRequest<List<UnsortedList>> sr = new SyncRequest<List<UnsortedList>>();

		try {

			String startDateString = "1989-10-03 11:26:36";
			sr.setLastSyncDate(startDateString); // bi önceki senkroniazyon saati
			sr.setEndSyncDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // þuanki saat

			Dao<UnsortedList, Integer> dao = DatabaseHelper.getDbHelper().getUnsortedListDataHelper();

			List<UnsortedList> data = dao.queryForAll();

			sr.setTypedObjects(data);

		} catch (SQLException e) {
			Tools.saveErrors(e);

		}

		return sr;
	}

	public int GetRowCount() {
		int count = 0;

		try {
			Dao<UnsortedList, Integer> dao = DatabaseHelper.getDbHelper().getUnsortedListDataHelper();
			count = (int) dao.countOf();
		} catch (Exception e) {
			Tools.saveErrors(e);

		}

		return count;
	}

	public void DeleteRow(int deleteId) {
		try {

			Dao<UnsortedList, Integer> dao = DatabaseHelper.getDbHelper().getUnsortedListDataHelper();
			DeleteBuilder<UnsortedList, Integer> deleteBuilder = dao.deleteBuilder();
			deleteBuilder.where().eq("Id", deleteId);
			deleteBuilder.delete();
		} catch (Exception e) {
			Tools.saveErrors(e);

		}
	}

	public List<UnsortedList> getColumn(String ColumnName) throws SQLException {
		Dao<UnsortedList, Integer> dao = DatabaseHelper.getDbHelper().getUnsortedListDataHelper();
		List<UnsortedList> results = dao.queryBuilder().distinct().selectColumns(ColumnName).query();
		return results;
	}

	public UnsortedList getRow(int id) {

		UnsortedList dmfb = null;

		try {
			Dao<UnsortedList, Integer> dao = DatabaseHelper.getDbHelper().getUnsortedListDataHelper();
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
