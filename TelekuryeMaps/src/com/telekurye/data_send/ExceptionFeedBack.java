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

@DatabaseTable(tableName = "errorlog")
public class ExceptionFeedBack {

	@DatabaseField(generatedId = true) private int	Id;
	@DatabaseField private String					UId;
	@DatabaseField private String					ERROR;
	@DatabaseField private String					CUSTOM_DATA;
	@DatabaseField private String					USER_CRASH_DATE;
	@DatabaseField private Boolean					IsCompleted;

	public void Insert() {

		try {
			Dao<ExceptionFeedBack, Integer> errorlogInsert = (DatabaseHelper.getDbHelper()).getExceptionFeedBackDataHelper();
			ExceptionFeedBack existenceCheck = errorlogInsert.queryForId(this.Id);

			if (existenceCheck != null) {
				errorlogInsert.update(this);
			}
			else {
				errorlogInsert.create(this);
			}
		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}

	}

	public void Update() {
		try {
			Dao<ExceptionFeedBack, Integer> errorlogUpdate = (DatabaseHelper.getDbHelper()).getExceptionFeedBackDataHelper();

			errorlogUpdate.update(this);
		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public SyncRequest<List<ExceptionFeedBack>> GetAllDataForSync() {

		SyncRequest<List<ExceptionFeedBack>> sr = new SyncRequest<List<ExceptionFeedBack>>();

		try {

			String startDateString = "1989-10-03 11:26:36";
			sr.setLastSyncDate(startDateString); // bi önceki senkroniazyon saati
			sr.setEndSyncDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // þuanki saat

			Dao<ExceptionFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getExceptionFeedBackDataHelper();

			List<ExceptionFeedBack> data = dao.queryForAll();

			sr.setTypedObjects(data);

		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}

		return sr;
	}

	public List<ExceptionFeedBack> GetAllData() {

		List<ExceptionFeedBack> data = new ArrayList<ExceptionFeedBack>();

		try {

			Dao<ExceptionFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getExceptionFeedBackDataHelper();

			QueryBuilder<ExceptionFeedBack, Integer> qBuilder = dao.queryBuilder();
			qBuilder.where().eq("IsCompleted", false);
			PreparedQuery<ExceptionFeedBack> pQuery = qBuilder.prepare();
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
			Dao<ExceptionFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getExceptionFeedBackDataHelper();
			count = (int) dao.countOf();
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}

		return count;
	}

	public void DeleteRow(int deleteId) {
		try {

			Dao<ExceptionFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getExceptionFeedBackDataHelper();
			DeleteBuilder<ExceptionFeedBack, Integer> deleteBuilder = dao.deleteBuilder();
			deleteBuilder.where().eq("Id", deleteId);
			deleteBuilder.delete();
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}
	}

	public List<ExceptionFeedBack> getColumn(String ColumnName) throws SQLException {
		Dao<ExceptionFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getExceptionFeedBackDataHelper();
		List<ExceptionFeedBack> results = dao.queryBuilder().distinct().selectColumns(ColumnName).query();
		return results;
	}

	public ExceptionFeedBack getRow(int id) {

		ExceptionFeedBack dmfb = null;

		try {
			Dao<ExceptionFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getExceptionFeedBackDataHelper();
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

	public String getUId() {
		return UId;
	}

	public void setUId(String uId) {
		UId = uId;
	}

	public String getERROR() {
		return ERROR;
	}

	public void setERROR(String eRROR) {
		ERROR = eRROR;
	}

	public String getCUSTOM_DATA() {
		return CUSTOM_DATA;
	}

	public void setCUSTOM_DATA(String cUSTOM_DATA) {
		CUSTOM_DATA = cUSTOM_DATA;
	}

	public String getUSER_CRASH_DATE() {
		return USER_CRASH_DATE;
	}

	public void setUSER_CRASH_DATE(String uSER_CRASH_DATE) {
		USER_CRASH_DATE = uSER_CRASH_DATE;
	}

	public Boolean getIsCompleted() {
		return IsCompleted;
	}

	public void setIsCompleted(Boolean isCompleted) {
		IsCompleted = isCompleted;
	}

}
