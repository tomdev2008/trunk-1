package com.fatalsignal.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

public class MetaDataUtil {
	public static String getString(Context context, String key) {
		String app_what = null;
		try {

			ApplicationInfo appInfo = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);

			Bundle metadata = appInfo.metaData;
			if (metadata != null) {
				app_what = metadata.getString(key);
			}

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return app_what;
	}
}
