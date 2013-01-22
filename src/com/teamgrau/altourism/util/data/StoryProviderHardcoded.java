package com.teamgrau.altourism.util.data;

import java.util.LinkedList;
import java.util.List;

import android.location.Location;
import com.teamgrau.altourism.util.data.model.POI;
import com.teamgrau.altourism.util.data.model.Story;

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

    public POI getPOI(Location position){
        return geschichten.get(0);
    }

    @Override
    public void listPOIs ( Location position, double radius, OnStoryProviderFinishedListener l ) {
        return;
    }

    public static final String THOMAS_LOCATION_PROVIDER = "Thomas LocationProvider";
	
	public StoryProviderHardcoded(){
        geschichten = new LinkedList<POI>();

        Location l = new Location(THOMAS_LOCATION_PROVIDER);
        l.setLatitude(52.513609);
        l.setLongitude(13.392119);
        POI poi = new POI("Gendarmenmarkt", l);
        poi.addStory(new Story("Auch andere tolle Sachen passieren hier"));
        poi.addStory(new Story("Waehrend der Gauklerfestspiele ist die Kirche hier abends mit einem herrlichen Licht angestrahlt"));
		geschichten.add(poi);

        l = new Location(THOMAS_LOCATION_PROVIDER);
        l.setLatitude(52.517396);
        l.setLongitude(13.398385);
        poi = new POI("Humboldt Uni", l);
        poi.addStory(new Story("Gegenüber am Reiterstandbild kann man wirklich interessante 'Gravierungen' auf dem Sockel lesen"));
		geschichten.add(poi);

        l = new Location(THOMAS_LOCATION_PROVIDER);
        l.setLatitude(52.519772);
        l.setLongitude(13.398385);
        poi = new POI("Altes Museum", l);
        poi.addStory(new Story("Die große Granitwanne vor dem Museum stammt aus einem größeren Findling in der Nähe " +
                " von Angermünde. Eine ineterssante Geschichte steckt dahinter"));
		geschichten.add(poi);

        l = new Location(THOMAS_LOCATION_PROVIDER);
        l.setLatitude(52.518806);
        l.setLongitude(13.400531);
        poi = new POI("Deutscher Dom", l);
        poi.addStory(new Story("Der Dom ist grundsätzlich für Besucher geoeffnet (kostenlos). Mindestens einen Guck wert!"));
		geschichten.add(poi);

        l = new Location(THOMAS_LOCATION_PROVIDER);
        l.setLatitude(52.524159);
        l.setLongitude(13.402376);
        poi = new POI("Hackescher Markt", l);
        poi.addStory(new Story("Am westlichen Durchgang unter den Schienen kann man die lustigsten Poster sehen."));
		geschichten.add(poi);

        l = new Location(THOMAS_LOCATION_PROVIDER);
        l.setLatitude(52.522201);
        l.setLongitude(13.412848);
        poi = new POI("Alexanderplatz", l);
        poi.addStory(new Story("Innerhalb einer Stunde treffen sich an der Weltzeituhr ca 100 Gruppen. Zählt bei " +
                "einem Kaffee mal mit!"));
		geschichten.add(poi);
	}

}
