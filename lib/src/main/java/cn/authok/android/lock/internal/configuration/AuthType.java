package cn.authok.android.lock.internal.configuration;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static cn.authok.android.lock.internal.configuration.AuthType.DATABASE;
import static cn.authok.android.lock.internal.configuration.AuthType.ENTERPRISE;
import static cn.authok.android.lock.internal.configuration.AuthType.PASSWORDLESS;
import static cn.authok.android.lock.internal.configuration.AuthType.SOCIAL;

@IntDef({DATABASE, ENTERPRISE, PASSWORDLESS, SOCIAL})
@Retention(RetentionPolicy.SOURCE)
public @interface AuthType {
    int DATABASE = 0;
    int ENTERPRISE = 1;
    int PASSWORDLESS = 2;
    int SOCIAL = 3;
}
