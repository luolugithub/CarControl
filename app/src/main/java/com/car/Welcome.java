package com.car;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class Welcome extends Activity {

	// 初始化定义
	NewProgressBar newProgressBar = null;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// 全屏显示
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.welcome);
		// 将此Activity添加到列表中
		CloseActivity.getInstance().addActivity(this);

		newProgressBar = (NewProgressBar) findViewById(R.id.newProgressBar);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				newProgressBar.setLoaction();
				newProgressBar.setVisibility(View.VISIBLE);

				// 跳转到MainActivity的界面
				Intent it = new Intent(Welcome.this, MainActivity.class);
				startActivity(it);
			}
		}, 400);
	}
}
