package com.telekurye.kml;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.DatabaseTable;
import com.telekurye.database.DatabaseHelper;
import com.telekurye.database.MapDatabaseHelper;
import com.telekurye.tools.Info;
import com.telekurye.tools.Tools;

@DatabaseTable(tableName = "Polygon")
public class Polygon {

	@DatabaseField(id = true) public Integer	id;
	@DatabaseField public Float					minlatitude;
	@DatabaseField public Float					maxlatitude;
	@DatabaseField public Float					minlongitude;
	@DatabaseField public Float					maxlongitude;
	@DatabaseField public String				coordinates;
	@DatabaseField public Integer				type;														// 1 -> Sokak, 2 -> Bina
	@DatabaseField public Long					polygonid;
	// @DatabaseField public Long streetid;
	@DatabaseField public Long					districtid;
	@DatabaseField public Long					villageid;

	public ArrayList<LatLng>					coors;

	private static String						db_path	= "/data/data/com.telekurye.mobileui/databases/";
	private static String						db_name	= Info.MAP_DBNAME;

	public Polygon() {

	}

	public void InsertOrUpdate(Context context) throws SQLException {
		Dao<Polygon, Integer> dbcontext = DatabaseHelper.getDbHelper().getDao(Polygon.class);
		// MapDBHelper.GetInstance(context).GetDBHelper(Polygon.class);

		if (this.id == null) {
			dbcontext.create(this);
			return;
		}

		Polygon data = dbcontext.queryForId(this.id);

		if (data != null) {
			dbcontext.update(this);
		}
		else {
			dbcontext.create(this);
		}
	}

	public static List<Polygon> GetAll(Context context, String db_path, String db_name) {

		List<Polygon> chapterDatabases = null;

		try {
			MapDatabaseHelper.SetPath(db_path);
			MapDatabaseHelper.SetName(db_name);

			MapDatabaseHelper dbh = new MapDatabaseHelper(context);

			boolean ishave = dbh.checkDataBase();

			Dao<Polygon, Integer> db = MapDatabaseHelper.GetInstance(context).GetDBHelper(Polygon.class);

			// GetInstance(context).GetDBHelper(Polygon.class);

			chapterDatabases = db.queryForAll();

		}
		catch (Exception e) {
			Tools.saveErrors(e);
		}

		return chapterDatabases;
	}

	public static List<Polygon> GetBuildingShapeByStreetId(Context context, Long streetId, String db_path, String db_name) {

		List<Polygon> chapterDatabases = null;

		try {
			MapDatabaseHelper.SetPath(db_path);
			MapDatabaseHelper.SetName(db_name);

			MapDatabaseHelper dbh = new MapDatabaseHelper(context);

			boolean ishave = dbh.checkDataBase();

			Dao<Polygon, Integer> db = MapDatabaseHelper.GetInstance(context).GetDBHelper(Polygon.class);

			// GetInstance(context).GetDBHelper(Polygon.class);

			// chapterDatabases = db.queryForAll();
			QueryBuilder<Polygon, Integer> qBuilder = db.queryBuilder();
			Where<Polygon, Integer> clause = qBuilder.where();
			clause.and(clause.eq("streetid", streetId), clause.eq("type", 2));
			PreparedQuery<Polygon> pQuery = qBuilder.prepare();
			chapterDatabases = db.query(pQuery);
		}
		catch (Exception e) {
			Tools.saveErrors(e);
		}

		return chapterDatabases;
	}

	public static List<Polygon> GetStreetShapeByStreetIdList(Context context, List<Integer> streetIdList, String db_path, String db_name) {
		List<Polygon> chapterDatabases = null;

		try {
			MapDatabaseHelper.SetPath(db_path);
			MapDatabaseHelper.SetName(db_name);

			MapDatabaseHelper dbh = new MapDatabaseHelper(context);

			Dao<Polygon, Integer> db = MapDatabaseHelper.GetInstance(context).GetDBHelper(Polygon.class);

			QueryBuilder<Polygon, Integer> qBuilder = db.queryBuilder();
			Where<Polygon, Integer> clause = qBuilder.where();
			clause.and(clause.in("polygonid", streetIdList), clause.eq("type", 1));
			PreparedQuery<Polygon> pQuery = qBuilder.prepare();
			chapterDatabases = db.query(pQuery);
		}
		catch (Exception e) {
			Tools.saveErrors(e);
		}

		return chapterDatabases;
	}

	public static List<Polygon> GetBuildingShapeByDistrictId(Context context, Long districtId, String db_path, String db_name) {

		List<Polygon> chapterDatabases = null;

		try {
			MapDatabaseHelper.SetPath(db_path);
			MapDatabaseHelper.SetName(db_name);

			MapDatabaseHelper dbh = new MapDatabaseHelper(context);

			Dao<Polygon, Integer> db = MapDatabaseHelper.GetInstance(context).GetDBHelper(Polygon.class);

			QueryBuilder<Polygon, Integer> qBuilder = db.queryBuilder();
			Where<Polygon, Integer> clause = qBuilder.where();

			clause.and(clause.eq("districtid", districtId), clause.eq("type", 2));

			PreparedQuery<Polygon> pQuery = qBuilder.prepare();
			chapterDatabases = db.query(pQuery);

		}
		catch (Exception e) {
			Tools.saveErrors(e);
		}

		return chapterDatabases;
	}

	public static List<Polygon> GetAround(Float minLat, Float maxLat, Float minLng, Float maxLng, Integer districtId, List<Integer> excludeShapeIdList, Context context, String db_path, String db_name) {

		List<Polygon> guideDatabases = new LinkedList<Polygon>();// null;

		try {
			MapDatabaseHelper.SetPath(db_path);
			MapDatabaseHelper.SetName(db_name);

			MapDatabaseHelper dbh = new MapDatabaseHelper(context);

			boolean ishave = dbh.checkDataBase();

			Dao<Polygon, Integer> db = MapDatabaseHelper.GetInstance(context).GetDBHelper(Polygon.class);

			QueryBuilder<Polygon, Integer> qBuilder = db.queryBuilder();

			Where<Polygon, Integer> where = qBuilder.where();

			where.or(where.in("polygonid", excludeShapeIdList), where.and(where.eq("districtid", districtId), where.and(where.gt("maxlatitude", minLat), where.lt("minlatitude", maxLat)), //
					where.and(where.gt("maxlongitude", minLng), where.lt("minlongitude", maxLng)))//
			);

			// where.and(where.and(where.gt("maxlatitude", minLat), where.lt("minlatitude", maxLat)), //
			// where.and(where.gt("maxlongitude", minLng), where.lt("minlongitude", maxLng)));

			PreparedQuery<Polygon> pQuery = qBuilder.prepare();

			List<Polygon> _guideDatabases = db.query(pQuery);

			if (_guideDatabases != null && _guideDatabases.size() > 0) {
				guideDatabases = _guideDatabases;
			}
		}
		catch (Exception e) {
			Tools.saveErrors(e);
		}

		return guideDatabases;
	}

	public static List<Long> GetNonMatchedShapeIdList(List<Long> excludeShapeIdList, Long districtId, Context context) {

		List<Long> streetNonMatchedShapeIdList = new ArrayList<Long>();

		try {

			MapDatabaseHelper.SetPath(db_path);
			MapDatabaseHelper.SetName(db_name);

			MapDatabaseHelper dbh = new MapDatabaseHelper(context);

			boolean ishave = dbh.checkDataBase();

			Dao<Polygon, Integer> db = MapDatabaseHelper.GetInstance(context).GetDBHelper(Polygon.class);
			QueryBuilder<Polygon, Integer> qBuilder = db.queryBuilder();

			qBuilder.where().eq("type", 2).eq("districtid", districtId).not().in("polygonid", excludeShapeIdList);

			PreparedQuery<Polygon> pQuery = qBuilder.prepare();

			List<Polygon> guideDatabases = db.query(pQuery);

			if (guideDatabases != null && guideDatabases.size() > 0) {
				for (Polygon polygon : guideDatabases) {
					streetNonMatchedShapeIdList.add(polygon.polygonid);
				}
			}

		}
		catch (Exception e) {
			Tools.saveErrors(e);
		}

		return streetNonMatchedShapeIdList;
	}

	public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
		double earthRadius = 6371; // kilometers
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		float dist = (float) (earthRadius * c);

		return dist;
	}

	public static List<Polygon> GetAroundHelper(float lat, float lng, float distance, Integer districtId, List<Integer> excludeShapeIdList, Context context, String db_path, String db_name) {
		float R = 6371; // earth radius in km

		float radius = 50; // km

		radius = distance;

		float x1 = (float) (lng - Math.toDegrees(radius / R / Math.cos(Math.toRadians(lat))));

		float x2 = (float) (lng + Math.toDegrees(radius / R / Math.cos(Math.toRadians(lat))));

		float y1 = (float) (lat + Math.toDegrees(radius / R));

		float y2 = (float) (lat - Math.toDegrees(radius / R));

		return GetAround(y2, y1, x1, x2, districtId, excludeShapeIdList, context, db_path, db_name);
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Polygon emp = (Polygon) obj;
		if (this.polygonid.equals(emp.polygonid)) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return polygonid.hashCode();
	}

}
