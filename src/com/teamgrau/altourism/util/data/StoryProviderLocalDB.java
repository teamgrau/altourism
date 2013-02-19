package com.teamgrau.altourism.util.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;
import com.teamgrau.altourism.util.data.database.AltourismDBHelper;
import com.teamgrau.altourism.util.data.database.DBDefinition;
import com.teamgrau.altourism.util.data.model.POI;
import com.teamgrau.altourism.util.data.model.Story;

import java.util.ArrayList;
import java.util.List;

/**
 * User: simon
 */
public class StoryProviderLocalDB implements StoryProvider {

    AltourismDBHelper AlDBHelper;

    // define the columns to return on a POI Query
    String[] POIProjection = {
            DBDefinition.POI.COLUMN_NAME_Geschichte,
            DBDefinition.POI.COLUMN_NAME_Title,
            DBDefinition.POI.COLUMN_NAME_Lat,
            DBDefinition.POI.COLUMN_NAME_Lng };


    public StoryProviderLocalDB( Context context ) {
        AlDBHelper = new AltourismDBHelper( context );
    }

    @Override
    // Does currently not take radius into account but returns all POI that are stored
    public List<POI> listPOIs( Location position, double radius ) {
        SQLiteDatabase db = AlDBHelper.getReadableDatabase();
        String sortOrder = DBDefinition.PositionEntry._ID + " ASC";
        Cursor c = db.query(
                DBDefinition.POI.TABLE_NAME,    // The table to query
                POIProjection,                  // The columns to return
                null,                                // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder,                                // The sort order
                null                                      // only return the n last points
        );

        c.moveToFirst();
        List<POI> list = new ArrayList<POI>();
        int n = c.getCount();
        String text;
        String title;
        POI poi;
        Location l;
        double Lat;
        double Lng;
        for (int i = 1; i <= n; ++i) {
            Lat = AltourismDBHelper.dbToDouble( c.getLong( c.getColumnIndex( DBDefinition.POI.COLUMN_NAME_Lat )));
            Lng = AltourismDBHelper.dbToDouble( c.getLong( c.getColumnIndex( DBDefinition.POI.COLUMN_NAME_Lng )));
            text = c.getString( c.getColumnIndex( DBDefinition.POI.COLUMN_NAME_Geschichte ));
            title = c.getString( c.getColumnIndex( DBDefinition.POI.COLUMN_NAME_Title ));
            l = new Location( "Thomas LocationProvider" );
            l.setLatitude( Lat );
            l.setLongitude( Lng );
            poi = new POI("test Title", l );
            poi.addStory( new Story( text, title ) );
            list.add( poi );
        }
        c.close();
        db.close();
        return list;
    }


    @Override
    public POI getPOI( Location position ) {
        SQLiteDatabase db = AlDBHelper.getReadableDatabase();
        String sortOrder = DBDefinition.PositionEntry._ID + " ASC";
        String selection =
                DBDefinition.POI.COLUMN_NAME_Lat + " =" + AltourismDBHelper.DoubleToDB( position.getLatitude() ) +
                        " AND " + DBDefinition.POI.COLUMN_NAME_Lng + " =" +
                        AltourismDBHelper.DoubleToDB( position.getLongitude() );

        Cursor c = db.query(
                DBDefinition.POI.TABLE_NAME,    // The table to query
                POIProjection,                  // The columns to return
                selection,                                // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder,                                // The sort order
                null                                      // only return the n last points
        );

        c.moveToFirst();
        POI poi = new POI( "test Title", position );
        int n = c.getCount();
        Log.d("Altourism beta", "stories got: " + n + "\n" + position.getLatitude() + "," + position.getLongitude());
        String text;
        String title;
        for (int i = 1; i <= n; ++i) {
            text = c.getString( c.getColumnIndex( DBDefinition.POI.COLUMN_NAME_Geschichte ));
            title = c.getString( c.getColumnIndex( DBDefinition.POI.COLUMN_NAME_Title ));
            poi.addStory( new Story( text, title ));
            c.moveToNext();       // we can move 1 past the last entry w/o negative effects
        }
        c.close();
        db.close();
        return poi;
    }


    @Override
    public void listPOIs( Location position, double radius, OnStoryProviderFinishedListener l ) {
        l.onStoryProviderFinished ( listPOIs ( position, radius ) );
    }
}
