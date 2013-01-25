package com.teamgrau.altourism.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.teamgrau.altourism.FullscreenActivity;
import com.teamgrau.altourism.R;
import com.teamgrau.altourism.util.data.StoryProvider;
import com.teamgrau.altourism.util.data.StoryProviderHardcoded;
import com.teamgrau.altourism.util.data.model.POI;
import com.teamgrau.altourism.util.data.model.Story;

import java.util.List;

/**
 * User: thomaseichinger
 * Date: 12/12/12
 * Time: 2:17 PM
 */
public class AltourismInfoWindowAdapter extends Activity{
    // For testing:
    public static final LatLng GEN_MARKT = new LatLng(52.513609,13.392119);
    public static final LatLng HUMB_UNI = new LatLng(52.517396, 13.394394);
    public static final LatLng ALT_MUS = new LatLng(52.519772, 13.398385);
    public static final LatLng D_DOM = new LatLng(52.518806, 13.400531);
    public static final LatLng HACK_MARKT = new LatLng(52.524159, 13.402376);
    public static final LatLng ALEXANDERPLATZ = new LatLng(52.522201,13.412848);



    public AltourismInfoWindowAdapter(){}

    @Override

    protected void onCreate( Bundle savedInstance ){
        super.onCreate( savedInstance );
        Bundle myBundle = getIntent().getExtras();
        setContentView(R.layout.altourism_info_window);

        Log.d("Altourism beta", String.valueOf(myBundle.getDouble("Lat")));
    }

    private void render(Marker marker, View view) {
        int badge;
        // Use the equals() method on a Marker to check for equals.  Do not use ==.
        badge = R.drawable.altourism_pov;
        ((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);

        String title = marker.getTitle();
        TextView titleUi = ((TextView) view.findViewById(R.id.title));
        if (title != null) {
            // Spannable string allows us to edit the formatting of the text.
            SpannableString titleText = new SpannableString(title);
            titleText.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, titleText.length(), 0);
            titleText.toString().toUpperCase();
            titleUi.setText(titleText);
            titleUi.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/miso-bold.otf"));
        } else {
            titleUi.setText("");
        }

        String snippet = marker.getSnippet();
        TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
        if (snippet != null) {
            SpannableString snippetText = new SpannableString(snippet);
            snippetText.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, snippetText.length(), 0);

            //snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12, 21, 0);
            snippetUi.setText(snippetText);
            snippetUi.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/miso-light.otf"));
        } else {
            snippetUi.setText("");
        }


        // Now we setup the Story list and show it in the InfoWindow
        ExpandableListView ev = (ExpandableListView) findViewById(R.id.expandableListView);
        ev.setAdapter(new BaseExpandableListAdapter() {
            final StoryProvider sp = new StoryProviderHardcoded();
            final List<POI> pois = sp.listPOIs(new Location("Simon"), 0.0);
            final List<Story> geschichten = pois.get(0).getStories();

            @Override
            public int getGroupCount() {
                return geschichten.size();
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return 1;
            }

            @Override
            public Object getGroup(int groupPosition) {
                return geschichten.get(groupPosition).getStoryText().substring(0, 20);
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                return geschichten.get(groupPosition).getStoryText();
            }

            @Override
            public long getGroupId(int groupPosition) {
                return groupPosition;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
                LinearLayout l = new LinearLayout(getBaseContext());
                l.setOrientation(LinearLayout.HORIZONTAL);
                l.setLayoutParams(new AbsListView.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                l.setGravity(Gravity.CENTER);
                ImageView iv = new ImageView(getBaseContext());
                iv.setLayoutParams(new AbsListView.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                iv.setScaleX(0.5f);
                iv.setScaleY(0.5f);
                iv.setImageResource(R.drawable.altourism_hcc_story_open);

                TextView tv = new TextView(getBaseContext());
                tv.setText(getGroup(groupPosition).toString());
                tv.setLayoutParams(new AbsListView.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)); // same heigth as the expand-arrow
                tv.setPadding(5, 0, 0, 0);
                tv.setSingleLine(true);

                l.addView(iv);
                l.addView(tv);
                return l;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                TextView tv = new TextView(getBaseContext());
                tv.setLayoutParams(new AbsListView.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                tv.setText(getChild(groupPosition, childPosition).toString());
                return tv;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return false;
            }
        });
    }
}
