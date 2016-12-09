package com.cdelg4do.waiterdroid.backgroundtaskhandler;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;


// This class represent a handler to execute operations in background.
// (the objects to execute must implement the BackgroundTaskRunnable interface)
// ----------------------------------------------------------------------------

public class BackgroundTaskHandler extends AsyncTask<Void,Integer,Boolean> {

    // Object attributes:
    private BackgroundTaskRunnable mTask;       // Object with the task to be executed on background
    private BackgroundTaskListener mListener;   // Listener to handle the results (if any)
    private ProgressDialog mProgress;           // Progress dialog to be shown
    private boolean mOperationFailed;           // Flag that indicates if the operation failed


    // Class constructors:

    // With progress dialog: if it is null, it will not be shown.
    // In other case, it must be already initialized and attached to a context.
    public BackgroundTaskHandler(BackgroundTaskRunnable task, BackgroundTaskListener listener, ProgressDialog progress) {

        mTask = task;
        mListener = listener;
        mProgress = progress;

        mOperationFailed = false;
    }

    // Without progress dialog (is the same than previous constructor, with progress = null)
    public BackgroundTaskHandler(BackgroundTaskRunnable task, BackgroundTaskListener listener) {

        mTask = task;
        mListener = listener;
        mProgress = null;

        mOperationFailed = false;
    }


    // Other methods:

    public boolean hasFailed() {
        return mOperationFailed;
    }

    public BackgroundTaskRunnable getTask() {
        return mTask;
    }

    public String operationId() {
        return mTask.operationId();
    }

    public Object getResult() {
        return mTask.getResult();
    }


    // Methods inherited from AsyncTask:

    // What to do on the UI thread BEFORE starting the background operation
    @Override
    protected void onPreExecute() {

        super.onPreExecute();

        if ( mProgress != null )
            mProgress.show();

        Log.d("BackgroundTaskHandler","INFO: Starting task " + mTask.operationId() );
    }

    // What to do DURING the background operation (on a background thread)
    @Override
    protected Boolean doInBackground(Void... params) {

        boolean success = mTask.execute();

        if ( !success )
            mOperationFailed = true;

        return success;
    }

    // What to do on the UI thread AFTER finishing the background operation (not cancelled)
    @Override
    protected void onPostExecute(Boolean result) {

        Log.d("BackgroundTaskHandler","INFO: Finished task " + mTask.operationId() );

        super.onPostExecute(result);

        if ( mProgress != null )
            mProgress.dismiss();

        mListener.onBackgroundTaskFinshed(this);
    }

    // What to do on the UI thread if the background operation was cancelled
    @Override
    protected void onCancelled() {

        Log.d("BackgroundTaskHandler","INFO: Cancelled task " + mTask.operationId() );

        mTask.cancel();
        mOperationFailed = true;

        if ( mProgress != null )
            mProgress.dismiss();

        mListener.onBackgroundTaskFinshed(this);
    }


}
