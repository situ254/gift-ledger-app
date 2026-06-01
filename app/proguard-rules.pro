# 混淆规则配置

# 保留 WebView 相关类
-keepclassmembers class * extends android.webkit.WebView {
    public *;
}

# 保留 JavaScript 接口
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# 保留 CookieManager
-keep class android.webkit.CookieManager { *; }
-keep class android.webkit.CookieSyncManager { *; }

# 保留 WebSettings
-keep class android.webkit.WebSettings { *; }

# 保留 Kotlin 协程
-keepclassmembernames class kotlinx.coroutines.internal.MainDispatcherFactory {
    accessible synthetic <fields>;
}
-keepclassmembernames class kotlinx.coroutines.android.AndroidExceptionPreHandler {
    accessible synthetic <methods>;
}

# 通用规则
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 保留 ViewBinding
-keep class * implements androidx.viewbinding.ViewBinding {
    public static * inflate(android.view.LayoutInflater);
    public static * inflate(android.view.LayoutInflater, android.view.ViewGroup, boolean);
}
