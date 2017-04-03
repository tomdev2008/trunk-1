# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html

# Optimizations: If you don't want to optimize, use the
# proguard-android.txt configuration file instead of this one, which
# turns off the optimization flags.  Adding optimization introduces
# certain risks, since for example not all optimizations performed by
# ProGuard works on all versions of Dalvik.  The following flags turn
# off various optimizations known to have issues, but the list may not
# be complete or up to date. (The "arithmetic" optimization can be
# used if you are only targeting Android 2.0 or later.)  Make sure you
# test thoroughly if you go this route.
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-dontpreverify

# The remainder of this file is identical to the non-optimized version
# of the Proguard configuration file (except that the other file has
# flags to turn off optimization).

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

-keepattributes *Annotation*
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**


# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions


-dontwarn sun.misc.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
#--->others
#-keep class com.aliulian.mall.brand.widget{*;}
#-libraryjars libs/android-async-http-1.4.5.jar
#-libraryjars libs/android-viewbadger.jar
#-libraryjars libs/glide-3.6.0.jar
#-libraryjars libs/gson-2.3.1.jar
#-libraryjars libs/libammsdk.jar
-keep class **.R$*{*;}
-keep class com.google.**{*;}
-keepclassmembers class * implements **.PresenterBase{
    *** onDestoryView();
}
-keep class **.domain.*{*;}
-keep class **.*$AliulianAndroidJSInterface{*;}
-keep class **.AliulianAndroidJSInterface{*;}
-keep class **.domain.**{*;}
-keep class **.entity.**{*;}
-keep class **.pojo.**{*;}
-keep interface **.service.**{*;}
-keep class *{
 *** getInstance();
}
-keep class com.bumptech.**
-keep class com.google.**{*;}
-keep class com.yang.util.MD5Util{*;}
-keep class org.apache.**{*;}
-keep class **.BuildConfig{*;}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}


##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson

#<---others

#--->umeng 统计
#-libraryjars ../libs/umeng-update-v2.6.0.1.jar
#-libraryjars ../Umeng_feedback／libs/com.umeng.fb.5.3.0.jar
#-libraryjars ../Umeng_Analyze／libs/umeng-analytics-v5.5.3.jar
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keep public class com.aliulian.mall.R$*{
public static final int *;
}
-keep class com.umeng.**
-dontwarn com.umeng.**
#<----umeng 统计

#--->支付宝
-dontwarn com.alipay.**
#-libraryjars libs/alipaySDK-20150602.jar
-keep class com.alipay.**
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.alipay.mobilesecuritysdk.*
-keep class com.ut.*
#<---支付宝


#-->getui
-dontwarn com.igexin.**
-keep class com.igexin.**{*;}
#<--getui

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-dontwarn org.apache.http.**

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}



-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**


-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* {*;}
-keep public class javax.**
-keep public class android.webkit.**

-keep class com.facebook.**
-keep class com.facebook.** { *; }
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}

-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}

-dontwarn twitter4j.**
-keep class twitter4j.** { *; }

-keep class com.tencent.** {*;}
-dontwarn com.tencent.**
-keep public class com.umeng.soexample.R$*{
    public static final int *;
}
-keep public class com.umeng.soexample.R$*{
    public static final int *;
}
-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}

-keep class com.sina.** {*;}
-dontwarn com.sina.**
-keep class  com.alipay.share.sdk.** {
   *;
}
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}
-keep class com.linkedin.** { *; }
-keepattributes Signature



#------>iapppay
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
#-dontpreverify
-dontwarn
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*


#-----------keep-------------------

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepattributes Exceptions,InnerClasses
-keep public class com.alipay.android.app.** {
    public <fields>;
    public <methods>;
}

# Keep names - Native method names. Keep all native class/method names.
-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}

-keepclasseswithmembers,allowshrinking class * {
    public <init>(android.content.Context,android.util.AttributeSet);
}

-keepclasseswithmembers,allowshrinking class * {
    public <init>(android.content.Context,android.util.AttributeSet,int);
}

-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * extends android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}


-ignorewarning
-keep public class * extends android.widget.TextView


#--------------unionpay 3.1.0--------------
-keep class com.unionpay.** {*;}
-keep class com.UCMobile.PayPlugin.** {*;}
-keep class cn.gov.pbc.tsm.client.mobile.android.bank.service.** {*;}

#--------------ecopay2.0--------------
-keep class com.payeco.android.plugin.** {*;}
-keep class org.payeco.http.entity.mime.** {*;}
-dontwarn com.payeco.android.plugin.**

-keepclassmembers class com.payeco.android.plugin {
  public *;
}

-keepattributes *Annotation*
-keepattributes *JavascriptInterface*


#--------------sms--------------
-keep class com.iapppay.sms.** {*;}

#--------------alipay-------------
-keep class com.ta.utdid2.** {
    public <fields>;
    public <methods>;
}
-keep class com.ut.device.** {
    public <fields>;
    public <methods>;
}
-keep class com.alipay.android.app.** {
    public <fields>;
    public <methods>;
}
-keep class com.alipay.sdk.** {
    public <fields>;
    public <methods>;
}
-keep class com.alipay.mobilesecuritysdk.** {
    public <fields>;
    public <methods>;
}
-keep class HttpUtils.** {
    public <fields>;
    public <methods>;
}


#-----------keep iapppay-------------------
-keep class com.iapppay.account.channel.ipay.IpayAccountApi {*;}
-keep class com.iapppay.account.channel.ipay.IpayOpenidApi {*;}
-keep class com.iapppay.pay.channel.oneclickpay.OnekeyPayHandler {*;}
-keep class com.iapppay.utils.RSAHelper {*;}
-keep class com.iapppay.account.channel.ipay.view.** {
    public <fields>;
    public <methods>;
}
-keep class com.iapppay.sdk.main.** {
    public <fields>;
    public <methods>;
}
-keep class com.iapppay.interfaces.callback.** {*;}

-keep class com.iapppay.interfaces.** {
    public <fields>;
    public <methods>;
}


#iapppay UI
-keep public class com.iapppay.ui.activity.** {
    public <fields>;
    public <methods>;
}

-keep public class com.iapppay.fastpay.ui.** {
    public <fields>;
    public <methods>;
}

-keep public class com.iapppay.fastpay.view.** {
    public <fields>;
    public <methods>;
}

# View
-keep public class com.iapppay.ui.widget.**{
    public <fields>;
    public <methods>;
}

-keep public class com.iapppay.ui.view.**{
    public <fields>;
    public <methods>;
}


#iapppay_sub_pay
-keep public class com.iapppay.pay.channel.** {
    public <fields>;
    public <methods>;
}

-keep class com.iapppay.tool {*;}
-keep class com.iapppay.service {*;}
-keep class com.iapppay.provider {*;}
-keep class com.iapppay.apppaysystem {*;}

#openid sdk
-keep class com.iapppay.openid.channel.IpayOpenidApi {*;}
#<-----iappphy


#TalkingData
-keep public class com.tendcloud.** {  public protected *;}
-dontwarn com.tendcloud.**




-keep class * extends android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keep public class com.ipaynow.**{
   *;
}

-keepclasseswithmembers,allowshrinking class * {
    public <init>(android.content.Context,android.util.AttributeSet);
}

-keepclasseswithmembers,allowshrinking class * {
    public <init>(android.content.Context,android.util.AttributeSet,int);
}

# Also keep - Enumerations. Keep the special static methods that are required in
# enumeration classes.
-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep names - Native method names. Keep all native class/method names.
-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}

#环信
-keep class com.hyphenate.** {*;}
-dontwarn  com.hyphenate.**