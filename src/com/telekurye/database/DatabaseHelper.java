package com.telekurye.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.telekurye.data.BasarShapeId;
import com.telekurye.data.BuildingTypes;
import com.telekurye.data.FinishedShapeHistory;
import com.telekurye.data.Locations;
import com.telekurye.data.MissionsBuildings;
import com.telekurye.data.MissionsStreets;
import com.telekurye.data.Person;
import com.telekurye.data.ProcessStatuses;
import com.telekurye.data.StreetTypes;
import com.telekurye.data.SyncTime;
import com.telekurye.data_send.ExceptionFeedBack;
import com.telekurye.data_send.MissionFeedBack;
import com.telekurye.data_send.MissionFeedBackPhoto;
import com.telekurye.data_send.VehicleFeedBack;
import com.telekurye.data_send.VisitFeedBack;
import com.telekurye.mobileui.Login;
import com.telekurye.tools.Info;
import com.telekurye.tools.Tools;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final int		DATABASE_VERSION	= Info.DATABASE_VERSION;
	private static final String		DATABASE_NAME		= Info.DATABASE_NAME;
	private static final String		MAP_DATABASE_NAME	= Info.MAP_DBNAME;
	private final Context			myContext;
	private static DatabaseHelper	dbHelper;
	private static final String		MAP_DATABASE_PATH	= "/data/data/com.telekurye.mobileui/databases/";
	private static Object			syncObject			= new Object();
	private Boolean					isHaveDB			= false;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.myContext = context;
		// this.getReadableDatabase();
		// this.getWritableDatabase();
	}

	public static DatabaseHelper getDbHelper() {
		synchronized (syncObject) {
			if (dbHelper == null) {
				dbHelper = new DatabaseHelper(Login.AppContext);
			}
		}
		return dbHelper;
		// return new DatabaseHelper(Login.AppContext);
	}

	private Dao<ProcessStatuses, Integer>		ProcessStatusesDataHelper		= null;
	private Dao<MissionsBuildings, Integer>		MissionsBuildingsDataHelper		= null;
	private Dao<MissionsStreets, Integer>		MissionsStreetsDataHelper		= null;
	private Dao<Person, Integer>				PersonDataHelper				= null;
	private Dao<BuildingTypes, Integer>			BuildingTypesDataHelper			= null;
	private Dao<StreetTypes, Integer>			StreetTypesDataHelper			= null;
	private Dao<Locations, Integer>				locationsDataHelper				= null;
	private Dao<SyncTime, Integer>				SyncTimeDataHelper				= null;
	private Dao<FinishedShapeHistory, Integer>	FinishedShapeHistoryDataHelper	= null;
	private Dao<BasarShapeId, Long>				BasarShapeIdDataHelper			= null;

	private Dao<MissionFeedBack, Integer>		MissionFeedBackDataHelper		= null;
	private Dao<MissionFeedBackPhoto, Integer>	MissionFeedBackPhotoDataHelper	= null;
	private Dao<VehicleFeedBack, Integer>		VehicleFeedBackDataHelper		= null;
	private Dao<VisitFeedBack, Integer>			VisitFeedBackDataHelper			= null;
	private Dao<ExceptionFeedBack, Integer>		ExceptionFeedBackDataHelper		= null;

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {

			TableUtils.createTable(connectionSource, ProcessStatuses.class);
			TableUtils.createTable(connectionSource, MissionsBuildings.class);
			TableUtils.createTable(connectionSource, MissionsStreets.class);
			TableUtils.createTable(connectionSource, Person.class);
			TableUtils.createTable(connectionSource, BuildingTypes.class);
			TableUtils.createTable(connectionSource, StreetTypes.class);
			TableUtils.createTable(connectionSource, Locations.class);
			TableUtils.createTable(connectionSource, MissionFeedBack.class);
			TableUtils.createTable(connectionSource, MissionFeedBackPhoto.class);
			TableUtils.createTable(connectionSource, VehicleFeedBack.class);
			TableUtils.createTable(connectionSource, VisitFeedBack.class);
			TableUtils.createTable(connectionSource, ExceptionFeedBack.class);
			TableUtils.createTable(connectionSource, SyncTime.class);
			TableUtils.createTable(connectionSource, FinishedShapeHistory.class);
			TableUtils.createTable(connectionSource, BasarShapeId.class);
		}
		catch (java.sql.SQLException e) {
			Tools.saveErrors(e);
		}
	}

	public void clearDatabase() {
		ConnectionSource connectionSource = getConnectionSource();
		try {
			TableUtils.clearTable(connectionSource, ProcessStatuses.class);
			TableUtils.clearTable(connectionSource, MissionsBuildings.class);
			TableUtils.clearTable(connectionSource, MissionsStreets.class);
			TableUtils.clearTable(connectionSource, BuildingTypes.class);
			TableUtils.clearTable(connectionSource, StreetTypes.class);
			TableUtils.clearTable(connectionSource, Locations.class);
			TableUtils.clearTable(connectionSource, MissionFeedBack.class);
			TableUtils.clearTable(connectionSource, MissionFeedBackPhoto.class);
			TableUtils.clearTable(connectionSource, VehicleFeedBack.class);
			TableUtils.clearTable(connectionSource, VisitFeedBack.class);
			TableUtils.clearTable(connectionSource, ExceptionFeedBack.class);
			TableUtils.clearTable(connectionSource, SyncTime.class);
			TableUtils.clearTable(connectionSource, FinishedShapeHistory.class);
			TableUtils.clearTable(connectionSource, BasarShapeId.class);
		}
		catch (SQLException e) {
			Tools.saveErrors(e);
		}
	}

	public void deleteDatabase() {
		ConnectionSource connectionSource = getConnectionSource();
		try {
			TableUtils.dropTable(connectionSource, ProcessStatuses.class, true);
			TableUtils.dropTable(connectionSource, MissionsBuildings.class, true);
			TableUtils.dropTable(connectionSource, MissionsStreets.class, true);
			TableUtils.dropTable(connectionSource, Person.class, true);
			TableUtils.dropTable(connectionSource, BuildingTypes.class, true);
			TableUtils.dropTable(connectionSource, StreetTypes.class, true);
			TableUtils.dropTable(connectionSource, Locations.class, true);
			TableUtils.dropTable(connectionSource, MissionFeedBack.class, true);
			TableUtils.dropTable(connectionSource, MissionFeedBackPhoto.class, true);
			TableUtils.dropTable(connectionSource, VehicleFeedBack.class, true);
			TableUtils.dropTable(connectionSource, VisitFeedBack.class, true);
			TableUtils.dropTable(connectionSource, ExceptionFeedBack.class, true);
			TableUtils.dropTable(connectionSource, SyncTime.class, true);
			TableUtils.dropTable(connectionSource, FinishedShapeHistory.class, true);
			TableUtils.dropTable(connectionSource, BasarShapeId.class, true);
		}
		catch (SQLException e) {
			Tools.saveErrors(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");

			TableUtils.dropTable(connectionSource, ProcessStatuses.class, true);
			TableUtils.dropTable(connectionSource, MissionsBuildings.class, true);
			TableUtils.dropTable(connectionSource, MissionsStreets.class, true);
			TableUtils.dropTable(connectionSource, Person.class, true);
			TableUtils.dropTable(connectionSource, BuildingTypes.class, true);
			TableUtils.dropTable(connectionSource, StreetTypes.class, true);
			TableUtils.dropTable(connectionSource, Locations.class, true);
			TableUtils.dropTable(connectionSource, MissionFeedBack.class, true);
			TableUtils.dropTable(connectionSource, MissionFeedBackPhoto.class, true);
			TableUtils.dropTable(connectionSource, VehicleFeedBack.class, true);
			TableUtils.dropTable(connectionSource, VisitFeedBack.class, true);
			TableUtils.dropTable(connectionSource, ExceptionFeedBack.class, true);
			TableUtils.dropTable(connectionSource, SyncTime.class, true);
			TableUtils.dropTable(connectionSource, FinishedShapeHistory.class, true);
			TableUtils.dropTable(connectionSource, BasarShapeId.class, true);

			onCreate(db, connectionSource);
		}
		catch (java.sql.SQLException e) {
			Tools.saveErrors(e);

		}
	}

	public void CreateDatabase(Context context) {
		try {
			boolean dbExist = checkDataBase();
			isHaveDB = dbExist;

			if (!dbExist) {
				// By calling this method and empty database will be created into the default system path
				// of your application so we are gonna be able to overwrite that database with our database.
				this.getReadableDatabase();

				try {
					copyDataBase(context);
				}
				catch (IOException e) {
					Tools.saveErrors(e);

				}
			}

		}
		catch (Exception e) {
			Tools.saveErrors(e);
		}

	}

	public void ReCreateDatabase(Context context) {
		try {
			this.getReadableDatabase();

			try {
				copyDataBase(context);
			}
			catch (IOException e) {
				Tools.saveErrors(e);
			}
		}
		catch (Exception e) {
			Tools.saveErrors(e);
		}

	}

	public Boolean getDbStatus() {
		return isHaveDB;
	}

	private void copyDataBase(Context context) throws IOException {

		// String path = Environment.getExternalStorageDirectory() + "/KMLTest/database";

		// Path to the just created empty db
		// String inputFileName = path + "/" + "silivri";

		// InputStream myInput = new FileInputStream(inputFileName);

		// Open your local db as the input stream
		InputStream myInput = context.getAssets().open(MAP_DATABASE_NAME);

		// Path to the just created empty db
		String outFileName = MAP_DATABASE_PATH + MAP_DATABASE_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = MAP_DATABASE_PATH + MAP_DATABASE_NAME;
			File file = new File(myPath);
			if (file.exists() && !file.isDirectory())
				checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		}
		catch (SQLiteException e) {
			Tools.saveErrors(e);
		}

		if (checkDB != null && checkDB.isOpen()) {
			checkDB.close();
		}

		return checkDB != null ? true : false;
	}

	public void clearBasarShapeId() {
		ConnectionSource connectionSource = getConnectionSource();
		try {
			TableUtils.clearTable(connectionSource, BasarShapeId.class);
		}
		catch (SQLException e) {
			Tools.saveErrors(e);
		}
	}

	public Dao<ProcessStatuses, Integer> getProcessStatusesDataHelper() throws SQLException {
		if (ProcessStatusesDataHelper == null) {
			ProcessStatusesDataHelper = getDao(ProcessStatuses.class);
		}
		return ProcessStatusesDataHelper;
	}

	public Dao<MissionsBuildings, Integer> getMissionsBuildingsDataHelper() throws SQLException {
		if (MissionsBuildingsDataHelper == null) {
			MissionsBuildingsDataHelper = getDao(MissionsBuildings.class);
		}
		return MissionsBuildingsDataHelper;
	}

	public Dao<MissionsStreets, Integer> getMissionsStreetsDataHelper() throws SQLException {
		if (MissionsStreetsDataHelper == null) {
			MissionsStreetsDataHelper = getDao(MissionsStreets.class);
		}
		return MissionsStreetsDataHelper;
	}

	public Dao<Person, Integer> getPersonDataHelper() throws SQLException {
		if (PersonDataHelper == null) {
			PersonDataHelper = getDao(Person.class);
		}
		return PersonDataHelper;
	}

	public Dao<BuildingTypes, Integer> getBuildingTypesDataHelper() throws SQLException {
		if (BuildingTypesDataHelper == null) {
			BuildingTypesDataHelper = getDao(BuildingTypes.class);
		}
		return BuildingTypesDataHelper;
	}

	public Dao<ExceptionFeedBack, Integer> getExceptionFeedBackDataHelper() throws SQLException {
		if (ExceptionFeedBackDataHelper == null) {
			ExceptionFeedBackDataHelper = getDao(ExceptionFeedBack.class);
		}
		return ExceptionFeedBackDataHelper;
	}

	public Dao<StreetTypes, Integer> getStreetTypesDataHelper() throws SQLException {
		if (StreetTypesDataHelper == null) {
			StreetTypesDataHelper = getDao(StreetTypes.class);
		}
		return StreetTypesDataHelper;
	}

	public Dao<Locations, Integer> getLocationsDataHelper() throws SQLException {
		if (locationsDataHelper == null) {
			locationsDataHelper = getDao(Locations.class);
		}
		return locationsDataHelper;
	}

	public Dao<MissionFeedBack, Integer> getMissionFeedBackDataHelper() throws SQLException {
		if (MissionFeedBackDataHelper == null) {
			MissionFeedBackDataHelper = getDao(MissionFeedBack.class);
		}
		return MissionFeedBackDataHelper;
	}

	public Dao<MissionFeedBackPhoto, Integer> getMissionFeedBackPhotoDataHelper() throws SQLException {
		if (MissionFeedBackPhotoDataHelper == null) {
			MissionFeedBackPhotoDataHelper = getDao(MissionFeedBackPhoto.class);
		}
		return MissionFeedBackPhotoDataHelper;
	}

	public Dao<VehicleFeedBack, Integer> getVehicleFeedBackDataHelper() throws SQLException {
		if (VehicleFeedBackDataHelper == null) {
			VehicleFeedBackDataHelper = getDao(VehicleFeedBack.class);
		}
		return VehicleFeedBackDataHelper;
	}

	public Dao<VisitFeedBack, Integer> getVisitFeedBackDataHelper() throws SQLException {
		if (VisitFeedBackDataHelper == null) {
			VisitFeedBackDataHelper = getDao(VisitFeedBack.class);
		}
		return VisitFeedBackDataHelper;
	}

	public Dao<SyncTime, Integer> getSyncTimeDataHelper() throws SQLException {
		if (SyncTimeDataHelper == null) {
			SyncTimeDataHelper = getDao(SyncTime.class);
		}
		return SyncTimeDataHelper;
	}

	public Dao<FinishedShapeHistory, Integer> getFinishedShapeHistoryDataHelper() throws SQLException {
		if (FinishedShapeHistoryDataHelper == null) {
			FinishedShapeHistoryDataHelper = getDao(FinishedShapeHistory.class);
		}
		return FinishedShapeHistoryDataHelper;
	}

	public Dao<BasarShapeId, Long> getBasarShapeIdDataHelper() throws SQLException {
		if (BasarShapeIdDataHelper == null) {
			BasarShapeIdDataHelper = getDao(BasarShapeId.class);
		}
		return BasarShapeIdDataHelper;
	}

}
