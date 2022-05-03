# lock-android v2

## Parcelables
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

## Descriptor classes
-keep public class cn.authok.android.lock.events.*
-keep public class cn.authok.android.lock.adapters.Country
-keep public interface cn.authok.android.lock.internal.configuration.OAuthConnection
-keep public interface cn.authok.android.lock.views.interfaces.IdentityListener