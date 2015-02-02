package com.telekurye.mobileui;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

import com.telekurye.tools.Info;

import android.app.Application;

@ReportsCrashes(
	    formUri = "https://telekurye.cloudant.com/acra-telekurye/_design/acra-storage/_update/report",
	    reportType = HttpSender.Type.JSON,
	    httpMethod = HttpSender.Method.POST, 
	    formUriBasicAuthLogin = "alwaytheyedlywhishoughte",//
	    formUriBasicAuthPassword = "LIt6LjUS7D0ihUQxn0DerBfe",//
	    formKey = "", // This is required for backward compatibility but not used
	    customReportContent = {
	            ReportField.APP_VERSION_CODE,
	            ReportField.APP_VERSION_NAME,
	            ReportField.ANDROID_VERSION,
	            ReportField.PACKAGE_NAME,
	            ReportField.REPORT_ID,
	            ReportField.BUILD,
	            ReportField.STACK_TRACE,
	            ReportField.USER_COMMENT,
	            ReportField.CUSTOM_DATA,
	            ReportField.AVAILABLE_MEM_SIZE,
	            ReportField.LOGCAT,
	            ReportField.USER_APP_START_DATE,
	            ReportField.APPLICATION_LOG,
	            ReportField.BRAND, 
	            ReportField.CRASH_CONFIGURATION,
	            ReportField.DEVICE_FEATURES,
	            ReportField.DEVICE_ID,
	            ReportField.DISPLAY,
	            ReportField.ENVIRONMENT,
	            ReportField.EVENTSLOG,
	            ReportField.FILE_PATH,
	            ReportField.INITIAL_CONFIGURATION,
	            ReportField.USER_IP,
	            ReportField.USER_EMAIL,
	            ReportField.TOTAL_MEM_SIZE	            
	    }
	    
//	      mode = ReportingInteractionMode.SILENT, // .TOAST , .DIALOG
//        resToastText = R.string.crash_toast_text // optional, displayed as soon as the crash occurs, before collecting data which can take a few seconds

//        resDialogText = R.string.crash_dialog_text,
//        resDialogIcon = android.R.drawable.ic_dialog_info, //optional. default is a warning sign
//        resDialogTitle = R.string.crash_dialog_title, // optional. default is your application name
//        resDialogCommentPrompt = R.string.crash_dialog_comment_prompt, // optional. when defined, adds a user text field input with this text resource as a label
//        resDialogOkToast = R.string.crash_dialog_ok_toast 
 
	)
 
public class myApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		ACRA.init(this);
		ACRA.getErrorReporter().putCustomData(" 01)  USER ID", ""+Info.UserId); 
		ACRA.getErrorReporter().putCustomData(" 02)  USERNAME", ""+Info.USERNAME);
		ACRA.getErrorReporter().putCustomData(" 03)  PASSWORD", ""+Info.PASSWORD);
		ACRA.getErrorReporter().putCustomData(" 04)  CURRENT VERSION", ""+Info.CURRENT_VERSION);
		ACRA.getErrorReporter().putCustomData(" 05)  DATABASE NAME", ""+Info.DATABASE_NAME); 
		ACRA.getErrorReporter().putCustomData(" 06)  DATABASE VERSION", ""+Info.DATABASE_VERSION);
		ACRA.getErrorReporter().putCustomData(" 07)  MAP DBNAME", ""+Info.MAP_DBNAME);
		ACRA.getErrorReporter().putCustomData(" 08)  IMEI", ""+Info.IMEI);
		ACRA.getErrorReporter().putCustomData(" 09)  MAP ZOOM LEVEL", ""+Info.MAP_ZOOM_LEVEL);
		ACRA.getErrorReporter().putCustomData(" 10)  ISTEST", ""+Info.ISTEST); 
		
	}
}