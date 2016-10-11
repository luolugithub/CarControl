package com.car;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class NewProgressBar extends View {

	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private float mRotate;
	private Matrix mMatrix = new Matrix();
	private Shader mShader;
	private int x, y;
	private Activity activity;

	public NewProgressBar(Context context, AttributeSet attr) {
		super(context, attr);
		this.activity = ((Activity) context);
		initPaint();
	}

	public NewProgressBar(Context context) {
		super(context);
		this.activity = ((Activity) context);
		initPaint();
	}

	private void initPaint() {
		mShader = new SweepGradient(x, y, new int[] { 0x66378300, 0xFF378300 },
				new float[] { 0.7f, 0.7f });
		mPaint.setShader(mShader);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(10);
		mPaint.setAntiAlias(true);
	}

	protected void onDraw(Canvas canvas) {
		Paint paint = mPaint;
		mMatrix.setRotate(mRotate, x, y);
		mShader.setLocalMatrix(mMatrix);
		mRotate += 5;
		if (mRotate >= 360) {
			mRotate = 0;
		}
		invalidate();
		canvas.drawCircle(x, y, 50, paint);
	}

	protected void setLoaction() {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		x = dm.widthPixels / 2;
		y = 2 * dm.heightPixels / 3;
		initPaint();
		invalidate();
	}
}
