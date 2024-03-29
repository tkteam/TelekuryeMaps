package com.telekurye.mobileui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.telekurye.data.sync.AutoSyncHelper;
import com.telekurye.tools.Tools;

public class CourierType extends Activity implements OnClickListener {

	public Button	courier_btn1, courier_btn2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.courier_type);
		Tools.disableScreenLock(this);

		courier_btn1 = (Button) findViewById(R.id.courier_btn1);
		courier_btn2 = (Button) findViewById(R.id.courier_btn2);

		courier_btn1.setOnClickListener(this);
		courier_btn2.setOnClickListener(this);

		AutoSyncHelper.GetInstance().startbackgroundUpdateAutomagically(CourierType.this);

	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.courier_btn1) {

			Intent i1 = new Intent(CourierType.this, VehicleMission.class);
			startActivity(i1);

			// try {
			// int i = 10 / 0;
			// }
			// catch (Exception e) {
			//
			// Tools.saveErrors(e);
			//
			// }
		}
		else if (v.getId() == R.id.courier_btn2) {
			Intent i1 = new Intent(CourierType.this, ExpandableList.class);
			startActivity(i1);
		}
		// else if (v.getId() == R.id.btnAppExit) {
		//
		// android.os.Process.killProcess(android.os.Process.myPid());
		// super.onDestroy();
		//
		// // finish();
		// // System.exit(0);
		//
		// // Intent intent = new Intent(Intent.ACTION_MAIN);
		// // intent.addCategory(Intent.CATEGORY_HOME);
		// // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// // startActivity(intent);
		// }
	}

	@Override
	public void onBackPressed() {

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
}
