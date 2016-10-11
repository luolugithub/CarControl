package per.emp.car;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class Welcome extends Activity {

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// ȫ����ʾ
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		final View view = View.inflate(this, R.layout.welcome, null);
		setContentView(view);
		// ����Activity��ӵ��б���
		CloseActivity.getInstance().addActivity(this);

		// ������ʾ����
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(1600);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				redirectTo();
			}

			// ��ת��main Activity
			private void redirectTo() {
				Intent intent = new Intent();
				intent.setClass(Welcome.this, MainActivity.class);
				startActivity(intent);
				// overridePendingTransition(R.anim.abc_fade_in,
				// R.anim.app_enter);
				finish();
				overridePendingTransition(R.anim.app_enter, R.anim.abc_fade_out);
			}
		});
	}
}
