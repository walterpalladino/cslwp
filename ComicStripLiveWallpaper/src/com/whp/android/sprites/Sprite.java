package com.whp.android.sprites;

import android.graphics.Bitmap;

public class Sprite {

	public final static long	MAX_ELAPSED_TIME	= 3 * 1000 ;
	private long				spriteAnimationStartTime;
	private int					spriteXPosition;
	private int					spriteYPosition;
	private Bitmap				bitmap;
	
	
	public long getSpriteAnimationStartTime() {
		return spriteAnimationStartTime;
	}
	public void setSpriteAnimationStartTime(long spriteAnimationStartTime) {
		this.spriteAnimationStartTime = spriteAnimationStartTime;
	}
	public int getSpriteXPosition() {
		return spriteXPosition;
	}
	public void setSpriteXPosition(int spriteXPosition) {
		this.spriteXPosition = spriteXPosition;
	}
	public int getSpriteYPosition() {
		return spriteYPosition;
	}
	public void setSpriteYPosition(int spriteYPosition) {
		this.spriteYPosition = spriteYPosition;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
}
