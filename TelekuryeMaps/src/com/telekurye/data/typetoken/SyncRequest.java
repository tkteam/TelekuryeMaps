package com.telekurye.data.typetoken;

public class SyncRequest<T> {

	private String	LastSyncDate;
	private String	EndSyncDate;
	private T		TypedObjects;

	public String getLastSyncDate() {
		return LastSyncDate;
	}

	public void setLastSyncDate(String lastSyncDate) {
		LastSyncDate = lastSyncDate;
	}

	public String getEndSyncDate() {
		return EndSyncDate;
	}

	public void setEndSyncDate(String endSyncDate) {
		EndSyncDate = endSyncDate;
	}

	public T getTypedObjects() {
		return TypedObjects;
	}

	public void setTypedObjects(T typedObjects) {
		TypedObjects = typedObjects;
	}

}