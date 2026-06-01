# 人情记账 ProGuard 规则

# ======= WebView 相关 =======
-keepclassmembers class * extends android.webkit.WebView {
    public *;
}
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-keep class android.webkit.CookieManager { *; }
-keep class android.webkit.CookieSyncManager { *; }
-keep class android.webkit.WebSettings { *; }

# ======= Kotlin =======
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod
-keepclassmembers class kotlinx.coroutines.internal.MainDispatcherFactory {
    <fields>;
}
-keepclassmembers class kotlinx.coroutines.android.AndroidExceptionPreHandler {
    <methods>;
}

# ======= AndroidX =======
-keep class * implements androidx.viewbinding.ViewBinding {
    public static * inflate(android.view.LayoutInflater);
    public static * inflate(android.view.LayoutInflater, android.view.ViewGroup, boolean);
}

# ======= 序列化 =======
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    !static !transient <fields>;
}

# ======= 应用类 =======
-keep class com.situdai.giftledger.** { *; }

# ======= 网络 =======
-dontwarn java.net.**
-keep class java.net.HttpURLConnection { *; }
