package cn.authok.android.lock.errors;

import androidx.annotation.NonNull;

import cn.authok.android.AuthokException;

public interface ErrorMessageBuilder<U extends AuthokException> {

    @NonNull
    AuthenticationError buildFrom(@NonNull U exception);
}
