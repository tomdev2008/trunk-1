package com.aibinong.yueaiapi.utils;

import com.aibinong.yueaiapi.api.ApiHelper;
import com.aibinong.yueaiapi.pojo.ConfigEntity;
import com.aibinong.yueaiapi.pojo.JsonRetEntity;
import com.aibinong.yueaiapi.pojo.MemberEntity;
import com.aibinong.yueaiapi.services.SysService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.util.ArrayList;

import okhttp3.internal.cache.DiskLruCache;
import okhttp3.internal.io.FileSystem;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by yourfriendyang on 16/7/12.
 */
public class ConfigUtil {
    private static final String CONFIG_SAVE_KEY = "com_aibinong_aiduobao_model_utils_configutil_config_save_key";
    private static final String ADDRESSLIST_SAVE_KEY = "com_aibinong_aiduobao_model_utils_addressutil_addresslist_save_key";
    private DiskLruCache mDiskLruCache;
    private static ConfigUtil mInstance;
    private Gson gson;
    private ArrayList<WeakReference<Subscriber<ConfigEntity>>> subscriberWeakReferenceList;
    private ArrayList<Subscriber<ConfigEntity>> subscriberList;
    private Subscription mCurrentRefreshSub;
    private ConfigEntity mConfigEntity;

    private ConfigUtil() {

    }

    public void init(String cachePath) {
        mDiskLruCache = DiskLruCache.create(FileSystem.SYSTEM, new File(cachePath + "config" + File.separator), 1, 1, 10 * 1024 * 1024);
        gson = new GsonBuilder().create();
        subscriberWeakReferenceList = new ArrayList<>();
        subscriberList = new ArrayList<>();
        getConfig();
    }

    public static ConfigUtil getInstance() {
        if (mInstance == null) {
            mInstance = new ConfigUtil();
        }
        return mInstance;
    }

    public MemberEntity getMemberByLevel(int levle) {
        MemberEntity memberEntity = null;
        if (getConfig() != null && getConfig().members != null) {
            ArrayList<MemberEntity> memberEntities = getConfig().members;
            for (MemberEntity entity : memberEntities) {
                if (entity.level == levle) {
                    memberEntity = entity;
                    break;
                }
            }
        }
        return memberEntity;
    }

    public void addSubscriber(Subscriber<ConfigEntity> subscriber) {
        if (subscriber != null) {
            subscriberWeakReferenceList.add(new WeakReference<>(subscriber));
        }
    }

    public ConfigEntity getConfig() {
        if (mConfigEntity == null) {
            //load from disk
            try {
                Source source = mDiskLruCache.get(CONFIG_SAVE_KEY).getSource(0);
                BufferedSource souce = Okio.buffer(source);
                String savedData = souce.readString(Charset.defaultCharset());
                mConfigEntity = gson.fromJson(savedData, ConfigEntity.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mConfigEntity;
    }

    public Observable<ConfigEntity> getOrRequireConfig() {
        if (mConfigEntity != null) {
            return Observable.just(mConfigEntity);
        } else {
            return Observable.create(new Observable.OnSubscribe<ConfigEntity>() {
                @Override
                public void call(final Subscriber<? super ConfigEntity> subscriber) {
                    requireRefresh(new Subscriber<ConfigEntity>() {
                        @Override
                        public void onCompleted() {
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onError(Throwable e) {
                            subscriber.onError(e);
                        }

                        @Override
                        public void onNext(ConfigEntity configEntity) {
                            subscriber.onNext(configEntity);
                        }
                    }, false);
                }
            });
        }
    }

    public void requireRefresh(Subscriber<ConfigEntity> subscriber) {
        requireRefresh(subscriber, true);
    }

    public void requireRefresh(Subscriber<ConfigEntity> subscriber, boolean weakReference) {
        if (subscriber != null) {
            if (weakReference) {
                subscriberWeakReferenceList.add(new WeakReference<>(subscriber));
            } else {
                subscriberList.add(subscriber);
            }
        }
        if (mCurrentRefreshSub != null) {
            mCurrentRefreshSub.unsubscribe();
        }
        mCurrentRefreshSub = ApiHelper.getInstance()
                .create(SysService.class)
                .config(null)
                .compose(ApiHelper.<JsonRetEntity<ConfigEntity>>doIoObserveMain())
                .subscribe(new Subscriber<JsonRetEntity<ConfigEntity>>() {
                    @Override
                    public void onCompleted() {
                        for (WeakReference<Subscriber<ConfigEntity>> subscriberRef
                                : subscriberWeakReferenceList) {
                            if (subscriberRef != null) {
                                Subscriber<ConfigEntity> outSubscriber = subscriberRef.get();
                                if (outSubscriber != null) {
                                    outSubscriber.onCompleted();
                                }
                            }
                        }
                        subscriberWeakReferenceList.clear();
                        for (Subscriber<ConfigEntity> subscriber
                                : subscriberList) {
                            subscriber.onCompleted();
                        }
                        subscriberList.clear();
                    }

                    @Override
                    public void onError(Throwable e) {
                        for (WeakReference<Subscriber<ConfigEntity>> subscriberRef
                                : subscriberWeakReferenceList) {
                            if (subscriberRef != null) {
                                Subscriber<ConfigEntity> outSubscriber = subscriberRef.get();
                                if (outSubscriber != null) {
                                    outSubscriber.onError(e);
                                }
                            }
                        }
                        subscriberWeakReferenceList.clear();
                        for (Subscriber<ConfigEntity> subscriber
                                : subscriberList) {
                            subscriber.onError(e);
                        }
                        subscriberList.clear();
                    }

                    @Override
                    public void onNext(JsonRetEntity<ConfigEntity> objectJsonRetEntity) {
                        //缓存起来
                        mConfigEntity = objectJsonRetEntity.getData();
                        if (mConfigEntity != null) {
                            DiskLruCache.Editor editor = null;
                            try {
                                try {
                                    mDiskLruCache.remove(CONFIG_SAVE_KEY);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                editor = mDiskLruCache.edit(CONFIG_SAVE_KEY);
                                BufferedSink sink = Okio.buffer(editor.newSink(0));
                                sink.writeString(gson.toJson(mConfigEntity), Charset.defaultCharset());
                                sink.close();
                                editor.commit();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                if (editor != null) {
                                    try {
                                        editor.abort();
                                    } catch (Exception e) {
//                                        e.printStackTrace();
                                    }
                                }
                            }
                            for (WeakReference<Subscriber<ConfigEntity>> subscriberRef
                                    : subscriberWeakReferenceList) {
                                if (subscriberRef != null) {
                                    Subscriber<ConfigEntity> outSubscriber = subscriberRef.get();
                                    if (outSubscriber != null) {
                                        outSubscriber.onNext(mConfigEntity);
                                    }
                                }
                            }
                            subscriberWeakReferenceList.clear();
                            for (Subscriber<ConfigEntity> subscriber
                                    : subscriberList) {
                                subscriber.onNext(mConfigEntity);
                            }
                            subscriberList.clear();
                        }
                    }
                });
    }

    public static final int MIN_AGE = 18, MAX_AGE = 50;
    public static final int MIN_DISTANCE = 10, MAX_DISTANCE = 100;

    public int getPairMinAge() {
        int minAge;
        minAge = LocalStorage.getInstance().getInt(LocalStorageKey.KEY_STORAGE_PAIR_MinAGE, MIN_AGE);
        return minAge;
    }

    public void setPairMinAge(int age) {
        LocalStorage.getInstance().putInt(LocalStorageKey.KEY_STORAGE_PAIR_MinAGE, age);
    }

    public int getPairMaxAge() {
        int minAge;
        minAge = LocalStorage.getInstance().getInt(LocalStorageKey.KEY_STORAGE_PAIR_MaxAGE, MAX_AGE);
        return minAge;
    }

    public void setPairMaxAge(int age) {
        LocalStorage.getInstance().putInt(LocalStorageKey.KEY_STORAGE_PAIR_MaxAGE, age);
    }

    public int getPairMinDistance() {
        int minAge;
        minAge = LocalStorage.getInstance().getInt(LocalStorageKey.KEY_STORAGE_PAIR_MinDistance, MIN_DISTANCE);
        return minAge;
    }

    public void setPairMinDistance(int age) {
        LocalStorage.getInstance().putInt(LocalStorageKey.KEY_STORAGE_PAIR_MinDistance, age);
    }

    public int getPairMaxDistance() {
        int minAge;
        minAge = LocalStorage.getInstance().getInt(LocalStorageKey.KEY_STORAGE_PAIR_MaxDistance, MIN_DISTANCE);
        return minAge;
    }

    public void setPairMaxDistance(int age) {
        LocalStorage.getInstance().putInt(LocalStorageKey.KEY_STORAGE_PAIR_MaxDistance, age);
    }

    public boolean getShowNotify() {
        boolean showNotify = true;
        showNotify = LocalStorage.getInstance().getBoolean(LocalStorageKey.KEY_STORAGE_SHOW_NOTIFY, showNotify);
        return showNotify;
    }

    public void setShowNotify(boolean show) {
        LocalStorage.getInstance().putBoolean(LocalStorageKey.KEY_STORAGE_SHOW_NOTIFY, show);
    }

    public boolean getShowNotifyDetail() {
        boolean showNotify = true;
        showNotify = LocalStorage.getInstance().getBoolean(LocalStorageKey.KEY_STORAGE_SHOW_NOTIFY_DETAIL, showNotify);
        return showNotify;
    }

    public void setShowNotifyDetail(boolean show) {
        LocalStorage.getInstance().putBoolean(LocalStorageKey.KEY_STORAGE_SHOW_NOTIFY_DETAIL, show);
    }

    public boolean isNeedGuide() {
        boolean needGuide = true;
        needGuide = LocalStorage.getInstance().getBoolean(LocalStorageKey.KEY_STORAGE_SHOW_GUIDE, needGuide);
        return needGuide;
    }

    public void seGuideShowed() {
        LocalStorage.getInstance().putBoolean(LocalStorageKey.KEY_STORAGE_SHOW_GUIDE, false);
    }

    public String getClientID() {
        String clientID = LocalStorage.getInstance().getString(LocalStorageKey.KEY_STORAGE_PUSH_CLIENT_ID, null);
        return clientID;
    }

    public void saveClientID(String clientID) {
        //持久化保存
        LocalStorage.getInstance().putString(LocalStorageKey.KEY_STORAGE_PUSH_CLIENT_ID, clientID);
    }

    private int mLikeTimesRemain;

    public int getLikeTimes() {
        return mLikeTimesRemain;
    }

    public void saveLikeTimesRemain(int times) {
        mLikeTimesRemain = times;
    }
}
