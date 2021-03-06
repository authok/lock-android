package cn.authok.android.lock.internal.configuration;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static cn.authok.android.lock.internal.configuration.PasswordStrength.EXCELLENT;
import static cn.authok.android.lock.internal.configuration.PasswordStrength.FAIR;
import static cn.authok.android.lock.internal.configuration.PasswordStrength.GOOD;
import static cn.authok.android.lock.internal.configuration.PasswordStrength.LOW;
import static cn.authok.android.lock.internal.configuration.PasswordStrength.NONE;

@IntDef({NONE, LOW, FAIR, GOOD, EXCELLENT})
@Retention(RetentionPolicy.SOURCE)
public @interface PasswordStrength {
    int NONE = 0;
    int LOW = 1;
    int FAIR = 2;
    int GOOD = 3;
    int EXCELLENT = 4;
}