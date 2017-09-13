#
#-libraryjars libs\alipaySDK-20150818.jar
#-libraryjars libs\android-support-v4.jar
#-libraryjars libs\gson-2.8.1.jar
#-libraryjars libs\QQConnect-opensdk-r5788-lite.jar
#-libraryjars libs\UPPayAssistEx.jar
#-libraryjars libs\UPPayPluginExPro.jar
#-libraryjars libs\wechat.jar

#忽略警告，避免打包时某些警告出现
-ignorewarnings
#指定压缩级别
-optimizationpasses 5
#包名不混合大小写
-dontusemixedcaseclassnames
#不跳过非公共的库的类
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
#不优化输入的类文件
-dontoptimize
#关闭预校验
-dontpreverify
#混淆时记录日志
-verbose
-printmapping proguardMapping.txt
#混淆时采用的算法
-optimizations !code/simplification/cast,!field/*,!class/merging/*
#保护注解
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
#保留行号
-keepattributes SourceFile,LineNumberTable
#keep相关注解
-keep class android.support.annotation.Keep
-keep @android.support.annotation.Keep class * {*;}
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.database.sqlite.SQLiteOpenHelper { public * ; }
-keep class android.support.** {*;}
#保持所有拥有本地方法的类名及本地方法名
-keepclasseswithmembernames class * {
    native <methods>;
}
#保持Activity中View及其子类入参的方法
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}
#枚举
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#保持自定义View的get和set相关方法
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#Parcelable
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#Fragment不需要在AndroidManifest.xml中注册，需要额外保护下
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Fragment
#保持所有实现Serializable接口的类成员
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}
-keepclassmembers class * {
    void *(**On*Event);
}
#webview
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}

-keep public class com.a8.zyfc.http.AsyncHttpRequest{ public *;}
-keep public class com.a8.zyfc.http.AsyncHttpResponseHandler{ public *;}
-keep public class com.a8.zyfc.http.BaseJsonHttpResponseHandler{ public *;}
-keep public class com.a8.zyfc.http.BinaryHttpResponseHandler{ public *;}
-keep public class com.a8.zyfc.http.Base64{ public *;}
-keep public class com.a8.zyfc.http.RequestHandle{ public *;}
-keep public class com.a8.zyfc.http.DataAsyncHttpResponseHandler{ public *;}
-keep public class com.a8.zyfc.http.FileAsyncHttpResponseHandler{ public *;}
-keep public class com.a8.zyfc.http.JsonHttpResponseHandler{ public *;}
-keep public class com.a8.zyfc.http.JsonStreamerEntity{ public *;}
-keep public class com.a8.zyfc.http.MySSLSocketFactory{ public *;}
-keep public class com.a8.zyfc.http.PersistentCookieStore{ public *;}
-keep public class com.a8.zyfc.http.PreemtiveAuthorizationHttpRequestInterceptor{ public *;}
-keep public class com.a8.zyfc.http.RangeFileAsyncHttpResponseHandler{ public *;}
-keep public class com.a8.zyfc.http.RequestParams{ public *;}
-keep public class com.a8.zyfc.http.AsyncHttpClient{ public *;}
-keep public class com.a8.zyfc.http.RetryHandler{ public *;}
-keep public class com.a8.zyfc.http.SimpleMultipartEntity{ public *;}
-keep public class com.a8.zyfc.http.TextHttpResponseHandler{ public *;}
-keep public class com.a8.zyfc.http.SyncHttpClient{ public *;}
-keep public class com.a8.zyfc.http.SerializableCookie{ public *;}
-keep public class com.a8.zyfc.http.ResponseHandlerInterface{ public *;}
-keep public class com.a8.zyfc.http.Base64OutputStream{ public *;}
-keep public class com.a8.zyfc.http.Base64DataException{ public *;}
-keep public class com.a8.zyfc.db.**{*;}
-keep public class com.a8.zyfc.A8SDKApi{ public *;}
-keep public class com.a8.zyfc.UserCallback{ public *;}
-keep public class com.a8.zyfc.PayCallback{ public *;}
-keep public class com.a8.zyfc.ShareCallBack{ public *;}
-keep public class com.a8.zyfc.model.**{ *;}
-keep public class com.a8.zyfc.pay.center.PaymentInfo{ public *;}
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}

#微信
-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.tencent.wxop.** {
   *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}

#银联
-keep  public class com.unionpay.uppay.net.HttpConnection {
	public <methods>;
}
-keep  public class com.unionpay.uppay.net.HttpParameters {
	public <methods>;
}
-keep  public class com.unionpay.uppay.model.BankCardInfo {
	public <methods>;
}
-keep  public class com.unionpay.uppay.model.PAAInfo {
	public <methods>;
}
-keep  public class com.unionpay.uppay.model.ResponseInfo {
	public <methods>;
}
-keep  public class com.unionpay.uppay.model.PurchaseInfo {
	public <methods>;
}
-keep  public class com.unionpay.uppay.util.DeviceInfo {
	public <methods>;
}
-keep  public class java.util.HashMap {
	public <methods>;
}
-keep  public class java.lang.String {
	public <methods>;
}
-keep  public class java.util.List {
	public <methods>;
}
-keep  public class com.unionpay.uppay.util.PayEngine {
	public <methods>;
	native <methods>;
}

-keep  public class com.unionpay.utils.UPUtils {
	native <methods>;
}
