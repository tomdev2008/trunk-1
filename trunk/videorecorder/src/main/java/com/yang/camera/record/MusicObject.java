package com.yang.camera.record;

import android.graphics.drawable.Drawable;

public class MusicObject {
	public MusicObject(String name, String path,Drawable icon) {
		musicName=name;
		musicPath=path;
		iconDrawable=icon;
	}

	public String musicName, musicPath;
	public Drawable iconDrawable;
}
