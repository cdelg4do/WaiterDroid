package com.cdelg4do.waiterdroid.backgroundtaskhandler;


// All classes to process the result of a background task must implement this.
// ----------------------------------------------------------------------------

public interface BackgroundTaskListener {

    void onBackgroundTaskFinished(BackgroundTaskHandler taskHandler);
}
