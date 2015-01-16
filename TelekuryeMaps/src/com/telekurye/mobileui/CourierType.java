package com.telekurye.mobileui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.telekurye.data.sync.AutoSyncHelper;
import com.telekurye.database.DatabaseHelper;
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

		// *-*
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		dbHelper.CreateDatabase(this);
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
	}

	@Override
	public void onBackPressed() {

	}

}
