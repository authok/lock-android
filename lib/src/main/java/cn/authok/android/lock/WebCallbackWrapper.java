package cn.authok.android.lock;

import androidx.annotation.NonNull;

import cn.authok.android.authentication.AuthenticationException;
import cn.authok.android.callback.Callback;
import cn.authok.android.provider.AuthCallback;
import cn.authok.android.result.Credentials;


/**
 * Internal class, meant to wrap a {@link AuthCallback} instance and expose it as a {@link Callback}
 */
class WebCallbackWrapper implements Callback<Credentials, AuthenticationException> {

    private final AuthCallback baseCallback;

    public WebCallbackWrapper(@NonNull AuthCallback callback) {
        this.baseCallback = callback;
    }

    @Override
    public void onFailure(@NonNull AuthenticationException error) {
        baseCallback.onFailure(error);
    }

    @Override
    public void onSuccess(@NonNull Credentials credentials) {
        baseCallback.onSuccess(credentials);
    }
}
