package com.whp.android.display;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class DisplayUtils {

	private static final int LOW_DPI_STATUS_BAR_HEIGHT = 19;
	private static final int MEDIUM_DPI_STATUS_BAR_HEIGHT = 25;
	private static final int HIGH_DPI_STATUS_BAR_HEIGHT = 38;

	@SuppressWarnings("deprecation")
	public static int getScreenHeight(Context context) {

		DisplayMetrics metrics = new DisplayMetrics();
		Display display = ((WindowManager) context
				.getSystemService(android.content.Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		display.getMetrics(metrics);

		return display.getHeight();

	}

	@SuppressWarnings("deprecation")
	public static int getScreenWidth(Context context) {

		DisplayMetrics metrics = new DisplayMetrics();
		Display display = ((WindowManager) context
				.getSystemService(android.content.Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		display.getMetrics(metrics);

		return display.getWidth();

	}

	public static int getStatusBarHeight(Context context) {
		int statusBarHeight;

		DisplayMetrics displayMetrics = new DisplayMetrics();
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getMetrics(displayMetrics);

		switch (displayMetrics.densityDpi) {
		case DisplayMetrics.DENSITY_HIGH:
			statusBarHeight = HIGH_DPI_STATUS_BAR_HEIGHT;
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			statusBarHeight = MEDIUM_DPI_STATUS_BAR_HEIGHT;
			break;
		case DisplayMetrics.DENSITY_LOW:
			statusBarHeight = LOW_DPI_STATUS_BAR_HEIGHT;
			break;
		default:
			statusBarHeight = MEDIUM_DPI_STATUS_BAR_HEIGHT;
		}
		return statusBarHeight;

	}

}
