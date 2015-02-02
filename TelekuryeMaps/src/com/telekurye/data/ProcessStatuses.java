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

@DatabaseTable(tableName = "processstatuses")
public class ProcessStatuses {

	@DatabaseField(id = true) private int	Id;
	@DatabaseField private String			StatusName;
	@DatabaseField private int				StatusCode;

	public void Insert() {
		try {
			Dao<ProcessStatuses, Integer> ProcessStatusesinsert = (DatabaseHelper.getDbHelper()).getProcessStatusesDataHelper();
			ProcessStatuses existenceCheck = ProcessStatusesinsert.queryForId(this.Id);

			if (existenceCheck != null) {
				ProcessStatusesinsert.update(this);
			} else {
				ProcessStatusesinsert.create(this);
			}

		} catch (SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public void Update() {
		try {
			Dao<ProcessStatuses, Integer> ProcessStatusesinsert = (DatabaseHelper.getDbHelper()).getProcessStatusesDataHelper();

			ProcessStatusesinsert.update(this);
		} catch (SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public SyncRequest<List<ProcessStatuses>> GetAllDataForSync() {

		SyncRequest<List<ProcessStatuses>> sr = new SyncRequest<List<ProcessStatuses>>();

		try {

			String startDateString = "1989-10-03 11:26:36";
			sr.setLastSyncDate(startDateString); // bi önceki senkroniazyon saati
			sr.setEndSyncDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // þuanki saat

			Dao<ProcessStatuses, Integer> dao = DatabaseHelper.getDbHelper().getProcessStatusesDataHelper();

			List<ProcessStatuses> data = dao.queryForAll();

			sr.setTypedObjects(data);

		} catch (SQLException e) {
			Tools.saveErrors(e);

		}

		return sr;
	}

	public List<ProcessStatuses> GetAllData() {

		List<ProcessStatuses> data = new ArrayList<ProcessStatuses>();

		try {

			Dao<ProcessStatuses, Integer> dao = DatabaseHelper.getDbHelper().getProcessStatusesDataHelper();
			data = dao.queryForAll();

		} catch (SQLException e) {
			Tools.saveErrors(e);

		}

		return data;
	}

	public int GetRowCount() {
		int count = 0;

		try {
			Dao<ProcessStatuses, Integer> dao = DatabaseHelper.getDbHelper().getProcessStatusesDataHelper();
			count = (int) dao.countOf();
		} catch (Exception e) {
			Tools.saveErrors(e);

		}

		return count;
	}

	public void DeleteRow(int deleteId) {
		try {

			Dao<ProcessStatuses, Integer> dao = DatabaseHelper.getDbHelper().getProcessStatusesDataHelper();
			DeleteBuilder<ProcessStatuses, Integer> deleteBuilder = dao.deleteBuilder();
			deleteBuilder.where().eq("Id", deleteId);
			deleteBuilder.delete();
		} catch (Exception e) {
			Tools.saveErrors(e);

		}
	}

	public List<ProcessStatuses> getColumn(String ColumnName) throws SQLException {
		Dao<ProcessStatuses, Integer> dao = DatabaseHelper.getDbHelper().getProcessStatusesDataHelper();
		List<ProcessStatuses> results = dao.queryBuilder().distinct().selectColumns(ColumnName).query();
		return results;
	}

	public ProcessStatuses getRow(int id) {

		ProcessStatuses dmfb = null;

		try {
			Dao<ProcessStatuses, Integer> dao = DatabaseHelper.getDbHelper().getProcessStatusesDataHelper();
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

	public String getStatusName() {
		return StatusName;
	}

	public void setStatusName(String statusName) {
		StatusName = statusName;
	}

	public int getStatusCode() {
		return StatusCode;
	}

	public void setStatusCode(int statusCode) {
		StatusCode = statusCode;
	}

}
