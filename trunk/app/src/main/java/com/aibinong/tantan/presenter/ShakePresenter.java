package com.aibinong.tantan.presenter;

// _______________________________________________________________________________________________\
//|                                                                                               |
//| Created by yourfriendyang on 16/11/4.                                                                |
//| yourfriendyang@163.com                                                                        |
//|_______________________________________________________________________________________________|

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.aibinong.tantan.pojo.LiuLianLocation;
import com.aibinong.tantan.util.LiuLianLocationUtil;
import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.ResponseResult;
import com.aibinong.yueaiapi.pojo.UserEntity;
import com.aibinong.yueaiapi.services.SearchService;

import rx.Subscriber;

public class ShakePresenter extends PresenterBase {


    public interface IShake {
        void onShakeFailed(Throwable e);

        void onShakeSuccess(UserEntity userEntity);

        void onShake();
    }

    private IShake mIShake;
    private long mLastUpdateTime;
    private static final int SPEED_SHRESHOLD = 4000;
    private static final long UPTATE_INTERVAL_TIME = 70;
    private static final long MAX_SHAKE_INTERVAL = 2000;
    private float lastX, lastY, lastZ;
    private SensorManager mSensorManager;

    public ShakePresenter(IShake iShake, SensorManager sensorManager) {
        mIShake = iShake;
        mSensorManager = sensorManager;
        mSensorEventListener = new SensorEventListener() {
            long lastShakeTime;

            public void onSensorChanged(SensorEvent event) {
                //现在检测时间
                long currentUpdateTime = System.currentTimeMillis();
                //两次检测的时间间隔
                long timeInterval = currentUpdateTime - mLastUpdateTime;
                //判断是否达到了检测时间间隔
                if (timeInterval < UPTATE_INTERVAL_TIME)
                    return;
                //现在的时间变成last时间
                mLastUpdateTime = currentUpdateTime;

                //获得x,y,z坐标
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                //获得x,y,z的变化值
                float deltaX = x - lastX;
                float deltaY = y - lastY;
                float deltaZ = z - lastZ;

                //将现在的坐标变成last坐标
                lastX = x;
                lastY = y;
                lastZ = z;

                double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / timeInterval * 10000;
                //达到速度阀值，发出提示
                if (speed >= SPEED_SHRESHOLD)
                    if (currentUpdateTime - lastShakeTime > MAX_SHAKE_INTERVAL) {
                        mIShake.onShake();
                        lastShakeTime = currentUpdateTime;
                    }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }

    private SensorEventListener mSensorEventListener;
    private Sensor sensor;

    public void registerShakeListener() {
        if (mSensorManager != null) {
            //获得重力传感器
            sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        //注册
        if (sensor != null) {
            mSensorManager.registerListener(mSensorEventListener, sensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    public void unRegShakeListener() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(mSensorEventListener);
        }
    }

    public void shake() {
        LiuLianLocation location = LiuLianLocationUtil.getInstance().getLocationSync();
        addToCycle(
                ApiHelper
                        .getInstance()
                        .create(SearchService.class)
                        .shake(location.isValid() ? (location.longitude + "") : null, location.isValid() ? (location.latitude + "") : null)
                        .compose(ApiHelper.<JsonRetEntity<UserEntity>>doIoObserveMain())
                        .subscribe(new Subscriber<JsonRetEntity<UserEntity>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mIShake.onShakeFailed(ResponseResult.fromThrowable(e));
                            }

                            @Override
                            public void onNext(JsonRetEntity<UserEntity> userEntityJsonRetEntity) {
                                mIShake.onShakeSuccess(userEntityJsonRetEntity.getData());
                            }
                        })
        );
    }
}
