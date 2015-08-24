package net.caiena.github.Util;

import android.support.annotation.UiThread;

public interface ActivityProgressUpdatable {

    @UiThread
    void updateProgressBar(final int progress,final int max);
}
