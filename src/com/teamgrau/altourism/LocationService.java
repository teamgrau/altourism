package com.teamgrau.altourism;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.LocationSource;
import com.teamgrau.altourism.util.AltourismLocationSource;

/**
 * User: thomaseichinger
 * Date: 1/17/13
 * Time: 10:54 AM
 */
public class LocationService extends Service {
    private NotificationManager mNM;

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.location_service_started;

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    private LocationSource mLS;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        LocationService getService() {
            return LocationService.this;
        }
    }

    public LocationSource getLocationSource() {
        return mLS;
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();

        mLS = new AltourismLocationSource( getApplicationContext () );
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i ( "LocationService", "Received start id " + startId + ": " + intent );
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        //Toast.makeText ( this, R.string.local_service_stopped, Toast.LENGTH_SHORT ).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.location_service_started);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, FullscreenActivity.class), 0);

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification.Builder (getApplicationContext ())
                                                .setSmallIcon ( R.drawable.altourism_logo_positive_smal )
                                                .setTicker ( text )
                                                .setContentTitle ( getText ( R.string.location_service_label ) )
                                                .setContentText ( text )
                                                .setContentIntent ( contentIntent )
                                                .setOngoing ( true )
                                                .build ();

        //Notification notification = new Notification(R.drawable.altourism_logo_positive_smal, text,
        //        System.currentTimeMillis());

        // Set the info for the views that show in the notification panel.
        //notification.setLatestEventInfo(this, getText(R.string.location_service_label),
        //        text, contentIntent);

        // Send the notification.
        mNM.notify(NOTIFICATION, notification);
    }
}
