package com.teamgrau.altourism;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

import java.nio.BufferUnderflowException;

public class MainActivity extends MapActivity implements
        ActionBar.OnNavigationListener {
    /**
     * The serialization (saved instance state) Bundle key representing the
     * current dropdown position.
     */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    private MapController mapController;
    private MapView mapView;
    private LocationManager locationManager;
    private MyOverlays itemizedoverlay;
    private MyLocationOverlay myLocationOverlay;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main); // bind the layout to the activity

        // Configure the Map
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapView.setSatellite(true);
        mapController = mapView.getController();
        mapController.setZoom(16); // Zoom 1 is world view
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                0, new GeoUpdateHandler());

        myLocationOverlay = new MyLocationOverlay(this, mapView);
        mapView.getOverlays().add(myLocationOverlay);

        myLocationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
                mapView.getController().animateTo(myLocationOverlay.getMyLocation());
            }
        });

        Drawable drawable = this.getResources().getDrawable(R.drawable.altourism_user_position);
        itemizedoverlay = new MyOverlays(this, drawable);
        createMarker();

        BubbleDisplayer bubble = new BubbleDisplayer(this);
        bubble.display(myLocationOverlay.getMyLocation());

        // Set up the action bar to show a dropdown list.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
                // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter<String>(actionBar.getThemedContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1, new String[] {
                        getString(R.string.title_section1),
                        getString(R.string.title_section2),
                        getString(R.string.title_section3), }), this);/**/

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Welcome from Altourism beta");
        dialog.setMessage("You will be shown your position, blablablablablablablabla");
        dialog.show();
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    public class GeoUpdateHandler implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            int lat = (int) (location.getLatitude() * 1E6);
            int lng = (int) (location.getLongitude() * 1E6);
            GeoPoint point = new GeoPoint(lat, lng);
            createMarker();
            mapController.animateTo(point); // mapController.setCenter(point);
            Toast.makeText(getBaseContext(), lat + " " + lng, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    private void createMarker() {
        GeoPoint p = mapView.getMapCenter();
        OverlayItem overlayitem = new OverlayItem(p, "", "");
        itemizedoverlay.addOverlay(overlayitem);
        if (itemizedoverlay.size() > 0) {
            mapView.getOverlays().add(itemizedoverlay);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.enableCompass();
    }

    @Override
    protected void onPause() {
        super.onPause();
        myLocationOverlay.disableMyLocation();
        myLocationOverlay.disableCompass();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore the previously serialized current dropdown position.
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Serialize the current dropdown position.
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
                .getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        // When the given dropdown item is selected, show its contents in the
        // container view.
        android.app.Fragment fragment = new DummySectionFragment();
        Bundle args = new Bundle();
        args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
        fragment.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.container, fragment);
        return true;
    }

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
	public static class DummySectionFragment extends android.app.Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			textView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return textView;
		}
	}
}

/*import java.util.List;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MainActivity extends MapActivity implements
		ActionBar.OnNavigationListener {

	private MyLocationOverlay locationOverlay = null;
	private MapView mapView = null;
    private LocationManager mlocationManager;
    private LocationListener mlistener;
    private Location mlocation;

    @Override
	protected void onResume() {
		super.onResume();
	    //locationOverlay.enableMyLocation();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	    //locationOverlay.disableMyLocation();
	}

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
/*	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    mapView.setSatellite(true);
	    MapController controller = mapView.getController();
	    controller.animateTo(new GeoPoint(52521339,13411018));
	    controller.setZoom(18);
	    
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    Drawable drawable = this.getResources().getDrawable(R.drawable.altourism_user_position);
	    MyOverlay overlay = new MyOverlay(drawable, this);
	    
	    GeoPoint point = new GeoPoint(52521339,13411018);
	    OverlayItem overlayitem = new OverlayItem(point, "Tach auch!", "I'm on Alexanderplatz");
	    
	    overlay.addOverlay(overlayitem);
	    mapOverlays.add(overlay);

        mlocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = mlocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!gpsEnabled) {
            // Build an alert dialog here that requests that the user enable
            // the location services, then when the user clicks the "OK" button,
            // call enableLocationSettings()
            DialogFragment d = new DialogFragment() {
                @Override
                public Dialog onCreateDialog(Bundle savedInstanceState) {
                    // Use the Builder class for convenient dialog construction
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.gps_not_available)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(settingsIntent);
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    // Create the AlertDialog object and return it
                    return builder.create();
                }

                @Override
                public void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    this.show(getFragmentManager(), "GPSErrorDialogFragment");
                }
            };
        }
        LocationProvider provider = mlocationManager.getProvider(LocationManager.NETWORK_PROVIDER);
        mlistener = new AltourismLocationListener(this);

        mlocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                10000,          // 10-second interval.
                10,             // 10 meters.
                mlistener);
	    
	    //locationOverlay = new MyLocationOverlay(this, mapView);
	    //mapOverlays.add(locationOverlay);


		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, new String[] {
								getString(R.string.title_section1),
								getString(R.string.title_section2),
								getString(R.string.title_section3), }), this);/**/
/*	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
		android.app.Fragment fragment = new DummySectionFragment();
		Bundle args = new Bundle();
		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
		fragment.setArguments(args);
		getFragmentManager().beginTransaction().replace(R.id.container, fragment);
		return true;
	}
	
	/**
     * This method zooms to the user's location with a zoom level of 10.
     */
/*    public void zoomToMyLocation(int lat, int lon) {
	    GeoPoint myLocationGeoPoint = new GeoPoint(lat, lon);
	    if(myLocationGeoPoint != null) {
	    	Toast.makeText(this, myLocationGeoPoint.getLatitudeE6()+myLocationGeoPoint.getLongitudeE6(), Toast.LENGTH_LONG).show();
            mapView.getController().animateTo(myLocationGeoPoint);
            mapView.getController().setZoom(10);
	    }
	    else {
            Toast.makeText(this, "Cannot determine location", Toast.LENGTH_LONG).show();
    }
    }

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
/*	public static class DummySectionFragment extends android.app.Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
/*		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			textView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return textView;
		}
	}

}*/
