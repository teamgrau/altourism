package com.teamgrau.altourism.util;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.teamgrau.altourism.R;

/**
 * User: thomaseichinger
 * Date: 12/12/12
 * Time: 2:17 PM
 */
public class AltourismInfoWindowAdapter extends Activity {
    // For testing:
    public static final LatLng GEN_MARKT = new LatLng(52.513609, 13.392119);
    public static final LatLng HUMB_UNI = new LatLng(52.517396, 13.394394);
    public static final LatLng ALT_MUS = new LatLng(52.519772, 13.398385);
    public static final LatLng D_DOM = new LatLng(52.518806, 13.400531);
    public static final LatLng HACK_MARKT = new LatLng(52.524159, 13.402376);
    public static final LatLng ALEXANDERPLATZ = new LatLng(52.522201, 13.412848);


    public AltourismInfoWindowAdapter() {
    }

    @Override

    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
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


    }
}
