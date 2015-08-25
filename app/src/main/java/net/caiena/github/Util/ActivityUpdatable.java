package net.caiena.github.Util;

import android.graphics.Bitmap;
import android.support.annotation.UiThread;

public interface ActivityUpdatable {

    @UiThread
    void updateProgressBar(final int progress,final int max);

    @UiThread
    void updateInfoActivity(final Bitmap circleBitmap,final String name,final String html);

    @UiThread
    void updateError();
}
