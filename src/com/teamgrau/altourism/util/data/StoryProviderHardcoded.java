package com.teamgrau.altourism.util.data;

import android.location.Location;
import com.teamgrau.altourism.util.data.model.POI;
import com.teamgrau.altourism.util.data.model.Story;

import java.util.LinkedList;
import java.util.List;

/**
 * Testklasse für einen Storyprovider, der 6 verschiedene POI's liefert
 *
 * @author simon
 */
public class StoryProviderHardcoded implements StoryProvider {

    private List<POI> geschichten;

    @Override
    public List<POI> listPOIs(Location position, double radius) {
        return geschichten;
    }

    public POI getPOI(Location position) {
        return geschichten.get(0);
    }

    @Override
    public void listPOIs(Location position, double radius, OnStoryProviderFinishedListener l) {
        l.onStoryProviderFinished ( listPOIs ( position, radius ) );
    }

    public static final String THOMAS_LOCATION_PROVIDER = "Thomas LocationProvider";

    public StoryProviderHardcoded() {
        geschichten = new LinkedList<POI>();

        Location l = new Location(THOMAS_LOCATION_PROVIDER);
        l.setLatitude(52.455983);
        l.setLongitude(13.296985);
        POI poi = new POI("Institut für Informatik", l);
        poi.addStory(new Story("Smart Cities", "LNdW"));
        geschichten.add(poi);

        l = new Location(THOMAS_LOCATION_PROVIDER);
        l.setLatitude(52.456245);
        l.setLongitude(13.295521);
        poi = new POI("Fachbereich Physik", l);
        poi.addStory(new Story("Elektrosmog, Nanomaschinen und das geheimnisvolle Majorana-Teilchen", "LNdW"));
        geschichten.add(poi);

        l = new Location(THOMAS_LOCATION_PROVIDER);
        l.setLatitude(52.457639);
        l.setLongitude(13.297078);
        poi = new POI("Institut für Biologie", l);
        poi.addStory(new Story("Kleine Gehirne ganz groß", "LNdW"));
        geschichten.add(poi);

        l = new Location(THOMAS_LOCATION_PROVIDER);
        l.setLatitude(52.45159);
        l.setLongitude(13.289312);
        poi = new POI("Institut für Ethonologie", l);
        poi.addStory(new Story("Athropologie der Emotionen", "LNdW"));
        geschichten.add(poi);

        l = new Location(THOMAS_LOCATION_PROVIDER);
        l.setLatitude(52.453777);
        l.setLongitude(13.293628);
        poi = new POI("Institut für Chemie und Biochemie", l);
        poi.addStory(new Story("Die vielen leuchtenden Facetten der Chemie", "LNdW"));
        geschichten.add(poi);

        l = new Location(THOMAS_LOCATION_PROVIDER);
        l.setLatitude(52.456101);
        l.setLongitude(13.300623);
        poi = new POI("Institut für Prähistorische Archäologie", l);
        poi.addStory(new Story("Essen im Topf, Dach über dem Kopf", "LNdW"));
        geschichten.add(poi);

        l = new Location(THOMAS_LOCATION_PROVIDER);
        l.setLatitude(52.457945);
        l.setLongitude(13.301632);
        poi = new POI("Fachbereich Veterinärmedizin / Institute für Mikrobiologie und Tierseuchen sowie für Tier- und Umwelthygiene", l);
        poi.addStory(new Story("One Health - Bakterielle Infektionserreger auf ihrer Rundreise zwischen Tier und Mensch", "LNdW"));
        geschichten.add(poi);

        l = new Location(THOMAS_LOCATION_PROVIDER);
        l.setLatitude(52.452649);
        l.setLongitude(13.291136);
        poi = new POI("Fachbereich Erziehungswissenschaft und Psychologie", l);
        poi.addStory(new Story("Wie fit ist mein Gehirn", "LNdW"));
        geschichten.add(poi);

        l = new Location(THOMAS_LOCATION_PROVIDER);
        l.setLatitude(52.456676);
        l.setLongitude(52.456676);
        poi = new POI("Institut für Mathematik", l);
        poi.addStory(new Story("Mathematik steckt überall drin", "LNdW"));
        geschichten.add(poi);
    }

}
