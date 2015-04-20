package com.telekurye.data;

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

@DatabaseTable(tableName = "login")
public class Person {

	@DatabaseField(id = true) private int	Id;
	@DatabaseField private String			Password;
	@DatabaseField private String			UserName;
	@DatabaseField private String			Name;
	@DatabaseField private String			Surname;
	@DatabaseField private Boolean			NeedDatabaseReset;
	@DatabaseField private int				DeviceId;
	@DatabaseField private String			SimNo;
	@DatabaseField private String			Version;

	public void Insert() {

		try {
			Dao<Person, Integer> Personinsert = (DatabaseHelper.getDbHelper()).getPersonDataHelper();
			Person existenceCheck = Personinsert.queryForId(this.Id);

			if (existenceCheck != null) {
				Personinsert.update(this);
			}
			else {
				Personinsert.create(this);
			}

		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public void Update() {
		try {
			Dao<Person, Integer> Personinsert = (DatabaseHelper.getDbHelper()).getPersonDataHelper();

			Personinsert.update(this);
		}
		catch (SQLException e) {
			Tools.saveErrors(e);
		}
	}

	public SyncRequest<List<Person>> GetAllDataForSync() {

		SyncRequest<List<Person>> sr = new SyncRequest<List<Person>>();

		try {

			String startDateString = "1989-10-03 11:26:36";
			sr.setLastSyncDate(startDateString); // bi önceki senkroniazyon saati
			sr.setEndSyncDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // þuanki saat

			Dao<Person, Integer> dao = DatabaseHelper.getDbHelper().getPersonDataHelper();

			List<Person> data = dao.queryForAll();

			sr.setTypedObjects(data);

		}
		catch (SQLException e) {
			Tools.saveErrors(e);
		}

		return sr;
	}

	public List<Person> GetAllData() {

		List<Person> data = new ArrayList<Person>();

		try {

			Dao<Person, Integer> dao = DatabaseHelper.getDbHelper().getPersonDataHelper();
			data = dao.queryForAll();

		}
		catch (SQLException e) {
			Tools.saveErrors(e);

		}

		return data;
	}

	public Person GetById(int Id) {

		Person person = new Person();

		try {

			Dao<Person, Integer> dao = DatabaseHelper.getDbHelper().getPersonDataHelper();
			// data = dao.queryForAll();
			QueryBuilder<Person, Integer> qBuilder = dao.queryBuilder();
			qBuilder.where().eq("Id", Id);

			PreparedQuery<Person> pQuery = qBuilder.prepare();
			List<Person> pList = dao.query(pQuery);

			if (pList.size() > 0) {
				person = pList.get(0);
			}

		}
		catch (SQLException e) {
			Tools.saveErrors(e);
		}

		return person;
	}

	public int GetRowCount() {
		int count = 0;

		try {
			Dao<Person, Integer> dao = DatabaseHelper.getDbHelper().getPersonDataHelper();
			count = (int) dao.countOf();
		}
		catch (Exception e) {
			Tools.saveErrors(e);
		}

		return count;
	}

	public void DeleteRow(int deleteId) {
		try {
			Dao<Person, Integer> dao = DatabaseHelper.getDbHelper().getPersonDataHelper();
			DeleteBuilder<Person, Integer> deleteBuilder = dao.deleteBuilder();
			deleteBuilder.where().eq("Id", deleteId);
			deleteBuilder.delete();
		}
		catch (Exception e) {
			Tools.saveErrors(e);
		}
	}

	public List<Person> getColumn(String ColumnName) throws SQLException {
		Dao<Person, Integer> dao = DatabaseHelper.getDbHelper().getPersonDataHelper();
		List<Person> results = dao.queryBuilder().distinct().selectColumns(ColumnName).query();
		return results;
	}

	public Person getRow(int id) {

		Person dmfb = null;
		try {
			Dao<Person, Integer> dao = DatabaseHelper.getDbHelper().getPersonDataHelper();
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

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getSurname() {
		return Surname;
	}

	public void setSurname(String surname) {
		Surname = surname;
	}

	public Boolean getNeedDatabaseReset() {
		return NeedDatabaseReset;
	}

	public void setNeedDatabaseReset(Boolean needDatabaseReset) {
		NeedDatabaseReset = needDatabaseReset;
	}

	public int getDeviceId() {
		return DeviceId;
	}

	public void setDeviceId(int deviceId) {
		DeviceId = deviceId;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getSimNo() {
		return SimNo;
	}

	public void setSimNo(String simNo) {
		SimNo = simNo;
	}

	public String getVersion() {
		return Version;
	}

	public void setVersion(String version) {
		Version = version;
	}
}
