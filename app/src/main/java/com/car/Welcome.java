package com.car;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class Welcome extends Activity {

	// ��ʼ������
	NewProgressBar newProgressBar = null;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// ȫ����ʾ
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.welcome);
		// ����Activity��ӵ��б���
		CloseActivity.getInstance().addActivity(this);

		newProgressBar = (NewProgressBar) findViewById(R.id.newProgressBar);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				newProgressBar.setLoaction();
				newProgressBar.setVisibility(View.VISIBLE);

				// ��ת��MainActivity�Ľ���
				Intent it = new Intent(Welcome.this, MainActivity.class);
				startActivity(it);
			}
		}, 400);
	}
}
