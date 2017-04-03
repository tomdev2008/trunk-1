/*
 * Copyright 2014 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fatalsignal.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class BitmapUtils {

	public interface ThumbGeneratorListener {
		public void onResult(boolean ret, String thumbFile, Bitmap thumb);
	}

	private BitmapUtils() {
	}

	/**
	 * Returns a bitmap showing a screenshot of the view passed in.
	 */
	public static Bitmap getBitmapFromView(final View v) {
		Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		v.draw(canvas);
		return bitmap;
	}

	public static boolean saveBitmap(Bitmap bmp, String path) {
		File file = new File(path);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if (file.exists()) {
			file.delete();
		}
		try {

			FileOutputStream out = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.JPEG, 80, out);
			out.flush();
			out.close();
			Log.i("saveBitmap success:", path);
			return true;
		} catch (Exception e) {
			Log.e("saveBitmap:" + path, e.toString());
		}
		return false;

	}

	// public static void generateThumb(final Context context,final String
	// fullPath,final boolean isPhoto,final String thumbFile,final
	// ThumbGeneratorListener listener)
	// {
	// new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	// String tempThumbFname=MD5Util.getMD5String(fullPath);
	// Bitmap thumb=null;
	// if (isPhoto) {
	// BitmapFactory.Options opt=new Options();
	// opt.inJustDecodeBounds=true;
	// BitmapFactory.decodeFile(fullPath,opt);
	// opt.inJustDecodeBounds=false;
	// thumb =
	// ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(fullPath,opt),
	// Constant.THUMB_SIZE,
	// (int)(opt.outHeight*(Constant.THUMB_SIZE/(opt.outWidth*1.0))));
	// }
	// else
	// {
	// MediaMetadataRetriever mediaMetadataRetriever=new
	// MediaMetadataRetriever();
	// mediaMetadataRetriever.setDataSource(fullPath);
	// thumb = mediaMetadataRetriever.getFrameAtTime();
	//
	// }
	// String thumbnailPath=DeviceUtils.getCacheDir(context,
	// Constant.THUMB_CACHE_DIR)+"/"+tempThumbFname;
	// boolean ret = BitmapUtils.saveBitmap(thumb, thumbnailPath);
	// listener.onResult(ret,thumbnailPath, thumb);
	// }
	// }).start();
	//
	//
	// }
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, boolean preferSmall) {
		// 源图片的宽度
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (width > reqWidth) {
			// 计算出实际宽度和目标宽度的比率
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = widthRatio;
		}
		if (preferSmall) {
			for (int i = 1; i < 10; i++) {
				double pow = Math.pow(2, i);
				if (pow >= inSampleSize) {
					inSampleSize = Math.round((float) pow);
					break;
				}
			}
		}
		return inSampleSize;
	}

	/***/
	/**
	 * 图片去色,返回灰度图片
	 * 
	 * @param bmpOriginal
	 *            传入的图片
	 * @return 去色后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();
		try{
		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
		}catch(OutOfMemoryError e)
		{
			return bmpOriginal;
		}
	}
	// Bitmap → byte[]
	public static byte[] bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
}
