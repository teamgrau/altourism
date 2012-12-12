package com.teamgrau.altourism.util;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.teamgrau.altourism.FullscreenActivity;
import com.teamgrau.altourism.R;

/**
 * User: thomaseichinger
 * Date: 12/12/12
 * Time: 2:17 PM
 */
public class AltourismInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    //private final RadioGroup mOptions;
    // For testing:
    public static final LatLng GEN_MARKT = new LatLng(52.513609,13.392119);
    public static final LatLng HUMB_UNI = new LatLng(52.517396, 13.394394);
    public static final LatLng ALT_MUS = new LatLng(52.519772, 13.398385);
    public static final LatLng D_DOM = new LatLng(52.518806, 13.400531);
    public static final LatLng HACK_MARKT = new LatLng(52.524159, 13.402376);
    public static final LatLng ALEXANDERPLATZ = new LatLng(52.522201,13.412848);

    // These a both viewgroups containing an ImageView with id "badge" and two TextViews with id
    // "title" and "snippet".
    private final View mWindow;
    private final View mContents;

    public AltourismInfoWindowAdapter(FullscreenActivity activity) {
        mWindow = activity.getLayoutInflater().inflate(R.layout.altourism_info_window, null);
        mContents = activity.getLayoutInflater().inflate(R.layout.altourism_info_window, null);
        //mOptions = (RadioGroup) activity.findViewById(R.id.custom_info_window_options);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        /*if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_window) {
            // This means that getInfoContents will be called.
            return null;
        }*/
        render(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        /*if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_contents) {
            // This means that the default info contents will be used.
            return null;
        }*/
        render(marker, mContents);
        return mContents;
    }

    private void render(Marker marker, View view) {
        int badge;
        // Use the equals() method on a Marker to check for equals.  Do not use ==.
        badge = R.drawable.ic_launcher;
        ((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);

        String title = marker.getTitle();
        TextView titleUi = ((TextView) view.findViewById(R.id.title));
        if (title != null) {
            // Spannable string allows us to edit the formatting of the text.
            SpannableString titleText = new SpannableString(title);
            titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
            titleUi.setText(titleText);
        } else {
            titleUi.setText("");
        }

        String snippet = marker.getSnippet();
        TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
        if (snippet != null) {
            SpannableString snippetText = new SpannableString(snippet);
            snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 10, 0);
            snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12, 21, 0);
            snippetUi.setText(snippetText);
        } else {
            snippetUi.setText("");
        }
    }
}
