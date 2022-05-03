package cn.authok.android.lock.internal.configuration;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static cn.authok.android.lock.internal.configuration.AuthMode.LOG_IN;
import static cn.authok.android.lock.internal.configuration.AuthMode.SIGN_UP;

@IntDef({LOG_IN, SIGN_UP})
@Retention(RetentionPolicy.SOURCE)
public @interface AuthMode {
    int LOG_IN = 0;
    int SIGN_UP = 1;
}
