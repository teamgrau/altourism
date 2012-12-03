package com.teamgrau.altourism;

import java.util.List;

import android.app.ActionBar;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

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
	
	@Override
	protected void onResume() {
		super.onResume();
	    locationOverlay.enableMyLocation();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	    locationOverlay.disableMyLocation();
	}

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

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
	    Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);
	    MyOverlay overlay = new MyOverlay(drawable, this);
	    
	    GeoPoint point = new GeoPoint(52521339,13411018);
	    OverlayItem overlayitem = new OverlayItem(point, "Tach auch!", "I'm on Alexanderplatz");
	    
	    overlay.addOverlay(overlayitem);
	    mapOverlays.add(overlay);
	    
	    locationOverlay = new MyLocationOverlay(this, mapView);
	    mapOverlays.add(locationOverlay);
	    
	    // call convenience method that zooms map on our location
        zoomToMyLocation();

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
     * This method zooms to the user's location with a zoom level of 10.
     */
    private void zoomToMyLocation() {
	    GeoPoint myLocationGeoPoint = locationOverlay.getMyLocation();
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
