package com.whp.wallpaper.comicstrip;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.whp.android.display.DisplayUtils;
import com.whp.android.sprites.Sprite;
import com.whp.android.sprites.SpriteManager;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;



public class ComicStripLiveWallpaperService extends WallpaperService {
	
	public static final String	SHARED_PREFS_NAME	= "com.whp.wallpaper.comicstrip";

	@Override
	public void onCreate()
	{
//		android.os.Debug.waitForDebugger(); 
		super.onCreate();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public Engine onCreateEngine() {
		return new ComicStripLiveWallpaperEngine(this);
	}


	private class ComicStripLiveWallpaperEngine extends Engine {

//		private long				elapsedTime			= -1;
/*		private long				MAX_ELAPSED_TIME	= 3 * 1000 ;
		private long				spriteAnimationStartTime;
		private int					spriteXPosition;
		private int					spriteYPosition;*/
		private int					actualSpriteId			= -1 ;
		
//		private float				touchX			= -1;
//		private float				touchY			= -1;
//		private final Paint			paint			= new Paint();

		private boolean touchEnabled;
		private boolean visible;
		
//		private int width;
//		private int height;

		private String [] baloonNames	= null;
//		private List<Bitmap> baloonSprites = new ArrayList<Bitmap>();
		
		private final Handler handler = new Handler();
		private final Runnable drawRunner = new Runnable() {
			@Override
			public void run() {
				drawFrame();
			}

		};
		
		private SharedPreferences	preferences;
		
		private Random randomGenerator = new Random();
		
		Bitmap _backgroundImage ;
		
		private int screenHeight ;  
		private int screenWidth;
		private float scale ;  

		private float _xOffset	= 0;
		private float _yOffset	= 0;
		private float _xOffsetStep	= 0;
		private float _yOffsetStep	= 0;
		private int _xPixelOffset	= 0;
		private int _yPixelOffset	= 0;
		/*
		private static final int LOW_DPI_STATUS_BAR_HEIGHT = 19;
		private static final int MEDIUM_DPI_STATUS_BAR_HEIGHT = 25;
		private static final int HIGH_DPI_STATUS_BAR_HEIGHT = 38;
		*/
		private int statusBarHeight;
		
		private Context context;
		
		private SpriteManager	spriteManager	= null;
	
		public ComicStripLiveWallpaperEngine(Context context) {
			this.context	= context;
			preferences = context.getSharedPreferences(SHARED_PREFS_NAME, 0);

			baloonNames	= getResources().getStringArray(R.array.comicstrip_baloon_list);
			//preloadSprites(baloonNames);
			spriteManager	= new SpriteManager(this.context);
			spriteManager.preloadSprites(baloonNames);
			
			Bitmap _background = BitmapFactory.decodeResource(getResources(), R.drawable.strip_bw);
			
			//getScreenMetrics(); 
			screenHeight	= DisplayUtils.getScreenHeight(this.context) ;  
			screenWidth		= DisplayUtils.getScreenWidth(this.context);
			
			float imageHeight = _background.getHeight(); 
			
			statusBarHeight	= DisplayUtils.getStatusBarHeight(this.context);
	        scale = (float) ((screenHeight - statusBarHeight) / imageHeight);  
	        
	        _backgroundImage	= Bitmap.createScaledBitmap(_background,  (int) (scale * _background.getWidth()),  (int) (scale * _background.getHeight()), true);  

		}
/*
		int getStatusBarHeight () {
	        int statusBarHeight;
	        
	        DisplayMetrics displayMetrics = new DisplayMetrics();
	        ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);

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
		*/
		/*
		 private void getScreenMetrics()  {  
			 
			    DisplayMetrics metrics = new DisplayMetrics();  
	            Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();  
	            display.getMetrics(metrics);  

	            screenHeight = display.getHeight();  
	            screenWidth = display.getWidth();  
	            
	        }  
*/

	
		@Override
		public void onVisibilityChanged(boolean visible) {
			this.visible = visible;
			if (visible) {
				handler.post(drawRunner);
			} else {
				handler.removeCallbacks(drawRunner);
			}
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			this.visible = false;
			handler.removeCallbacks(drawRunner);
		}
		
		@Override
		public void onOffsetsChanged (float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
			
			_xOffset		= xOffset;
			_yOffset		= yOffset;
			_xOffsetStep	= xOffsetStep;
			_yOffsetStep	= yOffsetStep;
			_xPixelOffset	= xPixelOffset;
			_yPixelOffset	= yPixelOffset;
			
			drawFrame();
		}
		

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
//			this.width = width;
//			this.height = height;
			super.onSurfaceChanged(holder, format, width, height);
		}

		@Override
		public void onTouchEvent(MotionEvent event) {
			if (touchEnabled) {

				float x = event.getX();
				float y = event.getY();
				SurfaceHolder holder = getSurfaceHolder();
				Canvas canvas = null;
				try {
					canvas = holder.lockCanvas();
					if (canvas != null) {
						canvas.drawColor(Color.BLACK);


					}
				} finally {
					if (canvas != null)
						holder.unlockCanvasAndPost(canvas);
				}
				super.onTouchEvent(event);
			}
		}

		private void drawFrame() {
			
			if (actualSpriteId < 0) {
				addSprite();
			} else {
				long now = SystemClock.elapsedRealtime();
//				if ((now - spriteAnimationStartTime) > MAX_ELAPSED_TIME) {
				if ((now - spriteManager.sprites.get(actualSpriteId).getSpriteAnimationStartTime()) > Sprite.MAX_ELAPSED_TIME) {
					addSprite();
				}
			}
			
			SurfaceHolder holder = getSurfaceHolder();
			Canvas canvas = null;
			try {
				canvas = holder.lockCanvas();
				if (canvas != null) {
					drawScene(canvas);
				}
			} finally {
				if (canvas != null)
					holder.unlockCanvasAndPost(canvas);
			}
			handler.removeCallbacks(drawRunner);
			if (visible) {
				handler.postDelayed(drawRunner, 5000);
			}
		}
		
		private void drawScene(Canvas canvas) {
			
			float xPosition = -(_backgroundImage.getWidth() - screenWidth) * _xOffset;
			canvas.drawBitmap(_backgroundImage, xPosition, statusBarHeight + _yOffset, null);

//			float xPosition = (screenWidth - _backgroundImage.getWidth()) * (1 - _xOffset);
//			 canvas.drawBitmap(_backgroundImage, _xOffset, statusBarHeight + _yOffset, null);
			
			if (actualSpriteId > -1) {
				//canvas.drawBitmap(baloonSprites.get(actualSpriteId), xPosition + spriteXPosition, spriteYPosition, null);
				canvas.drawBitmap(spriteManager.sprites.get(actualSpriteId).getBitmap(), xPosition + spriteManager.sprites.get(actualSpriteId).getSpriteXPosition(), spriteManager.sprites.get(actualSpriteId).getSpriteYPosition(), null);
				
			}
		}
		
		private void addSprite() {
			
			actualSpriteId	= getRandomValue (0, baloonNames.length - 1);
			
//			int drawableResourceId = getResources().getIdentifier(baloonNames[spriteId], "drawable", getPackageName());
			
//			actualSprite = BitmapFactory.decodeResource(getResources(), drawableResourceId);
//			spriteAnimationStartTime	= SystemClock.elapsedRealtime();
			Sprite sprite = spriteManager.sprites.get(actualSpriteId);
			sprite.setSpriteAnimationStartTime(SystemClock.elapsedRealtime());
//			elapsedTime	= 0;
			
//			spriteXPosition	= getRandomValue (0, _backgroundImage.getWidth() - actualSprite.getWidth()) ;
//			spriteYPosition	= getRandomValue (0, _backgroundImage.getHeight() - actualSprite.getHeight()) ;
			
//			spriteXPosition	= getRandomValue (0, _backgroundImage.getWidth() - baloonSprites.get(actualSpriteId).getWidth()) ;
//			spriteYPosition	= getRandomValue (0, _backgroundImage.getHeight() - baloonSprites.get(actualSpriteId).getHeight()) ;
			int spriteXPosition	= getRandomValue (0, _backgroundImage.getWidth() - sprite.getBitmap().getWidth()) ;
			int spriteYPosition	= getRandomValue (0, _backgroundImage.getHeight() - sprite.getBitmap().getHeight()) ;
			sprite.setSpriteXPosition(spriteXPosition);
			sprite.setSpriteYPosition(spriteYPosition);
			
			spriteManager.sprites.set(actualSpriteId, sprite);
						
		}
		
		private int getRandomValue(int minValue, int maxValue) {
			
			if (minValue >= maxValue) {
				return 0;
			}
			
			return randomGenerator.nextInt(maxValue - minValue) + minValue;
		}
		/*
		private void preloadSprites (String [] spriteList) {

			for (String name : spriteList) {
				int drawableResourceId = getResources().getIdentifier(name, "drawable", getPackageName());
				Bitmap sprite = BitmapFactory.decodeResource(getResources(), drawableResourceId);
				baloonSprites.add(sprite);
			}
			
		}
*/
	}
}
