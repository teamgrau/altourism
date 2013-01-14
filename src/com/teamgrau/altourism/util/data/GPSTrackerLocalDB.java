package com.teamgrau.altourism.util.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.teamgrau.altourism.util.Angle;
import com.teamgrau.altourism.util.Distance;
import com.teamgrau.altourism.util.data.database.AltourismDBHelper;
import com.teamgrau.altourism.util.data.database.DBDefinition;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Stores and retrieves GPS-locations in a local database
 * When calling the constructor the database will be set up if necessary.
 * This can potentially take some time so don't do that on startup.
 *
 * User: simon
 */
public class GPSTrackerLocalDB implements GPSTracker {

    AltourismDBHelper AlDBHelper;

    Stack<Location> last2;

    // define the columns to return on a PositionEntry Query
    String[] PositionEntryProjection = {
            DBDefinition.PositionEntry.COLUMN_NAME_Lat,
            DBDefinition.PositionEntry.COLUMN_NAME_Lng
    };


    public GPSTrackerLocalDB( Context context ){
        AlDBHelper = new AltourismDBHelper( context );

        last2 = new Stack<Location>();
        // init for proper functionality
        for (Location l : getLocations(2))
            last2.push(l);
    }

    @Override
    public void addLocation( Location position ) {
        // only add current location when it satisfies some criteria
        if(!relevant(position)) return;

        // a key-value map for the insert-method
        ContentValues values = new ContentValues();
        values.put( DBDefinition.PositionEntry.COLUMN_NAME_Lat, position.getLatitude() );
        values.put( DBDefinition.PositionEntry.COLUMN_NAME_Lng, position.getLongitude() );

        SQLiteDatabase db = AlDBHelper.getWritableDatabase();
        long newRowId;
        // the second argument null makes the framework insert no row if values is empty
        newRowId = db.insert( DBDefinition.PositionEntry.TABLE_NAME, null, values );
        db.close();

    }

    private boolean relevant(Location position) {
        // distance to the last location must be >= 20 meters and the angle of the current
        // vector to the last must exceed 10 radiants
        return ((Distance.calculateDistance(position, last2.peek(), Distance.KILOMETERS)>=0.02)
            && (Angle.computeAngle(position, last2.elementAt(1), last2.elementAt(0))>=10))
            ? true : false;
    }

    @Override
    public List<Location> getLocations( int n ) {

        // sort descending
        String sortOrder = DBDefinition.PositionEntry._ID + " DESC";

        // only return the n last points (sorted descending)
        String limit = "LIMIT 0, " + n;

        SQLiteDatabase db = AlDBHelper.getReadableDatabase();
        Cursor c = db.query(
                DBDefinition.PositionEntry.TABLE_NAME,    // The table to query
                PositionEntryProjection,                  // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder,                                // The sort order
                limit                                     // only return the n last points (sorted descending)
        );

        c.moveToFirst();
        Location l;
        List<Location> list = new ArrayList<Location>();
        for(int i = 1; i <= n; ++i){
            double Lat = c.getDouble( c.getColumnIndex( DBDefinition.PositionEntry.COLUMN_NAME_Lat ));
            double Lng = c.getDouble( c.getColumnIndex( DBDefinition.PositionEntry.COLUMN_NAME_Lng ));
            l = new Location( "Thomas LocationProvider" );
            l.setLatitude( Lat );
            l.setLongitude( Lng );
            list.add( l );
            c.moveToNext();       // we can move 1 past the last entry w/o negative effects
        }
        c.close();
        db.close();

        return list;
    }

    @Override
    public List<Location> getLocations( LatLngBounds b ) {

        // sort ascending
        String sortOrder = DBDefinition.PositionEntry._ID + " ASC";

        // get the borders for the WHERE clause
        double north = b.northeast.latitude;
        double east  = b.northeast.longitude;
        double south = b.southwest.latitude;
        double west  = b.southwest.longitude;
        String selection =
                DBDefinition.PositionEntry.COLUMN_NAME_Lat + " <= " + north + " AND " +
                DBDefinition.PositionEntry.COLUMN_NAME_Lat + " >= " + south + " AND " +
                DBDefinition.PositionEntry.COLUMN_NAME_Lng + " <= " + east  + " AND " +
                DBDefinition.PositionEntry.COLUMN_NAME_Lng + " >= " + west;

        SQLiteDatabase db = AlDBHelper.getReadableDatabase();

        Cursor c = db.query(
                DBDefinition.PositionEntry.TABLE_NAME,    // The table to query
                PositionEntryProjection,                  // The columns to return
                selection,                                // The WHERE clause
                null,                                     // The ? values for the WHERE clause (not used)
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        c.moveToFirst();
        Location l;
        List<Location> list = new ArrayList<Location>();
        int n = c.getCount();
        for(int i = 1; i <= n; ++i){
            double Lat = c.getDouble( c.getColumnIndex( DBDefinition.PositionEntry.COLUMN_NAME_Lat ));
            double Lng = c.getDouble( c.getColumnIndex( DBDefinition.PositionEntry.COLUMN_NAME_Lng ));
            l = new Location( "Thomas LocationProvider" );
            l.setLatitude( Lat );
            l.setLongitude( Lng );
            list.add( l );
            c.moveToNext();       // we can move 1 past the last entry w/o negative effects
        }

        c.close();
        db.close();
        return list;
    }
}
