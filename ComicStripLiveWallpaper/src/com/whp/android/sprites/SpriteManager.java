package com.whp.android.sprites;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class SpriteManager {

	public List<Sprite> sprites = new ArrayList<Sprite>();
	private Context context;
	
	public SpriteManager (Context context) {
		this.context	= context;
	}
	
	public void preloadSprites (String [] spriteList) {

		for (String name : spriteList) {
			int drawableResourceId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableResourceId);
			Sprite sprite	= new Sprite();
			sprite.setBitmap(bitmap);
			sprites.add(sprite);
		}
		
	}
	
}
