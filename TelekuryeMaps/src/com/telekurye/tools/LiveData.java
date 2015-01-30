package com.telekurye.tools;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.Marker;
import com.telekurye.data.Missions;
import com.telekurye.data.VersionUpdate;
import com.telekurye.data.typetoken.SyncResult;
import com.telekurye.expandablelist.Parent;
import com.telekurye.utils.PhotoInfo;

public class LiveData {

	public static String												DATE_FORMAT			= "yyyy-MM-dd HH:mm:ss";
	public static String												SYNCDATERANGE		= "{\"EndSyncDate\":\"2034-08-25T10:13:09\",\"LastSyncDate\":\"1986-08-22T00:30:00\"}";

	public static AsyncTask<Void, ArrayList<Parent>, ArrayList<Parent>>	syncBack2			= null;
	public static List<PhotoInfo>										photoinfo			= new ArrayList<PhotoInfo>();
	public static List<Marker>											streetMarkers		= new ArrayList<Marker>();
	public static int													userDailyMissionId	= 0;
	public static String												Earnings			= "";
	public static SyncResult<VersionUpdate>								versionControl;

	public static List<Missions>										mStreetsAll;

}
