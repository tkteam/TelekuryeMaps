package com.telekurye.database;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.telekurye.tools.Tools;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides the DAOs used by the other classes.
 */
public class MapDatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static String				DB_PATH				= "";
	private static String				DATABASE_NAME		= "";
	// any time you make changes to your database objects, you may have to increase the database version
	private static final int			DATABASE_VERSION	= 2;

	// private Map<Class<?>, Dao<?, Integer>> daoMap = new HashMap<Class<?>, Dao<?, Integer>>(); // # Generic hale getiriyorum
	private Map<Class<?>, Dao<?, ?>>	daoMap				= new HashMap<Class<?>, Dao<?, ?>>();

	public <T> Dao<T, Integer> GetDBHelper(Class<T> classType) {
		if (daoMap.containsKey(classType)) {
			return (Dao<T, Integer>) daoMap.get(classType);
		}
		else {
			try {
				Dao<T, Integer> newDao = getDao(classType);

				daoMap.put(classType, newDao);

				return newDao;
			}
			catch (SQLException e) {
				Tools.saveErrors(e);
				return null;
			}
		}
	}

	public <T> Dao<T, String> GetDBHelperStringId(Class<T> classType) {
		if (daoMap.containsKey(classType)) {
			return (Dao<T, String>) daoMap.get(classType);
		}
		else {
			try {
				Dao<T, String> newDao = getDao(classType);

				daoMap.put(classType, newDao);

				return newDao;
			}
			catch (SQLException e) {
				Tools.saveErrors(e);
				return null;
			}
		}
	}

	public <T, K> Dao<T, K> GetDBHelperGenericId(Class<T> classType) {
		if (daoMap.containsKey(classType)) {
			return (Dao<T, K>) daoMap.get(classType);
		}
		else {
			try {
				Dao<T, K> newDao = getDao(classType);

				daoMap.put(classType, newDao);

				return newDao;
			}
			catch (SQLException e) {
				Tools.saveErrors(e);
				return null;
			}
		}
	}

	public MapDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	private static MapDatabaseHelper	Instance;

	private static Object				oLocker	= new Object();

	public static MapDatabaseHelper GetInstance(Context context) {
		synchronized (oLocker) {
			if (Instance == null) {
				Instance = new MapDatabaseHelper(context);
			}
		}

		return Instance;
	}

	public boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DATABASE_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

		}
		catch (SQLiteException e) {
			Tools.saveErrors(e);

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	/**
	 * This is called when the database is first created. Usually you should call createTable statements here to create the tables that will store your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		/*
		 * try { Log.i(DatabaseHelper.class.getName(), "onCreate"); TableUtils.createTable(connectionSource, Card.class);
		 * 
		 * } catch (Exception e) { Log.e(DatabaseHelper.class.getName(), "Can't create database", e); throw new RuntimeException(e); }
		 */
	}

	/**
	 * This is called when your application is upgraded and it has a higher version number. This allows you to adjust the various data to match the new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		/*
		 * try { Log.i(DatabaseHelper.class.getName(), "onUpgrade"); 7TableUtils.dropTable(connectionSource, Card.class, true); // after we drop the old databases, we create the new ones onCreate(db,
		 * connectionSource); } catch (SQLException e) { Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e); throw new RuntimeException(e); }
		 */
	}

	/**
	 * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached value.
	 */
	public static String getpath() {
		return DB_PATH + DATABASE_NAME;
	}

	public static void SetPath(String dbpath) {
		DB_PATH = dbpath;

		return;
	}

	public static void SetName(String name) {
		DATABASE_NAME = name;

		return;
	}

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();

		daoMap.clear();
		Instance = null;
	}
}
