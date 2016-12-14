package com.cdelg4do.waiterdroid.backgroundtasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.cdelg4do.waiterdroid.backgroundtaskhandler.BackgroundTaskRunnable;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;


// This class represents the task to load an bitmap associated to an url.
// Implements the BackgroundTaskRunnable interface to be executed by a BackgroundTaskHandler.
// ----------------------------------------------------------------------------

public class DownLoadImageTask implements BackgroundTaskRunnable {

    // Class attributes:
    public final static String taskId = "LOAD_IMAGE_TASK";

    public final static String IMAGEVIEW_WEAKREF_KEY = "ImageView_WeakReference";
    public final static String BITMAP_KEY = "Bitmap";


    // Object attributes:
    private boolean isCancelled;

    private URL imageUrl;
    private Map cache;
    private final WeakReference<ImageView> imageViewWeakRef;
    private Bitmap bitmap;


    // Constructor:
    public DownLoadImageTask(URL url, ImageView img, Map<String,Bitmap> existingCache) {

        imageUrl = url;
        cache = existingCache;
        imageViewWeakRef = new WeakReference<ImageView>(img);
        bitmap = null;

        isCancelled = false;
    }


    // Methods inherited from BackgroundTaskRunnable

    // The product of this task is a map containing two values:
    // the downloaded Bitmap and a weak reference to the ImageView we want to link the bitmap to.
    public Object getProduct() {

        HashMap<String,Object> product = new HashMap<String, Object>();

        product.put(IMAGEVIEW_WEAKREF_KEY,imageViewWeakRef);
        product.put(BITMAP_KEY,bitmap);

        return product;
    }

    // Returns the operation taskId (to identify what operation was executed)
    public String getId() {
        return taskId;
    }

    // Cancels the operation, if possible
    public void cancel() {
        isCancelled = true;
    }

    // This method represents the operation itself to be executed
    // (returns true if the operation succeeded, or false in any other case)
    public boolean execute() {

        if (imageUrl == null)
            return false;

        // Look for the bitmap in the cache (if we are using a cache)
        // If cannot get the bitmap from there, try to load from the url
        if (cache != null)
            bitmap = (Bitmap) cache.get(imageUrl.toString());

        if (bitmap == null) {

            try {

                InputStream in = OpenHttpGetConnection(imageUrl);
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
            }
            catch (Exception e) {

                Log.d("DownLoadImageTask", "ERROR: exception while retrieving non-cached bitmap");
                e.printStackTrace();
                return false;
            }

            if (cache != null)
                cache.put(imageUrl.toString(), bitmap);
        }

        return true;
    }


    // Auxiliary methods:

    // Opens a Http Get connection to a given url and returns its inputStream
    private InputStream OpenHttpGetConnection(URL url) throws IOException {

        InputStream inputStream = null;

        try {
            URLConnection conn = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpConn.getInputStream();
            }
        }
        catch (Exception ex) {
        }

        return inputStream;
    }

}

