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

@DatabaseTable(tableName = "vehiclefeedback")
public class VehicleFeedBack {

	public int getStatu() {
		return Statu;
	}

	public void setStatu(int statu) {
		Statu = statu;
	}

	@DatabaseField(generatedId = true) private int	Id;
	@DatabaseField private int						UserId;
	@DatabaseField private int						KM;
	@DatabaseField private int						Statu;
	@DatabaseField private Date						CreateDate;
	@DatabaseField private String					PhotoName;
	@DatabaseField private Boolean					IsCompleted;

	public VehicleFeedBack() {
		IsCompleted = false;
	}

	public void Insert() {
		try {
			Dao<VehicleFeedBack, Integer> VehicleFeedBackinsert = (DatabaseHelper.getDbHelper()).getVehicleFeedBackDataHelper();
			VehicleFeedBack existenceCheck = VehicleFeedBackinsert.queryForId(this.Id);

			if (existenceCheck != null) {
				VehicleFeedBackinsert.update(this);
			}
			else {
				VehicleFeedBackinsert.create(this);
			}

		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public void Update() {
		try {
			Dao<VehicleFeedBack, Integer> VehicleFeedBackinsert = (DatabaseHelper.getDbHelper()).getVehicleFeedBackDataHelper();

			VehicleFeedBackinsert.update(this);
		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public SyncRequest<List<VehicleFeedBack>> GetAllDataForSync() {

		SyncRequest<List<VehicleFeedBack>> sr = new SyncRequest<List<VehicleFeedBack>>();

		try {

			String startDateString = "1989-10-03 11:26:36";
			sr.setLastSyncDate(startDateString); // bi önceki senkroniazyon saati
			sr.setEndSyncDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // þuanki saat

			Dao<VehicleFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getVehicleFeedBackDataHelper();

			QueryBuilder<VehicleFeedBack, Integer> qBuilder = dao.queryBuilder();
			qBuilder.where().not().eq("IsCompleted", true);
			PreparedQuery<VehicleFeedBack> pQuery = qBuilder.prepare();

			List<VehicleFeedBack> data = dao.query(pQuery);

			sr.setTypedObjects(data);

		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}

		return sr;
	}

	public SyncRequest<List<VehicleFeedBack>> GetAllDataForSync2() {

		SyncRequest<List<VehicleFeedBack>> sr = new SyncRequest<List<VehicleFeedBack>>();

		try {

			String startDateString = "1989-10-03 11:26:36";
			sr.setLastSyncDate(startDateString); // bi önceki senkroniazyon saati
			sr.setEndSyncDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // þuanki saat

			Dao<VehicleFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getVehicleFeedBackDataHelper();

			List<VehicleFeedBack> data = dao.queryForAll();

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
			Dao<VehicleFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getVehicleFeedBackDataHelper();
			count = (int) dao.countOf();
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}

		return count;
	}

	public void DeleteRow(int deleteId) {
		try {

			Dao<VehicleFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getVehicleFeedBackDataHelper();
			DeleteBuilder<VehicleFeedBack, Integer> deleteBuilder = dao.deleteBuilder();
			deleteBuilder.where().eq("Id", deleteId);
			deleteBuilder.delete();
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}
	}

	public List<VehicleFeedBack> getColumn(String ColumnName) throws SQLException {
		Dao<VehicleFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getVehicleFeedBackDataHelper();
		List<VehicleFeedBack> results = dao.queryBuilder().distinct().selectColumns(ColumnName).query();
		return results;
	}

	public VehicleFeedBack getRow(int id) {

		VehicleFeedBack dmfb = null;

		try {
			Dao<VehicleFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getVehicleFeedBackDataHelper();
			dmfb = dao.queryForAll().get(id);
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}

		return dmfb;
	}

	public SyncRequest<List<VehicleFeedBack>> getFirstRowForSync() {

		SyncRequest<List<VehicleFeedBack>> sr = new SyncRequest<List<VehicleFeedBack>>();
		List<VehicleFeedBack> mf = new ArrayList<VehicleFeedBack>();
		VehicleFeedBack dmfb = null;

		try {
			Dao<VehicleFeedBack, Integer> dao = DatabaseHelper.getDbHelper().getVehicleFeedBackDataHelper();
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

	public int getUserId() {
		return UserId;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}

	public int getKM() {
		return KM;
	}

	public void setKM(int kM) {
		KM = kM;
	}

	public Date getCreateDate() {
		return CreateDate;
	}

	public void setCreateDate(Date createDate) {
		CreateDate = createDate;
	}

	public String getPhotoName() {
		return PhotoName;
	}

	public void setPhotoName(String photoName) {
		PhotoName = photoName;
	}

	public Boolean getIsCompleted() {
		return IsCompleted;
	}

	public void setIsCompleted(Boolean isCompleted) {
		IsCompleted = isCompleted;
	}

}
