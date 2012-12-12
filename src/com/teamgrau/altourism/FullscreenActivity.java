package com.teamgrau.altourism;

import android.util.Log;
import android.widget.*;
import com.teamgrau.altourism.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
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
        implements AdapterView.OnItemSelectedListener
{
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
                                    .translationY(
                                            visible ? 0 : mControlsHeight )
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

        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();

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

        if (mMap!=null) mMap.setMyLocationEnabled(true);
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

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
        }
    }

    /**
     * Called when the Traffic checkbox is clicked.
     */
    public void onTrafficToggled(View view) {
        if (!checkReady()) {
            return;
        }
        mMap.setTrafficEnabled(((CheckBox) view).isChecked());
    }

    /**
     * Called when the MyLocation checkbox is clicked.
     */
    public void onMyLocationToggled(View view) {
        if (!checkReady()) {
            return;
        }
        mMap.setMyLocationEnabled(((CheckBox) view).isChecked());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (!checkReady()) {
            return;
        }
        Log.i("LDA", "item selected at position " + position + " with string "
                + (String) parent.getItemAtPosition(position));
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

    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
