package com.cdelg4do.waiterdroid.backgroundtaskhandler;


// All classes to be executed by a background task must implement this.
// ----------------------------------------------------------------------------

public interface BackgroundTaskRunnable {

    // This is meant to return true if the execution was successful, or false in any other case.
    boolean execute();

    // This is meant to return an object with the results of the operation
    // (it should be casted to the appropriate class)
    Object getResult();

    // This is meant to indicate the task to be cancelled, if it is possible
    void cancel();

    // String that identifies the type of operation executed
    // (this is used by the BackgroundTaskListener object to handle the operation results)
    String operationId();
}
