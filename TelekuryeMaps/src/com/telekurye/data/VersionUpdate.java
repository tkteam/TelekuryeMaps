package com.telekurye.data;

import java.util.Date;

public class VersionUpdate {

	private int					Id;		
	private int					CurrentVersion;	
	private String				ApkFile;
	private Date				ReleaseDate;
	private Boolean				NeedsUrgentUpdate;
	private Boolean				IsBeforeSync;
	private Boolean				NeedsDatabaseReset;

	public static final String	PREFS_VERSION		= "version_log";
	public static final String	PREFS_DO_REBOOT		= "do_reboot";
	public static final String	PREFS_LAST_VERSION	= "last_version";

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public int getCurrentVersion() {
		return CurrentVersion;
	}

	public void setCurrentVersion(int currentVersion) {
		CurrentVersion = currentVersion;
	}

	public String getApkFile() {
		return ApkFile;
	}

	public void setApkFile(String apkFile) {
		ApkFile = apkFile;
	}

	public Date getReleaseDate() {
		return ReleaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		ReleaseDate = releaseDate;
	}

	public Boolean getNeedsUrgentUpdate() {
		return NeedsUrgentUpdate;
	}

	public void setNeedsUrgentUpdate(Boolean needsUrgentUpdate) {
		NeedsUrgentUpdate = needsUrgentUpdate;
	}

	public Boolean getIsBeforeSync() {
		return IsBeforeSync;
	}

	public void setIsBeforeSync(Boolean isBeforeSync) {
		IsBeforeSync = isBeforeSync;
	}

	public Boolean getNeedsDatabaseReset() {
		return NeedsDatabaseReset;
	}

	public void setNeedsDatabaseReset(Boolean needsDatabaseReset) {
		NeedsDatabaseReset = needsDatabaseReset;
	}

}
