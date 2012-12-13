package com.teamgrau.altourism;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.SystemClock;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.*;
import com.teamgrau.altourism.util.AltourismInfoWindowAdapter;
import com.teamgrau.altourism.util.SystemUiHider;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class FullscreenActivity extends android.support.v4.app.FragmentActivity
        implements AdapterView.OnItemSelectedListener, GoogleMap.OnMarkerClickListener,
                   GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerDragListener {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    private GoogleMap mMap;

    private Marker mAlex;
    private Marker mAltMus;
    private Marker mHumUni;
    private Marker mHackMarkt;
    private Marker mGenMarkt;
    private Marker mDDom;


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_fullscreen );

        final View controlsView = findViewById( R.id.fullscreen_content_controls );
        final View contentView = findViewById( R.id.fullscreen_content );

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance( this, contentView,
                HIDER_FLAGS );
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener( new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi( Build.VERSION_CODES.HONEYCOMB_MR2 )
                    public void onVisibilityChange( boolean visible )
                    {
                        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2 )
                        {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if ( mControlsHeight == 0 )
                            {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if ( mShortAnimTime == 0 )
                            {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime );
                            }
                            controlsView
                                    .animate()
                                    .translationY(visible ? 0 : mControlsHeight )
                                    .setDuration( mShortAnimTime );
                        }
                        else
                        {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            controlsView.setVisibility( visible ? View.VISIBLE
                                    : View.GONE );
                        }

                        if ( visible && AUTO_HIDE )
                        {
                            // Schedule a hide().
                            delayedHide( AUTO_HIDE_DELAY_MILLIS );
                        }
                    }
                } );

        Spinner spinner = (Spinner) findViewById(R.id.layers_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.layers_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);
        setUpMapIfNeeded();

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view )
            {
                if ( TOGGLE_ON_CLICK )
                {
                    mSystemUiHider.toggle();
                }
                else
                {
                    mSystemUiHider.show();
                }
            }
        } );

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById( R.id.layers_spinner ).setOnTouchListener(
                mDelayHideTouchListener );
    }

    @Override
    protected void onPostCreate( Bundle savedInstanceState )
    {
        super.onPostCreate( savedInstanceState );

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide( 100 );
    }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch( View view, MotionEvent motionEvent )
        {
            if ( AUTO_HIDE )
            {
                delayedHide( AUTO_HIDE_DELAY_MILLIS );
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run()
        {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide( int delayMillis )
    {
        mHideHandler.removeCallbacks( mHideRunnable );
        mHideHandler.postDelayed( mHideRunnable, delayMillis );
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (!checkReady()) {
            return;
        }
        Log.i("LDA", "item selected at position " + position + " with string "
                + parent.getItemAtPosition(position));
        setLayer((String) parent.getItemAtPosition(position));
    }

    private void setLayer(String layerName) {
        if (layerName.equals(getString(R.string.normal))) {
            mMap.setMapType(MAP_TYPE_NORMAL);
        } else if (layerName.equals(getString(R.string.hybrid))) {
            mMap.setMapType(MAP_TYPE_HYBRID);
        } else if (layerName.equals(getString(R.string.satellite))) {
            mMap.setMapType(MAP_TYPE_SATELLITE);
        } else if (layerName.equals(getString(R.string.terrain))) {
            mMap.setMapType(MAP_TYPE_TERRAIN);
        } else {
            Log.i("LDA", "Error setting layer with name " + layerName);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing.
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        // Hide the zoom controls as the button panel will cover it.
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);

        // Add lots of markers to the map.
        addMarkersToMap();

        // Setting an info window adapter allows us to change the both the contents and look of the
        // info window.
        mMap.setInfoWindowAdapter(new AltourismInfoWindowAdapter(this));

        // Set listeners for marker events.  See the bottom of this class for their behavior.
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerDragListener(this);

        // Pan to see all markers in view.
        // Cannot zoom to bounds until the map has a size.
        final View mapView = getSupportFragmentManager().findFragmentById(R.id.map).getView();
        if (mapView.getViewTreeObserver().isAlive()) {
            mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @SuppressLint("NewApi") // We check which build version we are using.
                @Override
                public void onGlobalLayout() {
                    LatLngBounds bounds = new LatLngBounds.Builder()
                            .include(AltourismInfoWindowAdapter.HUMB_UNI)
                            .include(AltourismInfoWindowAdapter.HACK_MARKT)
                            .include(AltourismInfoWindowAdapter.GEN_MARKT)
                            .include(AltourismInfoWindowAdapter.D_DOM)
                            .include(AltourismInfoWindowAdapter.ALEXANDERPLATZ)
                            .include(AltourismInfoWindowAdapter.ALT_MUS)
                            .build();
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        //noinspection deprecation
                        mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                }
            });
        }
    }

    private void addMarkersToMap() {
        mAlex = mMap.addMarker(new MarkerOptions()
                .position(AltourismInfoWindowAdapter.ALEXANDERPLATZ)
                .title("Alexanderplatz")
                .snippet("hhuhuhuhuhuuhuuuhuhu and even more hihihiihihihihiihihihi")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.altourism_pov)));

        // Uses a colored icon.
        mAltMus = mMap.addMarker(new MarkerOptions()
                .position(AltourismInfoWindowAdapter.ALT_MUS)
                .title("Altes Museum")
                .snippet("hhuhuhuhuhuuhuuuhuhu and even more hihihiihihihihiihihihi")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.altourism_pov)));

        // Uses a custom icon.
        mDDom = mMap.addMarker(new MarkerOptions()
                .position(AltourismInfoWindowAdapter.D_DOM)
                .title("Deutscher Dom")
                .snippet("hhuhuhuhuhuuhuuuhuhu and even more hihihiihihihihiihihihi")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.altourism_pov)));

        // Creates a draggable marker. Long press to drag.
        mGenMarkt = mMap.addMarker(new MarkerOptions()
                .position(AltourismInfoWindowAdapter.GEN_MARKT)
                .title("Gendarmen Markt")
                .snippet("hhuhuhuhuhuuhuuuhuhu and even more hihihiihihihihiihihihi")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.altourism_pov)));

        // A few more markers for good measure.
        mHackMarkt = mMap.addMarker(new MarkerOptions()
                .position(AltourismInfoWindowAdapter.HACK_MARKT)
                .title("Hackescher Markt")
                .snippet("hhuhuhuhuhuuhuuuhuhu and even more hihihiihihihihiihihihi")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.altourism_pov)));
        mHumUni = mMap.addMarker(new MarkerOptions()
                .position(AltourismInfoWindowAdapter.HUMB_UNI)
                .title("Humbold Universitaet")
                .snippet("hhuhuhuhuhuuhuuuhuhu and even more hihihiihihihihiihihihi")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.altourism_pov)));

        // Creates a marker rainbow demonstrating how to create default marker icons of different
        // hues (colors).
        int numMarkersInRainbow = 12;
        for (int i = 0; i < numMarkersInRainbow; i++) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(
                            -30 + 10 * Math.sin(i * Math.PI / (numMarkersInRainbow - 1)),
                            135 - 10 * Math.cos(i * Math.PI / (numMarkersInRainbow - 1))))
                    .title("Marker " + i)
                    .icon(BitmapDescriptorFactory.defaultMarker(i * 360 / numMarkersInRainbow)));
        }
    }

    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /** Called when the Clear button is clicked. */
    public void onClearMap(View view) {
        if (!checkReady()) {
            return;
        }
        mMap.clear();
    }

    /** Called when the Reset button is clicked. */
    public void onResetMap(View view) {
        if (!checkReady()) {
            return;
        }
        // Clear the map because we don't want duplicates of the markers.
        mMap.clear();
        addMarkersToMap();
    }

    public void onMyLocationToggled() {
        //mMap.setMyLocationEnabled(findViewById(R.id.my_location).isEnabled());
    }

    public void onTrafficToggled() {
        //mMap.setTrafficEnabled(findViewById(R.id.traffic).isEnabled());
    }

    //
    // Marker related listeners.
    //

    @Override
    public boolean onMarkerClick(final Marker marker) {
        // This causes the marker at Perth to bounce into position when it is clicked.
        if (marker.equals(mHumUni)) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            Projection proj = mMap.getProjection();
            Point startPoint = proj.toScreenLocation(AltourismInfoWindowAdapter.HUMB_UNI);
            startPoint.offset(0, -100);
            final LatLng startLatLng = proj.fromScreenLocation(startPoint);
            final long duration = 1500;

            final Interpolator interpolator = new BounceInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);
                    double lng = t * AltourismInfoWindowAdapter.HUMB_UNI.longitude + (1 - t) * startLatLng.longitude;
                    double lat = t * AltourismInfoWindowAdapter.HUMB_UNI.latitude + (1 - t) * startLatLng.latitude;
                    marker.setPosition(new LatLng(lat, lng));

                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    }
                }
            });
        }
        // We return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(getBaseContext(), "Click Info Window", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        //mTopText.setText("onMarkerDragStart");
        Toast.makeText(getBaseContext(), "onMarkerDragStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        //mTopText.setText("onMarkerDragEnd");
        Toast.makeText(getBaseContext(), "onMarkerDragEnd", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        //mTopText.setText("onMarkerDrag.  Current Position: " + marker.getPosition());
        Toast.makeText(getBaseContext(), "onMarkerDrag.  Current Position: " + marker.getPosition(), Toast.LENGTH_SHORT).show();
    }
}