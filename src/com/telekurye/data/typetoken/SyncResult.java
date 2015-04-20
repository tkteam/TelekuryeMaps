package com.telekurye.data.typetoken;

public class SyncResult<T> {

	private int	ProcessStatus;
	private T	TargetObject;

	public int getProcessStatus() {
		return ProcessStatus;
	}

	public void setProcessStatus(int processStatus) {
		ProcessStatus = processStatus;
	}

	public T getTargetObject() {
		return TargetObject;
	}

	public void setTargetObject(T targetObject) {
		TargetObject = targetObject;
	}

}
