package com.telekurye.utils;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.telekurye.data.BasarShapeId;
import com.telekurye.data.BuildingTypes;
import com.telekurye.data.Earnings;
import com.telekurye.data.Missions;
import com.telekurye.data.Person;
import com.telekurye.data.ProcessStatuses;
import com.telekurye.data.StreetTypes;
import com.telekurye.data.VersionUpdate;
import com.telekurye.data.typetoken.SyncRequest;
import com.telekurye.data.typetoken.SyncResult;
import com.telekurye.database.DatabaseHelper;
import com.telekurye.tools.Info;
import com.telekurye.tools.LiveData;
import com.telekurye.tools.Tools;

public class JsonToDatabase {

	public Boolean saveLogin(Gson gson, String json) {

		Boolean needDBReset = false;
		try {

			Type listType = new TypeToken<SyncResult<Person>>() {
			}.getType();
			SyncResult<Person> person = gson.fromJson(json, listType);
			Person data = person.getTargetObject();

			needDBReset = data.getNeedDatabaseReset();
			if (data.getNeedDatabaseReset()) {
				// DatabaseHelper.getDbHelper().clearDatabase();
				data.setNeedDatabaseReset(false);
				data.Update();
			}

			Info.UserId = data.getId();

			data.Insert();
			ProcessStatuses ps = new ProcessStatuses();
			ps.setId(1);
			ps.setStatusName(Info.tagLogin);
			ps.setStatusCode(person.getProcessStatus());
			ps.Insert();

		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}

		return needDBReset;
	}

	public Boolean saveMissions(Activity act) {

		List<Missions> mb;

		Boolean retVal = false;

		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
			String json = new HttpRequestForJson(Info.tagSyncMissions, Tools.GetSyncDateRange(act)).getJson();
			Type listType = new TypeToken<SyncResult<ArrayList<Missions>>>() {
			}.getType();
			SyncResult<ArrayList<Missions>> missions = gson.fromJson(json, listType);

			mb = new ArrayList<Missions>();

			if (missions == null || missions.getProcessStatus() != 200) {
				return false;
			}

			if (missions.getTargetObject() != null) {
				for (int i = 0; i < missions.getTargetObject().size(); i++) {

					Missions mission = missions.getTargetObject().get(i);

					Dao<Missions, Integer> buildingsDao = DatabaseHelper.getDbHelper().getMissionsDataHelper();
					QueryBuilder<Missions, Integer> buildingQuery = buildingsDao.queryBuilder();
					Where<Missions, Integer> where = buildingQuery.where();
					where.eq("UserDailyMissionId", mission.getUserDailyMissionId());
					where.and();
					where.gt("ModifiedDate", mission.getModifiedDate());
					where.and();
					where.eq("IsCompleted", false);
					where.and();
					where.eq("IsDeleted", false);

					PreparedQuery<Missions> pQuery = buildingQuery.prepare();
					List<Missions> dbBuildings = buildingsDao.query(pQuery);
					if (dbBuildings.size() != 0) {
						buildingsDao.delete(dbBuildings);

					}
					// mb.add(missions.getTargetObject().get(i));
					Missions data1 = missions.getTargetObject().get(i);

					// ((Missions) data1).setUserId(new Person().GetAllData().get(0).getId());
					((Missions) data1).setUserId(Info.UserId);
					((Missions) data1).Insert();
				}
			}
			retVal = true;

			// LiveData.misBuildings = mb;
			// LiveData.misStreets = ms;

			ProcessStatuses ps = new ProcessStatuses();
			ps.setId(2);
			ps.setStatusName(Info.tagSyncMissions);
			ps.setStatusCode(missions.getProcessStatus());
			ps.Insert();
		}
		catch (Exception e) {
			Tools.saveErrors(e);
		}
		return retVal;
	}

	public void saveStreetTypes(Activity act) {

		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
			String json = new HttpRequestForJson(Info.tagStreetTypes, act).getJson();
			Type listType = new TypeToken<SyncResult<ArrayList<StreetTypes>>>() {
			}.getType();
			SyncResult<ArrayList<StreetTypes>> streetTypes = gson.fromJson(json, listType);
			for (int i = 0; i < streetTypes.getTargetObject().size(); i++) {
				StreetTypes data = streetTypes.getTargetObject().get(i);
				data.Insert();
			}
			ProcessStatuses ps = new ProcessStatuses();
			ps.setId(3);
			ps.setStatusName(Info.tagStreetTypes);
			ps.setStatusCode(streetTypes.getProcessStatus());
			ps.Insert();
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}

	}

	public void saveBuildingTypes(Activity act) {

		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
			String json = new HttpRequestForJson(Info.tagBuildingTypes, act).getJson();
			Type listType = new TypeToken<SyncResult<ArrayList<BuildingTypes>>>() {
			}.getType();
			SyncResult<ArrayList<BuildingTypes>> buildingTypes = gson.fromJson(json, listType);
			for (int i = 0; i < buildingTypes.getTargetObject().size(); i++) {
				BuildingTypes data = buildingTypes.getTargetObject().get(i);
				data.Insert();
			}
			ProcessStatuses ps = new ProcessStatuses();
			ps.setId(4);
			ps.setStatusName(Info.tagBuildingTypes);
			ps.setStatusCode(buildingTypes.getProcessStatus());
			ps.Insert();
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}

	}

	public String getJsonForRequest() {

		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		SyncRequest sr = new SyncRequest();

		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Date startDate = df.parse("1986-08-22T00:30:00");
			sr.setLastSyncDate(startDate.toGMTString());
			sr.setEndSyncDate(new Date().toGMTString());
		}
		catch (ParseException e) {
			Tools.saveErrors(e);

		}

		return gson.toJson(sr, SyncRequest.class);
	}

	public void versionControl() {

		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		SyncRequest<VersionUpdate> sr = new SyncRequest<VersionUpdate>();
		Type listType = new TypeToken<SyncRequest<VersionUpdate>>() {
		}.getType();

		sr.setLastSyncDate("1987-03-03T00:30:00");
		sr.setEndSyncDate("2087-03-03T00:30:00");

		VersionUpdate vu = new VersionUpdate();
		vu.setNeedsUrgentUpdate(false);
		vu.setId(1);
		vu.setCurrentVersion(Info.CURRENT_VERSION);

		sr.setTypedObjects(vu);

		String jsn = JSONHelper.ToJson(sr, listType);
		HttpRequestForJson httpreq = new HttpRequestForJson(Info.tagVersionControl, jsn);

		String json = httpreq.getJson();

		Type listType2 = new TypeToken<SyncResult<VersionUpdate>>() {
		}.getType();
		LiveData.versionControl = gson.fromJson(json, listType2);

	}

	public void saveEarnings(Activity act) {

		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
			String json = new HttpRequestForJson(Info.tagEarnings, act).getJson();
			Type listType = new TypeToken<SyncResult<Earnings>>() {
			}.getType();
			SyncResult<Earnings> data = gson.fromJson(json, listType);

			if (data != null && data.getTargetObject() != null) {
				LiveData.Earnings = data.getTargetObject().getText();
			}
		}
		catch (Exception e) {
			Tools.saveErrors(e);
		}

	}

	public void saveBasarShapeId(Activity act) {

		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
			String json = new MissionsStreets().GetDistrictIdListJsonForSync();
			Type listType = new TypeToken<SyncResult<ArrayList<BasarShapeId>>>() {
			}.getType();
			SyncResult<ArrayList<BasarShapeId>> basarShapeId = gson.fromJson(json, listType);

			if (basarShapeId != null && basarShapeId.getTargetObject() != null && basarShapeId.getTargetObject().size() > 0) {
				for (int i = 0; i < basarShapeId.getTargetObject().size(); i++) {
					BasarShapeId data = basarShapeId.getTargetObject().get(i);
					data.Insert();
				}
			}

			ProcessStatuses ps = new ProcessStatuses();
			ps.setId(5);
			ps.setStatusName(Info.tagBasarShapeId);
			ps.setStatusCode(basarShapeId.getProcessStatus());
			ps.Insert();
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}

	}

}
