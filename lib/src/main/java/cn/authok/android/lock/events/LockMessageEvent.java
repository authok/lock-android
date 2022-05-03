package cn.authok.android.lock.events;

import androidx.annotation.StringRes;

public class LockMessageEvent {

    @StringRes
    private final int messageRes;

    public LockMessageEvent(@StringRes int messageRes) {
        this.messageRes = messageRes;
    }

    @StringRes
    public int getMessageRes() {
        return messageRes;
    }
}
