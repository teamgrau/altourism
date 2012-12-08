package com.teamgrau.altourism;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
/**
 * User: thomaseichinger
 * Date: 12/5/12
 * Time: 2:42 PM
 */
public class BubbleDisplayer {
    private MainActivity activity;
    private MapView map;

    private MapController mapController;
    private List<Overlay> mapOverlays;
    private MyOverlay overlay;

    private MyOverlays overlays;
    private GeoPoint mapCenter;
    private int latSpan;
    private int lonSpan;
    private GeoPoint location;

    LinearLayout bubble;

    public BubbleDisplayer(MainActivity activity) {
        this.activity = activity;
        //map = (MapView) activity.findViewById(R.id.mapview);

        map.setBuiltInZoomControls(true);
        mapController = map.getController();
        mapOverlays = map.getOverlays();

        LayoutInflater inflater = activity.getLayoutInflater();
        bubble = (LinearLayout) inflater.inflate(R.layout.bubble, map, false);

        ImageButton bubbleClose = (ImageButton) bubble.findViewById(R.id.bubbleclose);
        bubbleClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Animation fadeOut = AnimationUtils.loadAnimation(BubbleDisplayer.this.activity, R.anim.fadeout);
                bubble.startAnimation(fadeOut);
                bubble.setVisibility(View.GONE);
            }
        });
    }

    public void display(GeoPoint location) {
        this.location = location;
        displayBubble();
    }

    public Activity getActivity() {
        return activity;
    }

    public void tapped() {
        displayBubble();
    }

    private void zoomToResultsOverlay(GeoPoint location) {
        calculateZoomExtents(location);
        mapController.zoomToSpan(latSpan, lonSpan);
        mapController.animateTo(mapCenter);
    }

    private void calculateZoomExtents(GeoPoint location) {

        /*List<Integer> lats = new ArrayList<Integer>();
        List<Integer> lons = new ArrayList<Integer>();

        lats.add(location.getLatitudeE6());
        lons.add(location.getLongitudeE6());

        /*latSpan = latMax - latMin;
        lonSpan = lonMax - lonMin;

        int latMid = latMin + (latSpan / 2);
        int lonMid = lonMin + (lonSpan / 2);*/

        mapCenter = location;
    }


    private void displayBubble() {

        map.removeView(bubble);
        bubble.setVisibility(View.GONE);

        TextView title = (TextView) bubble.findViewById(R.id.Title);
        title.setText("Welcome!");

        TextView description = (TextView) bubble.findViewById(R.id.Description);
        description.setText("Description here.");

        TextView text = (TextView) bubble.findViewById(R.id.Text);
        text.setText("This is your location, blablablba");

        MapView.LayoutParams params = new MapView.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                location, MapView.LayoutParams.BOTTOM_CENTER);

        bubble.setLayoutParams(params);

        map.addView(bubble);
        map.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        Runnable r = new Runnable() {
            public void run() {
                Animation fadeIn = AnimationUtils.loadAnimation(activity, R.anim.fadein);
                bubble.setVisibility(View.VISIBLE);
                bubble.startAnimation(fadeIn);
            }
        };

        /*Projection projection = map.getProjection();

        Point p = new Point();
        projection.toPixels(location, p);

        p.offset(0, -(bubble.getMeasuredHeight() / 2));
        GeoPoint target = projection.fromPixels(p.x, p.y);


        mapController.animateTo(target, r);*/

    }
}
