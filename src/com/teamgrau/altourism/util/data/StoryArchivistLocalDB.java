package com.teamgrau.altourism.util.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import com.teamgrau.altourism.util.data.database.AltourismDBHelper;
import com.teamgrau.altourism.util.data.database.DBDefinition;
import com.teamgrau.altourism.util.data.model.Story;

/**
 * User: simon
 */

public class StoryArchivistLocalDB implements StoryArchivist {

    AltourismDBHelper AlDBHelper;

    public StoryArchivistLocalDB( Context context ) {
        AlDBHelper = new AltourismDBHelper( context );
    }

    @Override
    public void storeGeschichte(Location position, Story story) {
        // insert storytexts
        ContentValues values = new ContentValues();
        values.put( DBDefinition.POI.COLUMN_NAME_Lat, AltourismDBHelper.DoubleToDB( position.getLatitude() ));
        values.put( DBDefinition.POI.COLUMN_NAME_Lng, AltourismDBHelper.DoubleToDB( position.getLongitude() ));
        values.put( DBDefinition.POI.COLUMN_NAME_Geschichte, story.getStoryText() );
        values.put( DBDefinition.POI.COLUMN_NAME_Title, story.getTitle() );

        SQLiteDatabase db = AlDBHelper.getWritableDatabase();
        long newRowId;
        // the second argument null makes the framework insert no row if values is empty
        newRowId = db.insert( DBDefinition.POI.TABLE_NAME, null, values );  // newRowId is the identifier for a story

        // insert story media
        values = new ContentValues();
        if ( story.getMedia() != null ) {
            Log.d( "Altourism beta", "number of media URI's: " + story.getMedia().size() );
            for ( Uri uri: story.getMedia() ) {
                values.put( DBDefinition.POI._ID, newRowId );
                values.put( DBDefinition.Media.COLUMN_NAME_URI, uri.toString() );
                newRowId = db.insert( DBDefinition.Media.TABLE_NAME, null, values);
                Log.d("Altourism beta", "inserted URI for Story into DB, new rowId is: " + newRowId);
            }
        }
        else {
            Log.d( "Altourism beta", "mediaList was null, it shoul not.." );
        }
        Log.d("Altourism beta", "inserted Story into DB, new rowId is: " + newRowId);
        //Log.d("Altourism beta", "coords: " + position.getLatitude() + " " + AltourismDBHelper.DoubleToDB(position.getLatitude()));
        db.close();
    }
}
