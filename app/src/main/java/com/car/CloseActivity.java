package com.car;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.Application;

public class CloseActivity extends Application {
	List<Activity> mList = new ArrayList<Activity>();
	private static CloseActivity instance;

	private CloseActivity() {
	}

	public synchronized static CloseActivity getInstance() {
		if (null == instance) {
			instance = new CloseActivity();
		}
		return instance;
	}

	// add Activity
	public void addActivity(Activity activity) {
		mList.add(activity);
	}

	public void exit() {
		try {
			for (Activity activity : mList) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}
}