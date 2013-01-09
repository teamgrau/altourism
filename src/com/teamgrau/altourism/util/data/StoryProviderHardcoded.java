package com.teamgrau.altourism.util.data;

import java.util.LinkedList;
import java.util.List;

import android.location.Location;
import com.teamgrau.altourism.util.data.model.POI;

/**
 * Testklasse für einen Storyprovider, der 6 verschiedene POI's liefert
 * 
 * @author simon
 *
 */
public class StoryProviderHardcoded implements StoryProvider {

    private List<POI> geschichten;
	
	@Override
	public List<POI> listPOIs(Location position, double radius) {
		return geschichten;
	}

    public static final String THOMAS_LOCATION_PROVIDER = "Thomas LocationProvider";
	
	public StoryProviderHardcoded(){
        geschichten = new LinkedList<POI>();
        Location l = new Location(THOMAS_LOCATION_PROVIDER);
        l.setLatitude(52.513609);
        l.setLongitude(13.392119);
		geschichten.add(new POI("Gendarmenmarkt", l));
        l = new Location(THOMAS_LOCATION_PROVIDER);
        l.setLatitude(52.517396);
        l.setLongitude(13.398385);
		geschichten.add(new POI("Humboldt Uni", l));
        l = new Location(THOMAS_LOCATION_PROVIDER);
        l.setLatitude(52.519772);
        l.setLongitude(13.398385);
		geschichten.add(new POI("Altes Museum", l));
        l = new Location(THOMAS_LOCATION_PROVIDER);
        l.setLatitude(52.518806);
        l.setLongitude(13.400531);
		geschichten.add(new POI("Deutscher Dom", l));
        l = new Location(THOMAS_LOCATION_PROVIDER);
        l.setLatitude(52.524159);
        l.setLongitude(13.402376);
		geschichten.add(new POI("Hackescher Markt", l));
        l = new Location(THOMAS_LOCATION_PROVIDER);
        l.setLatitude(52.522201);
        l.setLongitude(13.412848);
		geschichten.add(new POI("Alexanderplatz", l));
	}

}
