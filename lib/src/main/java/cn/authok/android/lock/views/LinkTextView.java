package cn.authok.android.lock.views;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;

import cn.authok.android.lock.R;

/**
 * A custom AppCompatTextView implementation that changes the background drawable when focused.
 */
public class LinkTextView extends androidx.appcompat.widget.AppCompatTextView {
    private Drawable focusedBackground;

    public LinkTextView(@NonNull Context context) {
        super(context);
        init();
    }

    public LinkTextView(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LinkTextView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        focusedBackground = ContextCompat.getDrawable(getContext(), R.drawable.cn_authok_lock_link_background);
        setFocusableInTouchMode(false);
        setFocusable(true);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        ViewUtils.setBackground(this, gainFocus ? focusedBackground : null);
    }
}
