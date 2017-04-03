package com.aibinong.tantan.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.aibinong.tantan.pojo.LiuLianLocation;
import com.fatalsignal.util.Log;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * @author 杨升迁(yourfriendyang@163.com)
 *         <p/>
 *         下午2:43:40 2015年5月20日
 */
public class LiuLianLocationUtil {
    public interface LiulianLocationListener {
        void onLocateSuccess(LiuLianLocation location);

        void onLocateFailed();

        Looper getLooper();
    }


    private static String TAG = LiuLianLocationUtil.class.getSimpleName();
    /**
     * 最短刷新时间间隔
     */
    private static final long MIN_REFRESH_TIME = 2000;
    /**
     * 最短刷新距离间隔
     */
    private static final float MIN_REFRESH_DISTANCE = 0.0f;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location lastKnownLocation;
    //    private Context mContext;
    private ReferenceQueue<LiulianLocationListener> listenerReferenceQueue = new ReferenceQueue<LiulianLocationListener>();
    private ArrayList<WeakReference<LiulianLocationListener>> locationListenerList = new ArrayList<WeakReference<LiulianLocationListener>>();

    private LiuLianLocationUtil() {
    }

    ;

    private static class InstanceHolder {
        static LiuLianLocationUtil instance = new LiuLianLocationUtil();
    }

    public static LiuLianLocationUtil getInstance() {
        return InstanceHolder.instance;
    }

    public void init(Context appContext) {
//        mContext=appContext;
        if (locationManager == null) {
            synchronized (LiuLianLocationUtil.class) {
                if (locationManager == null) {
                    initLocationListener(appContext);
                }
            }
        }
    }

    private void initLocationListener(Context context) {
        try {
            locationManager = (LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != locationManager) {
            locationListener = new LocationListener() {

                @Override
                public void onStatusChanged(String provider, int status,
                                            Bundle extras) {

                    notifyAllListener_onStatusChanged(provider, status, extras);
//                    locationManager.removeUpdates(locationListener);
                    Log.e(TAG, "onStatusChanged-->" + provider + status + extras);
                }

                @Override
                public void onProviderEnabled(String provider) {
                    Log.e(TAG, "onProviderEnabled" + provider);
                    notifyAllListener_onProviderEnabled(provider);
                }

                @Override
                public void onProviderDisabled(String provider) {
                    locationManager.removeUpdates(locationListener);
                    Log.e(TAG, "onProviderDisabled" + provider);
                    notifyAllListener_onProviderDisabled(provider);
                    notifyAllListener_onFailed();
                }

                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        lastKnownLocation = location;
                        notifyAllListener_onLocationChanged(location);
                    }

                    locationManager.removeUpdates(locationListener);
                    Log.e(TAG, "onLocationChanged-->" + location.getProvider());
                }
            };
            refreshLocation();
        }
    }

    private long lastRefreshTs;

    private void refreshLocation() {
        long ts = System.currentTimeMillis();
        if (ts - lastRefreshTs < 10000) {

            return;
        }
        lastRefreshTs = ts;
        if (null != locationManager && null != locationListener) {
            try {
                if (locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null) {
                    locationManager.requestSingleUpdate(
                            LocationManager.NETWORK_PROVIDER, locationListener, null);
                    Log.d("requestLocationUpdates  NETWORK_PROVIDER");
                } else if (locationManager.getProvider(LocationManager.GPS_PROVIDER) != null) {
                    locationManager.requestSingleUpdate(
                            LocationManager.GPS_PROVIDER, locationListener, null);
                    Log.d("requestLocationUpdates  GPS_PROVIDER");
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        notifyAllListener_onFailed();
    }

    /**
     * 回调所有的位置监听
     */
    private void notifyAllListener_onFailed() {
        cancelCountDown();
        for (int i = 0; i < locationListenerList.size(); i++) {
            WeakReference<LiulianLocationListener> listenerRef = locationListenerList
                    .get(i);
            final LiulianLocationListener listener = listenerRef.get();
            if (listener != null) {
                final LiuLianLocation cachedLocation = getLocationSync();
                if (listener.getLooper() != null) {
                    new Handler(listener.getLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (cachedLocation != null && cachedLocation.isValid()) {
                                listener.onLocateSuccess(cachedLocation);
                            } else {
                                listener.onLocateFailed();
                            }
                        }
                    });
                } else {
                    if (cachedLocation != null && cachedLocation.isValid()) {
                        listener.onLocateSuccess(cachedLocation);
                    } else {
                        listener.onLocateFailed();
                    }
                }
            }
        }
        // 清理一下不需要的对象
        Reference<? extends LiulianLocationListener> listenerRef;
        while ((listenerRef = listenerReferenceQueue.poll()) != null) {
            locationListenerList.remove(listenerRef);
        }
    }

    /**
     * 回调所有的位置监听
     */
    private void notifyAllListener_onStatusChanged(String provider, int status,
                                                   Bundle extras) {
//        for (int i = 0; i < locationListenerList.size(); i++) {
//            WeakReference<LiulianLocationListener> listenerRef = locationListenerList
//                    .get(i);
//            LiulianLocationListener listener = listenerRef.get();
//            if (listener != null) {
//                listener.onStatusChanged(provider, status, extras);
//            }
//        }
//        // 清理一下不需要的对象
//        Reference<? extends LiulianLocationListener> listenerRef;
//        while ((listenerRef = listenerReferenceQueue.poll()) != null) {
//            locationListenerList.remove(listenerRef);
//        }
//        listenerRef = null;
    }

    /**
     * 回调所有的位置监听
     */
    private void notifyAllListener_onProviderEnabled(String provider) {
//        for (int i = 0; i < locationListenerList.size(); i++) {
//            WeakReference<LiulianLocationListener> listenerRef = locationListenerList
//                    .get(i);
//            LocationListener listener = listenerRef.get();
//            if (listener != null) {
//                listener.onProviderEnabled(provider);
//            }
//        }
//        // 清理一下不需要的对象
//        Reference<? extends LocationListener> listenerRef;
//        while ((listenerRef = listenerReferenceQueue.poll()) != null) {
//            locationListenerList.remove(listenerRef);
//        }
//        listenerRef = null;
    }

    /**
     * 回调所有的位置监听
     */
    private void notifyAllListener_onProviderDisabled(String provider) {
//        for (int i = 0; i < locationListenerList.size(); i++) {
//            WeakReference<LiulianLocationListener> listenerRef = locationListenerList
//                    .get(i);
//            LocationListener listener = listenerRef.get();
//            if (listener != null) {
//                listener.onProviderDisabled(provider);
//            }
//        }
//        // 清理一下不需要的对象
//        Reference<? extends LocationListener> listenerRef;
//        while ((listenerRef = listenerReferenceQueue.poll()) != null) {
//            locationListenerList.remove(listenerRef);
//        }
//        listenerRef = null;
    }

    /**
     * 回调所有的位置监听
     */
    private void notifyAllListener_onLocationChanged(Location location) {
        cancelCountDown();
        final LiuLianLocation retLocation = new LiuLianLocation();
        retLocation.longitude = location.getLongitude();
        retLocation.latitude = location.getLatitude();

        //缓存起来
//        LocalStorage.getInstance().putString(LocalStorageKey.KEY_STORAGE_SAVED_LATITUDE, retLocation.latitude + "");
//        LocalStorage.getInstance().putString(LocalStorageKey.KEY_STORAGE_SAVED_LONGITUDE, retLocation.longitude + "");


        for (int i = 0; i < locationListenerList.size(); i++) {
            WeakReference<LiulianLocationListener> listenerRef = locationListenerList
                    .get(i);
            final LiulianLocationListener listener = listenerRef.get();
            if (listener != null) {
                Log.d("notify changed loop " + i, listener + "");
                if (listener.getLooper() != null) {
                    new Handler(listener.getLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onLocateSuccess(retLocation);
                        }
                    });
                } else {
                    listener.onLocateFailed();
                }
            }
        }
        // 清理一下不需要的对象
        Reference<? extends LiulianLocationListener> listenerRef;
        while ((listenerRef = listenerReferenceQueue.poll()) != null) {
            locationListenerList.remove(listenerRef);
        }
    }

    public void getLocation(LiulianLocationListener listener) {
        Log.i(TAG, "getLocation");
        if (listener != null) {
            WeakReference<LiulianLocationListener> listenerRef = new WeakReference<LiulianLocationListener>(listener, listenerReferenceQueue);
            if (!locationListenerList.contains(listenerRef)) {
                locationListenerList.add(listenerRef);
            }
            refreshLocation();
            startCountDown();
        }
    }

    boolean cancelCountDown = false;
    private Thread countDownThread;

    private void startCountDown() {
        //启动一个线程去做倒计时
        cancelCountDown = false;
        countDownThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
//                    e.printStackTrace();
                    Log.d("locate countdown interrupted");
                }
                if (!cancelCountDown) {
                    notifyAllListener_onFailed();
                }
            }
        });
        countDownThread.start();
    }

    private void cancelCountDown() {
        cancelCountDown = true;
        if (countDownThread != null) {
            countDownThread.interrupt();
        }
    }

    public LiuLianLocation getLocationSync() {
//        Log.i(TAG, "getLocationSync");
        LiuLianLocation retLocation = new LiuLianLocation();
        if (null != locationManager) {
            try {
                Location tempLocation = null;
                Location gpsLocation = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location netLocation = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Location passiveLocation = locationManager
                        .getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                if (gpsLocation != null) {
//                    Log.e(TAG, "-gpsLocation-" + gpsLocation.toString());
                    tempLocation = gpsLocation;
                } else if (netLocation != null) {
//                    Log.e(TAG, "-netLocation-" + netLocation.toString());
                    tempLocation = netLocation;
                } else if (passiveLocation != null) {
//                    Log.e(TAG, "-passiveLocation-" + passiveLocation.toString());
                    tempLocation = passiveLocation;
                } else if (lastKnownLocation != null) {
                    tempLocation = lastKnownLocation;
                }
                if (tempLocation == null) {
                    //没有任何可用位置就去刷新一下
                    refreshLocation();
                }

                if (tempLocation != null) {

                    retLocation.longitude = tempLocation.getLongitude();
                    retLocation.latitude = tempLocation.getLatitude();

//                    //缓存起来
//                    LocalStorage.getInstance().putString(LocalStorageKey.KEY_STORAGE_SAVED_LATITUDE, retLocation.latitude + "");
//                    LocalStorage.getInstance().putString(LocalStorageKey.KEY_STORAGE_SAVED_LONGITUDE, retLocation.longitude + "");

                } else {
//                    try {
//                        retLocation.latitude = Double.parseDouble(LocalStorage.getInstance().getString(LocalStorageKey.KEY_STORAGE_SAVED_LATITUDE, "0"));
//                        retLocation.longitude = Double.parseDouble(LocalStorage.getInstance().getString(LocalStorageKey.KEY_STORAGE_SAVED_LONGITUDE, "0"));
//                    } catch (NumberFormatException e) {
//                        LocalStorage.getInstance().remove(LocalStorageKey.KEY_STORAGE_SAVED_LATITUDE);
//                        LocalStorage.getInstance().remove(LocalStorageKey.KEY_STORAGE_SAVED_LONGITUDE);
//                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
//                PackageManager pm = mContext.getPackageManager();
//                boolean permission_FineLocation = (PackageManager.PERMISSION_GRANTED ==
//                        pm.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, mContext.getPackageName()));
//                boolean permission_CoarseLocation = (PackageManager.PERMISSION_GRANTED ==
//                        pm.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, mContext.getPackageName()));
//
//                if (!permission_FineLocation||!permission_CoarseLocation) {
//                    Toast.makeText(mContext, "无法获取位置"+permission_CoarseLocation+","+permission_FineLocation, Toast.LENGTH_SHORT).show();
//                }else {
//
//                }
            }
        }
        return retLocation;
    }

}
