# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# Retrofit2
    -ignorewarnings
	-dontwarn retrofit2.**
    -dontwarn javax.annotation.**
	-keep class retrofit2.** { *; }
	-keep class retrofit2.**
	-keep class com.base.frame.net.retrofit.converter.gson.**{ *; }
	-keep class com.base.frame.net.retrofit.converter.gson.**
    -keep class com.rejuvee.smartelectric.family.model.bean.**{*;}
	-keepnames class com.rejuvee.smartelectric.family.api.**{ *; }
    -keepnames class com.base.frame.net.**{ *; }
    -keep class com.rejuvee.smartelectric.family.model.nativedb.** {*;}
	-keepattributes Signature
	-keepattributes Exceptions
    -keepattributes *Annotation*
    -keep class * extends java.lang.annotation.Annotation { *; }
# OkHttp
	-dontwarn okhttp3.**
	-keep class okhttp3.** { *; }
	-dontwarn com.squareup.okhttp.**
	-keep class com.squareup.okhttp.** { *; }
	-dontwarn okio.**
	-dontwarn bean.**
	-dontwarn core.**

	-keep class com.daimajia.** {*;}
	-dontwarn com.daimajia.**
    -keep class android.** {*;}
    -dontwarn android.**
# UMeng
    -keepclassmembers class * {
       public <init> (org.json.JSONObject);
    }
    -keep public class com.rejuvee.smartelectric.family.R$*{
        public static final int *;
    }
    -keepclassmembers enum * {
        public static **[] values();
        public static ** valueOf(java.lang.String);
    }
#EventBus
    -keepattributes *Annotation*
    -keepclassmembers class ** {
        @org.greenrobot.eventbus.Subscribe <methods>;
    }
    -keep enum org.greenrobot.eventbus.ThreadMode { *; }
#RxJava
    -dontwarn rx.**
    -keepclassmembers class rx.** { *; }
# retrolambda
    -dontwarn java.lang.invoke.*
# WeixinSdk
    -keep class com.tencent.mm.opensdk.** {
    *;
    }

    -keep class com.tencent.wxop.** {
    *;
    }

    -keep class com.tencent.mm.sdk.** {
    *;
    }
#QQLogin
   -keep class * extends android.app.Dialog
   -keep class com.rejuvee.smartelectric.family.wxapi.** { *; }
   #-keep class com.rejuvee.smartelectric.family.MainMessageReceiverer

#ali push
   -keepclasseswithmembernames class ** {
       native <methods>;
   }
   -keepattributes Signature
   -keep class sun.misc.Unsafe { *; }
   -keep class com.taobao.** {*;}
   -keep class com.alibaba.** {*;}
   -keep class com.alipay.** {*;}
   -keep class com.ut.** {*;}
   -keep class com.ta.** {*;}
   -keep class anet.**{*;}
   -keep class anetwork.**{*;}
   -keep class org.android.spdy.**{*;}
   -keep class org.android.agoo.**{*;}
   -keep class android.os.**{*;}
   -dontwarn com.taobao.**
   -dontwarn com.alibaba.**
   -dontwarn com.alipay.**
   -dontwarn anet.**
   -dontwarn org.android.spdy.**
   -dontwarn org.android.agoo.**
   -dontwarn anetwork.**
   -dontwarn com.ut.**
   -dontwarn com.ta.**
   #-dontwarn androidx.renderscript.**
   -keep class androidx.renderscript.** { *; }